package ecs.Entities;
import edu.usu.graphics.Texture;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Baba {
    public static Entity create(Texture square, int x, int y){


        var baba = new Entity();

        baba.add(new ecs.Components.Appearance(square, Color.PURPLE));
        baba.add(new ecs.Components.Position(x, y));
        baba.add(new ecs.Components.Collision());
        baba.add(new ecs.Components.Object());

        return baba;
    }
}
