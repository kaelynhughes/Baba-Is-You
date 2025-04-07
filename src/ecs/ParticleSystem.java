package ecs;

import edu.usu.graphics.Color;
import edu.usu.graphics.Graphics2D;
import edu.usu.graphics.Rectangle;
import edu.usu.graphics.Texture;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;



public class ParticleSystem {
    private ArrayList<Particle> particles;
    private Random random;
    private float sizeRange;
    private float OFFSET_X;
    private float OFFSET_Y;
    private float CELL_SIZE;
    public ParticleSystem(float CELL_SIZE,float OFFSET_X, float OFFSET_Y) {
        particles = new ArrayList<>();
        random = new Random();
        sizeRange = .05f;
        this.CELL_SIZE = CELL_SIZE;
        this.OFFSET_X = OFFSET_X;
        this.OFFSET_Y = OFFSET_Y;
    }

    public void generateParticles(int pos_x, int pos_y, Vector2f velocityRange ,int count, float lifetimeRange) {
        for (int i = 0; i < count; i++) {
            Vector2f velocity = new Vector2f(
                    (random.nextFloat() - 0.5f) * velocityRange.x,
                    (random.nextFloat() - 0.5f) * velocityRange.y
            );
            float lifetime = random.nextFloat() * lifetimeRange + 0.5f;
            float size = random.nextFloat() * sizeRange;
            particles.add(new Particle(pos_x,pos_y, velocity, lifetime, size));
        }
    }

    public void update(float deltaTime) {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update(deltaTime);
            if (!particle.isAlive()) {
                iterator.remove();
            }
        }
    }

    public void render(Graphics2D graphics) {

        for (Particle particle : particles) {
            Rectangle rec = new Rectangle((-0.5f + OFFSET_X + particle.position.x* CELL_SIZE)-(particle.size/2),(-0.5f + OFFSET_Y + particle.position.y * CELL_SIZE)-(particle.size/2),particle.size,particle.size);
            Texture pic = new Texture("resources/images/pixel-star.png");
            graphics.draw(pic, rec,Color.WHITE);

        }
    }
}