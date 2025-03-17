package ecs.Entities;
import edu.usu.graphics.Texture;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Rock {
    public static Entity create(Texture square, int x, int y){


        var baba = new Entity();

        baba.add(new ecs.Components.Appearance(square, Color.YELLOW));
        baba.add(new ecs.Components.Position(x, y));
        baba.add(new ecs.Components.Collision());
        baba.add(new ecs.Components.Push());
        baba.add(new ecs.Components.Movable(ecs.Components.Movable.Direction.Stopped));

        return baba;
    }

}
