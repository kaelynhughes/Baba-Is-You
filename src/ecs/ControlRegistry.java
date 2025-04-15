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
    private final List<ControlInfo> controls;
    private ControlRegistry() {
        controls = new ArrayList<>();

        controls.add(new ControlInfo("Up", GLFW_KEY_W, Direction.Up));
        controls.add(new ControlInfo("Left", GLFW_KEY_A, Direction.Left));
        controls.add(new ControlInfo("Down", GLFW_KEY_S, Direction.Down));
        controls.add(new ControlInfo("Right", GLFW_KEY_D, Direction.Right));

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
        private final String display;
        private Integer key;
        private final Direction outcome;

        public ControlInfo(String display, Integer key, Direction outcome) {
            this.display = display;
            this.key = key;
            this.outcome = outcome;
        }

        private void updateKey(Integer newKey) {
            this.key = newKey;
        }

        public Integer getKey() {
            return key;
        }

        public String getOutcomeDisplay() {
            return display;
        }

        public String getKeyDisplay() {
            int scancode = glfwGetKeyScancode(key);
            return glfwGetKeyName(key, scancode);
        }

        public Direction getOutcome() {
            return outcome;
        }
    }

    public List<ControlInfo> getControls() {
        return controls;
    }

    public Map<Integer, Movable.Direction> getControlMap() {
        return controls.stream().collect(Collectors.toMap(ControlInfo::getKey, ControlInfo::getOutcome));
    }

    public boolean updateKey(Movable.Direction direction, Integer key) {
        // make sure the key isn't already in use
        if (controls.stream().anyMatch(control -> control.getKey().equals(key))) {
            return false;
        }

        ControlInfo control = controls.stream()
                .filter(controlItem -> controlItem.getOutcome().equals(direction))
                .findFirst()
                .orElse(null);
        // this should never happen!! but JUST IN CASE...
        if (control == null) {
            System.out.println("Attempted to update the control for a nonexistent outcome. This should not happen");
            return false;
        }

        control.updateKey(key);
        return true;
    }

    public int getControlsCount() {
        return controls.size();
    }
}
