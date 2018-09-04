/**
 * An OpNode is a node that contains an operator. Its left and right children are its left and right operands, which can
 * be a NumNode or an OpNode.
 * The eval method recursively evaluates the parse tree whose root is the OpNode.
 */

public class OpNode extends Node{
    Node left;
    Node right;
    char op;

    public OpNode(Node left, Node right, char op){
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public char getOp(){
        return op;
    }

    public Node getLeft(){
        return left;
    }

    public Node getRight(){
        return right;
    }

    public double eval(){
        double l = left.eval();
        double r = right.eval();
        switch(op){
            case '+': 	return l+r;
            case '-':	  return l-r;
            case '*':	  return l*r;
            case '/':	  return l/r;
            default:    System.out.println("Error!");
                return -1;
        }
    }

    public String postfix(){
        String l = left.postfix();
        String r = right.postfix();
        return l.concat(" ").concat(r).concat(" ").concat(String.valueOf(op));
    }
}
