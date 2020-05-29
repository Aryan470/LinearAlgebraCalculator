import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class TerminalMain {
	public static final boolean ISFILE = false;
	public static void main(String[] args) throws FileNotFoundException {
		Scanner console;
		if (ISFILE) {
			console = new Scanner(new File("compiled.la"));
		} else {
			console = new Scanner(System.in);
		}
		String command;
		Parser p = new Parser();

		do {
			System.out.print("\nLinAlg>");
			command = console.nextLine();
			try {
				long start = System.currentTimeMillis();
				LinearAlgebraObject output = p.parse(command);
				long end = System.currentTimeMillis();
				if (output != null) {
					System.out.println(output);
					System.out.println((end - start) / 1000.0 + "s");
				}
			} catch (Exception e) {
				if (command.equals("help")) {
					printHelp();
				} else if (!command.equals("exit")){
					printError(e);
				}
			}
		}
		while ((ISFILE ? console.hasNextLine() : true) && !command.equals("exit"));
        console.close();
	}

	public static void printHelp() {
		System.out.println("Linear Algebra Terminal v0.01");
		System.out.println("Try something like \"x = 2\", \"x + 10\", \"v = [2, 5, 3]\", or \"2 * v\"");
		System.out.println("Defined: +, -, *, /, mag, proj, det, inv, trans");
	}

	private static void printError(Exception e) {
		e.printStackTrace();
		System.out.println("Invalid command: " + e);
		System.out.println("Type \"help\" for help or \"exit\" to quit");
	}
}
