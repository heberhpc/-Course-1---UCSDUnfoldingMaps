package guimodule;

import processing.core.PApplet;
import processing.core.PShape;

public class MyDisplay extends PApplet{
		
	public void setup() {
		size(400,400);
		background(200, 200, 200);
	}
	
	public void draw () {
		
		//Face
		fill(255,255,0);
		ellipse(200,200,390,390);
		
		//leftEye
		fill(0,0,0);
		ellipse(120,130,50,70);
		
		//rightEye
		fill(0,0,0);
		ellipse(280,130,50,70);
	
		//
		fill(0,0,0);
		noFill();
		arc(200,280,85,85,0,PI);
	}
	

}
