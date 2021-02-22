import java.awt.*;

public class TeleportTunnel {

    private Point from;
    private Point to;
    private move reqMove;

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
    }

    public move getReqMove() {
        return reqMove;
    }

    public void setReqMove(move reqMove) {
        this.reqMove = reqMove;
    }

    public TeleportTunnel(int x1, int y1, int x2, int y2, move reqMove){
        from = new Point(x1,y1);
        to = new Point(x2,y2);
        this.reqMove = reqMove;
    }
}
