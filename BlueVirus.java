import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class BlueVirus extends Virus {

    public BlueVirus(int x, int y, GameBoard pb){
        super(x,y,pb,9);
    }

    @Override
    public void loadImages(){
        virusR = new Image[2];
        virusL = new Image[2];
        virusU = new Image[2];
        virusD = new Image[2];
        try {
            virusR[0] = ImageIO.read(new FileInputStream("resources/images/virus/blue.png"));
            virusR[1] = ImageIO.read(new FileInputStream("resources/images/virus/blue.png"));
            virusL[0] = ImageHelper.flipHoriz(ImageIO.read(new FileInputStream("resources/images/virus/blue.png")));
            virusL[1] = ImageHelper.flipHoriz(ImageIO.read(new FileInputStream("resources/images/virus/blue.png")));
            virusU[0] = ImageIO.read(new FileInputStream("resources/images/virus/blue.png"));
            virusU[1] = ImageIO.read(new FileInputStream("resources/images/virus/blue.png"));
            virusD[0] = ImageIO.read(new FileInputStream("resources/images/virus/blue.png"));
            virusD[1] = ImageIO.read(new FileInputStream("resources/images/virus/blue.png"));
        }catch(IOException e){
            System.err.println("Cannot Read Images !");
        }
    }

    move lastCMove;
    move pendMove = move.UP;

    @Override
    public move getMoveAI(){
        if(isPending){
            if(isStuck){
                if(pendMove == move.UP){
                    pendMove = move.DOWN;
                }else if(pendMove == move.DOWN){
                    pendMove = move.UP;
                }
                return pendMove;
            }else{
                return pendMove;
            }
        }
        if(isDead) {
            return baseReturner.getMove(logicalPosition.x,logicalPosition.y, parentBoard.virusBase.x,parentBoard.virusBase.y);
        }else {
            ArrayList<move> pm = getPossibleMoves();
            int i = ThreadLocalRandom.current().nextInt(pm.size());
            lastCMove = pm.get(i);
            return lastCMove;
        }
    }
}
