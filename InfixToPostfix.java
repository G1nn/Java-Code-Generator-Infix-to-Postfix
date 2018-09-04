
import java.io.IOException;
import java.lang.Double;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * The output is a Java file that converts an arithmetic expression in infix notation to a postfix one, and give out its result.
 * support + - * / ( ) and numbers with multiple digits and floating points
 * The input expression can not contain empty spaces.
 */



public class InfixToPostfix {

    //Auxiliary methods for evaluating the characters of the expression
    public static boolean isOp(char op){
        return (op == '+') || (op == '-') || (op == '*') || (op == '/');
    }
    public static boolean lowPrec(char op) {return (op == '+') || (op == '-');}
    public static boolean highPrec(char op) {return (op == '*') || (op == '/');}

    //Auxiliary methods for checking the form of the expression
    public static boolean oneNum(String expr){
        for (int i = 0; i < expr.length(); i++){
            if (isOp(expr.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static int twoNum(String expr) {
        int ct = 0;
        int index = 0;
        for (int i = 0; i < expr.length(); i++) {
            if (isOp(expr.charAt(i))) {
                ct++;
                index = i;
            }
        }
        if (ct == 1){
            return index; //index of the operator
        } else {
            return -1;
        }
    }

    public static boolean isBracketed(String expr) {
        if ((expr.charAt(0) == '(') && (expr.charAt(expr.length() - 1) == ')')) {
            for (int i = 1; i < expr.length() - 1; i++){
                if (expr.charAt(i) == ')'){
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }



    // The method parseTree builds a parse tree for the expression,
    // and outputs the root node of the tree.
    public static Node parseTree(String expr) {
        //Case 0: The expression is empty.
        if (expr == null || expr.length() == 0) {
            System.out.println("The input expression is empty.");
            return null;
        }
        //Case 1: The expression contains only one number.
        else if (oneNum(expr)) {
            return new NumNode(Double.parseDouble(expr));
        }
        //Case 2: The expression contains only the form of "num op num".
        else if (twoNum(expr) != -1) {
            int index = twoNum(expr);
            if (isBracketed(expr)){
                expr = expr.substring(1, expr.length() - 1);
                index = index - 1;
            }
            NumNode l = new NumNode(Double.parseDouble(expr.substring(0, index)));
            NumNode r = new NumNode(Double.parseDouble(expr.substring(index + 1, expr.length())));
            return new OpNode(l, r, expr.charAt(index));
        }
        //Case 3: Otherwise.
        else
        {
            //find the last executed operation aka the root node of the tree
            char lastOp = '*'; //dummy value for the first iteration
            char target = lastOp;
            int targetIndex = -1;
            int nest = 0;
            if (isBracketed(expr)) {
                expr = expr.substring(1, expr.length() - 1);
            }
            for (int i = 0; i < expr.length(); i++) {
                char curr = expr.charAt(i);
                if (curr == '('){
                    nest++;
                }
                if (curr == ')'){
                    nest--;
                }

                if (nest == 0 && isOp(curr)) {
                    if ((highPrec(lastOp) && lowPrec(curr)) || (highPrec(lastOp) && highPrec(curr)) || (lowPrec(lastOp) && lowPrec(curr))) {
                        target = curr;
                        lastOp = curr;
                        targetIndex = i;
                    }
                }
            }
            //Recursively parse the left and right expressions of the root node
            Node l = parseTree(expr.substring(0, targetIndex));
            Node r = parseTree(expr.substring(targetIndex + 1, expr.length()));
            return new OpNode(l, r, target);
        }
    }

    //Evaluate and output the expression in postfix and its result.
    public static String evaluate(Node root){
        return root.postfix().concat(" = ").concat(String.valueOf(root.eval()));
    }

    //Create a java file that contains a sequence of instructions to evaluate the postfix expression.
    public static Path makeFile(Node root){
        String postfix = root.postfix();
        String[] array = postfix.split(" ");
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("import java.util.ArrayList;" +
                "class Stack {\n" +
                "    private int count = 0;\n" +
                "    ArrayList stack;\n" +
                "\n" +
                "    public Stack(){\n" +
                "        stack = new ArrayList();\n" +
                "    }\n" +
                "\n" +
                "    public void push(double element){\n" +
                "        stack.add(element);\n" +
                "        count++;\n" +
                "    }\n" +
                "\n" +
                "    public double pop() {\n" +
                "        if (count == 0) {\n" +
                "            System.out.println(\"The stack is empty!\");\n" +
                "            return -1;\n" +
                "        } else {\n" +
                "            count--;\n" +
                "            return (double) stack.remove(stack.size() - 1);\n" +
                "        }\n" +
                "    }\n" +
                "}");
        lines.add("public class Execution{");
        lines.add("public static void main(String[] args) {");
        lines.add("Stack stk = new Stack();");
        lines.add("double left;");
        lines.add("double right;");
        for (int i = 0; i < array.length; i++){
            if (isOp(array[i].charAt(0))){
                lines.add("right = stk.pop();");
                lines.add("left = stk.pop();");
                lines.add("stk.push(left".concat(array[i]).concat("right);"));
            } else {
                lines.add("stk.push(".concat(array[i]).concat(");"));
            }
        }
        lines.add("System.out.println(stk.pop());}}");
        Path file = Paths.get("Execution.java");
        try{
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter an arithmetic expression without empty spaces: ");
        System.out.println("(Use only '+', '-', '*', '/', '(' and ')')");
        String s = sc.next();
        Node tree = parseTree(s);
        System.out.println(evaluate(tree));
        makeFile(tree);
    }
}
