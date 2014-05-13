/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery & Michael Terry
 */
package com.example.a4;

import android.graphics.Bitmap;
import android.graphics.Region;
import android.util.Log;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/*
 * Class the contains a list of fruit to display.
 * Follows MVC pattern, with methods to add observers,
 * and notify them when the fruit list changes.
 */
public class Model extends Observable {
    // List of fruit that we want to display
    private ArrayList<Fruit> shapes = new ArrayList<Fruit>();
    private Random random;
    private ArrayList<Bitmap> bmList;
    private int score;
    // Constructor
    Model() {
    	random = new Random();
        shapes.clear();
        score = 0;
    }
    
    // Model methods
    // You may need to add more methods here, depending on required functionality.
    // For instance, this sample makes to effort to discard fruit from the list.
    public void add(Fruit s) {
        shapes.add(s);
        setChanged();
        //notifyObservers();
    }
    
    public void scoreIncrease(){
    	score++;
    }
    
    public int getScore(){
    	return this.score;
    }

    public void remove(Fruit s) {
        shapes.remove(s);
    }

    public ArrayList<Fruit> getShapes() {
        return (ArrayList<Fruit>) shapes.clone();
    }

    // MVC methods
    // Basic MVC methods to bind view and model together.
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    // a helper to make it easier to initialize all observers
    public void initObservers() {
        setChanged();
        notifyObservers();
    }
 

    public void createFruit(){

        notifyObservers();
    	int x = this.random.nextInt(350);
    	x += 50;
    	double x_speed = this.random.nextInt(11);
    	x_speed *=5;
    	if(x > 200) x_speed = 0 - x_speed;
    	if(x > 350 || x< 50) x_speed *= 2;
    	int polygon = this.random.nextInt(2);
    	Fruit f;
    	//new Region(0, 0, this.bmList.get(1).getWidth(), this.bmList.get(1).getHeight());
    	if( polygon==1 ){
    		f = new Fruit(new float[] {0, 20, 20, 0, 40, 0, 60, 20, 60, 40, 40, 60, 20, 60, 0, 40},x_speed);
    	}
    	else{
    		f = new Fruit(x_speed);
    	}
        
        f.translate(x, 800);
        this.add(f);
    }
    
    public void addBitmapList(ArrayList<Bitmap> bmList){
    	this.bmList = bmList;
    }
    
    @Override
    public synchronized void deleteObserver(Observer observer) {
        super.deleteObserver(observer);
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void deleteObservers() {
        super.deleteObservers();
        setChanged();
        notifyObservers();
    }
}
