package com.example.a4;

import com.example.a4complete.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("DrawAllocation")
public class GameStart extends Activity implements View.OnClickListener, DialogInterface.OnClickListener{
	final Context context = this;
	private TextView t;
	private View v;
	
	public class StartView extends View{
		private Bitmap bitmap;
		public StartView(Context context, Bitmap bitmap) {
			super(context);
			this.bitmap = bitmap;
		}
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			Bitmap b = Bitmap.createScaledBitmap(this.bitmap, this.getWidth(), this.getHeight(), true);
			canvas.drawBitmap(b, 0,0,paint);
		}
		public void draw(){
			invalidate();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_start);

        Button start = (Button) findViewById(R.id.start_button);
        Button exit = (Button) findViewById(R.id.exit_button);
        start.setOnClickListener(this);
        exit.setOnClickListener(this);
        
	}
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fruitninja);
        StartView startview = new StartView(this.getApplicationContext(), bitmap);
        startview.setWillNotDraw(false);
        ViewGroup v1 = (ViewGroup) findViewById(R.id.start_view);
		v1.addView(startview);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.start_button:
			this.v = v;
			AlertDialog alert = new AlertDialog.Builder(context)
			.setTitle("\tWelcome to Fruit Ninja World\n"+"\t Do you really want to start?")
			.setPositiveButton("No", this)
			.setNeutralButton("Yes", this)
			.create();
			alert.show();
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		if(which == DialogInterface.BUTTON_NEUTRAL){
			Intent t = new Intent(v.getContext(), MainActivity.class);
			this.finish();
			v.getContext().startActivity(t);
		}
		else{
			//nothing
		}
	}
}
