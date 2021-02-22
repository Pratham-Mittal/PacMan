import java.awt.*;

public class PowerUp {

    public Point position;

    public PowerUp(int x, int y, int type){
        position = new Point(x,y);
        this.type = type;
    }

    public int type;

}
