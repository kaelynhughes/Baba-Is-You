package ecs.Components;

public class Push extends Component{

    public Push(){

    }

    @Override
    public Component copy() {
        return new Push();
    }
}
