package com.aryan.linearalgebracalculator;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.function.Function;
import java.time.Instant;

public class Parser {
    private HashMap<String, LinearAlgebraObject> sessionObjects;
    private HashMap<String, UserFunction> userFunctions;
    private Instant lastUsed;

    private static HashMap<String, Function<LinearAlgebraObject[], LinearAlgebraObject>> operations;
    static {
        operations = new HashMap<String, Function<LinearAlgebraObject[], LinearAlgebraObject>>();

		operations.put("create", x -> Operators.create(x));

		operations.put("add", x -> Operators.add(x[0], x[1]));
		operations.put("sub", x -> Operators.subtract(x[0], x[1]));
		operations.put("mult", x -> Operators.multiply(x[0], x[1]));
		operations.put("div", x -> Operators.divide(x[0], x[1]));
		operations.put("pow", x -> Operators.pow(x[0], x[1]));

		operations.put("mag", x -> Operators.magnitude((Vector)(x[0])));
		operations.put("proj", x -> Operators.project((Vector)(x[0]), (Vector)(x[1])));

		operations.put("inv", x -> Operators.inverse((Matrix)(x[0])));
		operations.put("det", x -> Operators.determinant((Matrix)(x[0])));
		operations.put("trans", x -> Operators.transpose((Matrix)(x[0])));
    }

	public Parser() {
        clearAll();
        lastUsed = Instant.now();
		System.out.println("Linear Algebra Terminal started");
	}

	// Clean input, compile to readable instructions, evaluate, return result
	public LinearAlgebraObject parse(String command) {
        command = command.replaceAll("\\s","");
		if (command.equals("clear")) {
			clearAll();
			return new Report("message", "Cleared memory");
		} else if (command.equals("vars")) {
			return new Report("variables", sessionObjects);
		} else if (command.equals("funcs")) {
            return new Report("functions", userFunctions);
        }
		if (command.isEmpty()) {
			return null;
		}
        String name = "ans";
		int equalsIndex = command.indexOf("=");
		if (equalsIndex != -1) {
			name = command.substring(0, equalsIndex);
			command = command.substring(equalsIndex + 1);
        }
        if (matchesFunction(name)) {
            UserFunction newFunc = new UserFunction(name + "=" + command);
            userFunctions.put(name.substring(0, name.indexOf("(")), newFunc);
            return newFunc;
        }
		String compiled = Compiler.compile(command);
        LinearAlgebraObject result = evaluate(compiled);
        lastUsed = Instant.now();

		sessionObjects.put(name, result);
		return result;
    }

    public Instant getLastUsed() {
        return lastUsed;
    }
    
    private boolean matchesFunction(String text) {
        return text.matches("\\w+\\(\\w+[\\w, ]*\\)");
    }

	private void clearAll() {
        this.sessionObjects = new HashMap<String, LinearAlgebraObject>();
        this.userFunctions = new HashMap<String, UserFunction>();
	}

	private LinearAlgebraObject evaluate(String expression) {
		int parenIndex = expression.indexOf("(");
		if (parenIndex == -1) {
			return readToken(expression);
		}

		String functionName = expression.substring(0, parenIndex);
		if (!operations.containsKey(functionName) && !userFunctions.containsKey(functionName)) {
			throw new IllegalArgumentException("\"" + functionName + "\" is not a valid operation");
		}

		String innerParams = expression.substring(parenIndex + 1, expression.length() - 1) + ",";
		ArrayList<String> tokens = new ArrayList<String>();
		int parenCount = 0;
		String currentToken = "";

		for (int i = 0; i < innerParams.length(); i++) {
			char currentChar = innerParams.charAt(i);

			if (currentChar == '(') {
				parenCount += 1;
			} else if (currentChar == ')') {
				parenCount -= 1;
			}

			if (parenCount == 0 && currentChar == ',') {
				tokens.add(currentToken);
				currentToken = "";
			} else {
				currentToken += currentChar;
			}
        }
        LinearAlgebraObject result;
        if (operations.containsKey(functionName)) {
            LinearAlgebraObject[] params = new LinearAlgebraObject[tokens.size()];
            for (int i = 0; i < tokens.size(); i++) {
                params[i] = evaluate(tokens.get(i));
            }
            // System.out.println(functionName + "(" + Arrays.toString(params) + ")\n");
            result =  operations.get(functionName).apply(params);
        } else {
            String[] params = tokens.toArray(new String[tokens.size()]);
            try {
                result = evaluate(Compiler.compile(userFunctions.get(functionName).apply(params)));
            } catch(StackOverflowError e) {
                throw new IllegalArgumentException("Functions cannot be defined in terms of each other");
            }
        }
        return result;
	}

	private LinearAlgebraObject readToken(String token) {
		// Try double or name
		try {
			return new Scalar(Double.parseDouble(token));
		} catch (NumberFormatException e) {
			if (sessionObjects.containsKey(token)) {
				return sessionObjects.get(token);
			} else {
				throw new IllegalArgumentException("\"" + token + "\" cannot be cast to double and is not valid session object");
			}
		}
	}
}
