package com.example.sergiotx.juego;

import android.graphics.Canvas;
import android.util.Log;


public class GameLoopThread extends Thread {

    private static final long FPS = 10;

    private GameView view;
    private boolean running = false;

    public GameLoopThread(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {
        long ticks = 1000/FPS;
        long startTime;
        long sleepTime;

        while (running) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c); //da error, pero funciona. Si cambiamos a draw como sugiere no funciona
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticks - (System.currentTimeMillis() - startTime);
            try{
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e){
                Log.d("GameLoopThread - run","Exception - sleeptime");
            }

        }
    }
}
