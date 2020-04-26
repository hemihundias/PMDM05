package com.mygdx.mijuego.personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.mijuego.Constants;

public class BackgroundEntity extends Actor {

    private Texture background;

    private World world;

    private Body body;

    private Fixture fixture;

    public BackgroundEntity(World world, Texture background, float x, float width, float y) {
        this.world = world;
        this.background = background;

        BodyDef def = new BodyDef();
        def.position.set(0, 0);
        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fixture = body.createFixture(box, 0);
        fixture.setUserData("background");
        box.dispose();


        setSize(width * Constants.PIXELS_IN_METERS, Constants.PIXELS_IN_METERS);
        setPosition(x * Constants.PIXELS_IN_METERS, (y - 1) * Constants.PIXELS_IN_METERS);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}

