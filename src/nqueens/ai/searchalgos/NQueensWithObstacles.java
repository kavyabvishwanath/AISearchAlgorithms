package nqueens.ai.searchalgos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Kavya on 19/09/2017.
 */
public class NQueensWithObstacles {
    static int size,p,n;
    static int[][] inputMatrix;
    static int offset1,offset2;


    /*
    Preprocessing
    treecolumns[i][j] has closest tree to (i,j) in column j
    treerows[i][j] has closest tree to (i,j) in row i
    this information is used to optimise the search so we need not iterate through the matrix for every search
    Example :
    3
    3
    1--
    221
    0--
    In the above partially filled matrix(intermediate step) isSafe(0,2) would check
    closestTree in column 0 = 1 , closest queen in column 0 = 0 so columnwise 0 is safe
    closestTree in row 2 = -1 closest queen in row 0 = -1 so rowwise 2 is safe
    So the below solution
    1--
    221
    100
     */

    static int[][] treecolumns,treerows;
    static Node[][] nd,pd;
    private static final String FILENAME = "input.txt";//nqueens/ai/searchalgos/input.txt";

    public static void main(String[] args) {

        BufferedReader br = null;
        FileReader fr = null;
        String algo="";


        try {
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);

            String sCurrentLine;
            int count=1;

            algo = br.readLine();


            sCurrentLine = br.readLine();
            n = Integer.parseInt(sCurrentLine);
            size=n;
            inputMatrix= new int[n][n];

            treerows = new int[size][size];
            treecolumns = new int[size][size];

            int columns[] = new int[size];
            int rows[] = new int[size];

            HashMap<Integer,Node> nd_temp = new HashMap<>();
            HashMap<Integer,Node> pd_temp = new HashMap<>();
            nd = new Node[size][size];
            pd = new Node[size][size];
            offset2=1;
            offset1=size-1;

            init(columns);
            init(rows);


            int i=0;
            while ((sCurrentLine = br.readLine()) != null) {

                if(count==1) {
                    p = Integer.parseInt(sCurrentLine);
                    //      System.out.//ln(p);
                    count++;
                }
                else {
                    //int j=0;
                    for(int j=0;j<sCurrentLine.length();j++){
                        //   System.out.println(i);
                        inputMatrix[i][j] = Integer.parseInt(String.valueOf(sCurrentLine.charAt(j)));
                        if(inputMatrix[i][j]==2){
                            columns[j]=i;
                            rows[i]=j;
                            nd_temp.put(i-j+offset1,new Node(i,j,0,null));
                            pd_temp.put(i+j+offset2,new Node(i,j,0,null));
                        }
                        treecolumns[i][j]=columns[j];
                        treerows[i][j]=rows[i];

                        if(nd_temp.get(i-j+offset1)!=null)
                            nd[i][j]=nd_temp.get(i-j+offset1);
                        else {
                            if(i==0 || j==size-1)
                                nd[i][j] = (new Node(-1, -1, 0, null));
                            else
                                nd[i][j] = (new Node(1000, 1000, 0, null));
                        }

                        if(pd_temp.get(i+j+offset2)!=null)
                            pd[i][j]=pd_temp.get(i+j+offset2);
                        else {
                            if(i==0 || j==size-1)
                                pd[i][j] = (new Node(-1, -1, 0, null));
                            else
                                pd[i][j]=new Node(1000, 1000, 0, null);
                        }
                        // j++;
                    }
                    i++;
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        }
        System.out.println(algo);
        if(algo.equals("DFS")) {
            DFSSearch dfsSearch = new DFSSearch();
            dfsSearch.main1();
        }
        if(algo.equals("BFS")){
            BFSSearch bfsSearch = new BFSSearch();
            bfsSearch.main1();
        }
        if(algo.equals("SA")){
            System.out.println("Called");
            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
            simulatedAnnealing.main1();
        }
    }

    private static void init(int[] columns) {
        for(int i=0;i<size;i++)
            columns[i]=-1;
    }
}
