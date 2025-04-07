import ecs.Components.Movable;
import ecs.Entities.*;
import ecs.GameObjectRegistry;
import ecs.Systems.*;
import ecs.Systems.Countdown;
import ecs.Systems.KeyboardInput;
import edu.usu.graphics.*;

import java.lang.System;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.System.out;

public class GameModel {
    private int GRID_SIZE = -1;


    private final List<Entity> removeThese = new ArrayList<>();
    private final List<Entity> addThese = new ArrayList<>();
    private Entity snake;
    private Entity baba;
    private Entity controller;

    private ecs.Systems.Renderer sysRenderer;
    private ecs.Systems.Collision sysCollision;
    private ecs.Systems.Movement sysMovement;
    private ecs.Systems.KeyboardInput sysKeyboardInput;
    private ecs.Systems.Countdown sysCountdown;
    private ecs.Systems.ReadRules sysReadRules;
    private ecs.Systems.Undo sysUndo;

    private HashMap<Character,String> objects = new HashMap<>();
    private HashMap<Character,String> text = new HashMap<>();

    public void initialize(Graphics2D graphics,int GRID_SIZE, String level_name) {

        this.GRID_SIZE = GRID_SIZE;

        objects = new HashMap<>();

        objects.put('w',"WALL");
        objects.put('r',"ROCK");
        objects.put('f',"FLAG");
        objects.put('b',"BABA");
        objects.put('l',"FLOOR");
        objects.put('g',"GRASS");
        objects.put('a',"WATER");
        objects.put('v',"LAVA");
        objects.put('h',"HEDGE");

        text = new HashMap<>();

        text.put('W',"WALL");
        text.put('R',"ROCK");
        text.put('F',"FLAG");
        text.put('B',"BABA");
        text.put('I',"IS");
        text.put('S',"STOP");
        text.put('P',"PUSH");
        text.put('V',"LAVA");
        text.put('A',"WATER");
        text.put('Y',"YOU");
        text.put('X',"WIN");
        text.put('N',"SINK");
        text.put('K',"DEFEAT");

        sysCollision = new Collision(
                //lamba for removing objects
                (Entity entity) -> {
                    // Remove the sinkable object
                    removeThese.add(entity);
                });


        sysMovement = new Movement(sysCollision);
        sysReadRules = new ReadRules(sysMovement);

        sysKeyboardInput = new KeyboardInput(graphics.getWindow());

        sysUndo = new Undo();
        sysCountdown = new Countdown(
                graphics,
                (Entity entity) -> {
                    removeEntity(entity);
                    //ecs.Entities.Snake.enableControls(snake);
                    //ecs.Entities.Baba.enableControls(baba);
                    //var movable = snake.get(ecs.Components.Movable.class);
                    //var movable = baba.get(ecs.Components.Movable.class);
                    //movable.facing = Movable.Direction.Stopped;
                    //addEntity(baba);
                });

        sysRenderer = new Renderer(graphics, GRID_SIZE);

        initializeController();
        sysMovement.addController(controller);
        sysUndo.addController(controller);


        var countdown = ecs.Entities.Countdown.create(.1);
        addEntity(countdown);

        //entites should be spawned in based on file input here
        initializeBorder();
        buildLevel(level_name);


        sysUndo.setEntityCallbacks(this::removeEntity, this::addEntity);
    }

    public void update(double elapsedTime) {
        // Because ECS framework, input processing is now part of the update
        sysKeyboardInput.update(elapsedTime);
        // Now do the normal update

        sysMovement.update(elapsedTime);
        sysCollision.update(elapsedTime);
        sysReadRules.update(elapsedTime);
        sysUndo.update(elapsedTime);
        for (var entity : removeThese) {
            removeEntity(entity);
        }
        removeThese.clear();

        for (var entity : addThese) {
            addEntity(entity);
        }
        addThese.clear();

        // Because ECS framework, rendering is now part of the update
        sysRenderer.update(elapsedTime);
        sysCountdown.update(elapsedTime);
    }

    private void addEntity(Entity entity) {
        sysKeyboardInput.add(entity);
        sysMovement.add(entity);
        sysCollision.add(entity);
        sysReadRules.add(entity);
        sysUndo.add(entity);
        sysRenderer.add(entity);
        sysCountdown.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysMovement.remove(entity.getId());
        sysCollision.remove(entity.getId());
        sysReadRules.remove(entity.getId());
        sysUndo.remove(entity.getId());
        sysRenderer.remove(entity.getId());
        sysCountdown.remove(entity.getId());
    }

    private void initializeBorder() {
        for (int position = 0; position < GRID_SIZE; position++) {
            var left = BorderBlock.create( 0, position);
            addEntity(left);

            var right = BorderBlock.create( GRID_SIZE - 1, position);
            addEntity(right);

            var top = BorderBlock.create( position, 0);
            addEntity(top);

            var bottom = BorderBlock.create(position, GRID_SIZE - 1);
            addEntity(bottom);
        }
    }


    private void initializeController() {

        var proposed = Controller.create();
        addEntity(proposed);
        controller = proposed;

    }
    private Entity createObject(String type,Texture square,Color color,int x,int y) {
        return GameObject.create(type,square,color,x,y);

    }


    private Entity createWord(Texture square,String text,int x,int y) {

        return Word.create(square, x, y,text);
    }

    private void buildLevel(String level){

        try (BufferedReader br = new BufferedReader(new FileReader(level))) {
            String line;

            int i = 0;
            int line_num = 0;

            String level_name = "";
            while ((line = br.readLine()) != null) {

                if (line_num == 0){
                    level_name = line;
                }
                else if(line_num == 1){
                    String[] info_to_parse = line.split(" ");
                    int num_1 = Integer.parseInt(info_to_parse[0]);
                    int num_2 = Integer.parseInt(info_to_parse[2]);
                    GRID_SIZE = Math.max(num_1, num_2);
                }
                else{
                    int j = 0;
                    for (char c : line.toCharArray()) {

                        //spawn object
                        if(objects.containsKey(c)){

                            GameObjectRegistry.GameObjectInfo obj = GameObjectRegistry.getObjectInfo(objects.get(c));

                            addEntity(createObject(objects.get(c),new Texture(obj.getImagePath()),obj.getColor(),j,i));
                        }
                        else if(text.containsKey(c)){
                            out.println(c);
                            GameObjectRegistry.GameObjectInfo word = GameObjectRegistry.getObjectInfo("WORD_"+text.get(c));
                            out.println(text.get(c));
                            addEntity(createWord(new Texture(word.getImagePath()),text.get(c),j,i));
                        }
                        j++;

                    }
                }

                line_num ++;
                //some features are places almost like layers, this allows us to read the next "layer"
                if(line_num > 2){
                    i ++;
                    if(i % GRID_SIZE == 0){
                        i = 0;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
