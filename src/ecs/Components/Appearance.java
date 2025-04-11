package ecs.Components;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class Appearance extends Component {
    public Texture image;
    public Color color;

    public int frame;
    public int frameSize;
    public int frames;

    public Appearance(Texture image, Color color) {
        this.image = image;
        this.color = color;
        this.frames = image.getWidth() / image.getHeight();
        this.frame = 0;
        // all our images are squares
        this.frameSize = image.getHeight();
    }

    public void nextFrame() {
        this.frame++;
        if (this.frame >= this.frames) {
            this.frame = 0;
        }
    }

    @Override
    public Component copy() {
        return new Appearance(this.image,this.color);
    }
}
