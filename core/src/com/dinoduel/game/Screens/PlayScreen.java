package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Scenes.Hud;
import com.dinoduel.game.Sprites.Dino;
import com.dinoduel.game.Sprites.GunBox.GreyGunBox;
import com.dinoduel.game.Sprites.InteractiveTileObject;
import com.dinoduel.game.Sprites.Ladder;
import com.dinoduel.game.Sprites.Weapons.AK;
import com.dinoduel.game.Sprites.Weapons.Bullet;
import com.dinoduel.game.Sprites.Weapons.Explosive;
import com.dinoduel.game.Sprites.Weapons.Grenade;
import com.dinoduel.game.Sprites.Weapons.Pistol;
import com.dinoduel.game.Sprites.Weapons.Shotgun;
import com.dinoduel.game.Sprites.Weapons.Sniper;
import com.dinoduel.game.Sprites.Weapons.Weapon;
import com.dinoduel.game.Tools.B2WorldCreator;
import com.dinoduel.game.Tools.WorldContactListener;

import java.util.ArrayList;

import static java.lang.StrictMath.abs;

public class PlayScreen extends AbstractScreen {
    //Main Game
    public DinoDuel game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    //Creates the hud

    //Map
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //private ShapeRenderer shapeRenderer = new ShapeRenderer();

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //Player
    private static Dino player1;
    private static Dino player2;
    private static Dino player3;
    private static Dino player4;

    //Player Sprites
    private TextureAtlas dinoAtlas;

    //Weapon Sprites
    private TextureAtlas weaponAtlas;

    //Crate sprites
    public TextureAtlas crateAtlas;
    //Weapon Spawns
    private boolean spawnWeapon;
    private float spawnX;
    private float spawnY;
    private int spawnType = -1;

    public static PlayScreen screen;
    //Weapon list
    public ArrayList<Weapon> allWeapons = new ArrayList<>();
    //Bullet list
    public ArrayList<Bullet> allBullets = new ArrayList<>();
    //GunBox List
    public ArrayList<InteractiveTileObject> allBoxes = new ArrayList<>();
    //Ladder List
    public ArrayList<Ladder> allLadders = new ArrayList<>();
    //Player List
    public ArrayList<Dino> allLivingPlayers = new ArrayList<>();
    public static ArrayList<Dino> allPlayers = new ArrayList<>();
    //Grey box List
    public ArrayList<GreyGunBox> allGreyGunBoxes = new ArrayList<>();
    //Controllers
    //Array<Controller> controllers = Controllers.getControllers();

    Controller p1Controller = null;
    private float p1Speedx;
    private float p1Speedy;


    private enum State {Running, Paused}

    public State currentState;

    //A Blank texture (Used for HealthBars)
    private Texture blank;
    private long startTime;

    public PlayScreen(DinoDuel game) {
        super(game);
        screen = this;
        dinoAtlas = new TextureAtlas("Dinos/DinoSprites.txt");
        weaponAtlas = new TextureAtlas("Weapons/weaponsV2.txt");
        crateAtlas = new TextureAtlas("DinoDuel Basic Tilesets/crates.txt");

        this.game = game;
        //Camera that follows the players
        gameCam = new OrthographicCamera();
        //Fits the proper aspect ratio
        gamePort = new FillViewport(DinoDuel.V_WIDTH / 2 / DinoDuel.PPM, DinoDuel.V_HEIGHT / 2 / DinoDuel.PPM, gameCam);
        //Creates the hud
        hud = new Hud(game.batch);
        //Renders the map
        game.manager.queueMap();
        game.manager.assetManager.finishLoading();
        map = game.manager.assetManager.get("DinoDuel Basic Tilesets/map1.tmx");


        renderer = new OrthogonalTiledMapRenderer(map, 1 / DinoDuel.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //Creates the world
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreator(world, map, this);

        //Players
        createPlayers();

        //contact listener stuff
        world.setContactListener(new WorldContactListener());
        blank = new Texture("blank.png");
        game.playingSong.stop();
        game.playingSong = game.manager.assetManager.get("Music/TheWhite.mp3");
        game.playingSong.play();
        startTime = System.currentTimeMillis();
        //Fixme Controller Support
        /*
        if (controllers.size == 0) {
            System.out.println("NO Controllers");
        } else {
            for (Controller c : controllers) {
                if (c.getName().contains("Xbox") || c.getName().contains("MAGIC")) {
                    p1Controller = c;
                }
            }
        }
         */

        allWeapons.add(new Grenade(30, 30, world, this));
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

        //Removes dead players
        for (int i = 0; i < allLivingPlayers.size(); i++) {
            if (allLivingPlayers.get(i).getPermaDead()) {
                allLivingPlayers.get(i).timeAlive = System.currentTimeMillis() - startTime;
                allLivingPlayers.remove(i);
            }
        }

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

        // Destroys empty weapons
        for (int i = 0; i < allWeapons.size(); i++) {
            if (allWeapons.get(i).flag) {
                System.out.println("Destroys");
                if (allWeapons.get(i).wBody != null) {
                    allWeapons.get(i).wBody.setAwake(false);
                    world.destroyBody(allWeapons.get(i).wBody);
                    allWeapons.get(i).wBody = null;
                }
                screen.allWeapons.remove(allWeapons.get(i));
                i--;
            }
        }

        //determined if gunboxes can spawn guns
        //spawns weapons if told to
        if (spawnWeapon) {
            Weapon spawn = null;
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
                case 4:
                    //Grenade
                    spawn = new Grenade(spawnX, spawnY, world, this);
            }
            spawnWeapon = false;
            spawnType = -1;
            if (spawn != null)
                allWeapons.add(spawn);
        }

        //Tests for players on a ladder
        for (Dino dino : allLivingPlayers) {
            dino.climbing = false;
            dino.currentLadder = null;
            for (Ladder ladder : allLadders) {
                if (ladder.bounds.contains(dino.getB2body().getPosition().x * DinoDuel.PPM, dino.getB2body().getPosition().y * DinoDuel.PPM - dino.getHeight() / 2)) {
                    if (dino.KEYUP || dino.KEYDOWN || dino.previousState == Dino.State.CLIMBING) {
                        dino.climbing = true;
                        dino.currentLadder = ladder;
                    }
                    break;
                }
            }

        }

        //Updates player sprite position
        player1.update(dt);
        for (Weapon updateWeapon : allWeapons) {
            if (updateWeapon.getUser() == player1) {
                updateWeapon.update(dt);
                updateWeapon.update = true;
            }
        }

        player2.update(dt);

        for (Weapon updateWeapon : allWeapons) {
            if (!updateWeapon.update) {
                updateWeapon.update(dt);
            }else
                updateWeapon.update = false;
        }

        //updates bullets
        for (Bullet updateBullet : allBullets) {
            updateBullet.update(dt);

        }
        //updates gunboxes
        for (InteractiveTileObject updateBox : allBoxes
        ) {
            updateBox.update(dt);
        }
        //sets the camera position
        setCameraPosition();
        gameCam.update();
        //tells it to only render what the camera can see
        renderer.setView(gameCam);
    }//end update

    private void handleInput(float dt) {
        //******************************Player1******************************
        if (player1.canMove) {
            player1.KEYUP = false;
            player1.KEYRIGHT = false;
            player1.KEYDOWN = false;
            player1.KEYLEFT = false;
            if (player1.climbing) {
                player1.getB2body().setLinearVelocity(0, 0);
            }
            if (player1.currentState != Dino.State.JUMPING && Gdx.input.isKeyJustPressed(Input.Keys.UP) && player1.currentState != Dino.State.CLIMBING && player1.currentState != Dino.State.FALLING) {
                player1.KEYUP = true;
                player1.getB2body().applyLinearImpulse(new Vector2(0, 4f), player1.getB2body().getWorldCenter(), true);

            } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                player1.KEYUP = true;
                if (player1.currentState == Dino.State.CLIMBING) {
                    player1.getB2body().setLinearVelocity(0, 1f);
                }
            }

            if (player1.climbing) {
                if ((player1.getB2body().getPosition().y * DinoDuel.PPM >= player1.currentLadder.bounds.y + player1.currentLadder.bounds.height - 3f) && Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    player1.getB2body().applyLinearImpulse(new Vector2(0, 1.5f), player1.getB2body().getWorldCenter(), true);
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player1.getB2body().getLinearVelocity().x <= 2) {
                player1.KEYRIGHT = true;
                if (player1.currentState != Dino.State.SLIDING) {
                    player1.getB2body().applyLinearImpulse(new Vector2(0.1f, 0), player1.getB2body().getWorldCenter(), true);
                }
                if (player1.currentState == Dino.State.CLIMBING) {
                    player1.getB2body().setLinearVelocity(1f, 0);
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player1.getB2body().getLinearVelocity().x >= -2) {
                player1.KEYLEFT = true;
                if (player1.currentState != Dino.State.SLIDING) {
                    player1.getB2body().applyLinearImpulse(new Vector2(-0.1f, 0), player1.getB2body().getWorldCenter(), true);
                }
                if (player1.currentState == Dino.State.CLIMBING) {
                    player1.getB2body().setLinearVelocity(-1f, 0);
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

                player1.KEYDOWN = true;
                if (player1.currentState != Dino.State.CLIMBING)
                    player1.playerDucking = true;
                else if (player1.climbing) {
                    player1.getB2body().setLinearVelocity(0, -1f);
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
                } else if (!player1.playerDucking) {
                    //kick implentation
                    player1.kick(allLivingPlayers);
                }
            }
            //Stops Sliding
            if (player1.getB2body().getLinearVelocity().x > 0.05f && !player1.KEYRIGHT && !player1.KEYLEFT) {
                player1.getB2body().applyLinearImpulse(new Vector2(-0.05f, 0), player1.getB2body().getWorldCenter(), true);
            } else if (player1.getB2body().getLinearVelocity().x < -0.05f && !player1.KEYLEFT && !player1.KEYRIGHT) {
                player1.getB2body().applyLinearImpulse(new Vector2(0.05f, 0), player1.getB2body().getWorldCenter(), true);
            }
        }
        //Fixme Controller Support
//player1.b2body.applyLinearImpulse(new Vector2(p1Speedx, p1Speedy), player1.b2body.getWorldCenter(), true);
        //******************************Player2******************************
        if (player2.canMove) {
            player2.KEYUP = false;
            player2.KEYRIGHT = false;
            player2.KEYDOWN = false;
            player2.KEYLEFT = false;

            if (player2.climbing && !Gdx.input.isKeyPressed(Input.Keys.W)) {
                player2.getB2body().setLinearVelocity(0, 0);
            }

            if (player2.currentState != Dino.State.JUMPING && Gdx.input.isKeyJustPressed(Input.Keys.W) && player2.currentState != Dino.State.CLIMBING && player2.currentState != Dino.State.FALLING) {
                player2.KEYUP = true;
                player2.getB2body().applyLinearImpulse(new Vector2(0, 4f), player2.getB2body().getWorldCenter(), true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                player2.KEYUP = true;
                if (player2.currentState == Dino.State.CLIMBING) {
                    player2.getB2body().setLinearVelocity(0, 1f);
                }
            }

            if (player2.climbing) {
                if ((player2.getB2body().getPosition().y * DinoDuel.PPM >= player2.currentLadder.bounds.y + player2.currentLadder.bounds.height - 3f) && Gdx.input.isKeyPressed(Input.Keys.W)) {
                    player2.getB2body().applyLinearImpulse(new Vector2(0, 1.5f), player2.getB2body().getWorldCenter(), true);

                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D) && player2.getB2body().getLinearVelocity().x <= 2) {
                player2.KEYRIGHT = true;
                player2.getB2body().applyLinearImpulse(new Vector2(0.1f, 0), player2.getB2body().getWorldCenter(), true);
                if (player2.currentState == Dino.State.CLIMBING) {
                    player2.getB2body().setLinearVelocity(1f, 0);
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A) && player2.getB2body().getLinearVelocity().x >= -2) {
                player2.KEYLEFT = true;
                player2.getB2body().applyLinearImpulse(new Vector2(-0.1f, 0), player2.getB2body().getWorldCenter(), true);
                if (player2.currentState == Dino.State.CLIMBING) {
                    player2.getB2body().setLinearVelocity(-1f, 0);
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                player2.KEYDOWN = true;
                if (player2.currentState != Dino.State.CLIMBING)
                    player2.playerDucking = true;
                else if (player2.climbing) {
                    player2.getB2body().setLinearVelocity(0, -1f);
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
                } else if (!player2.playerDucking) {
                    //kick implentation
                    player2.kick(allLivingPlayers);
                }
            }

            //Stops Sliding
            if (player2.getB2body().getLinearVelocity().x > 0.05f && !player2.KEYRIGHT && !player2.KEYLEFT) {
                player2.getB2body().applyLinearImpulse(new Vector2(-0.05f, 0), player2.getB2body().getWorldCenter(), true);
            } else if (player2.getB2body().getLinearVelocity().x < -0.05f && !player2.KEYLEFT && !player2.KEYRIGHT) {
                player2.getB2body().applyLinearImpulse(new Vector2(0.05f, 0), player2.getB2body().getWorldCenter(), true);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        }

        //Fixme Controller Support
        //*****************************TESTING CONTROLLER*************************************
        /*
        p1Controller.addListener(new ControllerAdapter() {
            @Override
            public boolean buttonDown(Controller controller, int buttonIndex) {
                return false;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonIndex) {
                if (buttonIndex == XBox.BUTTON_A) {
                    //jump
                    System.out.println("JUMP");
                }
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisIndex, float value) {
                if (axisIndex == XBox.AXIS_LX) {
                    p1Speedx = value;
                    System.out.println(value);
                } else if (axisIndex == XBox.AXIS_LY) {
                     p1Speedy= value;
                    System.out.println(value);
                }
                return false;
            }

            @Override
            public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
                return false;
            }
        });
         */
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

        //renders grayed boxes
        /*shapeRenderer.setProjectionMatrix(gameCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (InteractiveTileObject gunBox: allBoxes) {
            //Vector2 pos = gunBox.body.getWorldCenter();
            shapeRenderer.setColor(new Color());
            shapeRenderer.rect(gunBox.bounds.getX()/DinoDuel.PPM, gunBox.bounds.getY()/DinoDuel.PPM, gunBox.bounds.width/DinoDuel.PPM, gunBox.bounds.height/DinoDuel.PPM);

        }
        shapeRenderer.end();

         */


        //renderer our Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        //renders the Dino1
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();


        player1.draw(game.batch);
        //Used to draw at the same time as p1
        for (Weapon drawWeapon : allWeapons) {
            if (drawWeapon.getUser() == player1) {
                drawWeapon.setSize(drawWeapon.xSize / 10f / DinoDuel.PPM, drawWeapon.ySize / 10f / DinoDuel.PPM);
                drawWeapon.draw(game.batch);
                drawWeapon.drawn = true;
            }
        }


        player2.draw(game.batch);

        for (Weapon drawWeapon : allWeapons) {
            if (!drawWeapon.drawn) {
                drawWeapon.setSize(drawWeapon.xSize / 10f / DinoDuel.PPM, drawWeapon.ySize / 10f / DinoDuel.PPM);
                drawWeapon.draw(game.batch);
            } else {
                drawWeapon.drawn = false;
            }
        }


        for (Bullet drawBullet : allBullets) {
            if (drawBullet.draw) {
                drawBullet.draw(game.batch);
            }
        }

        //GreyGunBox
        for (GreyGunBox greyGunBox : allGreyGunBoxes) {
            greyGunBox.draw(game.batch);

        }


        //sets the batch to draw what the camera sees
        // FIXME: 2020-02-20 HUD
        /*

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
         */

        //Draws the health bars above each dino
        for (Dino dino : allLivingPlayers) {
            game.batch.setColor(Color.BLACK);
            game.batch.draw(blank, dino.getB2body().getPosition().x - 0.075f, dino.getB2body().getPosition().y + 0.094f, 0.16f, 0.04f);
            if (dino.health > 0.6f)
                game.batch.setColor(Color.GREEN);
            else if (dino.health > 0.2f)
                game.batch.setColor(Color.ORANGE);
            else
                game.batch.setColor(Color.RED);

            game.batch.draw(blank, dino.getB2body().getPosition().x - 0.07f, dino.getB2body().getPosition().y + 0.1f, 0.15f * dino.health, 0.03f);
        }


        game.batch.end();

        if (allLivingPlayers.size() == 1) {
            allLivingPlayers.get(0).timeAlive = System.currentTimeMillis() - startTime + 1;
            game.setScreen(new VictoryScreen(game));
            game.playingSoundEffect.stop();
            game.playingSong.stop();
            dispose();
        }
    }//end render

    private void setCameraPosition() {
//attach the gamecam to the the middle x and y coordinate
        gameCam.position.x = (player1.getB2body().getPosition().x + player2.getB2body().getPosition().x) / 2;
        gameCam.position.y = (player1.getB2body().getPosition().y + player1.getB2body().getPosition().y) / 2;

        float xRatio = DinoDuel.V_WIDTH / 2 / DinoDuel.PPM / abs(player1.getB2body().getPosition().x - player2.getB2body().getPosition().x);
        float yRatio = DinoDuel.V_HEIGHT / 2 / DinoDuel.PPM / abs(player1.getB2body().getPosition().y - player2.getB2body().getPosition().y);
        if (xRatio < yRatio) {
            float tempX = abs(player1.getB2body().getPosition().x - player2.getB2body().getPosition().x);
            if (DinoDuel.V_WIDTH / 2 / DinoDuel.PPM > tempX) {
                gameCam.viewportWidth = DinoDuel.V_WIDTH / 2 / DinoDuel.PPM + 1f;
                gameCam.viewportHeight = DinoDuel.V_HEIGHT / 2 / DinoDuel.PPM + 1f;

            } else {
                gameCam.viewportWidth = tempX + 1f;
                gameCam.viewportHeight = DinoDuel.V_HEIGHT / 2 / DinoDuel.PPM / xRatio + 1f;
            }
        } else {
            float tempY = abs(player1.getB2body().getPosition().y - player2.getB2body().getPosition().y);
            if (DinoDuel.V_HEIGHT / 2 / DinoDuel.PPM > tempY) {
                gameCam.viewportHeight = DinoDuel.V_HEIGHT / 2 / DinoDuel.PPM + 1f;
                gameCam.viewportWidth = DinoDuel.V_WIDTH / 2 / DinoDuel.PPM + 1f;
            } else {
                gameCam.viewportHeight = tempY + 1f;
                gameCam.viewportWidth = DinoDuel.V_WIDTH / 2 / DinoDuel.PPM / yRatio + 1;
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

    public void createPlayers() {
        String p1 = CharcterSelectMenu.getDinoData(1);
        String p2 = CharcterSelectMenu.getDinoData(2);
        String p3 = CharcterSelectMenu.getDinoData(3);
        String p4 = CharcterSelectMenu.getDinoData(4);
        player1 = new Dino(world, this, p1, new Vector2(0.5f, 0.2f), 3);
        allLivingPlayers.add(player1);
        player2 = new Dino(world, this, p2, new Vector2(2.5f, 1.5f), 3);
        allLivingPlayers.add(player2);
        if (!p3.equalsIgnoreCase("nullSprites")) {
            player3 = new Dino(world, this, p3, new Vector2(2.5f, 1.5f), 3);
            allLivingPlayers.add(player3);
        }
        if (!p4.equalsIgnoreCase("nullSprites")) {
            player4 = new Dino(world, this, p4, new Vector2(2.5f, 1.5f), 3);
            allLivingPlayers.add(player4);
        }
        for (int i = 0; i < allLivingPlayers.size(); i++) {
            allPlayers.add(allLivingPlayers.get(i));
        }
    }//end createPlayers

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
        game.batch.setColor(new Color(1, 1, 1, 1));
        map.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        super.dispose();
    }//end dispose

    public static String getDinoName(int playerNum) {
        if (playerNum == 2) {
            return player2.getName();
        } else if (playerNum == 3) {
            return player3.getName();
        } else if (playerNum == 4) {
            return player4.getName();
        }
        return player1.getName();
    }//end getDinoName

    public static float getDinoTime(int playerNum) {
        if (playerNum == 2) {
            return player2.timeAlive;
        } else if (playerNum == 3) {
            return player3.timeAlive;
        } else if (playerNum == 4) {
            return player4.timeAlive;
        }
        return player1.timeAlive;
    }//end getDinoTime
}//end class
