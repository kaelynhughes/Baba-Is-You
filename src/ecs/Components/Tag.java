package ecs.Components;

public class Tag extends Component {
    public String name;

    public Tag(String name) {
        this.name = name;
    }
    @Override
    public Component copy() {
        return new Tag(this.name);
    }
}