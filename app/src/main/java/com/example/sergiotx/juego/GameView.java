package com.example.sergiotx.juego;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {

    private Bitmap bmp;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    private List<Sprite> sprites = new ArrayList<Sprite>();
    private List<TempSprite> tempSprites = new ArrayList<TempSprite>();

    private long lastClick;
    private Bitmap bmpBlood;

    public GameView(Context context)  {
        super(context);

        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

        bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blood1);

        loadSprites();
    }

    private void loadSprites() {
        sprites.add(createSprite(R.drawable.drow));
        /*sprites.add(createSprite(R.drawable.bad1));
        sprites.add(createSprite(R.drawable.bad2));
        sprites.add(createSprite(R.drawable.bad3));
        sprites.add(createSprite(R.drawable.bad4));
        sprites.add(createSprite(R.drawable.bad5));
        sprites.add(createSprite(R.drawable.bad6));
        sprites.add(createSprite(R.drawable.good1));
        sprites.add(createSprite(R.drawable.good2));
        sprites.add(createSprite(R.drawable.good3));
        sprites.add(createSprite(R.drawable.good4));
        sprites.add(createSprite(R.drawable.good5));
        sprites.add(createSprite(R.drawable.good6));*/
    }

    private Sprite createSprite(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Sprite(this,bmp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        for (Sprite sprite : sprites) {
            sprite.onDraw(canvas);
        }
        for (int i = tempSprites.size() - 1; i >= 0; i--) {
            tempSprites.get(i).onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("GameView - onTouchEvent","TOQUE");

        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollition(x, y)) {
                        //sprites.remove(sprite); //no borrar el muñeco
                        //añadir sangre
                        tempSprites.add(new TempSprite(tempSprites, this, x, y, bmpBlood));
                        sprite.changeSpeed();
                        break;
                    }
                }
            }
        }
        return true;
    }
}
