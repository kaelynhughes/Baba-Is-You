package ecs.Entities;

import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Controller {
    public static Entity create(){

        var controller = new Entity();
        enableControls(controller);
        controller.add(new ecs.Components.PlayerInput());
        return controller;
    }
    public static void enableControls(Entity controller) {
        controller.add(new ecs.Components.KeyboardControlled(
                Map.of(
                        GLFW_KEY_UP, ecs.Components.Movable.Direction.Up,
                        GLFW_KEY_DOWN, ecs.Components.Movable.Direction.Down,
                        GLFW_KEY_LEFT, ecs.Components.Movable.Direction.Left,
                        GLFW_KEY_RIGHT, ecs.Components.Movable.Direction.Right
                )));
    }
}
