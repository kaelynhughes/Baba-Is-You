package ecs.Components;
import java.lang.System;
public class Movable extends Component {


    public enum Direction {
        Stopped,
        Up,
        Down,
        Left,
        Right,

        Undo
    }

    public Direction facing;
    public double moveInterval; // seconds
    public double elapsedInterval;

    public Movable(Direction facing) {
        this.facing = facing;
        this.moveInterval = moveInterval;

    }

    @Override
    public Component copy() {
        return new Movable(this.facing);
    }

}
