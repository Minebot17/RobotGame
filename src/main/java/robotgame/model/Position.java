package robotgame.model;

public class Position {

    public int x;
    public int y;

    public Position(){}

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position){
        x = position.x;
        y = position.y;
    }

    public Position add(int x, int y){
        return add(new Position(x, y));
    }

    public Position add(Position position) {
        return new Position(x + position.x, y + position.y);
    }

    @Override
    public String toString(){
        return "x: " + x + " y: " + y;
    }
}
