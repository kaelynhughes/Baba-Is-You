package ecs.Systems;

import ecs.Components.*;
import ecs.Components.Object;
import ecs.Entities.Entity;
import ecs.GameObjectRegistry;
import edu.usu.graphics.Color;
import edu.usu.graphics.Texture;

import java.lang.reflect.Array;
import java.util.*;

import static java.lang.System.exit;
import static java.lang.System.out;

public class ReadRules extends System {
    // Creating an empty Set
    HashSet<String> active_rules;
    HashSet<String> valid_rules;
    String[] objects = {"BABA", "ROCK", "FLAG", "WATER", "WALL", "LAVA", "FLOOR", "GRASS", "HEDGE"};
    String[] components = {"STOP", "PUSH", "DEFEAT", "WIN", "SINK", "YOU"};

    HashMap<String, Component> objectsTable;
    HashMap<String, Component> componentsTable;
    private HashMap<String, HashSet<Class<? extends Component>>> objectComponentsTable;
    ArrayList<Class>universalComponents;
    Movement sysMovement;

    GameObjectRegistry registry;

    public ReadRules(Movement sysMovement) {
        super(Object.class);


        objectComponentsTable = new HashMap<>();
        universalComponents = new ArrayList<>();
        // Initialize objects with empty component sets
        for (String obj : objects) {
            objectComponentsTable.put(obj, new HashSet<>());
        }


        //create list of components that CANNOT be removed
        universalComponents.add(ecs.Components.Object.class);
        universalComponents.add(ecs.Components.Position.class);
        universalComponents.add(ecs.Components.Appearance.class);
        universalComponents.add(ecs.Components.Movable.class);
        universalComponents.add(ecs.Components.Collision.class);
        universalComponents.add(ecs.Components.Tag.class);



        this.sysMovement = sysMovement;
        active_rules = new HashSet<String>();
        valid_rules = new HashSet<String>();
        componentsTable = new HashMap<String, Component>();
        componentsTable.put("STOP", new ecs.Components.Stop());
        componentsTable.put("PUSH", new ecs.Components.Push());
        componentsTable.put("DEFEAT", new ecs.Components.Defeat());
        componentsTable.put("WIN", new ecs.Components.Win());
        componentsTable.put("SINK", new ecs.Components.Sink());
        componentsTable.put("YOU", new ecs.Components.PlayerControlled());

        //ADD YOU RULES
        for (String obj : objects) {
            for (String comp : components)

                valid_rules.add(obj + "-IS-" + comp);
        }

        //ADD RULES TO CHANGE OBJECTS INTO OTHER OBJECTS
        for (String becomes : objects) {
            for (String obj : objects)

                if (!obj.equals(becomes)) {
                    valid_rules.add(obj + "-IS-" + becomes);

                }
        }
    }

    @Override
    public void update(double elapsedTime) {
        HashSet<String> new_active_rules = new HashSet<>();

        for (var entity : entities.values()) {
            if (entity.contains(Text.class)) {
                List<String> rules = checkAround(entity);
                for (String rule : rules) {
                    if (valid_rules.contains(rule)) {
                        new_active_rules.add(rule);
                        if (!active_rules.contains(rule)) {
                            active_rules.add(rule);
                            apply_rule(rule);
                        }
                    }
                }
            }
        }

        // Find and remove outdated rules
        HashSet<String> removed_rules = new HashSet<>(active_rules);
        removed_rules.removeAll(new_active_rules); // Rules that were active but are no longer valid

        for (String rule : removed_rules) {
            remove_rule(rule);
            active_rules.remove(rule);
        }
    }


    private void checkForRules(Entity entity, double elapsedTime) {


        var word = entity.get(Text.class);
        if (Objects.equals(word.text, "IS")) {

            List<String> rules = checkAround(entity);
            for (String rule : rules) {

                if (valid_rules.contains(rule) && !active_rules.contains(rule)) {
                    active_rules.add(rule);
                    apply_rule(rule);

                }
            }
        }
    }

    private List<String> checkAround(Entity target) {
        var position = target.get(Position.class);
        int target_x = position.getX();
        int target_y = position.getY();

        StringBuilder row_rule = new StringBuilder("-IS-");
        StringBuilder col_rule = new StringBuilder("-IS-");

        for (var entity : entities.values()) {

            if (!entity.contains(Text.class)) continue;

            var pos = entity.get(Position.class);
            var text = entity.get(Text.class);

            //find vertical rule
            if (pos.getY() - 1 == target_y && pos.getX() == target_x) {
                //word is above
                col_rule.append(text.text);

            } else if (pos.getY() + 1 == target_y && pos.getX() == target_x) {
                //word is below

                col_rule.insert(0, text.text);
            }

            //find horizontal rule
            if (pos.getY() == target_y && pos.getX() - 1 == target_x) {
                //word is to left

                row_rule.append(text.text);

            } else if (pos.getY() == target_y && pos.getX() + 1 == target_x) {
                //word is to right
                row_rule.insert(0, text.text);
            }


        }


        List<String> rules = new ArrayList<>();

        rules.add(col_rule.toString());
        rules.add(row_rule.toString());
        return rules;
    }

    // Called when a new rule is detected
    private void apply_rule(String rule) {
        out.println(rule);
        String[] parts = rule.split("-");

        String objectName = parts[0];  // e.g., "BABA"
        String action = parts[1];      // Always "IS"
        String newType = parts[2];     // e.g., "ROCK" or "PUSH"

        // Case 1: Object gains a new component (e.g., "BABA-IS-PUSH")


        String transformation = "";
        if (Arrays.asList(components).contains(newType)) {
            Class<? extends Component> componentClass = componentsTable.get(newType).getClass();
            objectComponentsTable.get(objectName).add(componentClass);
        }
        // Case 2: Object transforms into another object (e.g., "BABA-IS-ROCK")
        else {

            transformation = newType;
            HashSet<Class<? extends Component>> newComponents = new HashSet<>(objectComponentsTable.get(newType));
            objectComponentsTable.put(objectName, newComponents);
        }

        updateEntities(objectName,transformation);
    }

    // Applies changes to all entities in the game
    private void updateEntities(String target_objects, String transformation) {
        for (Entity entity : entities.values()) {
            if (entity.contains(ecs.Components.Tag.class)) {
                var tag = entity.get(ecs.Components.Tag.class);
                String objectType = tag.name;

                out.println("NOW WORKING WITH -- "+objectType);
                if (!objectComponentsTable.containsKey(objectType)) continue;

                HashSet<Class<? extends Component>> requiredComponents = objectComponentsTable.get(objectType);
                Set<Class<? extends Component>> currentComponents = entity.getComponents().keySet();

                // Add missing components
                for (Class<? extends Component> compClass : requiredComponents) {
                    if (!currentComponents.contains(compClass)) {
                        try {
                            entity.add(compClass.getDeclaredConstructor().newInstance());
                            if(compClass == PlayerControlled.class){
                                sysMovement.add(entity);
                            }

                            out.println("Added: " + compClass.getSimpleName() + " to " + objectType);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Get a copy of the components list to avoid modification errors
                List<Component> allComps = new ArrayList<>(entity.getComponents().values());

                // Remove extra components
                for (Component comp : allComps) {

                    if (!requiredComponents.contains(comp.getClass())) {
                        if (!universalComponents.contains(comp.getClass())) {
                            entity.remove(comp.getClass());

                            if(comp.getClass() == ecs.Components.PlayerControlled.class) {sysMovement.remove(entity.getId());}

                            out.println("Removed: " + comp.getClass().getSimpleName() + " from " + objectType);
                        }
                    }
                }

                if (entity.contains(Appearance.class) && !transformation.isEmpty() && Objects.equals(objectType, target_objects)) {
                    var appearance = entity.get(Appearance.class);
                    var newTag = entity.get(Tag.class);
                    GameObjectRegistry.GameObjectInfo info = GameObjectRegistry.getObjectInfo(transformation);

                    newTag.name = transformation;
                    // Fetch new appearance details from registry
                    Texture newTexture = new Texture(info.getImagePath()); // Get texture for the new type
                    Color newColor = info.getColor(); // Get color for the new type

                    // Apply the new appearance
                    appearance.image = newTexture;
                    appearance.color = newColor;

                    out.println("Updated Appearance for " + objectType + " -> Texture: " + newTexture + ", Color: " + newColor);

                }
            }
        }
    }
    private void remove_rule(String rule) {
        out.println("Removing rule: " + rule);
        String[] parts = rule.split("-");

        String objectName = parts[0];
        String action = parts[1];  // Always "IS"
        String newType = parts[2];

        // Case 1: Removing a component rule (e.g., "BABA-IS-PUSH")
        if (Arrays.asList(components).contains(newType)) {
            Class<? extends Component> componentClass = componentsTable.get(newType).getClass();
            objectComponentsTable.get(objectName).remove(componentClass);
        }
        // Case 2: Reverting object transformation (e.g., "BABA-IS-ROCK")
        else {
            objectComponentsTable.get(objectName).clear();
        }

        updateEntities(objectName, ""); // Reset entities of this type
    }
}