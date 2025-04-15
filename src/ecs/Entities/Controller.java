package ecs.Entities;

import ecs.Components.Movable;
import ecs.ControlRegistry;
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
        ControlRegistry controlRegistry = ControlRegistry.getInstance();
        controller.add(new ecs.Components.KeyboardControlled(controlRegistry.getControlMap()));
    }
}
