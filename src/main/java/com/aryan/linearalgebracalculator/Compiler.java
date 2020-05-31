package com.aryan.linearalgebracalculator;

import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.HashMap;

public class Compiler {
	public static HashMap<Character, BiFunction<String, String, String>> operatorMap = new HashMap<Character, BiFunction<String, String, String>>();
	public static HashMap<Character, Integer> precedenceMap = new HashMap<Character, Integer>();
	public static int maxPrecedence = 3;
	static {
		operatorMap.put(',', (String a, String b) -> String.format("%s,%s", a, b));
		operatorMap.put('+', (String a, String b) -> String.format("add(%s,%s)", a, b));
		operatorMap.put('-', (String a, String b) -> String.format("sub(%s,%s)", a, b));
		operatorMap.put('*', (String a, String b) -> String.format("mult(%s,%s)", a, b));
		operatorMap.put('/', (String a, String b) -> String.format("div(%s,%s)", a, b));
		operatorMap.put('^', (String a, String b) -> String.format("pow(%s,%s)", a, b));

		precedenceMap.put(',', 0);
		precedenceMap.put('+', 1);
		precedenceMap.put('-', 1);
		precedenceMap.put('*', 2);
		precedenceMap.put('/', 2);
		precedenceMap.put('^', 3);
	}

	public static String compile(String command) {
		/*
			find operators
				set lowest precedence
			if no operators and yes parenthesis
				return replace parenthesis with eval(parenthesis)
			if no operators and no parenthesis
				return this
			tokenize by operators at lowest precedence
			set LHS to be eval(first token)
			check operator
				find string formatting for operator
				set LHS to operator format(LHS, eval(RHS))
			return LHS
		*/
		command = clean(command);
		return evaluate(command);
	}

	private static String clean(String expression) {
		expression = expression.replaceAll("\\s", "");
		expression = expression.replace("[", "create(");
		expression = expression.replace("]", ")");
		return expression;
	}

	private static String evaluate(String command) {
		ArrayList<Integer> lowestOperators = findLowestOperators(command);
		// Here is a standalone token (function call, parenthesized expression, name, number)
		if (lowestOperators.size() == 0) {
			if (command.indexOf("(") != -1) {
				// This must be a function call or parenthesized expression
				int start = command.indexOf("(");
				// If the parenthesis are the start of the token, it is a parenthesized expression
				boolean isFunction = (start != 0);
				String insideParenthesis = command.substring(start + 1, command.length() - 1);
				String compiledInside = evaluate(insideParenthesis);

				if (isFunction) {
					String funcName = command.substring(0, start + 1);
					return funcName + compiledInside + ")";
				} else {
					return compiledInside;
				}
			} else {
				// Here we know that this must be a name or number
				return command;
			}
		}

		ArrayList<String> tokens = tokenize(command, lowestOperators);
		String LHS = evaluate(tokens.remove(0));

		for (int i = 0; i < lowestOperators.size(); i++) {
			char operator = command.charAt(lowestOperators.get(i));
			String RHS = evaluate(tokens.remove(0));
			LHS = operatorMap.get(operator).apply(LHS, RHS);
		}

		return LHS;
	}
	private static ArrayList<Integer> findLowestOperators(String command) {
		int parenCount = 0;
		int minPrecedence = maxPrecedence + 1;
		ArrayList<Integer> locations = new ArrayList<Integer>();
		for (int i = 0; i < command.length(); i++) {
			char current = command.charAt(i);
			if (current == '(') {
				parenCount += 1;
			} else if (current == ')') {
				parenCount -= 1;
			}
			if (parenCount == 0 && precedenceMap.containsKey(current)) {
				locations.add(i);
				minPrecedence = Math.min(precedenceMap.get(current), minPrecedence);
			}
		}
		if (parenCount != 0) {
			throw new IllegalArgumentException("Unmatched parenthesis in: " + command);
		}

		for (int i = locations.size() - 1; i >= 0; i--) {
			int currentIndex = locations.get(i);

			char currentOp = command.charAt(currentIndex);
			if (precedenceMap.get(currentOp) > minPrecedence) {
				locations.remove(i);
			}
		}

		return locations;
	}


	// Precondition: lowestOperators should have at least one location
	private static ArrayList<String> tokenize(String command, ArrayList<Integer> lowestOperators) {
		ArrayList<String> tokens = new ArrayList<String>();
		tokens.add(command.substring(0, lowestOperators.get(0)));
		for (int i = 1; i < lowestOperators.size(); i++) {
			// Take the string between the previous and current operator
			String newToken = command.substring(lowestOperators.get(i - 1) + 1, lowestOperators.get(i));
			tokens.add(newToken);
		}
		// Add final token
		tokens.add(command.substring(lowestOperators.get(lowestOperators.size() - 1) + 1));
		fillZero(tokens, lowestOperators);
		return tokens;
	}

	public static void fillZero(ArrayList<String> tokens, ArrayList<Integer> lowestOperators) {
		for (int i = tokens.size() - 1; i >= 0; i--) {
			if (tokens.get(i).isEmpty()) {
				tokens.set(i, "0");
			}
		}
	}
}
