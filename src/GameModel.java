import ecs.Components.Movable;
import ecs.Entities.*;
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

    public void initialize(Graphics2D graphics) {
        var texSquare = new Texture("resources/images/square-outline.png");

        var text_baba = new Texture("resources/images/baba_text.png");
        var flag_text = new Texture("resources/images/flag_text.png");
        var is_text = new Texture("resources/images/is_text.png");
        var kill_text = new Texture("resources/images/kill_text.png");
        var push_text = new Texture("resources/images/push_text.png");
        var stop_text = new Texture("resources/images/stop_text.png");
        var you_text = new Texture("resources/images/you_text.png");
        var wall_text = new Texture("resources/images/wall_text.png");
        var water_text = new Texture("resources/images/water_text.png");



        sysRenderer = new Renderer(graphics, GRID_SIZE);
        sysCollision = new Collision(
                //lamba for removing objects
                (Entity entity) -> {
                // Remove the sinkable object
                removeThese.add(entity);
            });


        sysMovement = new Movement(sysCollision);

        sysKeyboardInput = new KeyboardInput(graphics.getWindow());

        sysCountdown = new Countdown(
                graphics,
                (Entity entity) -> {
                    removeEntity(entity);
                    //ecs.Entities.Snake.enableControls(snake);
                    //ecs.Entities.Baba.enableControls(baba);
                    //var movable = snake.get(ecs.Components.Movable.class);
                    var movable = baba.get(ecs.Components.Movable.class);
                    movable.facing = Movable.Direction.Stopped;
                    addEntity(baba);
                });

        initializeBorder(texSquare);
        initializeBaba(texSquare);
        initializeBaba(texSquare);
        initializeBaba(texSquare);
        initializeController();
        sysMovement.addController(controller);
        addEntity(createFood(texSquare));
        addEntity(createFood(texSquare));
        addEntity(createFood(texSquare));
        addEntity(createFood(texSquare));

        addEntity(createWater(texSquare));

        addEntity(createWord(text_baba,"BABA"));
        addEntity(createWord(flag_text,"FLAG"));
        addEntity(createWord(is_text,"IS"));
        addEntity(createWord(you_text,"YOU"));
        addEntity(createWord(push_text,"PUSH"));
        addEntity(createWord(wall_text,"WALL"));
        addEntity(createWord(water_text,"WATER"));

        var countdown = ecs.Entities.Countdown.create(.1);
        addEntity(countdown);

    }

    public void update(double elapsedTime) {
        // Because ECS framework, input processing is now part of the update
        sysKeyboardInput.update(elapsedTime);
        // Now do the normal update
        sysMovement.update(elapsedTime);
        sysCollision.update(elapsedTime);

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
        sysRenderer.add(entity);
        sysCountdown.add(entity);
    }

    private void removeEntity(Entity entity) {
        sysKeyboardInput.remove(entity.getId());
        sysMovement.remove(entity.getId());
        sysCollision.remove(entity.getId());
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
    private Entity createWater(Texture square) {
        MyRandom rnd = new MyRandom();
        boolean done = false;

        Entity proposed = null;
        while (!done) {
            int x = (int) rnd.nextRange(1, GRID_SIZE - 1);
            int y = (int) rnd.nextRange(1, GRID_SIZE - 1);
            proposed = Water.create(square, x, y);
            if (!sysCollision.collidesWithAny(proposed)) {
                done = true;
            }
        }

        return proposed;
    }

    private Entity createWord(Texture square,String text) {
        MyRandom rnd = new MyRandom();
        boolean done = false;

        Entity proposed = null;
        while (!done) {
            int x = (int) rnd.nextRange(1, GRID_SIZE - 1);
            int y = (int) rnd.nextRange(1, GRID_SIZE - 1);
            proposed = Word.create(square, x, y,text);
            if (!sysCollision.collidesWithAny(proposed)) {
                done = true;
            }
        }

        return proposed;
    }

}
