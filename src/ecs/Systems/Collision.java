package ecs.Systems;

import ecs.Components.Appearance;
import ecs.Components.Movable;
import ecs.Components.Position;
import ecs.Components.Tag;
import ecs.Entities.Entity;
import ecs.ParticleSystem;
import edu.usu.graphics.Graphics2D;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;
import static java.lang.System.out;

public class Collision extends System {

    public interface IDestroy {
        void invoke(Entity entity);
    }



    private final IDestroy destroy;
    private ParticleSystem particleSystem;
    private Graphics2D graphics;
    public Collision(IDestroy destroy, Renderer renderer) {
        super(ecs.Components.Collision.class,ecs.Components.Position.class);
        this.destroy = destroy;
        this.particleSystem = new ParticleSystem(renderer.getCELL_SIZE(),renderer.getOFFSET_X(),renderer.getOFFSET_Y());
        graphics = renderer.getGraphics();
        //Collisions will be responsible for particle system since they only occur here



    }

    public boolean canMoveTo(int x, int y){
        for (var entity : entities.values()) {
            var position = entity.get(ecs.Components.Position.class);
            if (position.getX() == x && position.getY() == y &&(entity.contains(ecs.Components.Push.class) ||entity.contains(ecs.Components.Stop.class)) ) {
                return false;

            }
        }

        return true;
    }

    public ArrayList<Entity> getPushables(int start_x, int start_y, int x_inc, int y_inc, ArrayList<Entity> pushables){
        for (var entity : entities.values()) {
            var position = entity.get(ecs.Components.Position.class);

            //There is a stop object that should not be moved into
            if (position.getX() == start_x+x_inc && position.getY() == start_y + y_inc && entity.contains(ecs.Components.Stop.class)){

                return null;

            }

            //recursivly chains together pushable blocks until a stop collision is found
            if (position.getX() == start_x+x_inc && position.getY() == start_y + y_inc  && entity.contains(ecs.Components.Push.class)) {
                if(!pushables.contains(entity)){


                    if(getPushables(start_x +x_inc,start_y+y_inc, x_inc, y_inc, pushables) != null){
                        pushables.add(entity);
                    } else return null;
                }

            }


        }

        return pushables;


    }

    /**
     * Check to see if any movable components collide with any other
     * collision components.
     * <p>
     * Step 1: find all movable components first
     * Step 2: Test the movable components for collision with other (but not self) collision components
     */
    @Override
    public void update(double elapsedTime) {
        var movable = findMovable(entities);

        for (var entity : entities.values()) {
            for (var entityMovable : movable) {
                if ( !entityMovable.contains(ecs.Components.Text.class) && collides(entity, entityMovable) && entity != entityMovable) {

                        if (entity.contains(ecs.Components.Sink.class)){
                            int ent_x = entity.get(Position.class).getX();
                            int ent_y = entity.get(Position.class).getY();


                            particleSystem.generateParticles(ent_x,ent_y,new Vector2f(2.0f, 2.0f), 15 , 1.0f,"death");
                            destroy.invoke(entity);
                            destroy.invoke(entityMovable);

                        }
                        else if (entity.contains(ecs.Components.Defeat.class)){
                            int ent_x = entity.get(Position.class).getX();
                            int ent_y = entity.get(Position.class).getY();
                            out.println("defeat collision");
                            particleSystem.generateParticles(ent_x,ent_y,new Vector2f(2.0f, 2.0f), 15 , 1.0f,"death");
                            destroy.invoke(entityMovable);

                        }
                        else if (entity.contains(ecs.Components.PlayerControlled.class) && entityMovable.contains(ecs.Components.Win.class)){
                            int ent_x = entity.get(Position.class).getX();
                            int ent_y = entity.get(Position.class).getY();

                            particleSystem.generateParticles(ent_x,ent_y,new Vector2f(2.0f, 2.0f), 25 , 1.5f,"win");
                            out.println((entity.get(Tag.class).name));
                            out.println(entityMovable.get(Tag.class).name);
                            out.println("YOU WON!!");


                            //Exit level

                        }

                }
            }
        }
        particleSystem.update((float) elapsedTime);
        particleSystem.render(graphics);
    }

    /**
     * Public method that allows an entity with a single cell position
     * to be tested for collision with anything else in the game.
     */
    public boolean collidesWithAny(Entity proposed) {
        var aPosition = proposed.get(ecs.Components.Position.class);

        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Collision.class) && entity.contains(ecs.Components.Position.class)) {
                var ePosition = entity.get(ecs.Components.Position.class);

                    if (aPosition.getX() == ePosition.getX() && aPosition.getY() == ePosition.getY()) {
                        return true;
                    }
            }
        }

        return false;
    }


    /**
     * Returns a collection of all the movable entities.
     */
    private List<Entity> findMovable(Map<Long, Entity> entities) {
        var movable = new ArrayList<Entity>();

        for (var entity : entities.values()) {
            if (entity.contains(ecs.Components.Movable.class) && entity.contains(ecs.Components.Position.class)) {
                movable.add(entity);
            }
        }

        return movable;
    }

    /**
     * We know that only the snake is moving and that we only need
     * to check its head for collision with other entities.  Therefore,
     * don't need to look at all the segments in the position, with the
     * exception of the movable itself...a movable can collide with itself.
     */

    private boolean collides(Entity a, Entity b) {
        var aPosition = a.get(ecs.Components.Position.class);
        var bPosition = b.get(ecs.Components.Position.class);

        // A movable can collide with itself: Check segment against the rest
        if (a != b) {
            // Have to skip the first segment, that's why using a counted for loop

            return aPosition.getX() == bPosition.getX() && aPosition.getY() == bPosition.getY();


        }

        return false;
    }

}
