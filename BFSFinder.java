import java.awt.*;

//Finds Path Between two Maze Points Using Breadth-Frist Search (BFS)
public class BFSFinder {

    int[][] map;
    int x;
    int y;

    public BFSFinder(GameBoard pb){
        this.x = pb.m_x;
        this.y = pb.m_y;
        //init BFS map
        map = new int[pb.m_x][pb.m_y];
        for(int ii=0;ii<pb.m_y;ii++){
            for(int jj=0;jj<pb.m_x;jj++){
                if(pb.map[jj][ii]>0 && pb.map[jj][ii]<26){
                    map[jj][ii] = 1;
                }else{
                    map[jj][ii] = 0;
                }
            }
        }
    }

    private class MazeCell {
        int x;
        int y;
        boolean isVisited;

        public MazeCell(int x, int y) {
            this.x = x;
            this.y = y;
            isVisited =false;
        }

        public String toString() {
            return "x = " + x + " y = " + y;
        }
    }

    private boolean isValid(int i,int j,boolean[][] markMat) {
        return (i>=0 && i<x && j>=0 && j<y && map[i][j]==0 && !markMat[i][j]);
    }

    //Construct Parentship LinkedList
    public move getMove(int x, int y, int tx, int ty) {

        //already reached
        if(x==tx && y==ty){
            return move.NONE;
        }

        MazeCell[][] mazeCellTable = new MazeCell[x][y];
        Point[][] parentTable = new Point[x][y];
        boolean[][] markMat = new boolean[x][y];

        for (int ii = 0; ii < x; ii++) {
            for (int jj = 0; jj < y; jj++) {
                markMat[ii][jj] = false;
            }
        }

        MazeCell[] Q = new MazeCell[2000];
        int size = 1;

        MazeCell start = new MazeCell(x, y);
        mazeCellTable[x][y] = start;
        Q[0] = start;
        markMat[x][y] = true;

        for (int k = 0; k < size; k++) {
            int i = Q[k].x;
            int j = Q[k].y;

            //LEFT
            if (isValid(i - 1, j, markMat)) {
                MazeCell m = new MazeCell(i - 1, j);
                mazeCellTable[i - 1][j] = m;
                Q[size] = m;
                size++;
                markMat[i - 1][j] = true;
                parentTable[i - 1][j] = new Point(i, j);
            }
            //DOWN
            if (isValid(i, j + 1, markMat)) {
                MazeCell m = new MazeCell(i, j + 1);
                mazeCellTable[i][j + 1] = m;
                Q[size] = m;
                size++;
                markMat[i][j + 1] = true;
                parentTable[i][j + 1] = new Point(i, j);
            }
            //RIGHT
            if (isValid(i + 1, j, markMat)) {
                MazeCell m = new MazeCell(i + 1, j);
                mazeCellTable[i + 1][j] = m;
                Q[size] = m;
                size++;
                markMat[i + 1][j] = true;
                parentTable[i + 1][j] = new Point(i, j);
            }
            //UP
            if (isValid(i, j - 1, markMat)) {
                MazeCell m = new MazeCell(i, j - 1);
                mazeCellTable[i][j - 1] = m;
                Q[size] = m;
                size++;
                markMat[i][j - 1] = true;
                parentTable[i][j - 1] = new Point(i, j);
            }
        }

        int ttx = tx;
        int tty = ty;
        MazeCell t = mazeCellTable[ttx][tty];
        MazeCell tl = null;
        while (t != start) {
            Point tp = parentTable[ttx][tty];
            ttx = tp.x;
            tty = tp.y;
            tl = t;
            t = mazeCellTable[ttx][tty];
        }

        if (x == tl.x - 1 && y == tl.y) {
            return move.RIGHT;
        }
        if (x == tl.x + 1 && y == tl.y) {
            return move.LEFT;
        }
        if (x == tl.x && y == tl.y - 1) {
            return move.DOWN;
        }
        if (x == tl.x && y == tl.y + 1) {
            return move.UP;
        }
        return move.NONE;
    }

}