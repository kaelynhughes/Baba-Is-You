package ecs.Entities;
import edu.usu.graphics.Texture;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class Rock {
    public static Entity create(Texture square, int x, int y){


        var rock = new Entity();

        rock.add(new ecs.Components.Appearance(square, Color.YELLOW));
        rock.add(new ecs.Components.Position(x, y));
        rock.add(new ecs.Components.Collision());
        rock.add(new ecs.Components.Object());

        return rock;
    }

}
