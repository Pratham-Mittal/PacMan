import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.ArrayList;

public class GameBoard extends JPanel{


    Timer redrawTimer;
    ActionListener redrawAL;

    int[][] map;
    Image[] mapSegments;

    Image foodImage;
    Image[] pfoodImage;

    Image goImage;
    Image vicImage;

    Covman covman;
    ArrayList<Food> foods;
    ArrayList<PowerUp> pufoods;
    ArrayList<Virus> virus;
    ArrayList<TeleportTunnel> teleports;

    boolean isCustom = false;
    boolean isGameOver = false;
    boolean isWin = false;
    boolean drawScore = false;
    boolean clearScore = false;
    int scoreToAdd = 0;

    int score;
    JLabel scoreboard;

    LoopPlayer siren;
    boolean mustReactivateSiren = false;
    LoopPlayer pac6;

    public Point virusBase;

    public int m_x;
    public int m_y;

    MapData md_backup;
    GameWindow windowParent;

    public GameBoard(JLabel scoreboard, MapData md, GameWindow pw){
        this.scoreboard = scoreboard;
        this.setDoubleBuffered(true);
        md_backup = md;
        windowParent = pw;

        m_x = md.getX();
        m_y = md.getY();
        this.map = md.getMap();

        this.isCustom = md.isCustom();
        this.virusBase = md.getVirusBasePosition();

        //loadMap();

        covman = new Covman(md.getCovPosition().x,md.getCovPosition().y,this);
        addKeyListener(covman);

        foods = new ArrayList<>();
        pufoods = new ArrayList<>();
        virus = new ArrayList<>();
        teleports = new ArrayList<>();

        //TODO : read food from mapData (Map 1)

        if(!isCustom) {
            for (int i = 0; i < m_x; i++) {
                for (int j = 0; j < m_y; j++) {
                    if (map[i][j] == 0)
                        foods.add(new Food(i, j));
                }
            }
        }else{
            foods = md.getFoodPositions();
        }



        pufoods = md.getPufoodPositions();

        virus = new ArrayList<>();
        for(Data gd : md.getVirussData()){
            switch(gd.getType()) {
                case RED:
                    virus.add(new RedVirus(gd.getX(), gd.getY(), this));
                    break;
                case PINK:
                    virus.add(new GreenVirus(gd.getX(), gd.getY(), this));
                    break;
                case CYAN:
                    virus.add(new BlueVirus(gd.getX(), gd.getY(), this));
                    break;
            }
        }

        teleports = md.getTeleports();

        setLayout(null);
        setSize(20*m_x,20*m_y);
        setBackground(Color.black);

        mapSegments = new Image[28];
        mapSegments[0] = null;
        for(int ms=1;ms<28;ms++){
            try {
                mapSegments[ms] = ImageIO.read(new FileInputStream("resources/images/map segment/"+ms+".png"));
            }catch(Exception e){}
        }

        pfoodImage = new Image[5];
        for(int ms=0 ;ms<5;ms++){
            try {
                pfoodImage[ms] = ImageIO.read(new FileInputStream("resources/images/food/"+ms+".png"));
            }catch(Exception e){}
        }
        try{
            foodImage = ImageIO.read(new FileInputStream("resources/images/food.png"));
            goImage = ImageIO.read(new FileInputStream("resources/images/game_over.png"));
            vicImage = ImageIO.read(new FileInputStream("resources/images/victory.png"));

        }catch(Exception e){}


        redrawAL = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //Draw Board
                repaint();
            }
        };
        redrawTimer = new Timer(16,redrawAL);
        redrawTimer .start();

        SoundPlayer.play("pacman_start.wav");
        siren = new LoopPlayer("siren.wav");
        pac6 = new LoopPlayer("pac6.wav");
        siren.start();
    }

    private void collisionTest(){
        Rectangle pr = new Rectangle(covman.pixelPosition.x+13,covman.pixelPosition.y+13,2,2);
        Virus virusToRemove = null;
        for(Virus g : virus){
            Rectangle gr = new Rectangle(g.pixelPosition.x,g.pixelPosition.y,28,28);

            if(pr.intersects(gr)){
                if(!g.isDead()) {
                    if (!g.isWeak()) {
                        //Game Over
                        siren.stop();
                        SoundPlayer.play("pacman_lose.wav");
                        covman.moveTimer.stop();
                        covman.animTimer.stop();
                        g.moveTimer.stop();
                        isGameOver = true;
                        scoreboard.setText("    Press R to try again !");
                        scoreboard.setForeground(Color.red);
                        break;
                    } else {
                        //Eat Virus
                        SoundPlayer.play("pacman_eatghost.wav");
                        //getGraphics().setFont(new Font("Arial",Font.BOLD,20));
                        drawScore = true;
                        scoreToAdd++;
                        if(virus!=null)
                            g.die();
                        else
                            virusToRemove = g;
                    }
                }
            }
        }

        if(virusToRemove!= null){
            virus.remove(virusToRemove);
        }
    }

    private void update(){

        Food foodToEat = null;
        //Check food eat
        for(Food f : foods){
            if(covman.logicalPosition.x == f.position.x && covman.logicalPosition.y == f.position.y)
                foodToEat = f;
        }
        if(foodToEat!=null) {
            SoundPlayer.play("pacman_eatfruit.wav");
            foods.remove(foodToEat);
            score ++;
            scoreboard.setText("    Score : "+score);

            if(foods.size() == 0){
                siren.stop();
                pac6.stop();
                SoundPlayer.play("pacman_intermission.wav");
                isWin = true;
                covman.moveTimer.stop();
                for(Virus v : virus){
                    v.moveTimer.stop();
                }
            }
        }

        PowerUp puFoodToEat = null;
        //Check pu food eat
        for(PowerUp puf : pufoods){
            if(covman.logicalPosition.x == puf.position.x && covman.logicalPosition.y == puf.position.y)
                puFoodToEat = puf;
        }
        if(puFoodToEat!=null) {
            SoundPlayer.play("pacman_eat.wav");
            switch(puFoodToEat.type) {
                case 0:
                    //PACMAN 6
                    pufoods.remove(puFoodToEat);
                    siren.stop();
                    mustReactivateSiren = true;
                    pac6.start();
                    for (Virus v : virus) {
                        v.weaken();
                    }
                    scoreToAdd = 0;
                    break;
                default:
                    SoundPlayer.play("pacman_eatfruit.wav");
                    pufoods.remove(puFoodToEat);
                    scoreToAdd = 1;
                    drawScore = true;
            }

        }

        //Check Virus Undie
        for(Virus v:virus){
            if(v.isDead() && v.logicalPosition.x == virusBase.x && v.logicalPosition.y == virusBase.y){
                v.undie();
            }
        }

        //Check Teleport
        for(TeleportTunnel tp : teleports) {
            if (covman.logicalPosition.x == tp.getFrom().x && covman.logicalPosition.y == tp.getFrom().y && covman.activeMove == tp.getReqMove()) {
                //System.out.println("TELE !");
                covman.logicalPosition = tp.getTo();
                covman.pixelPosition.x = covman.logicalPosition.x * 28;
                covman.pixelPosition.y = covman.logicalPosition.y * 28;
            }
        }

        //Check isSiren
        boolean isSiren = true;
        for(Virus g:virus){
            if(g.isWeak()){
                isSiren = false;
            }
        }
        if(isSiren){
            pac6.stop();
            if(mustReactivateSiren){
                mustReactivateSiren = false;
                siren.start();
            }

        }



    }

    public void restart(){

        siren.stop();

        new GameWindow();
        windowParent.dispose();


    }
    @Override
    public void processEvent(AWTEvent ae){

        if(ae.getID()== Msg.UPDATE) {
            update();
        }else if(ae.getID()== Msg.COLTEST) {
            if (!isGameOver) {
                collisionTest();
            }
        }else if(ae.getID()== Msg.RESET){
            if(isGameOver)
                restart();
        }else {
            super.processEvent(ae);
        }
    }






}
