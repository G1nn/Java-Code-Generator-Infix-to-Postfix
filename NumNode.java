/**
 * NumNode is a node containing a number. It is a leaf node in the parse tree thus having no children.
 */

public class NumNode extends Node{
    double n;

    public NumNode(double n){
        this.n = n;
    }

    public double getValue(){
        return n;
    }

    public double eval(){
        return n;
    }

    public String postfix(){return String.valueOf(n);}
}
