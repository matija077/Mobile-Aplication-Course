package com.example.matija077.bouncingballfinallycompelte;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

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
    Bitmap ballBitmap;
    Paint myPaint;
    private ballInWallListener myBallInWallListener;

    public interface ballInWallListener {
        void onBallInWall(String input);
    }

    public void setBallInWallListener(ballInWallListener inputListener) {
        this.myBallInWallListener = inputListener;
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
    }

    public void setBallInMiddle() {
        this.positionX = this.availableWidth / 2;
        this.positionY = this.availableHeight / 2;
    }

    public void setBallColor(int inputColor) {
        this.currentBallColor = inputColor;
    }

    public void moveBall() {
        //don't move the ball is screen is not rendered
        if ((availableHeight == 0) || (availableWidth == 0)) return;

        positionX = positionX + speedX;
        positionY = positionY + speedY;

        boolean uZiduSam = false;

        if (positionX + ballRadius > availableWidth) {
            positionX = availableWidth - ballRadius;
            speedX = -speedX;
            uZiduSam = true;
        }

        if (positionY + ballRadius > availableHeight) {
            positionY = availableHeight - ballRadius;
            speedY = -speedY;
            uZiduSam = true;
        }

        if (positionX - ballRadius < 0) {
            positionX = 0 + ballRadius;
            speedX = -speedX;
            uZiduSam = true;
        }

        if (positionY - ballRadius < 0) {
            positionY = 0 + ballRadius;
            speedY = -speedY;
            uZiduSam = true;
        }

        if (uZiduSam) {
            if (myBallInWallListener != null) {
                myBallInWallListener.onBallInWall("wall");
            }
        }

        // Invalidate the whole view. If the view is visible,
        // onDraw(android.graphics.Canvas) will be called at some point in the future.
        //This must be called from a UI thread. To call from a non-UI thread,
        // call postInvalidate()
        invalidate();
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

        myPaint.setColor(Color.WHITE);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, availableWidth, availableHeight, myPaint);

        canvas.drawBitmap(ballBitmap, positionX - ballRadius, positionY - ballRadius, myPaint);
    }

}


