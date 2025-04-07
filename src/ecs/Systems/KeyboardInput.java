package ecs.Systems;

import ecs.Components.Movable;


import static java.lang.System.out;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class KeyboardInput extends System {

    private final long window;
    private boolean pressed;
    public KeyboardInput(long window) {
        super(ecs.Components.KeyboardControlled.class,ecs.Components.PlayerInput.class);
        pressed = false;
        this.window = window;
    }

    @Override
    public void update(double gameTime) {

        int i = 0;
        for (var entity : entities.values()) {

            i +=1;

            var input = entity.get(ecs.Components.KeyboardControlled.class);
            var play_input = entity.get(ecs.Components.PlayerInput.class);

            play_input.currentDirection = Movable.Direction.Stopped;

            if (glfwGetKey(window, input.lookup.get(Movable.Direction.Up)) == GLFW_PRESS) {
                if (!pressed) {
                    play_input.currentDirection  = input.keys.get(GLFW_KEY_UP);
                    pressed = true;
                }
            }
            else if (glfwGetKey(window, input.lookup.get(Movable.Direction.Down)) == GLFW_PRESS) {
                if (!pressed) {
                    play_input.currentDirection = input.keys.get(GLFW_KEY_DOWN);
                    pressed = true;
                }
            }
            else if (glfwGetKey(window, input.lookup.get(Movable.Direction.Left)) == GLFW_PRESS) {
                if (!pressed) {
                    play_input.currentDirection = input.keys.get(GLFW_KEY_LEFT);
                    pressed = true;
                }
            }
            else if (glfwGetKey(window, input.lookup.get(Movable.Direction.Right)) == GLFW_PRESS) {
                if (!pressed) {
                    play_input.currentDirection = input.keys.get(GLFW_KEY_RIGHT);
                    pressed = true;
                }
            }
            else if (glfwGetKey(window, input.lookup.get(Movable.Direction.Undo)) == GLFW_PRESS) {
                if (!pressed) {
                    play_input.currentDirection = input.keys.get(GLFW_KEY_Z);
                    pressed = true;
                }
            }
            else if (glfwGetKey(window, input.lookup.get(Movable.Direction.Reset)) == GLFW_PRESS) {
                if (!pressed) {
                    play_input.currentDirection = input.keys.get(GLFW_KEY_R);
                    pressed = true;
                }
            }
            else {

                pressed = false;
            }
        }
    }
}
