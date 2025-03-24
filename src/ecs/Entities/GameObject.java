package ecs.Entities;

import ecs.Components.Object;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;
import ecs.Components.*;

public class GameObject {

    public static Entity create(String type, Texture texture, Color color, int x, int y) {
        var obj = new Entity();

        obj.add(new ecs.Components.Position(x, y));
        obj.add(new ecs.Components.Movable(ecs.Components.Movable.Direction.Stopped));
        obj.add(new ecs.Components.Collision());
        obj.add(new ecs.Components.Appearance(texture, color));
        obj.add(new ecs.Components.Object());

        obj.add(new ecs.Components.Tag(type)); // Use a generic tag component

        return obj;
    }
}