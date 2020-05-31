package com.aryan.linearalgebracalculator;

public class UserFunction implements LinearAlgebraObject {
    // First variable locations, second variable locations, so on
    private String[] params;
    private String expression;
    private String name;
    private String compiled;

    // Parse "f(x, y) = 2 * x" into [x, y], 2 * vars[0]
    // Precondition: command is in the correct format
    public UserFunction(String command) {
        command = command.replaceAll("\\s", "");
        this.params = findParams(command);
        this.name = command.substring(0, command.indexOf("("));
        this.expression = command.substring(command.indexOf("=") + 1);
        this.compiled = generateCompiled();
    }

    private String[] findParams(String command) {
        int start = command.indexOf("(");
        int end = command.indexOf(")");
        command = command.substring(start + 1, end);
        return command.split(",");
    }

    private String generateCompiled() {
        String generated = expression;
        for (int i = 0; i < params.length; i++) {
            generated = generated.replace(params[i], "%" + (i + 1) + "$s");
        }
        return generated;
    }

    public String apply(String[] callParams) {
        for (int i = 0; i < callParams.length; i++) {
            callParams[i] = "(" + callParams[i] + ")";
        }
        return String.format(compiled, (Object[]) (callParams));
    }

    public String toString() {
        String out = name + "(" + params[0];
        for (int i = 1; i < params.length; i++) {
            out += ", " + params[i];
        }
        out += ") = ";
        out += expression;
        return out;
    }
}