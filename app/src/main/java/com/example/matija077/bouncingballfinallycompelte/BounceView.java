package com.example.matija077.bouncingballfinallycompelte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Matija077 on 4/3/2017.
 */

public class BounceView extends View {
    //Provides classes that expose basic user interface classes that handle screen layout
    // and interaction with the user.

    float positionX, positionY;
    int speedX, speedY;
    float ballRadius;
    int availableWidth, availableHeight;
    int currentBallColor;
    Bitmap ballBitmap, addLifeBitmap;
    Paint myPaint;
    //private ballInWallListener myBallInWallListener;
    private collisionListener myCollisionListener;

    boolean firstDraw = true;
    ArrayList myObstacle;
    ArrayList myObstacleExpireTimes;
    long currentTime = 0;
    long LIFE_UP_EXPIRATION_TIME = 6000;
    Random r;

   /* public interface ballInWallListener {
        void onBallInWall(String input);
    }

    public void setBallInWallListener(ballInWallListener inputListener) {
        this.myBallInWallListener = inputListener;
    }*/

   public interface collisionListener {
       void onCollision(String cause);
   }

   public void setCollisionListener(collisionListener inputListener) {
       this.myCollisionListener = inputListener;
   }

    public BounceView(Context context) {
        super(context);
        positionX = 0;
        positionY = 0;
        speedX = 0;
        speedY = 0;
        myPaint = new Paint();
        //If this view doesn't do any drawing on its own,
        // set this flag to allow further optimizations.
        //setWillNotDraw(false);
        //In computing, a bitmap is a mapping from some domain
        // (for example, a range of integers) to bits
        //creates Bitmap objects from various sources, including files, streams, and byte-arrays.
        //res	Resources: The resources object containing the image data
        //id	int: The resource id of the image data
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ballv2);
        addLifeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.life);
        myObstacle = new ArrayList();
        myObstacleExpireTimes = new ArrayList();
        r = new Random();
    }

    public BounceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        positionX = 0;
        positionY = 0;
        speedX = 0;
        speedY = 0;
        myPaint = new Paint();
        //setWillNotDraw(false);
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ballv2);
        addLifeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.life);
        myObstacle = new ArrayList();
        myObstacleExpireTimes = new ArrayList();
        r = new Random();
    }

    public void reset_upLife_moments() {
        //clear all obstacles
        myObstacle.clear();
        myObstacleExpireTimes.clear();

        // Let's generate times for life-up opportunities.
        // Let's assume 10 such opportunities in random ranges:
        // [0-10]s, [11-20]s, [21-30]s, ..., [91-100]s
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            int timeToShow = r.nextInt(10) + i * 10 + 1;
            timeToShow = 1000 * timeToShow;
            System.out.println(timeToShow);
            add_obstacle((long) timeToShow);
        }
    }

    private void add_obstacle(long momentToShow) {
        float minX = ballRadius * 2;
        float maxX = (float) availableWidth - ballRadius * 2;
        float minY = ballRadius * 2;
        float maxY = (float)availableHeight - ballRadius * 2;

        // random can be in range of ball radius distance fomr ball going from minimum to maximum
        // min_ is if max_ - min_ is 0.
        float random_x = r.nextFloat() * (maxX - minX) + minX;
        float random_y = r.nextFloat() * (maxY - minY) + minY;

        PointF newPoint = new PointF(random_x, random_y);
        myObstacle.add(newPoint);
        myObstacleExpireTimes.add(momentToShow);
    }

    public void remove_obstacle(int obstacleTooRemove) {
        if (myObstacle.size() > 0) {
            myObstacle.remove(obstacleTooRemove);
            myObstacleExpireTimes.remove(obstacleTooRemove);
        }
    }

    public void setSpeed(int x, int y, boolean gameStart) {
        if (gameStart) {
            this.speedX = x;
            this.speedY = y;
            return;
        }

        if (this.speedX < 0) {
            this.speedX = -x;
        } else {
            this.speedX = x;
        }
        if (this.speedY < 0) {
            this.speedY = -y;
        } else {
            this.speedY = y;
        }
    }

    public void setStartVector() {
        if (this.speedX < 0) {
            this.speedX = -this.speedX;
        }
        if (this.speedY < 0) {
            this.speedY = -this.speedY;
        }
    }

    public void setFirstdraw(boolean firstdraw) {
        this.firstDraw = firstdraw;
    }

    public void setBallPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    public void setBallRadius(float radius) {
        this.ballRadius = radius;
        int d = Math.round(radius * 2);
        //Creates a new bitmap, scaled from an existing bitmap, when possible.
        // If the specified width and height are the same as the current width and height of the
        // source bitmap, the source bitmap is returned and no new bitmap is created.
        ballBitmap = Bitmap.createScaledBitmap(ballBitmap, d, d, false);
        addLifeBitmap = Bitmap.createScaledBitmap(addLifeBitmap, d * 2, d * 2, false);
    }

    public void setBallInMiddle() {
        this.positionX = this.availableWidth / 2;
        this.positionY = this.availableHeight / 2;
    }

    public void setBallColor(int inputColor) {
        this.currentBallColor = inputColor;
    }

    public void moveBall(long currentTime) {
        //don't move the ball is screen is not rendered
        if ((availableHeight == 0) || (availableWidth == 0)) return;
        this.currentTime = currentTime;

        positionX = positionX + speedX;
        positionY = positionY + speedY;

        //boolean uZiduSam = false;

        if (positionX + ballRadius > availableWidth) {
            positionX = availableWidth - ballRadius;
            speedX = -speedX;

            if (myCollisionListener != null) {
                myCollisionListener.onCollision("wall");
            }
        }

        if (positionY + ballRadius > availableHeight) {
            positionY = availableHeight - ballRadius;
            speedY = -speedY;
            //uZiduSam = true;

            if (myCollisionListener != null) {
                myCollisionListener.onCollision("wall");
            }
        }

        if (positionX - ballRadius < 0) {
            positionX = 0 + ballRadius;
            speedX = -speedX;
            //uZiduSam = true;

            if (myCollisionListener !=null) {
                myCollisionListener.onCollision("wall");
            }
        }

        if (positionY - ballRadius < 0) {
            positionY = 0 + ballRadius;
            speedY = -speedY;
            //uZiduSam = true;

            if (myCollisionListener !=null) {
                myCollisionListener.onCollision("wall");
            }
        }

        /*if (uZiduSam) {
            if (myBallInWallListener != null) {
                myBallInWallListener.onBallInWall("wall");
            }
        }*/

        int obstacleIndex = isBallInObstacle();
        if (obstacleIndex != -1) {
            // we found obstacle that has been hit within regular time,
            // we can remove it from canvas, but we have earned life-up also!
            remove_obstacle(obstacleIndex);

            // raise event:
            if (myCollisionListener !=null) {
                myCollisionListener.onCollision("obstacle");
            }
        }

        // Invalidate the whole view. If the view is visible,
        // onDraw(android.graphics.Canvas) will be called at some point in the future.
        //This must be called from a UI thread. To call from a non-UI thread,
        // call postInvalidate()
        invalidate();
    }

    public int isBallInObstacle() {
        if (myObstacle.size() <= 0) {
            return -1;
        }

        // Here we should have a collection with valid obstacles only.
        // Apart of position, we must be aware of times also!
        for (int i = 0; i < myObstacle.size(); i++) {
            long beginTime = (long) myObstacleExpireTimes.get(i);
            long endTime = beginTime + LIFE_UP_EXPIRATION_TIME;
            if (!((this.currentTime) >= beginTime) &&
                    (this.currentTime <= endTime)) {
                continue;
            }

            //our own collision detector data
            PointF iObstacle = (PointF) myObstacle.get(i);
            float leftBorder = iObstacle.x - ballRadius * 2;
            float rightBorder = iObstacle.x + ballRadius * 2;
            float upperBorder = iObstacle.y - ballRadius * 2;
            float bottomBorder = iObstacle.y + ballRadius * 2;

            if ((positionX + ballRadius >= leftBorder) &&
                    (positionX + ballRadius <= rightBorder) &&
                    (positionY + ballRadius >= upperBorder) &&
                    (positionY + ballRadius <= bottomBorder)) {
                return i;
            }

            if ((positionX - ballRadius >= leftBorder) &&
                    (positionX - ballRadius <= rightBorder) &&
                    (positionY - ballRadius >= upperBorder) &&
                    (positionY - ballRadius <= bottomBorder)) {
                return i;
            }

            if ((positionX - ballRadius >= leftBorder) &&
                    (positionX - ballRadius <= rightBorder) &&
                    (positionY + ballRadius >= upperBorder) &&
                    (positionY + ballRadius <= bottomBorder)) {
                return i;
            }

            if ((positionX + ballRadius >= leftBorder) &&
                    (positionX + ballRadius <= rightBorder) &&
                    (positionY - ballRadius >= upperBorder) &&
                    (positionY - ballRadius <= bottomBorder)) {
                return i;
            }
        }

        return -1;
    }

    public void invokeCommand(String command) {
        if (command == "left") {
            if (speedX < 0) return;
        }
        speedX = -speedX;

        if(command =="up")

        {
            if (speedY < 0) return;
            speedY = -speedY;
        }

        if(command =="right")

        {
            if (speedX > 0) return;
            speedX = -speedX;
        }

        if(command =="down")

        {
            if (speedY > 0) return;
            speedY = -speedY;
        }

        invalidate();
    }
    //This is called during layout when the size of this view has changed. If you were just added
    // to the view hierarchy, you're called with the old values of 0
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.availableHeight = h;
        this.availableWidth = w;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Here we should have valid view dimensions (availableWidth, availableHeight).
        // So, we can calculate obstacles in space and time here.
        // This calculation should be applied only at the game beginning.
        if (firstDraw) {
            reset_upLife_moments();
            firstDraw = false;
        }

        //prepare canvas and paint
        myPaint.setColor(Color.WHITE);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.drawRect(0, 0, availableWidth, availableHeight, myPaint);

        // bouncing box:
        myPaint.setStrokeWidth(5f);
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, availableWidth, availableHeight, myPaint);

        //obstacles
        int obtacleToDeleteDueTimeExpire = -1;
        for (int i = 0; i < myObstacleExpireTimes.size(); i++) {
            long beginTime = (long) myObstacleExpireTimes.get(i);
            long endTime = beginTime +  LIFE_UP_EXPIRATION_TIME;
            if ((this.currentTime >= beginTime) &&
                    (this.currentTime <= endTime)) {
                //time ot show obstacle
                PointF obstaclePoint = (PointF) myObstacle.get(i);
                canvas.drawBitmap(addLifeBitmap,
                        obstaclePoint.x - ballRadius * 2,
                        obstaclePoint.y - ballRadius * 2,
                        myPaint);
            } else {
                obtacleToDeleteDueTimeExpire = i;
                break;
            }
        }
        if (obtacleToDeleteDueTimeExpire != -1) {
            myObstacle.remove(obtacleToDeleteDueTimeExpire);
            myObstacleExpireTimes.remove(obtacleToDeleteDueTimeExpire);
        }

        //ball
        myPaint.setColor(this.currentBallColor);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(ballBitmap, positionX - ballRadius, positionY - ballRadius, myPaint);
    }

}


