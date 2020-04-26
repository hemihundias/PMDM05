package com.mygdx.mijuego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.mijuego.Constants;
import com.mygdx.mijuego.personajes.EntityFactory;
import com.mygdx.mijuego.personajes.FloorEntity;
import com.mygdx.mijuego.personajes.PlayerEntity;
import com.mygdx.mijuego.personajes.ObstacleEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen extends BaseScreen {
    private Stage stage;
    private World world;
    private PlayerEntity player;
    private List<FloorEntity> floorList = new ArrayList<FloorEntity>();
    private List<ObstacleEntity> obstacleList = new ArrayList<ObstacleEntity>();
    private Sound jumpSound;
    private Sound dieSound;
    private Music backgroundMusic;
    private Vector3 position;

    public GameScreen(com.mygdx.mijuego.MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        position = new Vector3(stage.getCamera().position);

        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new GameContactListener());

        jumpSound = game.getManager().get("audio/jump.ogg");
        dieSound = game.getManager().get("audio/die.ogg");
        backgroundMusic = game.getManager().get("audio/song.mp3");

    }

    @Override
    public void show() {
        EntityFactory factory = new EntityFactory(game.getManager());

        player = factory.createPlayer(world, new Vector2(1.5f, 1.5f));

        floorList.add(factory.createFloor(world, 0, 1000, 1));
        floorList.add(factory.createFloor(world, 0, 1000, 11));

        int i = 16;
        while(i < 1000){
            Random r = new Random();
            int x = r.nextInt(4)+1;
            int y = 9;
            int c = 1;

            while(c <= x){
                obstacleList.add(factory.createObstacles(world, i, c));
                while(y > (x + 3)){
                    obstacleList.add(factory.createObstacles(world, i, y));
                    y--;
                }
                c++;
            }
            i = i + 6;
        }

        for (FloorEntity floor : floorList) {
            stage.addActor(floor);
        }
        for (ObstacleEntity spike : obstacleList) {
            stage.addActor(spike);
        }

        stage.addActor(player);

        stage.getCamera().position.set(position);
        stage.getCamera().update();

        backgroundMusic.setVolume(0.10f);
        backgroundMusic.play();
    }

    @Override
    public void hide() {
        stage.clear();

        player.detach();
        for (FloorEntity floor : floorList)
            floor.detach();
        for (ObstacleEntity spike : obstacleList)
            spike.detach();

        floorList.clear();
        obstacleList.clear();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.1f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        world.step(delta, 6, 2);

        if (player.getX() > 150 && player.isAlive()) {
            float speed = Constants.PLAYER_SPEED * delta * Constants.PIXELS_IN_METERS;
            stage.getCamera().translate(speed, 0, 0);
        }

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }

    private class GameContactListener implements ContactListener {

        private boolean areCollided(Contact contact, Object userA, Object userB) {
            Object userDataA = contact.getFixtureA().getUserData();
            Object userDataB = contact.getFixtureB().getUserData();

            if (userDataA == null || userDataB == null) {
                return false;
            }

            return (userDataA.equals(userA) && userDataB.equals(userB)) ||
                    (userDataA.equals(userB) && userDataB.equals(userA));
        }

        @Override
        public void beginContact(Contact contact) {

            if (areCollided(contact, "player", "obstacle")) {

                if (player.isAlive()) {
                    player.setAlive(false);

                    backgroundMusic.stop();
                    dieSound.play();

                    stage.addAction(
                            Actions.sequence(
                                    Actions.delay(1.5f),
                                    Actions.run(new Runnable() {

                                        @Override
                                        public void run() {
                                            game.setScreen(game.gameOverScreen);
                                        }
                                    })
                            )
                    );
                }
            }
        }

        @Override
        public void endContact(Contact contact) {
           if (areCollided(contact, "player", "floor")) {
                if (player.isAlive()) {
                    jumpSound.play();
                }
           }
        }

        @Override public void preSolve(Contact contact, Manifold oldManifold) { }
        @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
    }
}
