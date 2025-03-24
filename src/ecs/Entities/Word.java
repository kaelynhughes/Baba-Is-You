package ecs.Entities;
import edu.usu.graphics.Texture;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Word {
    public static Entity create(Texture square, int x, int y,String text){


        var word = new Entity();

        word.add(new ecs.Components.Appearance(square, Color.WHITE));
        word.add(new ecs.Components.Position(x, y));
        word.add(new ecs.Components.Collision());

        word.add(new ecs.Components.Push());
        word.add(new ecs.Components.Text(text));
        word.add(new ecs.Components.Object());
        word.add(new ecs.Components.Movable(ecs.Components.Movable.Direction.Stopped));

        return word;
    }
}
