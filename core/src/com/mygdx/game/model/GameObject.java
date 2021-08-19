package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;


public abstract class GameObject {          //родительский класс объектов
    private final Texture objTexture;
    private float xPos, yPos;               //эти переменные нужны для return'а

    public GameObject(float x, float y, Texture texture) {
        objTexture = texture;
        xPos = x;
        yPos = y;
    }

    public void updateCoordinates(float dependX, float dependY, float radius, float elapsedTime, float speedInSeconds) {
        float PositionInCycle = (elapsedTime / speedInSeconds) % 1;
        xPos = (float) (dependX + radius * Math.cos(Math.PI * 2 * PositionInCycle));
        yPos = (float) (dependY + radius * Math.sin(Math.PI * 2 * PositionInCycle));
    }

    public float getX() {
        return xPos;
    }

    public float getY() {
        return yPos;
    }

    public Texture getTexture() {
        return objTexture;
    }
}
