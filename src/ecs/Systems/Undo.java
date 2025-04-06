package ecs.Systems;

import ecs.Components.Movable;
import ecs.Components.PlayerControlled;
import ecs.Components.Position;
import ecs.Entities.Entity;

import java.util.*;

import static java.lang.System.out;

/**
 * UndoSystem stores previous game states as deep copies of all entities.
 */
public class Undo extends System {
    Entity controller;
    private java.util.function.Consumer<Entity> removeEntityCallback;
    private java.util.function.Consumer<Entity> addEntityCallback;

    private final Deque<Map<Long, Entity>> historyStack = new ArrayDeque<>();


    public Undo(){
        super(Position.class);

    }

    public void addController(Entity controller) {
        this.controller = controller;
    }

    public void setEntityCallbacks(
            java.util.function.Consumer<Entity> removeCallback,
            java.util.function.Consumer<Entity> addCallback
    ) {
        this.removeEntityCallback = removeCallback;
        this.addEntityCallback = addCallback;
    }
    /**
     * Call this method after applying a valid player move.
     */
    public void snapshot(Map<Long, Entity> currentEntities) {
        Map<Long, Entity> deepCopy = new HashMap<>();
        for (var entry : currentEntities.entrySet()) {
            deepCopy.put(entry.getKey(), deepCopyEntity(entry.getValue()));
        }
        historyStack.push(deepCopy);
    }

    /**
     * Pops the last saved game state and returns a deep copy of it.
     */
    public Map<Long, Entity> undo() {
        if (!historyStack.isEmpty()) {
            return historyStack.pop();
        }
        return null; // No history to undo
    }

    /**
     * Clears all saved states (e.g., on level restart)
     */
    public void clearHistory() {
        historyStack.clear();
    }

    /**
     * Deep copies an entity and its components.
     */
    private Entity deepCopyEntity(Entity original) {
        Entity copy = new Entity();
        for (var component : original.getComponents().values()) {

            copy.add(component.copy());

        }
        return copy;
    }

    @Override
    public void update(double elapsedTime) {

        var input = controller.get(ecs.Components.PlayerInput.class);

        if(input.currentDirection == Movable.Direction.Undo){

            restoreSnapshot();


        } else if(input.currentDirection == Movable.Direction.Right||
                input.currentDirection == Movable.Direction.Up||
                input.currentDirection == Movable.Direction.Left||
                input.currentDirection == Movable.Direction.Down){
                snapshot(entities);
        }

    }
    public void restoreSnapshot() {
        Map<Long, Entity> snapshot = undo();
        if (snapshot != null) {
            // Remove current entities
            for (Entity entity : new ArrayList<>(entities.values())) {
                removeEntityCallback.accept(entity);
            }

            // Clear current system's entities
            entities.clear();

            // Add restored ones
            for (Entity entity : snapshot.values()) {
                addEntityCallback.accept(entity);
            }

            // Replace system's internal entity map
            entities = new HashMap<>(snapshot); // If allowed, or otherwise do it via add()
        }
    }

}
