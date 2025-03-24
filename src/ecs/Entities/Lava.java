package ecs.Entities;
import edu.usu.graphics.Texture;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Lava {
    public static Entity create(Texture square, int x, int y){

        var lava = new Entity();

        lava.add(new ecs.Components.Appearance(square, new Color(1.0f, 0.5f, 0.0f)));
        lava.add(new ecs.Components.Position(x, y));
        lava.add(new ecs.Components.Collision());
        lava.add(new ecs.Components.Object());


        return lava;
    }

}
