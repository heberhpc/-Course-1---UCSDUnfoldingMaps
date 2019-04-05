package guimodule;

import java.awt.Color;
import processing.core.PApplet;
import processing.core.PImage;

public class SunClock extends PApplet{
	
	//IVARS
	int canvasWidth = 400;
	int canvasHeight = 400;
	int [] sunColor = new int[3];
	
	PImage back;

	public void setup() {
		
	//setup sizes
		this.size(canvasWidth,canvasHeight);
		background(255);
		stroke(0);
	
	//setup backgroud
		back = loadImage("palmTrees.jpg","jpg");
		back.resize(0,height);
		image(back,0,0);
	
	}
	
	public void draw() {

		fill (sunColor[0],sunColor[1],sunColor[2]);
		ellipse (width/4, height/4, width/5, height/5);
		sunColor1 ();
	
	}
	
	private int [] sunColor1 () {
		
		float differ = Math.abs(second()-30);
		float ration = differ /30;
		
		System.out.println("differ" + differ);
		System.out.println("ration" + ration);
		
		sunColor[0] = (int) (ration*255);
		sunColor[1] = (int) (ration*255);
		sunColor[2] = (int) 0;
		
		//System.out.println("R" + sunColor[0]);
		//System.out.println("G" + sunColor[1]);
		//System.out.println("B" + sunColor[2]);
		
		return sunColor;
	}

}
