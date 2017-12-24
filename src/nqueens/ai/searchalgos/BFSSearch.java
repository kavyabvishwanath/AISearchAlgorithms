package nqueens.ai.searchalgos;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Kavya on 15/09/2017.
 */


public class BFSSearch extends NQueensWithObstacles {
    static Integer[]  treeCheckRows,treeCheckColumns;
    static HashMap<Integer,LinkedList<Node>> treeCheckpDiagonal,treeChecknDiagonal;

   public void main1() {

            offset1=size-1;
            offset2=1;


       try {
           solveNqueens(inputMatrix,size,p);
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

    private  void solveNqueens(int[][] inputMatrix, int size, int lizards) throws IOException {
        Node Node = new Node(-1,-1,lizards,null);
        QuequeUtils quequeUtils = new QuequeUtils();
        quequeUtils.enque(Node);
        treeCheckRows = new Integer[size];
        treeCheckColumns = new Integer[size];
        treeCheckpDiagonal = new HashMap<>();
        treeChecknDiagonal = new HashMap<>();

        boolean exist=false;
        Node res=null;
        int[] maxLizardsperRow = getMaxLizardsperRow();
        while(quequeUtils.notEmpty()){
            Node curr = quequeUtils.deque();

            if(!(curr.parent!=null && curr.lizards==curr.parent.lizards-1 && curr.x==curr.x+1) && (curr.x!=-1 && maxLizardsperRow[curr.x]>curr.lizards)) {
                insertChildren(quequeUtils, curr, size, inputMatrix);
            }
            if(curr.x==-1) {
                System.out.println("here");
                insertChildren(quequeUtils, curr, size, inputMatrix);
            }
            if(goal(curr)) {
                res=curr;
                exist = true;
                break;
            }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter("/Users/sonu/IdeaProjects/AIAssignments/src/test/kavya/ai/dfs/output.txt"));

        if(exist){
            printPath(res,out);
            out.close();
        }
        else{
            out.write("FAIL");
            out.close();
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

    private static void printPath(Node res, BufferedWriter out) throws IOException {
        int[][] result=new int[size][size];

        out.write("OK\n");
        while(res!=null){
            if(res.x!=-1 && res.y!=-1)
                result[res.x][res.y]=1;
            res=res.parent;
        }

        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(inputMatrix[i][j]==2)
                    out.write("2");
                else
                    out.write(Integer.toString(result[i][j]));
            }
            if(i!=size-1)
                out.write("\n");
        }

    }

    private static boolean goal(Node curr) {
        if(curr.lizards<=0)
            return true;
        else
            return false;
    }



    private static void insertChildren(QuequeUtils quequeUtils, Node curr, int size, int[][] inputMatrix) {
        if(curr.x+1<size)
            quequeUtils.enque(new Node(curr.x+1,-1,curr.lizards,curr));

        HashMap<Integer, Node> columnsOccupied = new HashMap<Integer, Node>(size);//[size];
        HashMap<Integer, Node> pDiagonal = new HashMap<Integer, Node>(2*size-1);
        HashMap<Integer, Node> nDiagonal = new HashMap<Integer, Node>(2*size-1);
        if(!(curr.x==-1 && curr.y==-1))
            getConfigs(curr,columnsOccupied,pDiagonal,nDiagonal);
        for(int i=0;i<size;i++){
            if(curr.y==-1 && curr.x>-1){
                if(inputMatrix[curr.x][i]!=2) {
                    if (isSafe(curr.x, i, columnsOccupied, pDiagonal, nDiagonal))
                        quequeUtils.enque(new Node(curr.x, i, curr.lizards - 1, curr));
                }
            }
            else {
                if (curr.x + 1 < size && curr.x+1>=0) {
                    if(inputMatrix[curr.x+1][i]!=2) {
                        if (isSafe(curr.x + 1, i, columnsOccupied, pDiagonal, nDiagonal)) {
                            quequeUtils.enque(new Node(curr.x + 1, i, curr.lizards - 1, curr));
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
                            quequeUtils.enque(new Node(curr.x, i, curr.lizards - 1, curr));
                        }
                    }
                }
            }
        }
    }

    private static int getTree(int[][] inputMatrix, int x, int y) {
        for (int j = y; j < size; j++) {
            if(inputMatrix[x][j]==2)
                return j;
        }
        return -1;
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
            float pdt= Float.MAX_VALUE;
            float ndt=Float.MAX_VALUE;

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

            if(pdt < pd || (pdt==Float.MAX_VALUE && pd==-1) || (pd==-1) ) {
                if(ndt < nd || (ndt==Float.MAX_VALUE && nd==-1) || (nd==-1)) {
                    if(columnsOccupied.get(column)==null || (columnsOccupied.get(column)!=null && columnsOccupied.get(column).x<getClosestTree(row,column)))
                        return true;
                }
            }
        }
        return false;
    }

    private static int getClosestTree(int row,int column) {
        if(row<size) {
            for (int i = row; i >= 0; i--) {
                if (inputMatrix[i][column] == 2)
                    return i;
            }
        }
        return -1;
    }

    private static float distance(int row, int column, Node Node) {
        return (row- Node.x)*(row- Node.x) + (column- Node.y)*(column- Node.y);
    }

    private static Node getClosestRightTree(int row, int column) {
        if(row==0 || column==size-1)
            return new Node(-1,-1,0,null);
        for(int i=row,j=column;i>=0 && j<size;i--,j++){
            if(inputMatrix[i][j]==2)
                return new Node(i,j,0,null);
        }
        return new Node(1000,1000,0,null); // hard coding 1000,1000 to be the last co-ordinate in the matrix
    }

    private static Node getClosestLeftTree(int row, int column) {
      if(row==0 || column==0)
            return new Node(-1,-1,0,null);
        for(int i=row , j=column;i>=0 && j>=0;i--,j--){
            if(inputMatrix[i][j]==2)
                return new Node(i,j,0,null);
        }
        return new Node(1000,1000,0,null); // hard coding 1000,1000 to be the last co-ordinate in the matrix
    }

    private static void getConfigs(Node curr, HashMap<Integer, Node> columnsOccupied, HashMap<Integer, Node> pDiagonal, HashMap<Integer, Node> nDiagonal) {
        Node n = curr;
        while(n!=null){
            if(n.x==-1 && n.y==-1)
                break;
            if(columnsOccupied.get(n.y)==null && n.y>=0)
                columnsOccupied.put(n.y,n);
            if(pDiagonal.get(n.x+n.y+offset2)==null && n.y>=0)
                pDiagonal.put(n.x+n.y+offset2,new Node(n.x,n.y,0,null));
            if(nDiagonal.get(n.x-n.y+offset1)==null && n.y>=0)
                nDiagonal.put(n.x-n.y+offset1,new Node(n.x,n.y,0,null));
            n=n.parent;
        }
    }
}
