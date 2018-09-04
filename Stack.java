
import java.util.ArrayList;

public class Stack {
    private int count = 0;
    ArrayList stack;

    public Stack(){
        stack = new ArrayList();
    }

    public void push(double element){
        stack.add(element);
        count++;
    }

    public double pop() {
        if (count == 0) {
            System.out.println("The stack is empty!");
            return -1;
        } else {
            count--;
            return (double) stack.remove(stack.size() - 1);
        }
    }
}
