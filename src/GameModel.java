import ecs.Components.Movable;
import ecs.Entities.*;
import ecs.GameObjectRegistry;
import ecs.Systems.*;
import ecs.Systems.Countdown;
import ecs.Systems.KeyboardInput;
import edu.usu.graphics.*;

import java.lang.System;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private final int GRID_SIZE = 20;


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


    public void initialize(Graphics2D graphics) {
        var texSquare = new Texture("resources/images/square-outline.png");
        var rockText = new Texture("resources/images/rock.png");
        var flagText = new Texture("resources/images/flag.png");
        var text_baba = new Texture("resources/images/word-baba.png");
        var flag_text = new Texture("resources/images/word-flag.png");
        var is_text = new Texture("resources/images/word-is.png");
        var kill_text = new Texture("resources/images/word-kill.png");
        var push_text = new Texture("resources/images/word-push.png");
        var stop_text = new Texture("resources/images/word-stop.png");
        var you_text = new Texture("resources/images/word-you.png");
        var wall_text = new Texture("resources/images/word-wall.png");
        var water_text = new Texture("resources/images/word-water.png");

        var rock_text = new Texture("resources/images/word-rock.png");



        sysRenderer = new Renderer(graphics, GRID_SIZE);
        sysCollision = new Collision(
                //lamba for removing objects
                (Entity entity) -> {
                // Remove the sinkable object
                removeThese.add(entity);
            });


        sysMovement = new Movement(sysCollision);
        sysReadRules = new ReadRules(sysMovement);

        sysKeyboardInput = new KeyboardInput(graphics.getWindow());

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


        //entites should be spawned in based on file input here

        initializeBorder(texSquare);
        initializeBaba(texSquare);
        initializeBaba(texSquare);
        initializeBaba(texSquare);

        addEntity(createWord(text_baba,"BABA",2,2));
        addEntity(createWord(rock_text,"ROCK",4,11));
        addEntity(createWord(is_text,"IS",3,2));
        addEntity(createWord(is_text,"IS",5,2));
        addEntity(createWord(is_text,"IS",7,2));
        addEntity(createWord(you_text,"YOU",4,2));
        addEntity(createWord(stop_text,"STOP",4,3));
        addEntity(createWord(push_text,"PUSH",10,3));
        addEntity(createWord(flag_text,"FLAG",4,7));
        addEntity(createWord(wall_text,"WALL",6,3));
        addEntity(createWord(is_text,"IS",6,4));
        addEntity(createWord(stop_text,"STOP",6,5));

        initializeController();
        sysMovement.addController(controller);


        addEntity(createObject("BABA",texSquare,Color.GREEN,3,3));
        addEntity(createObject("ROCK",rockText,Color.RED,10,3));
        addEntity(createObject("FLAG",flagText,Color.YELLOW,6,10));
        GameObjectRegistry.GameObjectInfo wall = GameObjectRegistry.getObjectInfo("WALL");


        addEntity(createObject("WALL",new Texture(wall.getImagePath())  ,wall.getColor(),5,11));
        addEntity(createObject("WALL",new Texture(wall.getImagePath())  ,wall.getColor(),6,11));
        addEntity(createObject("WALL",new Texture(wall.getImagePath())  ,wall.getColor(),7,11));
        addEntity(createObject("WALL",new Texture(wall.getImagePath())  ,wall.getColor(),8,11));
        addEntity(createObject("WALL",new Texture(wall.getImagePath())  ,wall.getColor(),9,11));
        addEntity(createObject("WALL",new Texture(wall.getImagePath())  ,wall.getColor(),10,11));




        var countdown = ecs.Entities.Countdown.create(.1);
        addEntity(countdown);

    }

    public void update(double elapsedTime) {
        // Because ECS framework, input processing is now part of the update
        sysKeyboardInput.update(elapsedTime);
        // Now do the normal update
        sysMovement.update(elapsedTime);
        sysCollision.update(elapsedTime);
        sysReadRules.update(elapsedTime);
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
        sysRenderer.add(entity);
        sysCountdown.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysMovement.remove(entity.getId());
        sysCollision.remove(entity.getId());
        sysReadRules.remove(entity.getId());
        sysRenderer.remove(entity.getId());
        sysCountdown.remove(entity.getId());
    }

    private void initializeBorder(Texture square) {
        for (int position = 0; position < GRID_SIZE; position++) {
            var left = BorderBlock.create(square, 0, position);
            addEntity(left);

            var right = BorderBlock.create(square, GRID_SIZE - 1, position);
            addEntity(right);

            var top = BorderBlock.create(square, position, 0);
            addEntity(top);

            var bottom = BorderBlock.create(square, position, GRID_SIZE - 1);
            addEntity(bottom);
        }
    }

    private void initializeBaba(Texture square) {
        MyRandom rnd = new MyRandom();
        boolean done = false;

        while (!done) {
            int x = (int) rnd.nextRange(1, GRID_SIZE - 1);
            int y = (int) rnd.nextRange(1, GRID_SIZE - 1);
            var proposed = Baba.create(square, x, y);
            if (!sysCollision.collidesWithAny(proposed)) {
                addEntity(proposed);
                baba = proposed;
                done = true;
            }
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

    private Entity createFood(Texture square) {
        MyRandom rnd = new MyRandom();
        boolean done = false;

        Entity proposed = null;
        while (!done) {
            int x = (int) rnd.nextRange(1, GRID_SIZE - 1);
            int y = (int) rnd.nextRange(1, GRID_SIZE - 1);
            proposed = Rock.create(square, x, y);
            if (!sysCollision.collidesWithAny(proposed)) {
                done = true;
            }
        }

        return proposed;
    }

    private Entity createWord(Texture square,String text,int x,int y) {

        return Word.create(square, x, y,text);
    }

}
