import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;

public class Covman implements KeyListener{

    //Move Vars
    Timer moveTimer;
    ActionListener moveAL;
    public move activeMove;
    move toMove;
    boolean isStuck = true;

    //Animation Vars
    Timer animTimer;
    ActionListener animAL;
    Image[] cov;
    int activeImage = 0;
    int addFactor = 1;

    public Point pixelPosition;
    public Point logicalPosition;

    private GameBoard parentBoard;


    public Covman(int x, int y, GameBoard pb) {

        logicalPosition = new Point(x,y);
        pixelPosition = new Point(28*x,28*y);

        parentBoard = pb;

        cov = new Image[5];

        activeMove = move.NONE;
        toMove = move.NONE;

        try {
            cov[0] = ImageIO.read(new FileInputStream("resources/images/pac/pac0.png"));
            cov[1] = ImageIO.read(new FileInputStream("resources/images/pac/pac1.png"));
            cov[2] = ImageIO.read(new FileInputStream("resources/images/pac/pac2.png"));
            cov[3] = ImageIO.read(new FileInputStream("resources/images/pac/pac3.png"));
            cov[4] = ImageIO.read(new FileInputStream("resources/images/pac/pac4.png"));
        }catch(IOException e){
            System.err.println("Cannot Read Images !");
        }

        //animation timer
        animAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                activeImage = activeImage + addFactor;
                if(activeImage==4 || activeImage==0){
                    addFactor *= -1;
                }
            }
        };
        animTimer = new Timer(40,animAL);
        animTimer.start();


        moveAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                //update logical position
                if((pixelPosition.x % 28 == 0) && (pixelPosition.y % 28 == 0)){
                    if(!isStuck) {
                        switch (activeMove) {
                            case RIGHT:
                                logicalPosition.x++;
                                break;
                            case LEFT:
                                logicalPosition.x--;
                                break;
                            case UP:
                                logicalPosition.y--;
                                break;
                            case DOWN:
                                logicalPosition.y++;
                                break;
                        }
                        //send update message
                        parentBoard.dispatchEvent(new ActionEvent(this, Msg.UPDATE,null));
                    }
                    isStuck = true;
                    animTimer.stop();

                    if(toMove != move.NONE && isPossibleMove(toMove) ) {
                        activeMove = toMove;
                        toMove = move.NONE;
                    }
                }else{
                    isStuck = false;
                    animTimer.start();
                }

                switch(activeMove){
                    case RIGHT:
                        if((pixelPosition.x >= (parentBoard.m_x-1) * 28)&&parentBoard.isCustom){
                            return;
                        }

                        if(logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x-1 && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y-1 ) {
                            if (parentBoard.map[logicalPosition.x + 1][logicalPosition.y] > 0) {
                                return;
                            }
                        }
                        pixelPosition.x ++;
                        break;
                    case LEFT:
                        if((pixelPosition.x <= 0)&&parentBoard.isCustom){
                            return;
                        }

                        if(logicalPosition.x > 0 && logicalPosition.x < parentBoard.m_x-1 && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y-1 ) {
                            if (parentBoard.map[logicalPosition.x - 1][logicalPosition.y] > 0) {
                                return;
                            }
                        }
                        pixelPosition.x--;
                        break;
                    case UP:
                        if((pixelPosition.y <= 0)&&parentBoard.isCustom){
                            return;
                        }

                        if(logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x-1 && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y-1 ) {
                            if(parentBoard.map[logicalPosition.x][logicalPosition.y-1]>0){
                                return;
                            }
                        }
                        pixelPosition.y--;
                        break;
                    case DOWN:
                        if((pixelPosition.y >= (parentBoard.m_y-1) * 28)&&parentBoard.isCustom){
                            return;
                        }

                        if(logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x-1 && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y-1 ) {
                            if(parentBoard.map[logicalPosition.x][logicalPosition.y+1]>0){
                                return;
                            }
                        }
                        pixelPosition.y ++;
                        break;
                }

                //send Messege to CovidBoard to check collision
                parentBoard.dispatchEvent(new ActionEvent(this, Msg.COLTEST,null));

            }
        };
        moveTimer = new Timer(9,moveAL);
        moveTimer.start();

    }

    public boolean isPossibleMove(move move){
        if(logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x-1 && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y-1 ) {
            switch(move){
                case RIGHT:
                    return !(parentBoard.map[logicalPosition.x + 1][logicalPosition.y] > 0);
                case LEFT:
                    return !(parentBoard.map[logicalPosition.x - 1][logicalPosition.y] > 0);
                case UP:
                    return !(parentBoard.map[logicalPosition.x][logicalPosition.y - 1] > 0);
                case DOWN:
                    return !(parentBoard.map[logicalPosition.x][logicalPosition.y+1] > 0);
            }
        }
        return false;
    }

    public Image getCovidImage(){
        return cov[activeImage];
    }

    @Override
    public void keyReleased(KeyEvent ke){
        //
    }

    @Override
    public void keyTyped(KeyEvent ke){
        //
    }

    //Handle Arrow Keys
    @Override
    public void keyPressed(KeyEvent ke){
        switch(ke.getKeyCode()){
            case 37:
                toMove = move.LEFT;
                break;
            case 38:
                toMove = move.UP;
                break;
            case 39:
                toMove = move.RIGHT;
                break;
            case 40:
                toMove = move.DOWN;
                break;
            case 82:
                parentBoard.dispatchEvent(new ActionEvent(this, Msg.RESET,null));
                break;
        }

    }


}
