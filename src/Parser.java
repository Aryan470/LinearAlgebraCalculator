import java.util.HashMap;
import java.util.ArrayList;
import java.util.function.Function;

public class Parser {
	private HashMap<String, LinearAlgebraObject> sessionObjects;
	private static HashMap<String, Function<LinearAlgebraObject[], LinearAlgebraObject>> operations;

	public Parser() {
		clearVar();
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

		System.out.println("Linear Algebra Terminal started");
	}

	// Clean input, compile to readable instructions, evaluate, return result

	public LinearAlgebraObject parse(String command) {
		command = command.replaceAll("\\s","");
		if (command.equals("clear")) {
			clearVar();
			return null;
		} else if (command.equals("vars")) {
			for (String key : sessionObjects.keySet()) {
				System.out.println(key + " = " + sessionObjects.get(key));
			}
			return null;
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
		String compiled = Compiler.compile(command);
		LinearAlgebraObject result = evaluate(compiled);

		sessionObjects.put(name, result);
		return result;
	}

	private void clearVar() {
		this.sessionObjects = new HashMap<String, LinearAlgebraObject>();
	}

	private LinearAlgebraObject evaluate(String expression) {
		int parenIndex = expression.indexOf("(");
		if (parenIndex == -1) {
			return readToken(expression);
		}

		if (expression.charAt(expression.length() - 1) != ')') {
			throw new IllegalArgumentException("Malformed expression (missing end parenthesis): \"" + expression + "\"");
		}

		String functionName = expression.substring(0, parenIndex);
		if (!operations.containsKey(functionName)) {
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

		LinearAlgebraObject[] params = new LinearAlgebraObject[tokens.size()];
		for (int i = 0; i < tokens.size(); i++) {
			params[i] = evaluate(tokens.get(i));
		}

		//System.out.println(functionName + "(" + Arrays.toString(params) + ")\n");
		return operations.get(functionName).apply(params);
	}

	private LinearAlgebraObject readToken(String token) {
		// Try double or name
		try {
			return new Scalar(Double.parseDouble(token));
		} catch (NumberFormatException e) {
			if (sessionObjects.containsKey(token)) {
				return sessionObjects.get(token);
			} else {
				throw new IllegalArgumentException(token + " cannot be cast to double and is not valid session object");
			}
		}
	}
}
