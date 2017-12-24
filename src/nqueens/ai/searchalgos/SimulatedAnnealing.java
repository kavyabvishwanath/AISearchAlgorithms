package nqueens.ai.searchalgos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.log;
import static java.lang.StrictMath.abs;

/**
 * Created by Kavya on 16/09/2017.
 */

class SANode{
    int x;
    int y;
    SANode(int x,int y){
        this.x=x;
        this.y=y;
    }
}

public class SimulatedAnnealing extends NQueensWithObstacles {

    static ArrayList<SANode> nodeList;
    static ArrayList<SANode> occupied;
    static int[][] copyInput;
    static long startTime;
    static final long NANOSEC_PER_SEC = 1000l*1000*1000;



    public static void main1() {
        size = n;
        int ins;
        nodeList = new ArrayList<>();
        offset1 = size - 1;
        offset2 = 0;
        occupied = new ArrayList<>();
        copyInput = copyArray(inputMatrix);


        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (inputMatrix[i][j] != 2) {
                    nodeList.add(new SANode(i, j));
                }

            }
        }

        solveNqueens();

    }

    private static void solveNqueens() {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        boolean exists=false;
        if(nodeList.size()<=0){
            try {
                out.write("FAIL\n");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        getRandomConfig();


        if(nodeList.size()<p){
            exists=false;
            try {
                out.write("FAIL\n");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }



         startTime = System.nanoTime();
        long startMillis = System.currentTimeMillis();

        long count=1000;
        while ((System.nanoTime()-startTime)< 5*50*NANOSEC_PER_SEC){


            double T = getSchedule((System.currentTimeMillis()-startMillis)/60);
            count+=1000;
            System.out.println((System.currentTimeMillis()-startMillis));


            int currentConflicts=getConflicts(inputMatrix);

            if(currentConflicts==0) {
                exists=true;
                break;
            }


            setOccupied(inputMatrix);
            int next[][] = getSuccesor(inputMatrix);
            int nextConflicts = getConflicts(next);

            System.out.println("Current Conflicts "+currentConflicts+" Next conflicts "+nextConflicts);

            double deltaE = currentConflicts-nextConflicts;



            if(nextConflicts==0) {
                inputMatrix=next;
                exists = true;
                break;
            }

            if(deltaE>0) {
                inputMatrix=next;
            }
            else if(deltaE==0){
                int select=(int)(Math.random()*2);
                System.out.println("Select "+select);
                if(select==1){
                    inputMatrix=next;
                }
            }
            else{
                double propability = Math.exp(deltaE/T);
                double randomp = Math.random();
                System.out.println("Probability "+propability+" "+randomp);

                if(propability>randomp)
                    inputMatrix=next;
            }
            System.out.println(count);

        }

        try {
            if(exists) {
                System.out.println("Found");
                printMatrix(inputMatrix, out);
                out.close();
            }
            else {
                out.write("FAIL");
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void printMatrix(int[][] inputMatrix, BufferedWriter out) throws IOException {
        out.write("OK\n");
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++) {
                try {
                    out.write(Integer.toString(inputMatrix[i][j]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if(i!=size-1)
                    out.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setOccupied(int[][] inputMatrix) {
        occupied.clear();
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(inputMatrix[i][j]==1)
                    occupied.add(new SANode(i,j));
            }
        }
    }

    private static int[][] getSuccesor(int[][] input) {

        int ind = (int)(Math.random()*occupied.size());
        SANode temp = occupied.get(ind);
        int[][] re = copyArray(input);
        getAvailable(re);
        re[temp.x][temp.y]=0;

       while(true){

            int row = (int)(Math.random()*nodeList.size());
            SANode n = nodeList.get(row);
            if(re[n.x][n.y]==0 && (n.x!=temp.x && n.y!=temp.y)){
                re[n.x][n.y]=1;
                break;
            }
            if((System.nanoTime()-startTime)> 5*50*NANOSEC_PER_SEC){
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter("/Users/sonu/IdeaProjects/AIAssignments/src/test/kavya/ai/dfs/output.txt"));
                    out.write("FAIL");
                    out.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return re;
    }

    private static void getAvailable(int[][] re) {
        nodeList.clear();
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(re[i][j]==0)
                    nodeList.add(new SANode(i,j));
            }
        }

    }

    private static int[][] copyArray(int[][] input) {
        int[][] res = new int[size][size];
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++)
                res[i][j]=input[i][j];
        }
        return res;
    }

    private static int getConflicts(int[][] matrix) {
        int rowConflicts=0,columnConflicts=0;
        boolean lizardRow=false;
        boolean treeBtn=false;

        for(int i=0;i<size;i++){
            lizardRow=false;treeBtn=false;
            for(int j=0;j<size;j++){
                if(matrix[i][j]==1 && lizardRow==true && treeBtn==false){
                    rowConflicts++;
                }
                if(matrix[i][j]==1){
                    lizardRow=true;
                    treeBtn = false;
                }
                if(matrix[i][j]==2){
                    lizardRow=false;
                    treeBtn = true;
                }
            }
        }

        for(int j=0;j<size;j++){
            lizardRow=false;treeBtn=false;
            for(int i=0;i<size;i++){
                if(matrix[i][j]==1 && lizardRow==true && treeBtn==false){
                    columnConflicts++;
                }
                if(matrix[i][j]==1){
                    lizardRow=true;
                    treeBtn = false;
                }
                if(matrix[i][j]==2){
                    lizardRow=false;
                    treeBtn = true;
                }
            }
        }


        HashMap<Integer,SANode> pDiagonalConflicts = new HashMap<>();
        HashMap<Integer,SANode> nDiagonalConflicts = new HashMap<>();
        int pDiagonal=0,nDiagonal=0;


        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){

                if(matrix[i][j]==1 && pDiagonalConflicts.get(i-j+offset1)!=null)
                    pDiagonal++;
                if(matrix[i][j]==1 && pDiagonalConflicts.get(i-j+offset1)==null)
                    pDiagonalConflicts.put(i-j+offset1,new SANode(i,j));
                if(matrix[i][j]==2)
                    pDiagonalConflicts.put(i-j+offset1,null);

                if(matrix[i][j]==1 && nDiagonalConflicts.get(i+j+offset2)!=null)
                    nDiagonal++;
                if(matrix[i][j]==1 && nDiagonalConflicts.get(i+j+offset2)==null)
                    nDiagonalConflicts.put(i+j+offset2,new SANode(i,j));
                if(matrix[i][j]==2)
                    nDiagonalConflicts.put(i+j+offset2,null);
            }
        }

       return pDiagonal+nDiagonal+rowConflicts+columnConflicts;
    }

    private static double getSchedule(double T) {
        return  1/log(T);
    }

    private static void getRandomConfig() {
        SANode pick;
        for(int i=0;i<p;){
            pick= nodeList.get((int)(Math.random()*nodeList.size()));
            if(inputMatrix[pick.x][pick.y]!=1) {
                inputMatrix[pick.x][pick.y] = 1;
                occupied.add(pick);
                i++;
            }
        }
        occupied.clear();
    }

}
