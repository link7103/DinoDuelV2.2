package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Scenes.Hud;
import com.dinoduel.game.Sprites.Dino;
import com.dinoduel.game.Sprites.InteractiveTileObject;
import com.dinoduel.game.Tools.B2WorldCreator;
import com.dinoduel.game.Tools.WorldContactListener;
import com.dinoduel.game.Weapons.AK;
import com.dinoduel.game.Weapons.Barrett;
import com.dinoduel.game.Weapons.Gun;
import com.dinoduel.game.Weapons.Mossberg;
import com.dinoduel.game.Weapons.PPK;
import com.dinoduel.game.Weapons.Weapon;

import java.util.ArrayList;

import static java.lang.StrictMath.abs;

public class PlayScreen implements Screen {
    //Main Game
    private DinoDuel game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    //Creates the hud

    //Map
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //Asset Manager
    private AssetManager assetManager;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //Player
    public static Dino player1;
    private Dino player2;
    //Player Sprites
    private TextureAtlas dinoAtlas;
    //Weapon Sprites
    public TextureAtlas weaponAtlas;

    private boolean spawnWeapon;
    private float spawnX;
    private float spawnY;
    int spawnType = -1;

    public static PlayScreen screen;

    //dictates wether a player can jump
    // TODO: 2020-02-03 change this to better suit number of players
    public boolean[] canJump = {true, true};


    //weapon list
    public ArrayList<Gun> guns = new ArrayList<>();

    private Gun gun;

    public PlayScreen(DinoDuel game) {
        screen = this;
        dinoAtlas = new TextureAtlas("Dinos/DinoSprites.txt");
        weaponAtlas = new TextureAtlas("Weapons/weapons.txt");

        this.game = game;
        //Camera that follows the players
        gameCam = new OrthographicCamera();
        //Fits the proper aspect ratio
        gamePort = new FitViewport(DinoDuel.V_WIDTH / DinoDuel.PPM, DinoDuel.V_HEIGHT / DinoDuel.PPM, gameCam);
        //Creates the hud
        hud = new Hud(game.batch);
        //Renders the map
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        assetManager.load("DinoDuel Basic Tilesets/map2.tmx", TiledMap.class);
        assetManager.finishLoading();
        map = assetManager.get("DinoDuel Basic Tilesets/map2.tmx", TiledMap.class);

        renderer = new OrthogonalTiledMapRenderer(map, 1 / DinoDuel.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


        //Creates the world
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map, this);

        //Player1
        player1 = new Dino(world, this, "douxSprites", 0);
        player2 = new Dino(world, this, "tardSprites", 48);

        //Barrett test Fix (needed for the rest of em)w
        gun = new PPK(40, 32, world, this);
        guns.add(gun);

        //contact listener stuff
        world.setContactListener(new WorldContactListener());
    }//end constructor

    public TextureAtlas getDinoAtlas() {
        return dinoAtlas;
    }//end getDinoAtlas

    public TextureAtlas getweaponAtlas() {
        return weaponAtlas;
    }//end getWeaponAtlas

    @Override
    public void show() {
    }//end show

    public void spawnWeapon(InteractiveTileObject object) {
        spawnWeapon = true;
        spawnType = object.onHeadHit();
        spawnX = object.getSpawnX();
        spawnY = object.getSpawnY();
    }//end spawnWeapon

    //dt = delta time
    public void update(float dt) { //Updates the screen every frame
        //handle user input first
        handleInput(dt);
        //takes 1 step in the physics simulation ( 60 times per second)
        world.step(1 / 60f, 6, 2);

        //spawns weapons if told to
        if (spawnWeapon) {
            Gun spawn;
            //int rand = (int) (Math.random() * 4);
            //Gdx.app.log("num", String.valueOf(rand));

            switch (spawnType) {
                default:
                    //PPK
                    spawn = new PPK(spawnX, spawnY, world, this);
                    break;
                case 1:
                    //Mossberg
                    spawn = new Mossberg(spawnX, spawnY, world, this);
                    break;
                case 2:
                    //Barrett
                    spawn = new Barrett(spawnX, spawnY, world, this);
                    break;
                case 3:
                    //AK
                    spawn = new AK(spawnX, spawnY, world, this);
                    break;
            }
            spawnWeapon = false;
            spawnType = -1;
            guns.add(spawn);
        }
        //updates player sprite position
        player1.update(dt);
        for (Gun updateGun : guns) {
            if (updateGun.getUser() == player1) {
                updateGun.update();
                updateGun.update = true;
            }
        }

        player2.update(dt);

        for (Gun updateGun : guns) {
            if (!gun.drawn)
                updateGun.update();
            else
                updateGun.update = false;
        }

        setCameraPosition();
        gameCam.update();
        //tell it to only render what the camera can see
        renderer.setView(gameCam);
    }//end update

    private void handleInput(float dt) {
        //Player1
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && canJump[0]) {
            if (player1.currentState != Dino.State.JUMPING)
                player1.b2body.applyLinearImpulse(new Vector2(0, 4f), player1.b2body.getWorldCenter(), true);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player1.b2body.getLinearVelocity().x <= 2) {
            player1.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player1.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player1.b2body.getLinearVelocity().x >= -2) {
            player1.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player1.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player1.playerDucking = true;
        } else {
            player1.playerDucking = false;
        }
        //Player2
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && canJump[1]) {
            if (player2.currentState != Dino.State.JUMPING)
                player2.b2body.applyLinearImpulse(new Vector2(0, 4f), player2.b2body.getWorldCenter(), true);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && player2.b2body.getLinearVelocity().x <= 2) {
            player2.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player2.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && player2.b2body.getLinearVelocity().x >= -2) {
            player2.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player2.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player2.playerDucking = true;
        } else {
            player2.playerDucking = false;
        }

        //calls the pickup method for player1
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {

            player1.pickupGun(guns);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {

            if (player1.hasWeapon)
                player1.dropGun();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            gameCam.zoom += 0.02;
            //If the A Key is pressed, add 0.02 to the Camera's Zoom
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            gameCam.zoom -= 0.02;
            //If the Q Key is pressed, subtract 0.02 from the Camera's Zoom
        }
    }//end handleInput

    @Override
    public void render(float deltaTime) {
        //seperates update logic from render
        update(deltaTime);

        //clears the game screen with black
        Gdx.gl.glClearColor(92/255.0f, 152/255.0f, 142/255.0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renders the game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        //renders the Dino1
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();


        player1.draw(game.batch);
        for (Gun drawGun : guns) {
            if (drawGun.getUser() == player1) {
                drawGun.setSize(drawGun.xSize / 10 / DinoDuel.PPM, drawGun.ySize / 10 / DinoDuel.PPM);
                drawGun.draw(game.batch);
                drawGun.drawn = true;
            }
        }
        player2.draw(game.batch);

        for (Gun drawGun : guns) {
            if (!drawGun.drawn) {
                drawGun.setSize(drawGun.xSize / 10 / DinoDuel.PPM, drawGun.ySize / 10 / DinoDuel.PPM);
                drawGun.draw(game.batch);
            } else {
                drawGun.drawn = false;
            }
        }

        game.batch.end();

        //sets the batch to draw what the camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }//end render

    public void setCameraPosition() {
//attach the gamecam to the the middle x and y coordinate
        gameCam.position.x = (player1.b2body.getPosition().x + player2.b2body.getPosition().x) / 2;

        gameCam.position.y = (player1.b2body.getPosition().y + player1.b2body.getPosition().y) / 2;
        float xRatio = DinoDuel.V_WIDTH / DinoDuel.PPM / abs(player1.b2body.getPosition().x - player2.b2body.getPosition().x);
        float yRatio = DinoDuel.V_HEIGHT / DinoDuel.PPM / abs(player1.b2body.getPosition().y - player2.b2body.getPosition().y);

        if (xRatio < yRatio) {
            float tempX = abs(player1.b2body.getPosition().x - player2.b2body.getPosition().x);
            if(DinoDuel.V_WIDTH/DinoDuel.PPM >tempX){
                gameCam.viewportWidth = DinoDuel.V_WIDTH/DinoDuel.PPM+ 0.5f;
                gameCam.viewportHeight = DinoDuel.V_HEIGHT/DinoDuel.PPM+ 0.5f;

            }else {
                gameCam.viewportWidth = tempX + 0.5f;
                gameCam.viewportHeight = DinoDuel.V_HEIGHT / DinoDuel.PPM / xRatio + 0.5f;
            }
        } else {
            float tempY = abs(player1.b2body.getPosition().y - player2.b2body.getPosition().y);
            if(DinoDuel.V_HEIGHT/DinoDuel.PPM > tempY){
                gameCam.viewportHeight = DinoDuel.V_HEIGHT/DinoDuel.PPM+ 0.5f;
                gameCam.viewportWidth = DinoDuel.V_WIDTH/DinoDuel.PPM + 0.5f;
            }else {
                gameCam.viewportHeight = tempY + 0.5f;
                gameCam.viewportWidth = DinoDuel.V_WIDTH / DinoDuel.PPM / yRatio + 0.5f;
            }
        }

// These values will need to be scaled according to the world coordinates. (for each map/level)
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        float x = layer.getTileWidth() * layer.getWidth() / DinoDuel.PPM;
        float y = layer.getTileHeight() * layer.getHeight() / DinoDuel.PPM;
        // The left boundary of the map (x)
        int mapLeft = 0;
        // The right boundary of the map (x + width)
        float mapRight = 0 + x;
        // The bottom boundary of the map (y)
        int mapBottom = 0;
        // The top boundary of the map (y + height)
        float mapTop = 0 + y;
        // The camera dimensions, halved
        float cameraHalfWidth = gameCam.viewportWidth * .5f;
        float cameraHalfHeight = gameCam.viewportHeight * .5f;

        // Move camera after player as normal
        float cameraLeft = gameCam.position.x - cameraHalfWidth;
        float cameraRight = gameCam.position.x + cameraHalfWidth;
        float cameraBottom = gameCam.position.y - cameraHalfHeight;
        float cameraTop = gameCam.position.y + cameraHalfHeight;
// Horizontal axis
        if (x < gameCam.viewportWidth) {
            gameCam.position.x = mapRight / 2;
        } else if (cameraLeft <= mapLeft) {
            gameCam.position.x = mapLeft + cameraHalfWidth;
        } else if (cameraRight >= mapRight) {
            gameCam.position.x = mapRight - cameraHalfWidth;
        }
// Vertical axis
        if (y < gameCam.viewportHeight) {
            gameCam.position.y = mapTop / 2;
        } else if (cameraBottom <= mapBottom) {
            gameCam.position.y = mapBottom + cameraHalfHeight;
        } else if (cameraTop >= mapTop) {
            gameCam.position.y = mapTop - cameraHalfHeight;
        }
    }//end setCameraPosition

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }//end resize

    @Override
    public void pause() {

    }//end pause

    @Override
    public void resume() {

    }//end resume

    @Override
    public void hide() {

    }//end hide

    @Override
    public void dispose() {
        //map.dispose();
        assetManager.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }//end dispose


}//end class
