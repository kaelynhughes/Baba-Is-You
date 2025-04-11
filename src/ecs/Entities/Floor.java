package ecs.Entities;

import ecs.Entities.Entity;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

public class Floor{

    public static Entity create(Texture square, int x, int y) {
        var floor = new Entity();

        floor.add(new ecs.Components.Appearance(square, new Color(1.0f, 0.5f, 0.0f)));
        floor.add(new ecs.Components.Position(x, y));
        floor.add(new ecs.Components.Collision());
        floor.add(new ecs.Components.Object());

        return floor;
    }
}
