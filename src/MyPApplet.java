/**/

//IMPORTS
import java.awt.Color;

import processing.core.*;


//CLASS DEFINITION
public class MyPApplet extends PApplet{
	
	//IVARS
	PImage backgroud;
	int canvasWidth = 600;
	int canvasHeight = 600;
	
	
	//
	public void setup() {
		
		//setup sizes
		this.size(canvasWidth,canvasHeight);
		
		//setup backgroud
		backgroud = loadImage("palmTrees.jpg","jpg");
		this.setBackground(Color.RED);
		
	
		
	}
	
	//
	public void draw() {
	
		backgroud.resize(0, height);
		image(backgroud, 0, 0);
		
		fill (255,209,0);
		ellipse (width/4, height/4, width/5, height/5);
		

	
	}
}
