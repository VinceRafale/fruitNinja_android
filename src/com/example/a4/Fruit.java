/**
 * CS349 Winter 2014
 * Assignment 4 Demo Code
 * Jeff Avery
 */
package com.example.a4;
import java.util.Random;

import android.R;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.*;
import android.graphics.Path.Op;
import android.os.Build;
import android.util.Log;

/**
 * Class that represents a Fruit. Can be split into two separate fruits.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
@SuppressLint("NewApi")
public class Fruit {
    private Path path = new Path();
    private Path line;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Matrix transform = new Matrix();
    private double lastTime;
    private double velocity;
    private double gravity;
    private double lastX, lastY;
    private boolean frag;
    private double x_speed;
    private Random random;
    private boolean droping;

    /**
     * A fruit is represented as Path, typically populated 
     * by a series of points 
     */
    Fruit(double x_speed) {
        init();
        this.path.reset();
        this.path.addCircle(40, 40, 40, Path.Direction.CCW);
        lastTime = System.currentTimeMillis();
        velocity = 600;
        gravity = 300;
        this.x_speed = x_speed;
        frag = false;
        RectF fruit_bounds = new RectF();
        path.computeBounds(fruit_bounds, true);
    }
    Fruit(float[] points, double x_speed) {
        init();
        this.path.reset();
        this.path.moveTo(points[0], points[1]);
        for (int i = 2; i < points.length; i += 2) {
            this.path.lineTo(points[i], points[i + 1]);
        }
        this.path.moveTo(points[0], points[1]);
        lastTime = System.currentTimeMillis();
        velocity = 600;
        gravity = 300;
        this.x_speed = x_speed;
        frag = false;
    }

    Fruit(Region region) {
        init();
        this.path = region.getBoundaryPath();
        lastTime = System.currentTimeMillis();
        velocity = 600;
        gravity = 300;
        frag = false;
    }

    Fruit(Path path, boolean fragment, double x_speed) {
        init();
        if(fragment){
            paint.setColor(Color.RED);
        }
        lastTime = System.currentTimeMillis();
        velocity = 0;
        gravity = 300;
        this.path = path;
        this.x_speed = x_speed;
        this.frag = fragment;
        RectF fruit_bounds = new RectF();
        path.computeBounds(fruit_bounds, true);
    }

  
    private void init() {
    	this.random = new Random();
    	int color = this.random.nextInt(4);
    	switch(color){
    	case 0:
            this.paint.setColor(Color.BLUE);
    		break;
    	case 1:
            this.paint.setColor(Color.GREEN);
    		break;
    	case 2:
            this.paint.setColor(Color.BLACK);
    		break;
    	case 3:
            this.paint.setColor(Color.YELLOW);
    		break;
    	default:
            this.paint.setColor(Color.BLUE);
    		break;
    	}
        this.paint.setStrokeWidth(5);
    }

    /**
     * The color used to paint the interior of the Fruit.
     */
    public int getFillColor() { return paint.getColor(); }
    public void setFillColor(int color) { paint.setColor(color); }

    /**
     * The width of the outline stroke used when painting.
     */
    public double getOutlineWidth() { return paint.getStrokeWidth(); }
    public void setOutlineWidth(float newWidth) { paint.setStrokeWidth(newWidth); }

    /**
     * Concatenates transforms to the Fruit's affine transform
     */
    public void rotate(float theta) { transform.postRotate(theta); }
    public void scale(float x, float y) { transform.postScale(x, y); }
    public void translate(float tx, float ty) { transform.postTranslate(tx, ty); }
    public boolean fragment(){
    	return this.frag;
    }

    /**
     * Returns the Fruit's affine transform that is used when painting
     */
    public Matrix getTransform() { return transform; }

    /**
     * The path used to describe the fruit shape.
     */
    public Path getTransformedPath() {
        Path originalPath = new Path(path);
        Path transformedPath = new Path();
        originalPath.transform(transform, transformedPath);
        return transformedPath;
    }

    /**
     * Paints the Fruit to the screen using its current affine
     * transform and paint settings (fill, outline)
     */
    public void draw(Canvas canvas) {
        // TODO BEGIN CS349
        // tell the shape to draw itself using the matrix and paint parameters
    	double t = (System.currentTimeMillis() - lastTime)/1000;
    	float deltaX = 0;
    	float deltaY = 0;
    	double currentX = this.x_speed*t;
    	double currentY = 0-((velocity*t) - (gravity*t*t/2));
    	deltaX = (float) (currentX - lastX);
    	deltaY = (float) (currentY - lastY);
    	if(deltaY>0){ this.droping = true;}
    	else{
    		this.droping = false;
    	}
    	lastX = currentX;
    	lastY = currentY;
    	this.translate(deltaX, deltaY);
    	//paint.setColor(Color.BLUE);
    	canvas.drawPath(getTransformedPath(), paint);
        // TODO END CS349
    }

    /**
     * Tests whether the line represented by the two points intersects
     * this Fruit.
     */
    public boolean intersects(PointF p1, PointF p2) {
        // TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in 
        // rotate path and create region for comparison
    	if(this.frag){
    		return false;
    	}
    	Matrix temp_trans = new Matrix();
    	line = new Path();
    	line.moveTo(p1.x, p1.y);
    	line.lineTo(p2.x, p2.y);
    	//System.out.println(p1+" "+p2);
    	
    	Path transferred_line = new Path();
    	Path temp_fruit = new Path();
    	double delta_x = p2.x-p1.x;
        double delta_y = p2.y - p1.y;
        double theta = Math.atan2(delta_x, delta_y);
        if(Math.abs(theta) > Math.PI/2){
        	theta = 0 - theta;
        	if(theta < 0){
        		theta = 0 - theta - Math.PI;
        	}
        	else{
        		theta = Math.PI - theta;
        	}
        }
        temp_trans.postTranslate(0-p1.x, 0-p1.y);
        temp_trans.postRotate((float)Math.toDegrees(0-theta));

    	line.transform(temp_trans, transferred_line);
    	getTransformedPath().transform(temp_trans, temp_fruit);
    	
    	RectF line_bounds = new RectF();
    	transferred_line.computeBounds(line_bounds, true);
    	RectF fruit_bounds = new RectF();
    	temp_fruit.computeBounds(fruit_bounds, true);
    	return line_bounds.intersect(fruit_bounds);
    	
        // TODO END CS349
    }

    /**
     * Returns whether the given point is within the Fruit's shape.
     */
    public boolean contains(PointF p1) {
        Region region = new Region();
        boolean valid = region.setPath(getTransformedPath(), new Region());
        return valid && region.contains((int) p1.x, (int) p1.y);
    }

    //check if the point is on this line
    public boolean valid_point_in_line(PointF line1_p1, PointF line1_p2, PointF point){
    	float x1 = line1_p1.x;
        float y1 = line1_p1.y;
        float x2 = line1_p2.x;
        float y2 = line1_p2.y;
        float max_x = x1 > x2 ? x1 : x2;
        float max_y = y1 > y2 ? y1 : y2;
        float min_x = x1 <= x2 ? x1 : x2;
        float min_y = y1 <= y2 ? y1 : y2;
        if(max_x == min_x){ 
        	//System.out.println("same x");
        	point.x = max_x;
        }
        if(max_y == min_y){ 
        	//System.out.println("same y");
        	point.y = max_y;
        }
        //System.out.println(x1+" "+x2+" "+y1+" "+y2+" "+point);
        if(point.x > max_x ||point.x < min_x ||point.y > max_y ||point.y < min_y){
        	return false;
        }
        return true;
    }
    
  //calculate the interct point of line 1 and line 2
    public PointF InterctPoint(PointF line1_p1, PointF line1_p2,PointF line2_p1, PointF line2_p2){
        float x1 = line1_p1.x;
        float y1 = line1_p1.y;
        float x2 = line1_p2.x;
        float y2 = line1_p2.y;
        float x3 = line2_p1.x;
        float y3 = line2_p1.y;
        float x4 = line2_p2.x;
        float y4 = line2_p2.y;

        PointF p = null;

        float d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);//difference between slope
        if (d != 0) {
            float xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
            float yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
            p = new PointF(xi, yi);
    		//System.out.println("points create: "+p);
        }
        if(p != null){
        	if((!valid_point_in_line(line2_p1, line2_p2, p)) || (!valid_point_in_line(line1_p1, line1_p2, p))){
        		//System.out.println("invalid points : "+p);
        		p = null;
        	}
        }
        return p;
    }
    
    public PointF[] fruitShapeInterctionPoints(PointF line1_p1, PointF line1_p2){
    	PointF[] p = new PointF[4];
        PointF[] point = new PointF[2];
        int j = 0;
        
        Path fruit = getTransformedPath();
        RectF fruit_bounds = new RectF();
        fruit.computeBounds(fruit_bounds, true);
        PointF p1 = new PointF(fruit_bounds.left, fruit_bounds.top);
        PointF p2 = new PointF(fruit_bounds.right, fruit_bounds.top);
        PointF p3 = new PointF(fruit_bounds.left, fruit_bounds.bottom);
        PointF p4 = new PointF(fruit_bounds.right, fruit_bounds.bottom);
        
        // Top line
        p[0] = InterctPoint(line1_p1,line1_p2,p1,p2);

        // Bottom line
        p[1] = InterctPoint(line1_p1,line1_p2,p1,p3);
    
        // Left side...
        p[2] = InterctPoint(line1_p1,line1_p2,p2,p4);

        // Right side
        p[3] = InterctPoint(line1_p1,line1_p2,p3,p4);
       
        for(int i = 0; i < 4; i++){
        	if(p[i] != null){
        		if(j > 1){
        			j = 1;
        			System.out.println("j cannot be over 1");
        		}
        		point[j] = p[i];
        		j++;
        	}
        }

        return point;
    		
    }
    /**
     * This method assumes that the line represented by the two points
     * intersects the fruit. If not, unpredictable results will occur.
     * Returns two new Fruits, split by the line represented by the
     * two points given.
     */
    public Fruit[] split(PointF p1, PointF p2) {
    	Path topPath = null;
    	Path bottomPath = null;
    	PointF[] point;
    	// TODO BEGIN CS349
        // calculate angle between points
        // rotate and flatten points passed in
        // rotate region
        // define region masks and use to split region into top and bottom
    	point = fruitShapeInterctionPoints(p1, p2);
    	for(int i = 0; i < point.length; i++){
    		//System.out.println("length: "+point.length+"  intersect point is: "+point[i]);
    		if(point[i]==null){
    			//not go through
    			return null;
    		}
    	}
    	PointF point1 = point[0];
    	PointF point2 = point[1];
    	
    	double delta_x = point2.x - point1.x;
    	double delta_y = point2.y - point1.y;
    	double delta = delta_y/delta_x;
    	double theta = Math.atan2(delta_y, delta_x);
    	double theta2 = theta;
        if(Math.abs(theta) > Math.PI/2){
        	theta = 0 - theta;
        	if(theta < 0){
        		theta = 0 - theta - Math.PI;
        	}
        	else{
        		theta = Math.PI - theta;
        	}
        }
        Matrix temp_trans = new Matrix();
        temp_trans.postTranslate(0-point1.x, 0-point1.y);
        //Graphics2D.printMatrix(temp_trans);
        temp_trans.postRotate((float) Math.toDegrees(0-theta));
        //Graphics2D.printMatrix(temp_trans);
        Path fruit_at_x_axis = new Path();
    	getTransformedPath().transform(temp_trans, fruit_at_x_axis);
    	RectF fruit_bounds = new RectF();
    	fruit_at_x_axis.computeBounds(fruit_bounds, true);
        int topAreaY2 = (int) Math.min(fruit_bounds.top, 0);
        int topAreaY1 = (int) Math.min(fruit_bounds.bottom, 0);
        int bottomAreaY2 = (int) Math.max(fruit_bounds.top, 0);
        int bottomAreaY1 = (int) Math.max(fruit_bounds.bottom, 0);
        
        Rect topRect = new Rect((int)fruit_bounds.left, topAreaY2, (int)fruit_bounds.right, topAreaY1);
        Rect bottomRect = new Rect((int)fruit_bounds.left, bottomAreaY2, (int)fruit_bounds.right, bottomAreaY1);

        
        Region total = new Region();
        Region top = new Region();
        top.setPath(fruit_at_x_axis, new Region(new Rect((int)fruit_bounds.left, (int)fruit_bounds.top,
        		(int)fruit_bounds.right, (int)fruit_bounds.bottom)));
        Region bottom = new Region();
        bottom.setPath(fruit_at_x_axis, new Region(new Rect((int)fruit_bounds.left, (int)fruit_bounds.top,
        		(int)fruit_bounds.right, (int)fruit_bounds.bottom)));
        
        if(top.op(topRect, Region.Op.INTERSECT)){
        	//System.out.println("intersect successfully");
        }
        bottom.op(bottomRect, Region.Op.INTERSECT);
        
        temp_trans.invert(temp_trans);
        Path tmptop = top.getBoundaryPath();
        Path tmpbottom = bottom.getBoundaryPath();
        topPath = new Path();
        bottomPath = new Path();
        tmptop.transform(temp_trans,topPath);
        tmpbottom.transform(temp_trans, bottomPath);
        
        double topSpeed,bottomSpeed;
        topSpeed = 40;
        bottomSpeed = 40;
        // TODO END CS349
        if (topPath != null && bottomPath != null) {
        	//System.out.println("slice fruit to two pieces");
        	if((delta_y>0&&delta_x<0) ||(delta_y<0&&delta_x>0)){ topSpeed = 0 - topSpeed;}
        	else{bottomSpeed = 0 - bottomSpeed;}
           return new Fruit[] { new Fruit(topPath,true,topSpeed), new Fruit(bottomPath,true,bottomSpeed) };
        }
        return new Fruit[0];
    }
    
    public boolean CouldBeRemoved(){
        RectF fruit_bounds = new RectF();
        this.getTransformedPath().computeBounds(fruit_bounds, true);
    	if(fruit_bounds.top > 800 && this.droping){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
}
