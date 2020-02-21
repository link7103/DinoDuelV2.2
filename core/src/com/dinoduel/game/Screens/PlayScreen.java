package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Scenes.Hud;
import com.dinoduel.game.Sprites.Dino;
import com.dinoduel.game.Sprites.InteractiveTileObject;
import com.dinoduel.game.Sprites.Ladder;
import com.dinoduel.game.Tools.B2WorldCreator;
import com.dinoduel.game.Tools.WorldContactListener;
import com.dinoduel.game.Weapons.AK;
import com.dinoduel.game.Weapons.Bullet;
import com.dinoduel.game.Weapons.Sniper;
import com.dinoduel.game.Weapons.Gun;
import com.dinoduel.game.Weapons.Shotgun;
import com.dinoduel.game.Weapons.Pistol;

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
    //Weapon Spawns
    private boolean spawnWeapon;
    private float spawnX;
    private float spawnY;
    private int spawnType = -1;

    public static PlayScreen screen;
    //Weapon list
    public ArrayList<Gun> allWeapons = new ArrayList<>();
    //Bullet list
    public ArrayList<Bullet> allBullets = new ArrayList<>();
    //GunBox List
    public ArrayList<InteractiveTileObject> allBoxes = new ArrayList<>();
    //Ladder List
    public ArrayList<Ladder> allLadders = new ArrayList<>();
    //Player List
    public ArrayList<Dino> allPlayers = new ArrayList<>();

    //A Blank texture (Used for HealthBars)
    public Texture blank;

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
        assetManager.load("DinoDuel Basic Tilesets/map1.tmx", TiledMap.class);
        assetManager.finishLoading();
        map = assetManager.get("DinoDuel Basic Tilesets/map1.tmx", TiledMap.class);

        renderer = new OrthogonalTiledMapRenderer(map, 1 / DinoDuel.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //Creates the world
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map, this);

        //Players
        player1 = new Dino(world, this, "douxSprites", 0);
        player2 = new Dino(world, this, "tardSprites", 48);
        allPlayers.add(player1);
        allPlayers.add(player2);

        //contact listener stuff
        world.setContactListener(new WorldContactListener());
        // TODO: 2020-02-20 Health
        blank = new Texture("blank.png");

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

        //destroys bullets that have collided
        for (int i = 0; i < allBullets.size(); i++) {
            if (allBullets.get(i).flag) {
                allBullets.get(i).bBody.setAwake(false);
                world.destroyBody(allBullets.get(i).bBody);
                allBullets.get(i).bBody = null;
                allBullets.remove(allBullets.get(i));
                i--;
            }
        }
        //takes 1 step in the physics simulation ( 60 times per second)
        world.step(1 / 60f, 6, 2);


        //determined if gunboxes can spawn guns
        //spawns weapons if told to
        if (spawnWeapon) {
            Gun spawn = null;
            //int rand = (int) (Math.random() * 4);
            //Gdx.app.log("num", String.valueOf(rand));
            switch (spawnType) {
                case -1:
                    break;
                default:
                    //Pistol
                    spawn = new Pistol(spawnX, spawnY, world, this);
                    break;
                case 1:
                    //Shotgun
                    spawn = new Shotgun(spawnX, spawnY, world, this);
                    break;
                case 2:
                    //Sniper
                    spawn = new Sniper(spawnX, spawnY, world, this);
                    break;
                case 3:
                    //AK
                    spawn = new AK(spawnX, spawnY, world, this);
                    break;
            }
            spawnWeapon = false;
            spawnType = -1;
            if (spawn != null)
                allWeapons.add(spawn);
            spawn = null;
        }
        // FIXME: 2020-02-17 The problem is the ladder is not properly setup (no width or height and at 0,0)
        //tests for players on ladder
        for (Dino dino : allPlayers) {
            for (Ladder ladder : allLadders) {
                //System.out.println("x "+ladder.bounds.x+ "dino x " + dino.b2body.getPosition().x);
                if (ladder.bounds.contains(dino.b2body.getPosition().x * DinoDuel.PPM, dino.b2body.getPosition().y * DinoDuel.PPM - dino.getHeight() / 2)) {
                    if (dino.KEYUP || dino.KEYDOWN) {
                        dino.climbing = true;
                        dino.currentLadder = ladder;
                    }
                    break;

                } else {
                    dino.currentLadder = null;
                    dino.climbing = false;
                    // dino.currentState = Dino.State.FALLING;
                }
            }
        }


        //updates player sprite position
        player1.update(dt);
        for (Gun updateGun : allWeapons) {
            if (updateGun.getUser() == player1) {
                updateGun.update();
                updateGun.update = true;
            }
        }

        player2.update(dt);

        for (Gun updateGun : allWeapons) {
            if (!updateGun.drawn)
                updateGun.update();
            else
                updateGun.update = false;
        }

        for (Bullet updateBullet : allBullets) {
            updateBullet.update();

        }
        setCameraPosition();
        gameCam.update();
        //tells it to only render what the camera can see
        renderer.setView(gameCam);
    }//end update

    private void handleInput(float dt) {
        //Player1
        player1.KEYUP = false;
        player1.KEYRIGHT = false;
        player1.KEYDOWN = false;
        player1.KEYLEFT = false;


        if (player1.climbing && !Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player1.b2body.setLinearVelocity(0, 0);
        }

        if (player1.currentState != Dino.State.JUMPING && Gdx.input.isKeyJustPressed(Input.Keys.UP) && player1.currentState != Dino.State.CLIMBING && player1.currentState != Dino.State.FALLING) {
            player1.KEYUP = true;
            player1.b2body.applyLinearImpulse(new Vector2(0, 4f), player1.b2body.getWorldCenter(), true);

        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player1.KEYUP = true;
            if (player1.currentState == Dino.State.CLIMBING) {
                player1.b2body.setLinearVelocity(0, 1f);
            }
        }


        if (player1.currentLadder != null) {
            if ((player1.b2body.getPosition().y * DinoDuel.PPM >= player1.currentLadder.bounds.y + player1.currentLadder.bounds.height - 3f) && Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                player1.b2body.applyLinearImpulse(new Vector2(0, 4f), player1.b2body.getWorldCenter(), true);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player1.b2body.getLinearVelocity().x <= 2) {
            player1.KEYRIGHT = true;
            player1.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player1.b2body.getWorldCenter(), true);
            if (player1.currentState == Dino.State.CLIMBING) {
                player1.b2body.setLinearVelocity(1f, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player1.b2body.getLinearVelocity().x >= -2) {
            player1.KEYLEFT = true;
            player1.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player1.b2body.getWorldCenter(), true);
            if (player1.currentState == Dino.State.CLIMBING) {
                player1.b2body.setLinearVelocity(-1f, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (player1.currentState != Dino.State.CLIMBING)
                player1.playerDucking = true;
            else if (player1.currentLadder != null) {
                player1.b2body.setLinearVelocity(0, -1f);
                player1.KEYDOWN = true;
            }
        } else {
            player1.playerDucking = false;
        }
        //calls the Pickup/Drop method
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
            if (player1.hasWeapon) {
                player1.dropWeapon();
            } else {
                player1.pickupWeapon(allWeapons);
            }
        }

        //calls shoot method
        if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH)) {
            if (player1.hasWeapon) {
                player1.useWeapon();
            } else {
                //kick implentation
                player1.kick();
            }


        }

        //Player2
        player2.KEYUP = false;
        player2.KEYRIGHT = false;
        player2.KEYDOWN = false;
        player2.KEYLEFT = false;

        if (player2.climbing && !Gdx.input.isKeyPressed(Input.Keys.W)) {
            player2.b2body.setLinearVelocity(0, 0);
        }

        if (player2.currentState != Dino.State.JUMPING && Gdx.input.isKeyJustPressed(Input.Keys.W) && player2.currentState != Dino.State.CLIMBING && player2.currentState != Dino.State.FALLING) {
            player2.KEYUP = true;
            player2.b2body.applyLinearImpulse(new Vector2(0, 4f), player2.b2body.getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player2.KEYUP = true;
            if (player2.currentState == Dino.State.CLIMBING) {
                player2.b2body.setLinearVelocity(0, 1f);
            }
        }


        if (player2.currentLadder != null) {
            if ((player2.b2body.getPosition().y * DinoDuel.PPM >= player2.currentLadder.bounds.y + player2.currentLadder.bounds.height - 3f) && Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                player2.b2body.applyLinearImpulse(new Vector2(0, 4f), player2.b2body.getWorldCenter(), true);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player2.b2body.getLinearVelocity().x <= 2) {
            player2.KEYRIGHT = true;
            player2.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player2.b2body.getWorldCenter(), true);
            if (player2.currentState == Dino.State.CLIMBING) {
                player2.b2body.setLinearVelocity(1f, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player2.b2body.getLinearVelocity().x >= -2) {
            player2.KEYLEFT = true;
            player2.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player2.b2body.getWorldCenter(), true);
            if (player2.currentState == Dino.State.CLIMBING) {
                player2.b2body.setLinearVelocity(-1f, 0);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (player2.currentState != Dino.State.CLIMBING)
                player2.playerDucking = true;
            else if (player2.currentLadder != null) {
                player2.b2body.setLinearVelocity(0, -1f);
                player2.KEYDOWN = true;
            }
        } else {
            player2.playerDucking = false;
        }
//calls the Pickup/Drop method
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            if (player2.hasWeapon) {
                player2.dropWeapon();
            } else {
                player2.pickupWeapon(allWeapons);
            }
        }

//calls shoot method
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (player2.hasWeapon) {
                player2.useWeapon();
            } else {
                //kick implentation
                player2.kick();
            }
        }
    }//end handleInput

    @Override
    public void render(float deltaTime) {
        //seperates update logic from render
        update(deltaTime);

        //clears the game screen with black
        //Gdx.gl.glClearColor(92 / 255.0f, 152 / 255.0f, 142 / 255.0f, 0);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renders the game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        //renders the Dino1
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();


        player1.draw(game.batch);
        //Used to draw at the same time as p1
        for (Gun drawGun : allWeapons) {
            if (drawGun.getUser() == player1) {
                drawGun.setSize(drawGun.xSize / 10 / DinoDuel.PPM, drawGun.ySize / 10 / DinoDuel.PPM);
                drawGun.draw(game.batch);
                drawGun.drawn = true;
            }
        }


        player2.draw(game.batch);

        for (Gun drawGun : allWeapons) {
            if (!drawGun.drawn) {
                drawGun.setSize(drawGun.xSize / 10 / DinoDuel.PPM, drawGun.ySize / 10 / DinoDuel.PPM);
                drawGun.draw(game.batch);
            } else {
                drawGun.drawn = false;
            }
        }


        for (Bullet drawBullet : allBullets) {
            if (drawBullet.draw) {
                drawBullet.draw(game.batch);
            }
        }


        //sets the batch to draw what the camera sees
        // FIXME: 2020-02-20 HUD
        /*

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


         */

        //Draws the health bars above each dino
        for (Dino dino : allPlayers) {
            game.batch.setColor(Color.BLACK);
            game.batch.draw(blank, dino.b2body.getPosition().x - 0.075f, dino.b2body.getPosition().y + 0.094f, 0.16f, 0.04f);
            if (dino.health > 0.6f)
                game.batch.setColor(Color.GREEN);
            else if (dino.health > 0.2f)
                game.batch.setColor(Color.ORANGE);
            else
                game.batch.setColor(Color.RED);

            game.batch.draw(blank, dino.b2body.getPosition().x - 0.07f, dino.b2body.getPosition().y + 0.1f, 0.15f * dino.health, 0.03f);
        }


        game.batch.end();

    }//end render

    public void setCameraPosition() {
//attach the gamecam to the the middle x and y coordinate
        gameCam.position.x = (player1.b2body.getPosition().x + player2.b2body.getPosition().x) / 2;

        gameCam.position.y = (player1.b2body.getPosition().y + player1.b2body.getPosition().y) / 2;
        float xRatio = DinoDuel.V_WIDTH / DinoDuel.PPM / abs(player1.b2body.getPosition().x - player2.b2body.getPosition().x);
        float yRatio = DinoDuel.V_HEIGHT / DinoDuel.PPM / abs(player1.b2body.getPosition().y - player2.b2body.getPosition().y);

        if (xRatio < yRatio) {
            float tempX = abs(player1.b2body.getPosition().x - player2.b2body.getPosition().x);
            if (DinoDuel.V_WIDTH / DinoDuel.PPM > tempX) {
                gameCam.viewportWidth = DinoDuel.V_WIDTH / DinoDuel.PPM + 1f;
                gameCam.viewportHeight = DinoDuel.V_HEIGHT / DinoDuel.PPM + 1f;

            } else {
                gameCam.viewportWidth = tempX + 1f;
                gameCam.viewportHeight = DinoDuel.V_HEIGHT / DinoDuel.PPM / xRatio + 1f;
            }
        } else {
            float tempY = abs(player1.b2body.getPosition().y - player2.b2body.getPosition().y);
            if (DinoDuel.V_HEIGHT / DinoDuel.PPM > tempY) {
                gameCam.viewportHeight = DinoDuel.V_HEIGHT / DinoDuel.PPM + 1f;
                gameCam.viewportWidth = DinoDuel.V_WIDTH / DinoDuel.PPM + 1f;
            } else {
                gameCam.viewportHeight = tempY + 1f;
                gameCam.viewportWidth = DinoDuel.V_WIDTH / DinoDuel.PPM / yRatio + 1;
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
