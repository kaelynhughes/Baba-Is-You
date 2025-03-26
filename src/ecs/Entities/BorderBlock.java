package ecs.Entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class BorderBlock {

    public static Entity create( int x, int y) {
        var border = new Entity();

        border.add(new ecs.Components.Position(x,y));
        border.add(new ecs.Components.Collision());
        border.add(new ecs.Components.Stop());


        return border;
    }
}
