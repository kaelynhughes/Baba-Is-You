package ecs.Components;

public class PlayerInput extends Component {

    public Movable.Direction currentDirection = Movable.Direction.Stopped;

    @Override
    public Component copy() {
        return new PlayerInput(); // doesn't copy
    }
}

