package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if(args.length == 0){
            Utils.message("Please enter atleast one arguement.");
        }

        String firstArg = args[0];
        Repository gitlet = new Repository();

        switch(firstArg) {
            case "init":
                gitlet.init();
                break;
            case "add":
                gitlet.add(args[1]);
                break;
            // TODO: FILL THE REST IN
        }
    }
}
