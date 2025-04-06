package ecs.Components;

public class Win extends Component{

    public Win(){

    }
    @Override
    public Component copy() {
        return new Win();
    }
}
