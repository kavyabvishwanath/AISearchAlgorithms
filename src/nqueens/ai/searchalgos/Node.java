package nqueens.ai.searchalgos;
/**
 * Created by sonu on 19/09/2017.
 */
class Node {
    boolean explored;
    Node prev;
    Node parent,next;
    int x;

    public Node(int x, int y, int lizards, Node parent) {
        this.x = x;
        this.y = y;
        this.lizards = lizards;
        this.parent = parent;
    }

    int y;
    int lizards;

    public Node(int x, int y, int lizards, Node parent, boolean explored, Node prev ){
        this.x=x;
        this.y=y;
        this.lizards=lizards;
        this.parent=parent;
        this.explored=explored;
        this.prev=prev;
    }

//    public Node(int x, int y, int lizards, Node parent) {
//        this.x = x;
//        this.y = y;
//        this.lizards = lizards;
//        this.parent = parent;
//    }
}