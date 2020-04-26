package com.mygdx.mijuego.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.mijuego.Constants;

public class PlayerEntity extends Actor {
    private Texture texture;
    private World world;
    private Body body;
    private Fixture fixture;
    private boolean alive = true;
    private boolean canJump = true;

    public PlayerEntity(World world, Texture texture, Vector2 position) {
        this.world = world;
        this.texture = texture;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(box, 3);
        fixture.setUserData("player");
        box.dispose();

        setSize(Constants.PIXELS_IN_METERS, Constants.PIXELS_IN_METERS);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * Constants.PIXELS_IN_METERS,
                (body.getPosition().y - 0.5f) * Constants.PIXELS_IN_METERS);
        batch.draw(texture, getX() , getY(), getWidth() * 2, getHeight());
    }

    @Override
    public void act(float delta) {
        if(!alive){
            canJump = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if(canJump){
                jump();
            }
        }

        if (alive) {
            float speedY = body.getLinearVelocity().y;
            body.setLinearVelocity(Constants.PLAYER_SPEED, speedY);
            body.applyForceToCenter(0, -Constants.IMPULSE_JUMP * 0.75f, true);
        }
    }

    public void jump() {
        Vector2 position = body.getPosition();
        body.applyLinearImpulse(0, Constants.IMPULSE_JUMP, position.x, position.y, true);

    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }



}
