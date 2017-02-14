package com.example.sergiotx.juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.List;

/**
 * Created by SergioTx on 14/02/2017.
 */

public class TempSprite {

    private float x,y;
    private Bitmap bmp;
    private int life = 5;
    private List<TempSprite> temps;

    public TempSprite(List<TempSprite> temps, GameView gameView, float x,
                      float y, Bitmap bmp) {
        this.x = Math.min(Math.max(x - bmp.getWidth() / 2, 0),
                gameView.getWidth() - bmp.getWidth());
        this.y = Math.min(Math.max(y - bmp.getHeight() / 2, 0),
                gameView.getHeight() - bmp.getHeight());
        this.bmp = bmp;
        this.temps = temps;
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawBitmap(bmp, x, y, null);
    }

    private void update() {
        if (--life < 1) {
            temps.remove(this);
        }
    }
}
