import java.awt.*;
import java.util.ArrayList;

public class MapData {

    private int x;
    private int y;
    private int[][] map;
    private Point covPosition;
    private Point virusBasePosition;
    private boolean isCustom;
    private ArrayList<Food> foodPositions;
    private ArrayList<PowerUp> pufoodPositions;
    private ArrayList<TeleportTunnel> teleports;
    private ArrayList<Data> virusData;

    public MapData(){
        foodPositions = new ArrayList<>();
        pufoodPositions = new ArrayList<>();
        teleports = new ArrayList<>();
        virusData = new ArrayList<>();
    }

    public MapData(int x,int y){
        this.x = x;
        this.y = y;

        foodPositions = new ArrayList<>();
        pufoodPositions = new ArrayList<>();
        teleports = new ArrayList<>();
        virusData = new ArrayList<>();
    }

    public MapData(int x, int y,int[][] map,Point manPosition){
        this.x = x;
        this.y = y;
        this.map = map;
        covPosition = manPosition;

        foodPositions = new ArrayList<>();
        pufoodPositions = new ArrayList<>();
        teleports = new ArrayList<>();
        virusData = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public Point getCovPosition() {
        return covPosition;
    }

    public void setCovPosition(Point covmanPosition) {
        this.covPosition = covmanPosition;
    }

    public Point getVirusBasePosition() {
        return virusBasePosition;
    }

    public void setGVirusBasePosition(Point virusBasePosition) {
        this.virusBasePosition = virusBasePosition;
    }

    public ArrayList<Food> getFoodPositions() {
        return foodPositions;
    }

    public ArrayList<PowerUp> getPufoodPositions() {
        return pufoodPositions;
    }

    public ArrayList<TeleportTunnel> getTeleports() {
        return teleports;
    }

    public ArrayList<Data> getVirussData() {
        return virusData;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }
}
