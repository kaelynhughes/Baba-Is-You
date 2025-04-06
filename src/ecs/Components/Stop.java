package ecs.Components;

public class Stop extends Component{

    public Stop(){

    }
    @Override
    public Component copy() {
        return new Stop();
    }
}
