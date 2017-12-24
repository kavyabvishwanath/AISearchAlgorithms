package nqueens.ai.searchalgos;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Kavya on 16/09/2017.
 */

class StackUtils {
    Node head=null,tail=null;

    public void push(Node startnode) {
        if(tail==null){
            startnode.next=null;
            head = startnode;
            tail=startnode;
            return;
        }
        startnode.next=null;
        tail.next=startnode;
        tail=tail.next;
    }

    private void printStack() {
        Node temp = tail;
        while(temp!=null){
            temp=temp.prev;
        }
    }

    public boolean notEmpty() {
        return  (tail!=null)? true : false;
    }

    public Node getHead() {
        return tail;
    }

    public Node pop() {
        tail=tail.prev;
        return tail;
    }
}



public class DFSSearch extends NQueensWithObstacles {
    static HashMap<Integer, Node> columnsOccupied;
    static HashMap<Integer, Node> pDiagonal;
    static HashMap<Integer, Node> nDiagonal;

    void main1() {


        boolean flag=false;


        Node startnode = new Node(-1,-1,p,null,false,null);
        startnode.prev=null;
        StackUtils stackUtils = new StackUtils();
        stackUtils.push(startnode);
        boolean exists=false;
        columnsOccupied = new HashMap<Integer, Node>(size);//[size];
        pDiagonal = new HashMap<Integer, Node>(2*size-1);
        nDiagonal = new HashMap<Integer, Node>(2*size-1);
        Node res=null;
        Node temp=null;
        int[] maxLizardsperRow = getMaxLizardsperRow();

        while (stackUtils.notEmpty()){
            Node curr;


            if(flag) {
                clearflags(temp);
                curr = stackUtils.pop();
                flag=false;
            }
            else {
                 curr = stackUtils.getHead();
            }

            if(curr==null){
                break;
            }
            if(goal(curr)){
                res=curr;
                exists=true;
                break;
            }
            markflags(curr);

            if(curr.explored){
                clearflags(curr);
                curr=stackUtils.pop();
            }
            if(curr==null){
                break;
            }

            if(!curr.explored) {
                if(curr.x!=-1 && curr.lizards>maxLizardsperRow[curr.x]){
                    clearflags(curr);
                    curr=stackUtils.pop();
                    curr.explored = true;
                }
                else {
                    markflags(curr);
                    if (curr.parent != null) {
                        if (!(curr.x == curr.parent.x + 1 && curr.lizards == curr.parent.lizards - 1)) {
                            insertAllChildren(curr, stackUtils);
                        }
                    }

                    if (curr.x == -1 && curr.y == -1)
                        insertAllChildren(curr, stackUtils);

                    curr.explored = true;
                }

            }
            else {
                flag = true;
                temp=curr;
            }
        }

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(exists){
            Node temp1 = res;
            int[][] result = new int[size][size];
            try {
                printMatrix(temp1,result,out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            try {
                out.write("FAIL\n");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int[] getMaxLizardsperRow() {
        int[] res= new int[size];
        int treeCount=0;
        for(int i=size-1;i>=0;i--){
            int count=0;
            for(int j=size-1;j>=0;j--){
                if(inputMatrix[i][j]==2)
                    count++;
            }
            treeCount=treeCount+count+1;
            res[i]=treeCount;
        }
        return res;
    }

    private static void printMatrix( int[][] result){

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++) {
                System.out.print(result[i][j]);
            }
            System.out.println();
        }
    }

    private static void printMatrix(Node temp, int[][] result, BufferedWriter out) throws IOException {
        while(temp!=null){
            if(temp.x>-1 && temp.y>-1)
                result[temp.x][temp.y]=1;
            temp=temp.parent;
        }
        out.write("OK\n");
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++) {
                if(inputMatrix[i][j]==2)
                    out.write(Integer.toString(inputMatrix[i][j]));
                else
                    out.write(Integer.toString(result[i][j]));
            }
            if(i!=size-1)
                out.write("\n");
        }

    }

    private static void init(int[] columns) {
        for(int i=0;i<size;i++)
            columns[i]=-1;
    }

    private static void clearflags(Node curr) {
        Node n = curr;
        boolean cflag =false, pflag =false, nflag = false;

        if(n!=null && n.x>-1 && n.y>-1) {
            columnsOccupied.remove(curr.y);
            pDiagonal.remove(n.x + n.y + offset2);//, null);
            nDiagonal.remove(n.x - n.y + offset1);//, null);
        }
        while(n!=null){
            if(n.x==-1 && n.y==-1)
                break;

            n=n.parent;

            if(columnsOccupied.get(n.y)==null && n.y>=0 && (n.y==curr.y)) {
                cflag=true;
                columnsOccupied.put(n.y, n);
            }
            if(pDiagonal.get(n.x+n.y+offset2)==null && n.y>=0 && (n.x+n.y+offset2==curr.x+curr.y+offset2)) {
                pflag=true;
                pDiagonal.put(n.x + n.y + offset2, new Node(n.x, n.y, 0, null));
            }
            if(nDiagonal.get(n.x-n.y+offset1)==null && n.y>=0 && (n.x-n.y+offset1 == curr.x-curr.y+offset1)) {
                nflag=true;
                nDiagonal.put(n.x - n.y + offset1, new Node(n.x, n.y, 0, null));
            }
            if(cflag && pflag && nflag)
                break;

        }
    }

    private static void markflags(Node n) {
        if(n.y>=0) {
            Node temp=new Node(n.x, n.y, 0, null);
            columnsOccupied.put(n.y, n);
            pDiagonal.put(n.x + n.y + offset2, temp);
            nDiagonal.put(n.x - n.y + offset1, temp);
        }
    }

    private static boolean goal(Node curr) {
        if(curr.lizards<=0)
            return true;
        else
            return false;
    }

    private static void insertAllChildren(Node curr, StackUtils stackUtils) {
        if(curr.x+1<size && curr!=null )
            stackUtils.push(new Node(curr.x+1,-1,curr.lizards,curr,false,stackUtils.getHead()));


        for(int i=0;i<size;i++){
            if(curr.y==-1 && curr.x>-1){
                if(inputMatrix[curr.x][i]!=2) {
                    if (isSafe(curr.x, i, columnsOccupied, pDiagonal, nDiagonal))
                        stackUtils.push(new Node(curr.x, i, curr.lizards - 1, curr,false,stackUtils.getHead()));
                }
            }
            else {
                if (curr.x + 1 < size && curr.x+1>=0) {
                    if(inputMatrix[curr.x+1][i]!=2) {
                        if (isSafe(curr.x + 1, i, columnsOccupied, pDiagonal, nDiagonal)) {
                            stackUtils.push(new Node(curr.x + 1, i, curr.lizards - 1, curr,false,stackUtils.getHead()));
                        }
                    }
                }
            }
        }

        if (curr.x > -1 && curr.y + 1 >= 0 && curr.x<size) {
            int treeinRow = getTree(inputMatrix, curr.x, curr.y + 1);
            if (treeinRow != -1) {
                for (int i = treeinRow + 1; i < size; i++) {
                    if(inputMatrix[curr.x][i]!=2) {
                        if (isSafe(curr.x, i, columnsOccupied, pDiagonal, nDiagonal)) {
                            stackUtils.push(new Node(curr.x, i, curr.lizards - 1, curr,false,stackUtils.getHead()));
                        }
                    }
                }
            }
        }
    }

    private static boolean isSafe(int row, int column, HashMap<Integer, Node> columnsOccupied, HashMap<Integer, Node> pDiagonal, HashMap<Integer, Node> nDiagonal) {
        if(columnsOccupied.get(column)==null && pDiagonal.get(row+column+offset2)==null && nDiagonal.get(row-column+offset1)==null)
            return true;
        else{
            if(columnsOccupied.get(column)!=null && pDiagonal.get(row+column+offset2)==null && nDiagonal.get(row-column+offset1)==null){
                if(columnsOccupied.get(column).x>getClosestTree(row,column) || row==columnsOccupied.get(column).x+1) {
                    return false;
                }

            }
            Node nDiagonalClosestTree = getClosestLeftTree(row,column);
            Node pDiagonalClosestTree = getClosestRightTree(row,column);
            float pdt=40000000; // hardcoded value to indicate no obstacle in this diagonal to save
            float ndt=40000000;

            if(nDiagonalClosestTree.x!=-1)
                ndt = distance(row,column,nDiagonalClosestTree);
            if(pDiagonalClosestTree.x!=-1)
                pdt = distance(row,column,pDiagonalClosestTree);

            float pd=-1;
            if(pDiagonal.get(row+column+offset2)!=null)
                pd = distance(row,column,pDiagonal.get(row+column+offset2));
            float nd=-1;

            if(nDiagonal.get(row-column+offset1)!=null)
                nd = distance(row,column,nDiagonal.get(row-column+offset1));

            if(pdt < pd || (pdt==40000000 && pd==-1) || (pd==-1) ) {
                if(ndt < nd || (ndt==40000000 && nd==-1) || (nd==-1)) {
                   if(columnsOccupied.get(column)==null || (columnsOccupied.get(column)!=null && columnsOccupied.get(column).x<getClosestTree(row,column)))
                        return true;
                }
            }
        }
        return false;

    }

    private static int getTree(int[][] inputMatrix, int row, int column) {
        for(int i=column;i<size;i++){
            if(inputMatrix[row][i]==2)
                return i;
        }
        return -1;
    }


    private static int getClosestTree(int row,int column) {
        return treecolumns[row][column];
    }

    private static float distance(int row, int column, Node Node) {
        return (row- Node.x)*(row- Node.x) + (column- Node.y)*(column- Node.y);
    }

    private static Node getClosestRightTree(int row, int column) {
        return pd[row][column];
    }

    private static Node getClosestLeftTree(int row, int column) {
        return nd[row][column];
    }

}
