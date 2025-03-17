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
        baba.add(new ecs.Components.PlayerControlled());
        baba.add(new ecs.Components.Stop());
        baba.add(new ecs.Components.Movable(ecs.Components.Movable.Direction.Stopped));

        return baba;
    }
    public static void enableControls(Entity baba) {
        baba.add(new ecs.Components.KeyboardControlled(
                Map.of(
                        GLFW_KEY_UP, ecs.Components.Movable.Direction.Up,
                        GLFW_KEY_DOWN, ecs.Components.Movable.Direction.Down,
                        GLFW_KEY_LEFT, ecs.Components.Movable.Direction.Left,
                        GLFW_KEY_RIGHT, ecs.Components.Movable.Direction.Right
                )));
    }
}
