package ecs.Components;

public class Sink extends Component{

    public Sink(){

    }
    @Override
    public Component copy() {
        return new Sink();
    }
}
