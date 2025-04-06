package ecs.Components;

public class Object extends Component {

    public Object (){

    }

    @Override
    public Component copy() {
        return new Object();
    }
}
