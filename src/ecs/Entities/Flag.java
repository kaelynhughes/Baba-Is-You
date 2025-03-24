package ecs.Entities;
import edu.usu.graphics.Texture;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Flag {
    public static Entity create(Texture square, int x, int y){


        var flag = new Entity();

        flag.add(new ecs.Components.Appearance(square, Color.YELLOW));
        flag.add(new ecs.Components.Position(x, y));
        flag.add(new ecs.Components.Collision());
        flag.add(new ecs.Components.Object());

        return flag;
    }

}
