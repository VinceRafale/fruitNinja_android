/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.*;

/*
 * View of the main game area.
 * Displays pieces of fruit, and allows players to slice them.
 */
@SuppressLint("ViewConstructor")
public class MainView extends View implements Observer {
    private final Model model;
    private final MouseDrag drag = new MouseDrag();
    private Bitmap background,b;
    private int missed_fruit;
    private Paint paint;
    MainActivity main;
    // Constructor
    MainView(Context context, Model m, ArrayList<Bitmap> bmList) {
        super(context);
        // register this view with the model
        model = m;
        model.addObserver(this);
        this.missed_fruit = 0;
        // TODO BEGIN CS349
        // test fruit, take this out before handing in!
        this.background = bmList.get(0);
        
        // TODO END CS349
        model.addBitmapList(bmList);
        // add controller
        // capture touch movement, and determine if we intersect a shape
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Log.d(getResources().getString(R.string.app_name), "Touch down");
                        drag.start(event.getX(), event.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        // Log.d(getResources().getString(R.string.app_name), "Touch release");
                        drag.stop(event.getX(), event.getY());

                        // find intersected shapes
                        Iterator<Fruit> i = model.getShapes().iterator();
                        while(i.hasNext()) {
                            Fruit s = i.next();
                            if (s.intersects(drag.getStart(), drag.getEnd())) {
                            	//System.out.println("intersect!!!!");
                                try {
                                    Fruit[] newFruits = s.split(drag.getStart(), drag.getEnd());

                                    // TODO BEGIN CS349
                                    // you may want to place the fruit more carefully than this
                                    if(newFruits != null){
                                    	newFruits[0].translate(0, -10);
                                    	newFruits[1].translate(0, +10);
                                    	newFruits[0].setFillColor(Color.RED);
                                    	newFruits[1].setFillColor(Color.RED);
                                    	// TODO END CS349
                                    	model.add(newFruits[0]);
                                    	model.add(newFruits[1]);
                                    	model.remove(s);
                                    	model.scoreIncrease();
                                    }
                                    else{
                                    	System.out.println("intersect but no a cut!");
                                    	continue;
                                    }
                                    // TODO BEGIN CS349
                                    // delete original fruit from model
                                    //model.remove(s);
                                    // TODO END CS349

                                } catch (Exception ex) {
                                    Log.e("fruit_ninja", "Error: " + ex.getMessage());
                                }
                            } else {
                                //s.setFillColor(Color.BLUE);
                            }
                            invalidate();
                        }
                        break;
                }
                return true;
            }
        });
    }
    
    // inner class to track mouse drag
    // a better solution *might* be to dynamically track touch movement
    // in the controller above
    class MouseDrag {
        private float startx, starty;
        private float endx, endy;

        protected PointF getStart() { return new PointF(startx, starty); }
        protected PointF getEnd() { return new PointF(endx, endy); }

        protected void start(float x, float y) {
            this.startx = x;
            this.starty = y;
        }

        protected void stop(float x, float y) {
            this.endx = x;
            this.endy = y;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint == null) paint = new Paint();
        // draw background
        if(b==null && background != null){
        	b = Bitmap.createScaledBitmap(background, this.getWidth(), this.getHeight(), true);
        }
        if(this.b != null){
        	canvas.drawBitmap(b, 0, 0, paint);
        }
        else{
        	setBackgroundColor(Color.BLACK);
        }
        paint.setColor(Color.BLUE);
        paint.setTextSize(50);
        switch(this.missed_fruit){
		case 0:
			canvas.drawText("O", 240, 50, paint);
			canvas.drawText("O", 280, 50, paint);
			canvas.drawText("O", 320, 50, paint);
			canvas.drawText("O", 360, 50, paint);
			canvas.drawText("O", 400, 50, paint);
			canvas.drawText("O", 440, 50, paint);
			break;
		case 1:
			canvas.drawText("O", 240, 50, paint);
			canvas.drawText("O", 280, 50, paint);
			canvas.drawText("O", 320, 50, paint);
			canvas.drawText("O", 360, 50, paint);
			canvas.drawText("O", 400, 50, paint);
			paint.setColor(Color.RED);
			canvas.drawText("X", 440, 50, paint);
			break;
		case 2:
			canvas.drawText("O", 240, 50, paint);
			canvas.drawText("O", 280, 50, paint);
			canvas.drawText("O", 320, 50, paint);
			canvas.drawText("O", 360, 50, paint);
			paint.setColor(Color.RED);
			canvas.drawText("X", 400, 50, paint);
			canvas.drawText("X", 440, 50, paint);
			break;
		case 3:
			canvas.drawText("O", 240, 50, paint);
			canvas.drawText("O", 280, 50, paint);
			canvas.drawText("O", 320, 50, paint);
			paint.setColor(Color.RED);
			canvas.drawText("X", 360, 50, paint);
			canvas.drawText("X", 400, 50, paint);
			canvas.drawText("X", 440, 50, paint);
			break;
		case 4:
			canvas.drawText("O", 240, 50, paint);
			canvas.drawText("O", 280, 50, paint);
			paint.setColor(Color.RED);
			canvas.drawText("X", 320, 50, paint);
			canvas.drawText("X", 360, 50, paint);
			canvas.drawText("X", 400, 50, paint);
			canvas.drawText("X", 440, 50, paint);
			break;
		case 5:
			canvas.drawText("O", 240, 50, paint);
			paint.setColor(Color.RED);
			canvas.drawText("X", 280, 50, paint);
			canvas.drawText("X", 320, 50, paint);
			canvas.drawText("X", 360, 50, paint);
			canvas.drawText("X", 400, 50, paint);
			canvas.drawText("X", 440, 50, paint);
			break;
		default:
			paint.setColor(Color.RED);
			canvas.drawText("X", 240, 50, paint);
			canvas.drawText("X", 280, 50, paint);
			canvas.drawText("X", 320, 50, paint);
			canvas.drawText("X", 360, 50, paint);
			canvas.drawText("X", 400, 50, paint);
			canvas.drawText("X", 440, 50, paint);
			break;	
		}
        // draw all pieces of fruit
        for (Fruit s : model.getShapes()) {
            s.draw(canvas);
        }
    }
    public void addMainActivity(MainActivity main){
    	this.main = main;
    }
    public boolean GameOver(){
    	if(this.missed_fruit>5){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
    @Override
    public void update(Observable observable, Object data) {
    	for (Fruit s : model.getShapes()) {
			  if(s.CouldBeRemoved()){
				  //System.out.println("remove a fruit!");
				  if(!s.fragment()) this.missed_fruit++;
				  model.remove(s);
			      if(this.missed_fruit > 5){
			       	main.GameOver();
			      }
			  }
		  }
        invalidate();
    }
}
