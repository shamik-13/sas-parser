/**
 * CLI dispatcher for the SAS parser tools.
 *
 * Usage:
 *   java -jar sas-parser.jar validate <file>
 *   java -jar sas-parser.jar validate --batch <files...>
 *   java -jar sas-parser.jar audit [--include-proc-body] <files...>
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(2);
        }

        String command = args[0];
        String[] rest = new String[args.length - 1];
        System.arraycopy(args, 1, rest, 0, rest.length);

        switch (command) {
            case "validate":
                ValidateCommand.run(rest);
                break;
            case "audit":
                AuditCommand.run(rest);
                break;
            default:
                System.err.println("Unknown command: " + command);
                printUsage();
                System.exit(2);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("  java -jar sas-parser.jar validate <file>");
        System.err.println("  java -jar sas-parser.jar validate --batch <files...>");
        System.err.println("  java -jar sas-parser.jar audit [--include-proc-body] <files...>");
    }
}
