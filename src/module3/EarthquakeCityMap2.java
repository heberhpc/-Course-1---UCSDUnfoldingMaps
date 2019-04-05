package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;
import de.fhpotsdam.unfolding.providers.Yahoo;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Heber Henrique Pereira Coutinho
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap2 extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	// colors for markers and key
	int [] high = {255,0,0};
	int [] moderate = {255,255,0};
	int [] low = {0,0,255};
	
	
	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 700, 500, selectProvider(1));
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    
	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    
	    //TODO (Step 3): Add a loop here that calls createMarker (see below) 
	    // to create a new SimplePointMarker for each PointFeature in 
	    // earthquakes.  Then add each new SimplePointMarker to the 
	    // List markers (so that it will be added to the map in the line below)  
	    for (PointFeature p : earthquakes ) {
	    	
	    	markers.add(new SimplePointMarker(p.getLocation(), p.getProperties()));
	    	
	    }
	    
	    // Add the markers to the map so that they are displayed
	    map.addMarkers(markers);
	    
	    //
	    personalizeMarkers (markers);
	    
	}
		
	/* createMarker: A suggested helper method that takes in an earthquake 
	 * feature and returns a SimplePointMarker for that earthquake
	 * 
	 * In step 3 You can use this method as-is.  Call it from a loop in the 
	 * setp method.  
	 * 
	 *
	 * TODO (Step 4): Add code to this method so that it adds the proper 
	 * styling to each marker based on the magnitude of the earthquake.  
	*/
	
	//
	private void personalizeMarkers(List<Marker> m) {
		
		for (Marker l : m) {
			
			String s = l.getProperty("magnitude").toString();
			//System.out.println(s);
			
			double mag = Double.parseDouble(s);
			System.out.println(mag);
			
			if (mag >= 4 ) {
				((SimplePointMarker)l).setRadius((float) 15.0);
				l.setColor(color(high[0],high[1],high[2]));
			}
			
			if (mag < 4) {
				((SimplePointMarker)l).setRadius((float) 5.0);
				l.setColor(color (low[0],low[1],low[2]));

			}
			if (mag >4 && mag <5 ){
				((SimplePointMarker)l).setRadius((float) 10.0);
				l.setColor(color (moderate[0],moderate[1],moderate[2]));
			}
			
			
			
			
			//String c = l.getProperty("magnitude").toString();
			//System.out.println(c);
			
			
			//System.out.println(l.getProperty("magnitude").toString());
		}
		
		
	}
	
	//
	private SimplePointMarker createMarker(PointFeature feature)
	{  
		// To print all of the features in a PointFeature (so you can see what they are)
		// uncomment the line below.  Note this will only print if you call createMarker 
		// from setup
		//System.out.println(feature.getProperties());
		
		// Create a new SimplePointMarker at the location given by the PointFeature
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		
		// Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);
		
		// TODO (Step 4): Add code below to style the marker's size and color 
	    // according to the magnitude of the earthquake.  
	    // Don't forget about the constants THRESHOLD_MODERATE and 
	    // THRESHOLD_LIGHT, which are declared above.
	    // Rather than comparing the magnitude to a number directly, compare 
	    // the magnitude to these variables (and change their value in the code 
	    // above if you want to change what you mean by "moderate" and "light")
	    
	    
	    // Finally return the marker
	    return marker;
	}

	//
	public AbstractMapProvider selectProvider (int p){	
		switch(p){
		case 1 : return new OpenStreetMap.OpenStreetMapProvider();
		case 2 : return new OpenStreetMap.CloudmadeProvider("", 0);
		case 3 : return new StamenMapProvider.Toner();
		case 4 : return new Google.GoogleMapProvider();
		case 5 : return new Google.GoogleTerrainProvider();
		case 6 : return new Microsoft.RoadProvider();
		case 7 : return new Microsoft.AerialProvider();
		case 8 : return new Yahoo.RoadProvider();
		case 9 : return new Yahoo.HybridProvider();
		default : return new OpenStreetMap.OpenStreetMapProvider();
		}
	}
	
	//
	public void draw() {
	    background(10);
	    map.draw();
	    addKey();
	}

	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey(){
		
		int xP = 40;
		int yP = 120;
		int distanceY = 40;
		
		fill(255,255,255);
		rect(20,50, 160,500);
		
		fill(0,0,0);
		text("EarthQuakes", 60, 90);
		
		fill(high[0],high[1],high[2]);
		ellipse(xP,yP, 15,15);
		fill(0,0,0);
		text("5.0 + Magnitude", xP + 20, yP+5);
		
		fill(moderate[0],moderate[1],moderate[2]);
		ellipse(xP,yP+distanceY, 10,10);
		fill(0,0,0);
		text("4.0 + Magnitude", xP + 20, yP+distanceY+5);
		
		fill(low[0],low[1],low[2]);
		ellipse(xP,yP+distanceY*2, 10,10);
		fill(0,0,0);
		text("Below 4.0", xP + 20, (yP+distanceY*2)+5);
		
		// Remember you can use Processing's graphics methods here
	
	}
}
