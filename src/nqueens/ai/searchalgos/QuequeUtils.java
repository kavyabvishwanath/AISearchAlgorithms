package nqueens.ai.searchalgos;

/**
 * Created by sonu on 15/09/2017.
 */
public class QuequeUtils {

    Node head=null,tail;
    int size=0;

    synchronized
    public void enque(Node Node) {
        size++;
        if(head==null){
            Node.next=null;
            head= Node;
            tail=head;
        //    printQueue();
            return;
        }
        Node.next=null;
        tail.next= Node;
        tail= Node;
       // printQueue();
    }

    public boolean notEmpty() {
        if(head!=null)
            return true;
        else
            return false;
    }

    public Node deque() {
        size--;
        Node temp=head;
        head=head.next;
        return temp;
    }

    public int getSize(){
        return size;
    }

    public void printQueue(){
        Node temp = head;
        while(temp!=null){
            System.out.println(temp.x + " " + temp.y+ " "+temp.lizards );
            if(temp.parent!=null)
                System.out.println("parent: " + temp.parent.x + " "+temp.parent.y + " "+temp.parent.lizards);
            temp=temp.next;
        }
    }

}
