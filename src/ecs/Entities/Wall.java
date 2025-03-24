package ecs.Entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class Wall {
    public static Entity create(Texture square, int x, int y) {
        var wall = new Entity();

        wall.add(new ecs.Components.Appearance(square, Color.GREEN));
        wall.add(new ecs.Components.Position(x,y));
        wall.add(new ecs.Components.Collision());
        wall.add(new ecs.Components.Object());
        return wall;
    }
}
