package ecs;

import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.*;

import ecs.Components.Movable;
import ecs.Components.Movable.Direction;

// using the Singleton design pattern to ensure that no two registries ever conflict
// & to avoid passing the same instance through like every constructor
public class ControlRegistry {
    private static ControlRegistry registry;
    private List<ControlInfo> controls;
    private ControlRegistry() {
        controls = new ArrayList<>();

        controls.add(new ControlInfo("Up", GLFW_KEY_UP, Direction.Up));
        controls.add(new ControlInfo("Down", GLFW_KEY_DOWN, Direction.Down));
        controls.add(new ControlInfo("Left", GLFW_KEY_LEFT, Direction.Left));
        controls.add(new ControlInfo("Right", GLFW_KEY_RIGHT, Direction.Right));

        controls.add(new ControlInfo("Undo", GLFW_KEY_Z, Direction.Undo));
        controls.add(new ControlInfo("Reset", GLFW_KEY_R, Direction.Reset));
    }

    public static ControlRegistry getInstance() {
        if (registry == null) {
            registry = new ControlRegistry();
        }
        return registry;
    }

    public static class ControlInfo {
        final String display;
        Integer key;
        Direction outcome;

        public ControlInfo(String display, Integer key, Direction outcome) {
            this.display = display;
            this.key = key;
            this.outcome = outcome;
        }

        public void updateKey(Integer newKey) {
            // deregister current key
            this.key = newKey;
            // register new key
        }

        public Integer getKey() {
            return key;
        }

        public String getDisplay() {
            return display;
        }

        public Direction getOutcome() {
            return outcome;
        }
    }

    public Map<Integer, Movable.Direction> getControlMap() {
        return controls.stream().collect(Collectors.toMap(ControlInfo::getKey, ControlInfo::getOutcome));
    }
}
