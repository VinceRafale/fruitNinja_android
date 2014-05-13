/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.TextView;
import com.example.a4complete.R;

import java.util.Observable;
import java.util.Observer;

/*
 * View to display the Title, and Score
 * Score currently just increments every time we get an update
 * from the model (i.e. a new fruit is added).
 */
@SuppressLint("ViewConstructor")
public class TitleView extends TextView implements Observer {
    private int sec = 0;
    private int min = 0;
    private Model model;
    // Constructor requires model reference
    public TitleView(Context context, Model model) {
        super(context);

        // set width, height of this view
        this.setHeight(235);
        this.setWidth(MainActivity.displaySize.x);

        // register with model so that we get updates
        model.addObserver(this);
    }

    public void addModel(Model model){
    	this.model = model;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO BEGIN CS349
        int score = 0;
        if(this.model != null)
        	score = model.getScore();
        // TODO END CS349
        setBackgroundColor(Color.BLUE);
        setTextSize(16);
        setText("  "+getResources().getString(R.string.app_title) + "   high score: " + score+"\n  Time: "+min+" : "+sec);
    }

    // Update from model
    // ONLY useful for testing that the view notifications work
    @Override
    public void update(Observable observable, Object data) {
        // TODO BEGIN CS349
        // do something more meaningful here
        // TODO END CS349
        sec++;
        if(sec > 59){
        	min += 1;
        	sec %= 60;
        }
        invalidate();
    }
}
