package gitlet;

import static gitlet.Utils.message;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Rishabh Choudhury
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        //Empty arguement list
        if (args.length == 0) {
            Utils.message("Please enter a command.");
            System.exit(0);
        }

        String firstArg = args[0];
        Repository gitlet = new Repository();
        //Check repo has been created first
        if (!firstArg.equals("init")) {
            gitlet.checkRepoExists();
        }

        switch (firstArg) {
            case "init" -> {
                commandsWithNoArgument(args);
                gitlet.init();
            }
            case "log" -> {
                commandsWithNoArgument(args);
                gitlet.log();
            }
            case "global-log" -> {
                commandsWithNoArgument(args);
                gitlet.globalLog();
            }
            case "status" -> {
                commandsWithNoArgument(args);
                gitlet.status();
            }
            case "add" -> {
                commandsWithOneArgument(args);
                gitlet.add(args[1]);
            }
            case "commit" -> {
                commandsWithOneArgument(args);
                gitlet.commit(args[1]);
            }
            case "find" -> {
                commandsWithOneArgument(args);
                gitlet.find(args[1]);
            }
            case "rm" -> {
                commandsWithOneArgument(args);
                gitlet.rm(args[1]);
            }
            case "reset" -> {
                commandsWithOneArgument(args);
                gitlet.reset(args[1]);
            }
            case "branch" -> {
                commandsWithOneArgument(args);
                gitlet.branch(args[1]);
            }
            case "rm-branch" -> {
                commandsWithOneArgument(args);
                gitlet.removeBranch(args[1]);
            }
            case "merge" -> {
                commandsWithOneArgument(args);
                gitlet.merge(args[1]);
            }
            case "checkout" -> {
                checkoutCommandArguments(args);
                gitlet.checkout(args);
            }
            default -> {
                System.out.println("No command with that name exists.");
            }
        }
    }

    // ========================== Helper functions for checking ==================================== //
    // ========================== correct number of arguments / argument format ==================== //

    public static void commandsWithNoArgument(String[] args) {
        if (args.length > 1) {
            message("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void commandsWithOneArgument(String[] args) {
        if (args.length > 2) {
            message("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void checkoutCommandArguments(String[] args) {
        if (args.length > 4) {
            message("Incorrect operands.");
            System.exit(0);
        }

        // checkout [commit id] -- [file name]
        if (args.length == 4) {
            if (!args[2].equals("--")) {
                message("Incorrect operands.");
                System.exit(0);
            }
        }

        // checkout -- [file name]
        if (args.length == 3) {
            if (!args[1].equals("--")) {
                message("Incorrect operands.");
                System.exit(0);
            }
        }
    }
}
