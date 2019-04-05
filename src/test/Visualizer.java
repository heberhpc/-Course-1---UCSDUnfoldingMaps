package test;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PGraphics;
	
	
public class Visualizer extends PApplet{
	PGraphics pg;
	
	
	
	

	public void setup() {
		setBackground(Color.red);
		size(400,400);
		//p.rect(50, 50, 50, 50);
		
		pg = createGraphics(100, 100);
	}
	
	public void draw() {
		
		pg.rect(50,50,50,50);
		
		pg.endDraw();
		
	}
}
