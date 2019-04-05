package module1;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/** HelloWorld
  * An application with two maps side-by-side zoomed in on different locations.
  * Author: UC San Diego Coursera Intermediate Programming team
  * @author Heber Henrique Pereira Coutinho (heberhpc@gmail.com)
  * Date: 19/05/2018
  * */
public class HelloWorld extends PApplet
{
	/** Your goal: add code to display second map, zoom in, and customize the background.
	 * Feel free to copy and use this code, adding to it, modifying it, etc.  
	 * Don't forget the import lines above. */

	// You can ignore this.  It's to keep eclipse from reporting a warning
	private static final long serialVersionUID = 1L;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// IF YOU ARE WORKING OFFLINE: Change the value of this variable to true
	private static final boolean offline = false;
	
	/** The map we use to display our home town: La Jolla, CA */
	UnfoldingMap map1;
	
	/** The map you will use to display your home town (Londrina-PR-Brazil)*/ 
	UnfoldingMap map2;
	
	// More generic size
	private int mapWidth = 800;
	private int mapHeight = 600;
	private int mapBorder = 50;
	
	public void setup() {
		
		size(mapWidth, mapHeight, P2D);  // Set up the Applet window to be 800x600
		                      // The OPENGL argument indicates to use the 
		                      // Processing library's 2D drawing
		                      // You'll learn more about processing in Module 3

		// This sets the background color for the Applet.  
		// Play around with these numbers and see what happens!
		this.background(100, 100, 200);
		
		// Select a map provider
		AbstractMapProvider provider = new Microsoft.RoadProvider();
		// Set a zoom level
		int zoomLevel = 10;
		
		//if work offline is true: Set a new provider and a new zoom level.
		if (offline) {
			// If you are working offline, you need to use this provider 
			// to work with the maps that are local on your computer.  
			provider = new MBTilesMapProvider(mbTilesString);
			// 3 is the maximum zoom level for working offline
			zoomLevel = 3;
		}
		
		// some calculations to setup size and position
		//MAP1
		int widthMap1 = (this.getWidth() - (mapBorder*3))/2 ;
		int heightMap1 = (this.getHeight()) - (mapBorder * 2); 
		int xMap1 = this.getWidth() - (widthMap1*2) - (mapBorder*2);
		int yMap1 = (this.getHeight()- heightMap1) / 2 ;
		
		//MAP2
		int widthMap2 = (this.getWidth() - (mapBorder*3))/2 ;
		int heightMap2 = (this.getHeight()) - (mapBorder * 2); 
		int xMap2 = xMap1 + widthMap1 + mapBorder;
		int yMap2 = yMap1;
		
		
		// Create a new UnfoldingMap to be displayed in this window.  
		// The 2nd-5th arguments give the map's x, y, width and height
		// When you create your map we want you to play around with these 
		// arguments to get your second map in the right place.
		// The 6th argument specifies the map provider.  
		// There are several providers built-in.
		// Note if you are working offline you must use the MBTilesMapProvider
		map1 = new UnfoldingMap(this, xMap1, yMap1, widthMap1, heightMap1, provider);

		// The next line zooms in and centers the map at 
	    // 32.9 (latitude) and -117.2 (longitude)
	    map1.zoomAndPanTo(zoomLevel, new Location(32.9f, -117.2f));
		
		// This line makes the map interactive
		MapUtils.createDefaultEventDispatcher(this, map1);
		
		//code to ASSIGNMENT 1 
		map2 = new UnfoldingMap (this, xMap2, yMap2, widthMap2, heightMap2, provider);
		map2.zoomAndPanTo(zoomLevel+1, new Location(-23.321264f, -51.2358038f));
		MapUtils.createDefaultEventDispatcher(this, map2);
	}

	/** Draw the Applet window.  */
	public void draw() {
		// So far we only draw map1...
		map1.draw();
		
		// TODO: Add code so that both maps are displayed
		map2.draw();
	}
}
