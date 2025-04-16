package ecs;

import java.io.*;
import java.text.ParseException;
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
    private final File controlSaveFile;
    // if either of these locations are changed, make sure they match!!
    private final String saveFileDirectory = "resources/config";
    private final String saveFileLocation = "resources/config/controls.txt";
    private final Map<String, Direction> translationMap = Map.of(
            "Up", Direction.Up,
            "Down", Direction.Down,
            "Left", Direction.Left,
            "Right", Direction.Right,
            "Undo", Direction.Undo,
            "Reset", Direction.Reset
    );
    private ControlRegistry() {
        controls = new ArrayList<>();
        controlSaveFile = new File(saveFileLocation);
        if (controlSaveFile.exists()) {
            populateFromFile();
        } else {
            populateDefaults();
        }
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
            Map<Integer, String> arrows = Map.of(
                    GLFW_KEY_UP, "Up Arrow",
                    GLFW_KEY_DOWN, "Down Arrow",
                    GLFW_KEY_LEFT, "Left Arrow",
                    GLFW_KEY_RIGHT, "Right Arrow"
            );
            if (arrows.containsKey(key)) {
                return arrows.get(key);
            }
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
        // we are currently erasing and rewriting the whole file on every update
        // this is inefficient but insignificant with such a short file
        // ideally (or if this becomes more complex) we would find and alter the correct line
        saveToFile();
        return true;
    }

    public int getControlsCount() {
        return controls.size();
    }

    private void populateFromFile() {
        // if the file contains any invalid values, we will return to the default configs
        // this will erase the old file
        try {
            Scanner scanner = new Scanner(controlSaveFile);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] splitData = data.split(",");
                if (splitData.length != 2) {
                    throw new ParseException("Illegal storage format; expected two comma-separated values, found " + data, 0);
                }

                Direction direction = translationMap.get(splitData[0]);
                if (direction == null) {
                    throw new ParseException("Illegal control type: " + splitData[0], 0);
                }

                // parseInt throws the error if the key is invalid, so we don't have to throw one ourselves
                int key = Integer.parseInt(splitData[1]);

                controls.add(new ControlInfo(
                        splitData[0],
                        key,
                        direction
                ));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Something went wrong; no file was found in the correct location, and this function should not have been run without checking first that a file existed.");
            populateDefaults();
        } catch (ParseException ex) {
            System.out.println("Tried to read in illegal control. " + ex.getMessage());
            populateDefaults();
        } catch (NumberFormatException ex) {
            System.out.println("Tried to read in illegal key. " + ex.getMessage());
            populateDefaults();
        }
    }

    private void populateDefaults() {
        controls.add(new ControlInfo("Up", GLFW_KEY_W, Direction.Up));
        controls.add(new ControlInfo("Left", GLFW_KEY_A, Direction.Left));
        controls.add(new ControlInfo("Down", GLFW_KEY_S, Direction.Down));
        controls.add(new ControlInfo("Right", GLFW_KEY_D, Direction.Right));

        controls.add(new ControlInfo("Undo", GLFW_KEY_Z, Direction.Undo));
        controls.add(new ControlInfo("Reset", GLFW_KEY_R, Direction.Reset));

        saveToFile();
    }

    private void saveToFile() {
        try {
            File directory = new File(saveFileDirectory);
            if (!directory.exists()) {
                boolean success = directory.mkdir();
                if (!success) {
                    throw new IOException("Directory does not exist; failed to create one.");
                }
            }
            if (!controlSaveFile.exists() || !controlSaveFile.isFile()) {
                boolean success = controlSaveFile.createNewFile();
                if (!success) {
                    throw new IOException("File does not exist; failed to create one.");
                }
            }
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(saveFileLocation));
            for (ControlInfo control: controls) {
                fileWriter.write(control.getOutcomeDisplay() + "," + control.getKey());
                fileWriter.newLine();
            }
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println("Something went wrong creating the config save file. " + ex.getMessage());
        }
    }
}
