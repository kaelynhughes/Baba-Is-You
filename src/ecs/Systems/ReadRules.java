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
    String[] objects = {"BABA","ROCK","FLAG","WATER","WALL","LAVA","FLOOR","GRASS","HEDGE"};
    String[] components = {"STOP","PUSH","DEFEAT","WIN","SINK","YOU"};

    HashMap<String,Component> objectsTable;
    HashMap<String,Component> componentsTable;

    Movement sysMovement;

    GameObjectRegistry registry;
    public ReadRules(Movement sysMovement) {
        super(Object.class);

        this.sysMovement = sysMovement;
        active_rules =new HashSet<String>();
        valid_rules =new HashSet<String>();
        componentsTable =new HashMap<String,Component>();
        componentsTable.put("STOP", new ecs.Components.Stop());
        componentsTable.put("PUSH", new ecs.Components.Push());
        componentsTable.put("DEFEAT", new ecs.Components.Defeat());
        componentsTable.put("WIN", new ecs.Components.Win());
        componentsTable.put("SINK", new ecs.Components.Sink());
        componentsTable.put("YOU", new ecs.Components.PlayerControlled());

        //ADD YOU RULES
        for(String obj : objects){
            for(String comp : components)

                valid_rules.add(obj+"-IS-"+comp);
        }

        //ADD RULES TO CHANGE OBJECTS INTO OTHER OBJECTS
        for(String becomes : objects){
            for(String obj : objects)

                if(!obj.equals(becomes)){
                    valid_rules.add(obj+"-IS-"+becomes);

                }
        }
    }

    @Override
    public void update(double elapsedTime) {
        int i = 0;

        for (var entity : entities.values()) {
            if(entity.contains(Text.class)){
                checkForRules(entity, elapsedTime);
            }
            i++;
        }
    }


    private void checkForRules(Entity entity, double elapsedTime) {


        var word = entity.get(Text.class);
        if(Objects.equals(word.text, "IS")){

            List<String> rules = checkAround(entity);
            for(String rule : rules){

                if(valid_rules.contains(rule) && !active_rules.contains(rule)){
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

            if(!entity.contains(Text.class)) continue;

            var pos = entity.get(Position.class);
            var text = entity.get(Text.class);

            //find vertical rule
            if(pos.getY() -1 == target_y && pos.getX() == target_x ){
                //word is above
                col_rule.append(text.text);

            }else if (pos.getY() +1 == target_y && pos.getX() == target_x ){
                //word is below

                col_rule.insert(0, text.text);
            }

            //find horizontal rule
            if(pos.getY()== target_y && pos.getX()-1 == target_x ){
                //word is to left

                row_rule.append(text.text);

            }else if (pos.getY() == target_y && pos.getX() + 1== target_x){
                //word is to right
                row_rule.insert(0, text.text);
            }


        }


        List<String> rules = new ArrayList<>();

        rules.add(col_rule.toString());
        rules.add(row_rule.toString());
        return rules;
    }

    private void apply_rule(String rule){

        out.println(rule);
        String[] rules = rule.split("-");

        String objectName = rules[0];  // e.g., "BABA"
        String action = rules[1];      // Always "IS"
        String newType = rules[2];     // e.g., "ROCK" or "PUSH"

        boolean addComponent = false;
        if(Arrays.asList(components).contains(newType)){
            addComponent = true;
        }

        if(addComponent){

            for(Entity entity : entities.values()){

                if(entity.contains(ecs.Components.Tag.class)){

                    var tag = entity.get(ecs.Components.Tag.class);

                    if(Objects.equals(tag.name, objectName)){
                        if(!entity.contains(componentsTable.get(newType).getClass())){
                            entity.add(componentsTable.get(newType));
                            if(newType.equals("YOU") || newType.equals("PUSH")) sysMovement.add(entity);
                        }
                    }
                }
            }

        }
        //perform Transformation
        else{

            ArrayList<Component> comps_to_add = new ArrayList<>();

            //finds entity with all classes we need to add
            for(Entity entity : entities.values()){

                //we have found the class and can now begin taking all components
                if(entity.contains(ecs.Components.Tag.class)){
                    var newTag = entity.get(ecs.Components.Tag.class);

                    if(newTag.name.equals(newType)){

                        comps_to_add = new ArrayList<>(entity.getComponents().values());
                        break;
                    }
                }
            }

            out.println(comps_to_add);

            //TODO: FIX THIS vvvvvv
            for(Entity entity : entities.values()){
                out.println("---");
                if(entity.contains(ecs.Components.Tag.class)){

                    var tag = entity.get(ecs.Components.Tag.class);

                    if(Objects.equals(tag.name, objectName)){
                        out.println("CURRENT ENTITY - "+tag.name);
                        var appearance = entity.get(ecs.Components.Appearance.class);
                        GameObjectRegistry.GameObjectInfo obj_info = GameObjectRegistry.getObjectInfo(newType);

                        //change image here;
                        appearance.image = new Texture(obj_info.getImagePath());
                        //change color here:
                        appearance.color = obj_info.getColor();

                        out.println("-- TARGET ENTITY (BEFORE ADD) --");
                        out.println(entity.getComponents().values());

                        //adds necassary components
                        for(Component comp : comps_to_add){
                            if(!entity.contains(comp.getClass())){
                                entity.add(comp);
                                out.println("ADDED - "+ comp);
                            }
                        }

                        out.println("-- TARGET ENTITY (AFTER ADD) --");
                        out.println(entity.getComponents().values());
                        out.println("-- LOOP --");
                        //removes necassary components
                        out.println(comps_to_add);


                        for(Component comp : comps_to_add){
                            boolean shouldRemove = true;
                            for (Component newComp : comps_to_add) {
                                if (comp.getClass() == newComp.getClass()) {
                                    shouldRemove = false;
                                    break;
                                }
                            }
                            if (shouldRemove) {
                                entity.remove(comp.getClass());
                                out.println("REMOVED - " + comp);
                            }
                        }

                        tag.name = newType;

                    }
                }
            }
        }
    }
}