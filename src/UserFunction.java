import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserFunction implements LinearAlgebraObject {
    // First variable locations, second variable locations, so on
    private int[][] varLocs;
    private String[] params;
    private String expression;
    private String name;

    // Parse "f(x, y) = 2 * x" into [x, y], 2 * vars[0]
    // Precondition: command is in the correct format
    public UserFunction(String command) {
        command = command.replaceAll("\\s", "");
        this.params = findParams(command);
        this.name = command.substring(0, command.indexOf("("));
        this.varLocs = findVars(command);
        this.expression = command.substring(command.indexOf("=") + 1);
    }

    private int[][] findVars(String command) {
        int[][] locArr = new int[params.length][];
        for (int i = 0; i < params.length; i++) {
            ArrayList<Integer> locList = new ArrayList<Integer>();
            Pattern p = Pattern.compile(params[i]);
            Matcher m = p.matcher(command);

            while (m.matches()) {
                locList.add(m.start());
            }

            locArr[i] = new int[locList.size()];
            for (int j = 0; j < locList.size(); j++) {
                locArr[i][j] = locList.get(j);
            }
        }
        return locArr;
    }

    private String[] findParams(String command) {
        int start = command.indexOf("(");
        int end = command.indexOf(")");
        command = command.substring(start + 1, end);
        return command.split(",");
    }

    // TODO: Takes tokenized parameters, returns replaced call
    private String format(String[] callParams) {
        for (int i = 0; i < callParams.length; i++) {
            callParams[i] = "(" + callParams[i] + ")";
        }
        
        return "UNIMPLEMENTED";
    }

    public String toString() {
        String out = "%s(%s";
        for (int i = 1; i < params.length; i++) {
            out += ", " + params[i];
        }
        out += ") = ";
        out += expression;
        return out;
    }
}