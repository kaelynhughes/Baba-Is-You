package ecs.Components;

import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class Position extends Component {

    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(int x, int y){
        this.x += x;
        this.y += y;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public Component copy() {
        return new Position(this.x,this.y);
    }
}
