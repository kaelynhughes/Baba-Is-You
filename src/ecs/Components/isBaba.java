package ecs.Components;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class isBaba extends Component {

    public Texture image;
    public Color color;

    public isBaba(){
        var baba_image = new Texture("resources/images/square-outline.png");

        this.color = Color.PURPLE;
        this.image = baba_image;

    }




}
