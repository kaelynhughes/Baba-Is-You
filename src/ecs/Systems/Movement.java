package ecs.Systems;

import ecs.Components.Movable;
import ecs.Components.Position;
import ecs.Components.PlayerInput;
import ecs.Components.PlayerControlled;
import ecs.Entities.Entity;

import java.util.ArrayList;

import static java.lang.System.out;

public class Movement extends System {
    Entity controller;
    Collision collisionSystem;
    public Movement(Collision collisionSystem) {
        super(Movable.class, Position.class, PlayerControlled.class);
        this.collisionSystem = collisionSystem;
    }

    @Override
    public void update(double elapsedTime) {
        int i = 0;

        for (var entity : entities.values()) {
            moveEntity(entity, elapsedTime);
            out.println("Times moved - "+i);
            i++;
        }
    }
    public void addController(Entity controller) {
        this.controller = controller;
    }

    private void moveEntity(Entity entity, double elapsedTime) {
        var input = entity.get(Position.class);
        var movable = entity.get(Movable.class);

        if (input == null || movable == null) return; // Ensure components exist
        var movement = controller.get(ecs.Components.PlayerInput.class);

        switch (movement.currentDirection) {
            case Up -> move(entity, 0, -1);
            case Down -> move(entity, 0, 1);
            case Left -> move(entity, -1, 0);
            case Right -> move(entity, 1, 0);
        }
    }

    private void move(Entity entity, int xIncrement, int yIncrement) {
        var position = entity.get(Position.class);

        int proposed_x = position.getX()+xIncrement;
        int proposed_y = position.getY()+yIncrement;

        //check if the space is free where we want to go
        if (collisionSystem.canMoveTo(proposed_x, proposed_y)) {
            position.update(xIncrement, yIncrement);
        } else{
            //if not, check pushable
            ArrayList<Entity> pushables = collisionSystem.getPushables(position.getX(),position.getY(),xIncrement,yIncrement,new ArrayList<>());

            if(pushables != null){
                for(var push :  pushables){
                    var push_position = push.get(Position.class);
                    push_position.update(xIncrement, yIncrement);
                }
                position.update(xIncrement, yIncrement);
            }





        }
        //check if we can push instead

    }
}
