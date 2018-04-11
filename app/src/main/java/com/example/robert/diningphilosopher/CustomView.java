package com.example.robert.diningphilosopher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.*;
import android.view.MotionEvent;
import android.view.View;

import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.graphics.Color.CYAN;
import static com.example.robert.diningphilosopher.MainActivity.height;
import static com.example.robert.diningphilosopher.MainActivity.width;

public class CustomView extends View {

    public static int[] philosopherX = {width / 2 - 175, width / 2 - 1000, width / 2 - 850, width / 2 + 450, width / 2 + 600};
    public static int[] philosopherY = {height / 2 - 600, height / 2 - 200, height / 2 + 400, height / 2 + 400, height / 2 - 200};
    public static int[] plateX = {width / 2, width / 2 - 320, width / 2 + 320, width / 2 - 200, width / 2 + 200};
    public static int[] plateY = {height / 2 - 325, height / 2 - 80, height / 2 - 80, height / 2 + 275, height / 2 + 275};
    public static int[] foodX = {width / 2, width / 2 - 320, width / 2 - 200, width / 2 + 200, width / 2 + 320};
    public static int[] foodY = {height / 2 - 325, height / 2 - 80, height / 2 + 275, height / 2 + 275, height / 2 - 80};
    public static int[] forkX = {width / 2 + 250, width / 2 - 250, width / 2 - 380, width / 2, width / 2 + 380};
    public static int[] forkY = {height / 2 - 325, height / 2 - 325, height / 2 + 150, height / 2 + 415, height / 2 + 150};
    public static int[] forkXdraw = {width / 2 + 150, width / 2 - 150, width / 2 - 250, width / 2, width / 2 + 250};
    public static int[] forkYdraw = {height / 2 - 225, height / 2 - 225, height / 2 + 75, height / 2 + 275, height / 2 + 75};
    public static int[] colors = {Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.LTGRAY, Color.rgb(255, 165, 0), Color.rgb(210, 105, 30)};
    public static int[] forks = null;
    public static int[] philStatus = null;
    public static Thread[] philosophers = null;

    int foodchoice = 0; //0 = peas, 1 = oranges
    MediaPlayer button;
    MediaPlayer song;
    boolean option = false;

    private Paint paint;

    public CustomView(Context context, int[] forksArray, int[] philArray, Thread[] philosophers, MediaPlayer button, MediaPlayer song) {
        super(context);
        this.forks = forksArray;
        this.philStatus = philArray;
        this.philosophers = philosophers;
        this.button = button;
        this.song = song;

        song.setLooping(true);
        song.start();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setStrokeWidth(16);

        //draw background
        canvas.drawColor(Color.BLACK);

        //draw philospher names
        for (int i = 0; i < 5; i++) {
            paint.setTextSize(70);
            paint.setColor(colors[i]);
            canvas.drawText("Philosopher " + (i + 1), philosopherX[i], philosopherY[i], paint);
        }

        //draw table
        paint.setColor(Color.rgb(153, 76, 0));
        canvas.drawCircle(width / 2, height / 2, 450, paint);
        paint.setColor(Color.rgb(102, 51, 0));
        canvas.drawCircle((width / 2) - 2, height / 2 + 2, 410, paint);

        //draw plates
        for (int i = 0; i < 5; i++) {
            paint.setColor(Color.BLACK);
            canvas.drawCircle(plateX[i] - 2, plateY[i] - 2, 100, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(plateX[i] - 2, plateY[i] - 2, 90, paint);
            paint.setColor(Color.LTGRAY);
            canvas.drawCircle(plateX[i] - 2, plateY[i] - 2, 70, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(plateX[i] - 2, plateY[i] - 2, 58, paint);
        }

        //draw statuses
        for (int i = 0; i < 5; i++) {
            if (philosophers[i] != null) {
                switch (philStatus[i]) {
                    case 0: //waiting
                        paint.setColor(Color.RED);
                        canvas.drawText("Waiting...", philosopherX[i], philosopherY[i] + 70, paint);
                        break;
                    case 1: //eating
                        paint.setColor(Color.GREEN);
                        canvas.drawText("Eating...", philosopherX[i], philosopherY[i] + 70, paint);
                        paint.setColor(colors[i]);
                        canvas.drawLine(forkX[i], forkY[i], forkXdraw[i], forkYdraw[i], paint);
                        canvas.drawLine(forkX[(i + 1) % 5], forkY[(i + 1) % 5], forkXdraw[(i + 1) % 5], forkYdraw[(i + 1) % 5], paint);
                        if (foodchoice == 0) {
                            paint.setColor(Color.BLACK);
                            canvas.drawCircle(foodX[i], foodY[i], 25, paint);
                            paint.setColor(Color.GREEN);
                            canvas.drawCircle(foodX[i], foodY[i], 18, paint);
                            break;
                        }
                        if (foodchoice == 1) {
                            paint.setColor(Color.BLACK);
                            canvas.drawCircle(foodX[i], foodY[i], 48, paint);
                            paint.setColor(colors[4]);
                            canvas.drawCircle(foodX[i], foodY[i], 40, paint);
                            break;
                        }
                        if (foodchoice == 2) {
                            paint.setColor(Color.BLACK);
                            canvas.drawCircle(foodX[i], foodY[i], 48, paint);
                            paint.setColor(Color.RED);
                            canvas.drawCircle(foodX[i], foodY[i], 40, paint);
                            break;
                        }
                    case 2: //Resting
                        paint.setColor(Color.WHITE);
                        canvas.drawText("Resting...", philosopherX[i], philosopherY[i] + 70, paint);
                        break;
                }
            }
        }


        //draw big plate
        paint.setColor(Color.BLACK);
        canvas.drawCircle(width / 2, height / 2, 150, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(width / 2, height / 2, 140, paint);
        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(width / 2, height / 2, 100, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(width / 2, height / 2, 90, paint);

        //draw food on big plate
        if (foodchoice == 0) {
            //draw peas
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    paint.setColor(Color.BLACK);
                    canvas.drawCircle((width / 2 - 52) + (i * 50), height / 2 - 52 + (j * 50), 25, paint);
                    paint.setColor(Color.GREEN);
                    canvas.drawCircle((width / 2 - 52) + (i * 50), height / 2 - 52 + (j * 50), 18, paint);
                }
            }
        } else if (foodchoice == 1) {
            //draw orange
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 - 38, height / 2 - 38, 48, paint);
            paint.setColor(colors[4]);
            canvas.drawCircle(width / 2 - 38, height / 2 - 38, 40, paint);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 + 25, height / 2 - 38, 48, paint);
            paint.setColor(colors[4]);
            canvas.drawCircle(width / 2 + 25, height / 2 - 38, 40, paint);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 - 10, height / 2, 48, paint);
            paint.setColor(colors[4]);
            canvas.drawCircle(width / 2 - 10, height / 2, 40, paint);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 + 15, height / 2 + 20, 48, paint);
            paint.setColor(colors[4]);
            canvas.drawCircle(width / 2 + 15, height / 2 + 20, 40, paint);
        } else if (foodchoice == 2) {
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 - 38, height / 2 - 38, 48, paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(width / 2 - 38, height / 2 - 38, 40, paint);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 + 25, height / 2 - 38, 48, paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(width / 2 + 25, height / 2 - 38, 40, paint);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 - 10, height / 2, 48, paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(width / 2 - 10, height / 2, 40, paint);
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 + 15, height / 2 + 20, 48, paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(width / 2 + 15, height / 2 + 20, 40, paint);
        }


        //draw button
        paint.setColor(Color.GREEN);
        canvas.drawRoundRect(width / 2 + 500, 100, width / 2 + 1000, 300, 50, 50, paint);
        paint.setColor(Color.rgb(34, 139, 34));
        canvas.drawRoundRect(width / 2 + 510, 110, width / 2 + 990, 290, 50, 50, paint);
        paint.setColor(Color.WHITE);
        if (option == false) {
            canvas.drawText("Options", width / 2 + 625, 225, paint);
        } else {
            canvas.drawText("Back", width/2 + 650, 225, paint);
        }

        //draw option box
        if (option == true) {
            paint.setColor(Color.GREEN);
            canvas.drawRoundRect(width / 2 - 500, 300, width / 2 + 500, 1200, 50, 50, paint);
            paint.setColor(Color.rgb(34, 139, 34));
            canvas.drawRoundRect(width / 2 - 490, 310, width / 2 + 490, 1190, 50, 50, paint);

            //draw options
            paint.setColor(Color.WHITE);
            canvas.drawText("Select Food", width / 2 - 200, 400, paint);
            //food selections
            //peas
            paint.setColor(Color.GREEN);
            canvas.drawRoundRect(width / 2 - 450, 425, width / 2 - 170, 650, 50, 50, paint);
            if (foodchoice == 0) {
                paint.setColor(Color.CYAN);
                canvas.drawRoundRect(width / 2 - 440, 435, width / 2 - 180, 640, 50, 50, paint);
            } else {
                paint.setColor(Color.rgb(34, 139, 34));
                canvas.drawRoundRect(width / 2 - 440, 435, width / 2 - 180, 640, 50, 50, paint);
            }
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 - 310, 537, 25, paint);
            paint.setColor(Color.GREEN);
            canvas.drawCircle(width / 2 - 310, 537, 18, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("Peas", width / 2 - 390, 630, paint);

            //orange
            paint.setColor(Color.GREEN);
            canvas.drawRoundRect(width / 2 - 160, 425, width / 2 + 150, 650, 50, 50, paint);
            if (foodchoice == 1) {
                paint.setColor(Color.CYAN);
                canvas.drawRoundRect(width / 2 - 150, 435, width / 2 + 140, 640, 50, 50, paint);
            } else {
                paint.setColor(Color.rgb(34, 139, 34));
                canvas.drawRoundRect(width / 2 - 150, 435, width / 2 + 140, 640, 50, 50, paint);
            }
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 - 10, 525, 48, paint);
            paint.setColor(colors[4]);
            canvas.drawCircle(width / 2 - 10, 525, 40, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("Oranges", width / 2 - 140, 630, paint);

            //food 3
            paint.setColor(Color.GREEN);
            canvas.drawRoundRect(width / 2 + 160, 425, width / 2 + 450, 650, 50, 50, paint);
            if (foodchoice == 2) {
                paint.setColor(Color.CYAN);
                canvas.drawRoundRect(width / 2 + 170, 435, width / 2 + 440, 640, 50, 50, paint);
            } else {
                paint.setColor(Color.rgb(34, 139, 34));
                canvas.drawRoundRect(width / 2 + 170, 435, width / 2 + 440, 640, 50, 50, paint);
            }
            paint.setColor(Color.BLACK);
            canvas.drawCircle(width / 2 + 305, 525, 48, paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(width / 2 + 305, 525, 40, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("Tomato", width / 2 + 180, 630, paint);

            //draw about box
            paint.setColor(Color.GREEN);
            canvas.drawRoundRect(width / 2 - 450, 700, width / 2 + 450, 1150, 50, 50, paint);
            paint.setColor(Color.rgb(34, 139, 34));
            canvas.drawRoundRect(width / 2 - 440, 710, width / 2 + 440, 1140, 50, 50, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("© 2017 Robert Pierucci", width/2 - 350, 780, paint);
            canvas.drawText("ver 0.2", width/2 - 100, 840, paint);
        }



        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("TOUCHED");
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ((x > width / 2 + 500 && x < width / 2 + 1000) && (y > 100 && y < 300)) {
                    //play sound
                    button.start();
                    if (option == false) {
                        option = true;
                    } else {
                        option = false;
                    }
                }
                //peas selected
                if ((option == true) && ((x > width / 2 - 440 && x < width / 2 - 180) && (y < 650 && y > 425))) {
                    //play sound
                    button.start();
                    foodchoice = 0;
                }
                //orange selected
                if ((option == true) && ((x > width / 2 - 150 && x < width / 2 + 140) && (y < 650 && y > 425))) {
                    //play sound
                    button.start();
                    foodchoice = 1;
                }
                //tomato selected
                if ((option == true) && ((x > width / 2 + 170 && x < width / 2 + 400) && (y < 650 && y > 425))) {
                    //play sound
                    button.start();
                    foodchoice = 2;
                }
                break;
        }
        return true;
    }


}