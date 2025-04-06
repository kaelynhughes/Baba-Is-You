package ecs.Components;

public class PlayerControlled extends Component{
    /**
     * The class itself is all the information needed, therefore, no data
     * associated with this component.
     */
    @Override
    public Component copy() {
        return new PlayerControlled();
    }
}
