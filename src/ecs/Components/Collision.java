package ecs.Components;

/**
 * The class itself is all the information needed, therefore, no data
 * associated with this component.
 */
public class Collision extends Component {
    @Override
    public Component copy() {
        return new Collision();
    }
}
