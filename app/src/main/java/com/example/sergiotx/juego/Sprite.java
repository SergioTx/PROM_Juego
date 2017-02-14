package com.example.sergiotx.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;


public class Sprite {

    private static final int BMP_ROWS = 4, BMP_COLUMNS = 3;

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };

    private int x = 0, y = 0;
    private int xSpeed, ySpeed;
    private GameView gameView;
    private Bitmap bmp;
    private int currentFrame = 0;
    private int width, height;

    public Sprite(GameView gameView, Bitmap bmp){
        this.gameView = gameView;
        this.bmp = bmp;
        //velocidad
        Random rnd = new Random();
        xSpeed = rnd.nextInt(50) - 5;
        ySpeed = rnd.nextInt(50) - 5;

        //filas y columnas
        width = bmp.getWidth() / BMP_COLUMNS;
        height = bmp.getHeight() / BMP_ROWS;
    }

    private void update(){

        if (x >= gameView.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;

        if (y >= gameView.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;

        currentFrame = ++currentFrame % BMP_COLUMNS;
    }

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    public void onDraw(Canvas canvas){
        update();

        int srcX = currentFrame * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x,y,x + width, y + height);

        canvas.drawBitmap(bmp,src,dst,null);
    }

    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }

    //cambia la velocidad a otra aleatoria
    public void changeSpeed() {
        Random rnd = new Random();
        xSpeed = rnd.nextInt(100);
        ySpeed = rnd.nextInt(100);
    }
}
