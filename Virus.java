import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public abstract class Virus {
    //Anim Vars
    Timer animTimer;
    ActionListener animAL;

    //Pending Vars
    Timer pendingTimer;
    ActionListener pendingAL;

    //Move Vars
    Timer moveTimer;
    ActionListener moveAL;
    public move activeMove;
    protected boolean isStuck = true;
    boolean isPending = false;

    Timer unWeakenTimer1;
    Timer unWeakenTimer2;
    ActionListener unweak1;
    ActionListener unweak2;
    int unweakBlinks;
    boolean isWhite = false;

    protected boolean isWeak = false;
    protected boolean isDead = false;

    public boolean isWeak() {
        return isWeak;
    }

    public boolean isDead() {
        return isDead;
    }

    //Image[] pac;
    Image virusImg;
    int activeImage = 0;
    int addFactor = 1;

    public Point pixelPosition;
    public Point logicalPosition;

    Image[] virusR;
    Image[] virusL;
    Image[] virusU;
    Image[] virusD;

    Image[] virusW;
    Image[] virusWW;
    Image virusEye;

    int virusNormalDelay;
    int virusWeakDelay = 30;
    int virusDeadDelay = 5;

    BFSFinder baseReturner;

    protected GameBoard parentBoard;

    public Virus(int x, int y, GameBoard pb, int virusDelay) {

        logicalPosition = new Point(x,y);
        pixelPosition = new Point(28*x,28*y);

        parentBoard = pb;

        activeMove = move.RIGHT;

        virusNormalDelay = virusDelay;

        loadImages();

        //load weak Image
        virusW = new Image[2];
        try {
            virusW[0] = ImageIO.read(new FileInputStream("resources/images/virus/red.png"));
            virusW[1] = ImageIO.read(new FileInputStream("resources/images/virus/sneeze.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        virusWW = new Image[2];
        try {
            virusWW[0] = ImageIO.read(new FileInputStream("resources/images/virus/corona.png"));
            virusWW[1] = ImageIO.read(new FileInputStream("resources/images/virus/corona.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            virusEye = ImageIO.read(new FileInputStream("resources/images/eye.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //animation timer
        animAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                activeImage = (activeImage + 1) % 2;
            }
        };
        animTimer = new Timer(100,animAL);
        animTimer.start();

        moveAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

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
                        parentBoard.dispatchEvent(new ActionEvent(this, Msg.UPDATE,null));
                    }


                    activeMove = getMoveAI();
                    isStuck = true;

                }else{
                    isStuck = false;

                }

                //TODO : fix virus movements
                switch(activeMove){
                    case RIGHT:
                        if(pixelPosition.x >= (parentBoard.m_x-1) * 28){
                            return;
                        }
                        if((logicalPosition.x+1 < parentBoard.m_x) && (parentBoard.map[logicalPosition.x+1][logicalPosition.y]>0) && ((parentBoard.map[logicalPosition.x+1][logicalPosition.y]<26)||isPending)){
                            return;
                        }
                        pixelPosition.x ++;
                        break;
                    case LEFT:
                        if(pixelPosition.x <= 0){
                            return;
                        }
                        if((logicalPosition.x-1 >= 0) && (parentBoard.map[logicalPosition.x-1][logicalPosition.y]>0) && ((parentBoard.map[logicalPosition.x-1][logicalPosition.y]<26)||isPending)){
                            return;
                        }
                        pixelPosition.x --;
                        break;
                    case UP:
                        if(pixelPosition.y <= 0){
                            return;
                        }
                        if((logicalPosition.y-1 >= 0) && (parentBoard.map[logicalPosition.x][logicalPosition.y-1]>0) && ((parentBoard.map[logicalPosition.x][logicalPosition.y-1]<26)||isPending)){
                            return;
                        }
                        pixelPosition.y--;
                        break;
                    case DOWN:
                        if(pixelPosition.y >= (parentBoard.m_y-1) * 28){
                            return;
                        }
                        if((logicalPosition.y+1 < parentBoard.m_y) && (parentBoard.map[logicalPosition.x][logicalPosition.y+1]>0) && ((parentBoard.map[logicalPosition.x][logicalPosition.y+1]<26)||isPending)){
                            return;
                        }
                        pixelPosition.y ++;
                        break;
                }

                parentBoard.dispatchEvent(new ActionEvent(this, Msg.COLTEST,null));
            }
        };
        moveTimer = new Timer(virusDelay,moveAL);
        moveTimer.start();

        unweak1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unWeakenTimer2.start();
                unWeakenTimer1.stop();
            }
        };
        unWeakenTimer1 = new Timer(7000,unweak1);

        unweak2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(unweakBlinks == 10){
                    unweaken();
                    unWeakenTimer2.stop();
                }
                if(unweakBlinks % 2 == 0){
                    isWhite = true;
                }else{
                    isWhite = false;
                }
                unweakBlinks++;
            }
        };
        unWeakenTimer2 = new Timer(250,unweak2);


        pendingAL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPending = false;
                pendingTimer.stop();
            }
        };
        pendingTimer = new Timer(7000,pendingAL);

        baseReturner = new BFSFinder(pb);
        //start AI
        activeMove = getMoveAI();

    }

    //load Images from Resource
    public abstract void loadImages();

    //get Move Based on AI
    public abstract move getMoveAI();

    //get possible Moves
    public ArrayList<move> getPossibleMoves(){
        ArrayList<move> possibleMoves = new ArrayList<>();

        if(logicalPosition.x >= 0 && logicalPosition.x < parentBoard.m_x-1 && logicalPosition.y >= 0 && logicalPosition.y < parentBoard.m_y-1 ) {
            //System.out.println(this.toString());
            if (!(parentBoard.map[logicalPosition.x + 1][logicalPosition.y] > 0)) {
                possibleMoves.add(move.RIGHT);
            }

            if (!(parentBoard.map[logicalPosition.x - 1][logicalPosition.y] > 0)) {
                possibleMoves.add(move.LEFT);
            }

            if(!(parentBoard.map[logicalPosition.x][logicalPosition.y-1]>0)){
                possibleMoves.add(move.UP);
            }

            if(!(parentBoard.map[logicalPosition.x][logicalPosition.y+1]>0)){
                possibleMoves.add(move.DOWN);
            }
        }

        return possibleMoves;
    }

    public Image getVirusImage(){
        if(!isDead) {
            if (!isWeak) {
                switch (activeMove) {
                    case RIGHT:
                        return virusR[activeImage];
                    case LEFT:
                        return virusL[activeImage];
                    case UP:
                        return virusU[activeImage];
                    case DOWN:
                        return virusD[activeImage];
                }
                return virusR[activeImage];
            } else {
                if (isWhite) {
                    return virusWW[activeImage];
                } else {
                    return virusW[activeImage];
                }
            }
        }else{
            return virusEye;
        }
    }


    public void weaken(){
        isWeak = true;
        moveTimer.setDelay(virusWeakDelay);
        unweakBlinks = 0;
        isWhite = false;
        unWeakenTimer1.start();
    }

    public void unweaken(){
        isWeak = false;
        moveTimer.setDelay(virusNormalDelay);
    }

    public void die(){
        isDead = true;
        moveTimer.setDelay(virusNormalDelay);
    }

    public void undie(){
        //Shift Left Or Right
        int r = ThreadLocalRandom.current().nextInt(3);
        if (r == 0) {
            //Do nothing
        }
        if(r==1){
            logicalPosition.x += 1;
            pixelPosition.x += 28;
        }
        if(r==2){
            logicalPosition.x -= 1;
            pixelPosition.x -= 28;
        }
        isPending = true;
        pendingTimer.start();

        isDead = false;
        isWeak = false;
        moveTimer.setDelay(virusNormalDelay);
    }

}
