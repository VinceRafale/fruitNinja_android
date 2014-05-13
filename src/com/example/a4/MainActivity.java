/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4;

import java.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.*;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import com.example.a4complete.R;

public class MainActivity extends Activity implements DialogInterface.OnClickListener{
    private Model model;
    private MainView mainView;
    private TitleView titleView;
    public static Point displaySize;
    Handler handler;
    private int times;
    final Context context = this;
    public MainActivity main;
    private ArrayList<Bitmap> bmList;
    private Timer timer1, timer2;
    AlertDialog alert;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getResources().getString(R.string.app_name), "Touch release");
        this.getWindow().setTitle("CS349 A4 Demo");
        // save display size
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);
        times = 0;
        // initialize model
        model = new Model();
        handler = new Handler();
        timer1 = new Timer();
        timer1.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						model.createFruit();
					}
				});
			}
		}, 1000,1000);
        setContentView(R.layout.main);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bmList = new ArrayList<Bitmap>();
        // create the views and add them to the main activity
        titleView = new TitleView(this.getApplicationContext(), model);

        ViewGroup v1 = (ViewGroup) findViewById(R.id.main_1);
        v1.addView(titleView);

        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        bmList.add(background);

        mainView = new MainView(this.getApplicationContext(), model,bmList);
        ViewGroup v2 = (ViewGroup) findViewById(R.id.main_2);
        v2.addView(mainView);
        titleView.addModel(model);
        mainView.addMainActivity(this);
        main = this;
        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mainView.update(null, null);
					}
				});
			}
		}, 1000,25);
        // notify all views
        model.initObservers();
    }


    public void GameOver(){
		timer1.cancel();
		timer2.cancel();
    	alert = new AlertDialog.Builder(context)
		.setTitle("\t\t\t\t\t\tGame Over. \n\t\t\t\t   Want to restart?")
		.setNegativeButton("Restart", main)
		.setNeutralButton("Home Page", main)
		.setPositiveButton("Exit", main)
		.setCancelable(false)
		.create();
		alert.show();
    }
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		Intent t;
		switch(which){
		case DialogInterface.BUTTON_POSITIVE:
			alert.cancel();
			this.finish();
			break;
		case DialogInterface.BUTTON_NEUTRAL:
			alert.cancel();
			t = new Intent(getBaseContext(), GameStart.class);
			startActivity(t);
			this.finish();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			alert.cancel();
			t = new Intent(getBaseContext(), MainActivity.class);
			startActivity(t);
			this.finish();
			break;
		default:
			break;
		}
		
	}
}
