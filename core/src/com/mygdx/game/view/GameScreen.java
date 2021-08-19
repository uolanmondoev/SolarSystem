package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.model.Earth;
import com.mygdx.game.model.Mars;
import com.mygdx.game.model.Moon;
import com.mygdx.game.model.Sun;


public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Sprite background;
    private final float screenWidth = Gdx.graphics.getWidth();  //ширина и
    private final float screenHeight = Gdx.graphics.getHeight();//высота окна
    private float startTime;            //для pause
    private boolean paused = false;
    private Stage stage;
    private float pausedTime;
    private float pauseElapsedNanoseconds = 0;
    //Создание планет ит.п
    Earth earth = new Earth(
            520, screenHeight / 2,
            new Texture(Gdx.files.internal("earth.png")));
    Moon moon = new Moon(
            540, screenHeight / 2,
            new Texture(Gdx.files.internal("moon.png")));
    Mars mars = new Mars(
            600, screenHeight / 2,
            new Texture(Gdx.files.internal("mars.png")));
    Sun sun = new Sun(
            screenWidth / 2,
            screenHeight / 2,
            new Texture(Gdx.files.internal("sun.png")));
    //подсчитывание радиусов
    private final float rEarthToSun = (float) (
            Math.sqrt(Math.pow(earth.getX() - sun.getX(), 2)
                    + Math.pow(earth.getY() - sun.getY(), 2)));

    private final float rMoonToEarth = (float) (
            Math.sqrt(Math.pow(earth.getX() - moon.getX(), 2)
                    + Math.pow(earth.getY() - moon.getY(), 2)));

    private final float rMarsToSun = (float) (
            Math.sqrt(Math.pow(mars.getX() - sun.getX(), 2)
                    + Math.pow(mars.getY() - sun.getY(), 2)));

    @Override
    public void show() {
        startTime = TimeUtils.nanoTime();
        batch = new SpriteBatch();

        background = new Sprite(new Texture(Gdx.files.internal("space.png")));
        background.setSize(screenWidth, screenHeight);  //спрайт фона
        //создание кнопки
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        BitmapFont font = new BitmapFont();
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons.atlas"));
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("pause");
        textButtonStyle.checked = skin.getDrawable("play");
        TextButton button = new TextButton("", textButtonStyle);
        button.setHeight(64);
        button.setWidth(64);
        button.setPosition(screenWidth / 2 - 32, 32);
        stage.addActor(button);
        //действие при нажатии кнопки
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                paused = !paused;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //логика паузы
        if (paused) {
            pauseElapsedNanoseconds = TimeUtils.nanoTime() - pausedTime;
        } else {
            startTime += pauseElapsedNanoseconds;
            float elapsedNanoseconds = TimeUtils.nanoTime() - startTime;
            float elapsedTime = (MathUtils.nanoToSec * elapsedNanoseconds);
            pauseElapsedNanoseconds = 0;
            pausedTime = TimeUtils.nanoTime();
            //обновление координат планет
            earth.updateCoordinates(
                    sun.getX(), sun.getY(), rEarthToSun, elapsedTime, 20
            );
            moon.updateCoordinates(
                    earth.getX(), earth.getY(), rMoonToEarth, elapsedTime, 2
            );
            mars.updateCoordinates(
                    sun.getX(), sun.getY(), rMarsToSun, elapsedTime, 40
            );

        }
        //отрисовка планет
        batch.begin();
        background.draw(batch);
        batch.draw(
                earth.getTexture(), earth.getX() - 10, earth.getY() - 10, 20, 20
        );
        batch.draw(
                moon.getTexture(), moon.getX() - 5, moon.getY() - 5, 10, 10
        );
        batch.draw(
                sun.getTexture(), sun.getX() - 50, sun.getY() - 50, 100, 100
        );
        batch.draw(
                mars.getTexture(), mars.getX() - 15, mars.getY() - 15, 30, 30
        );
        batch.end();
        stage.draw(); //отрисовка кнопки
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
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }
}
