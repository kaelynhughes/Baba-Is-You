package ecs.Components;

public class Text extends Component {
    public String text;
    public Text(String text) {
        this.text = text;
    }
    @Override
    public Component copy() {
        return new Text(this.text);
    }
}
