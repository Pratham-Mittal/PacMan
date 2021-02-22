import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class RedVirus extends Virus {

    BFSFinder bfs;

    public RedVirus(int x, int y, GameBoard pb){
        super(x,y,pb,12);
    }

    @Override
    public void loadImages(){
        virusR = new Image[2];
        virusL = new Image[2];
        virusU = new Image[2];
        virusD = new Image[2];
        try {
            virusR[0] = ImageIO.read(new FileInputStream("resources/images/virus/red.png"));
            virusR[1] = ImageIO.read(new FileInputStream("resources/images/virus/red.png"));
            virusL[0] = ImageHelper.flipHoriz(ImageIO.read(new FileInputStream("resources/images/virus/red.png")));
            virusL[1] = ImageHelper.flipHoriz(ImageIO.read(new FileInputStream("resources/images/virus/red.png")));
            virusU[0] = ImageIO.read(new FileInputStream("resources/images/virus/red.png"));
            virusU[1] = ImageIO.read(new FileInputStream("resources/images/virus/red.png"));
            virusD[0] = ImageIO.read(new FileInputStream("resources/images/virus/red.png"));
            virusD[1] = ImageIO.read(new FileInputStream("resources/images/virus/red.png"));

        }catch(IOException e){
            System.err.println("Cannot Read Images !");
        }
    }

    move pendingMove = move.UP;

    //find closest path using BFS
    @Override
    public move getMoveAI(){
        if(isPending){
            if(isStuck){
                if(pendingMove == move.UP){
                    pendingMove = move.DOWN;
                }else if(pendingMove == move.DOWN){
                    pendingMove = move.UP;
                }
                return pendingMove;
            }else{
                return pendingMove;
            }
        }
        if(bfs==null)
            bfs = new BFSFinder(parentBoard);
        if(isDead) {
            return baseReturner.getMove(logicalPosition.x,logicalPosition.y, parentBoard.virusBase.x,parentBoard.virusBase.y);
        }else{
            return bfs.getMove(logicalPosition.x,logicalPosition.y,parentBoard.covman.logicalPosition.x,parentBoard.covman.logicalPosition.y);
        }
    }


}
