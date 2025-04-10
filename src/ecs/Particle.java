package ecs;

import org.joml.Vector2f;

class Particle {
    Vector2f position;
    Vector2f velocity;
    float size;
    float lifetime;

    String type;

    public Particle(int pos_x,int pos_y, Vector2f velocity, float lifetime,float size,String type) {
        this.position = new Vector2f(pos_x,pos_y);
        this.velocity = new Vector2f(velocity);
        this.lifetime = lifetime;
        this.size = size;
        this.type = type;
    }

    public String getType(){
        return this.type;

    }
    public void update(float deltaTime) {
        position.add(velocity.mul(deltaTime, new Vector2f()));
        lifetime -= deltaTime;
    }

    public boolean isAlive() {
        return lifetime > 0;
    }
}