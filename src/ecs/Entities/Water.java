package ecs.Entities;
import edu.usu.graphics.Texture;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Water {
    public static Entity create(Texture square, int x, int y){


        var water = new Entity();

        water.add(new ecs.Components.Appearance(square, Color.CORNFLOWER_BLUE));
        water.add(new ecs.Components.Position(x, y));
        water.add(new ecs.Components.Collision());

        return water;
    }

}
