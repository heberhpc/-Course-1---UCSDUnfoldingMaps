package module6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractShapeMarker;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.MultiMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;
import de.fhpotsdam.unfolding.providers.Yahoo;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Heber Henrique Pereira Coutinho
 * Date: Jun 13, 2018
 * */
public class EarthquakeCityMap4_mod6 extends PApplet {
	
	// We will use member variables, instead of local variables, to store the data
	// that the setUp and draw methods will need to access (as well as other methods)
	// You will use many of these variables, but the only one you should need to add
	// code to modify is countryQuakes, where you will store the number of earthquakes
	// per country.
	
	// You can ignore this.  It's to get rid of eclipse warnings
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFILINE, change the value of this variable to true
	private static final boolean offline = false;
	
	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	

	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";
	
	// The files containing city names and info and country names and info
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	
	// The map
	private UnfoldingMap map;
	
	// Markers for each city
	private List<Marker> cityMarkers;
	
	// Markers for each earthquake
	private List<Marker> quakeMarkers;
	
	// A List of country markers
	private List<Marker> countryMarkers;
	
	// NEW IN MODULE 5
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	
	//to select a provider. See selectProvider() method.
	private int provider = 6;
	
	public void setup() {		
		// (1) Initializing canvas and map tiles
		size(900, 700, OPENGL);
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom";  // The same feed, but saved August 7, 2015
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, selectProvider(provider));
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
		    //earthquakesURL = "2.5_week.atom";
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// FOR TESTING: Set earthquakesURL to be one of the testing files by uncommenting
		// one of the lines below.  This will work whether you are online or offline
		//earthquakesURL = "test1.atom";
		//earthquakesURL = "test2.atom";
		
		// Uncomment this line to take the quiz
		//earthquakesURL = "quiz2.atom";
		
		
		// (2) Reading in earthquake data and geometric properties
	    //     STEP 1: load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		//     STEP 2: read in city data
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new CityMarker(city));
		}
	    
		//     STEP 3: read in earthquake RSS feed
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    quakeMarkers = new ArrayList<Marker>();
	    
	    for(PointFeature feature : earthquakes) {
		  //check if LandQuake
		  if(isLand(feature)) {
		    quakeMarkers.add(new LandQuakeMarker(feature));
		  }
		  // OceanQuakes
		  else {
		    quakeMarkers.add(new OceanQuakeMarker(feature));
		  }
	    }

	    // could be used for debugging
	    printQuakes();
	 		
	    // (3) Add markers to map
	    //     NOTE: Country markers are not added to the map.  They are used
	    //           for their geometric properties
	    map.addMarkers(quakeMarkers);
	    map.addMarkers(cityMarkers);
	    
	    //Sort Eartquakes and print only the first (n parameter)
	    //Reverse order by magnitude
	    sortAndPrint(20);

	}  // end setup
	
	
	public void draw() {
		background(0);
		map.draw();
		addKey();
		
	}

	//Sort And Print
	private void sortAndPrint (int numToPrint) {
		
		/*Sorting the List (Reverse order: higher to lower magnitude)*/
		Object [] topQuakes = quakeMarkers.toArray();
		Arrays.sort(topQuakes);
		
		/*Printing*/
		//Set true just for test and print all quakes
		//Otherwise, set false, for print just "numToPrint" top Earthquakes
		boolean printAll = false;
		
		if (printAll) {
			System.out.println("Printing all Earthquakes (FOR TEST)");
			for (Object o : topQuakes) {	
				String t = ((EarthquakeMarker)o).getTitle();
				float m = ((EarthquakeMarker)o).getMagnitude();
				System.out.println("Magnitude: " + m + " - Name " + t);
			}
		} else  {
			//Print Just the top "n" quakes
			int n=0;
		
			// First, make  that, if the list of earthquakes is less than "n",
			// the loop will go through only this list... otherwise, print the 
			// top "n" Earthquakes
			if (numToPrint < topQuakes.length) {
				n = numToPrint;
			}else if (numToPrint > topQuakes.length) {
				n = topQuakes.length;
			}
			System.out.println("***The top "+ n + " Earthquakes***");
			for (int i = 0 ; i < n; i ++ ) {
				String t = ((EarthquakeMarker)topQuakes[i]).getTitle();
				float  m = ((EarthquakeMarker)topQuakes[i]).getMagnitude();
				System.out.println("Magnitude: "+ m +" - Name"+t);
			}
		}
	}
	
	//
	public void mouseMoved() {
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;

		}
		selectMarkerIfHover(quakeMarkers);
		selectMarkerIfHover(cityMarkers);
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers){
		
		// Abort if there is other lastSelected = It prevents "two tags" at same time
		// OBS:	I JUST REALIZED IT LOOKING AT THE UCSD IMPLEMENTATION, SO I COPIED THIS "IF"
		if (lastSelected !=null) {
			return;
		}
		
		for (Marker m : markers) {
			if (m.isInside(map, mouseX, mouseY)) {
				lastSelected = (CommonMarker) m; 
				m.setSelected(true);
				return;
			}else {
				//only to prevent ... ensure that the marker will not be "selected - true"
				m.setSelected(false);
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an earthquake and its threat circle of cities
	 * Or if a city is clicked, it will display all the earthquakes 
	 * where the city is in the threat circle
	 */
	@Override
	public void mouseClicked() {
		
		// clear the last selection
		if (lastClicked != null) {
			lastClicked.setSelected(false);
			lastClicked = null;		
		}
		
		// Check where was the click
		selectMarkerIfClicked(quakeMarkers);
		selectMarkerIfClicked(cityMarkers);
		
		// Hide or show mark according to context
		if (lastClicked == null) {
			//System.out.println("The lastClicked is null unhide marks" + lastClicked);
			unhideMarkers();
			
		}else {
			//System.out.println("The lastClicked is not null, hiden marks" + lastClicked);
			hidedMarks() ;
		}
	}
	
	private void hidedMarks() {
		
		//IF IS A EARTQUAKE_MARKER
		if (lastClicked instanceof EarthquakeMarker ) {
			System.out.println("EarthquakeMarker Selected");
			
			// First define what EarthquakeMarker must be displayed
			for (Marker e: quakeMarkers) {
				if (e != lastClicked) {
					e.setHidden(true);
				}
			}
			
			// Second define what CityMarker must be displayed
			for (Marker c: cityMarkers ) {
				
				double threadDistance = ((EarthquakeMarker) lastClicked).threatCircle();
				double distanceFromlastClicked = lastClicked.getDistanceTo(c.getLocation());
				
				if (distanceFromlastClicked < threadDistance ) {
					c.setHidden(false);
				}else {
					c.setHidden(true);
				}
				
				
			}
			
		
		//IF IS A CITY_MARKER
		} else if (lastClicked instanceof CityMarker) {
			System.out.println("Citymarker Selected");
			
			//First define what cities must be displayed
			for (Marker c: cityMarkers) {
				if (c != lastClicked) {
					c.setHidden(true);
				}
			}
			
			//Second define what earthquakes must be displayed
			for (Marker e: quakeMarkers) {
				
				double threadDistance = ((EarthquakeMarker) e).threatCircle();
				double distanceFromlastClicked = e.getDistanceTo(lastClicked.getLocation());
				
				if (distanceFromlastClicked < threadDistance) {
					e.setHidden(false);
				}else {
					e.setHidden(true);
				}	
			}
		}
	}
	
	//
	private void unhideMarkers() {
		for(Marker marker : quakeMarkers) {
			marker.setHidden(false);
		}
			
		for(Marker marker : cityMarkers) {
			marker.setHidden(false);
		}
	}

	//
	private void selectMarkerIfClicked(List<Marker> markers) {
		
		for (Marker m: markers) {
			
			if (m.isInside(map,mouseX, mouseY)) {
				lastClicked = (CommonMarker) m;
				return;
			}
		}
	}

	// helper method to draw key in GUI
	private void addKey() {	
		// Remember you can use Processing's graphics methods here
		fill(255, 250, 240);
		
		int xbase = 25;
		int ybase = 50;
		
		int rec1Width  = 150;
		int rec1Height = 250;
		
		rect(xbase, ybase, rec1Width, rec1Height);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Earthquake Key", xbase+25, ybase+25);
		
		fill(150, 30, 30);
		int tri_xbase = xbase + 35;
		int tri_ybase = ybase + 50;
		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
				tri_ybase+CityMarker.TRI_SIZE);

		fill(0, 0, 0);
		textAlign(LEFT, CENTER);
		text("City Marker", tri_xbase + 15, tri_ybase);
		
		text("Land Quake", xbase+50, ybase+70);
		text("Ocean Quake", xbase+50, ybase+90);
		text("Size ~ Magnitude", xbase+25, ybase+110);
		
		fill(255, 255, 255);
		ellipse(xbase+35, 
				ybase+70, 
				10, 
				10);
		rect(xbase+35-5, ybase+90-5, 10, 10);
		
		fill(color(255, 255, 0));
		ellipse(xbase+35, ybase+140, 12, 12);
		fill(color(0, 0, 255));
		ellipse(xbase+35, ybase+160, 12, 12);
		fill(color(255, 0, 0));
		ellipse(xbase+35, ybase+180, 12, 12);
		
		textAlign(LEFT, CENTER);
		fill(0, 0, 0);
		text("Shallow", xbase+50, ybase+140);
		text("Intermediate", xbase+50, ybase+160);
		text("Deep", xbase+50, ybase+180);

		text("Past hour", xbase+50, ybase+200);
		
		fill(255, 255, 255);
		int centerx = xbase+35;
		int centery = ybase+200;
		ellipse(centerx, centery, 12, 12);

		strokeWeight(2);
		line(centerx-8, centery-8, centerx+8, centery+8);
		line(centerx-8, centery+8, centerx+8, centery-8);

		int xbase2 = 25;
		int ybase2 = ybase + rec1Height +4 ;
		int rec2Width  = 150;
		int rec2Height = 348;
		
		rect(xbase2, ybase2 , rec2Width, rec2Height);
		
		if (lastClicked != null) {
			
			fill(0);
			textAlign(LEFT, CENTER);
			textSize(12);
			
			
			if (lastClicked instanceof EarthquakeMarker) {
				
				fill(0);
				textSize(12);
				text("EARTHQUAKE", xbase2+5, ybase2+20 );
				
				String name = ((EarthquakeMarker) lastClicked).getTitle();
				float mag = ((EarthquakeMarker) lastClicked).getMagnitude();
				
				fill(0);
				textSize(12);
				text(name, xbase2+5,ybase2+10,rec2Width-5, 110);
				text("Magnitude "+ mag, xbase2+5,ybase2+110 );
				
			}else if (lastClicked instanceof CityMarker) {
				
				fill(0);
				textSize(12);
				text("CITY", xbase2+5, ybase2+20 );
				
				String city = ((CityMarker) lastClicked).getCity();
				String country = ((CityMarker) lastClicked).getCountry();
				float pop = ((CityMarker) lastClicked).getPopulation();
				
				fill(0);
				textSize(12);
				text(city + " - "+ country, xbase2+5,ybase2+10,rec2Width-5, 110);
				text("Population "+ pop, xbase2+5,ybase2+110 );
				
			}
		}
		
	}


	// Checks whether this quake occurred on land.  If it did, it sets the 
	// "country" property of its PointFeature to the country where it occurred
	// and returns true.  Notice that the helper method isInCountry will
	// set this "country" property already.  Otherwise it returns false.
	private boolean isLand(PointFeature earthquake) {
		
		// IMPLEMENT THIS: loop over all countries to check if location is in any of them
		// If it is, add 1 to the entry in countryQuakes corresponding to this country.
		for (Marker country : countryMarkers) {
			if (isInCountry(earthquake, country)) {
				return true;
			}
		}
		
		// not inside any country
		return false;
	}
	
	// prints countries with number of earthquakes
	// You will want to loop through the country markers or country features
	// (either will work) and then for each country, loop through
	// the quakes to count how many occurred in that country.
	// Recall that the country markers have a "name" property, 
	// And LandQuakeMarkers have a "country" property set.
	private void printQuakes() {
		
		System.out.println("=====//=====");
		System.out.println("Count and Printing Earthquakes by Country");
		System.out.println("=====//=====");
		
		for (Marker m : countryMarkers) {
			//Choose a country (sequence in the list)
			String nameCountry = (String) m.getProperty("name");
			int quakeCountryCounter=0;
				
			//for each earthquake PointMarker compare with actual country
			for (Marker e: quakeMarkers) {
				String earthquakeCountry = (String) e.getProperty("country");
							
				if (nameCountry.equals(earthquakeCountry)) {
					quakeCountryCounter++;
				} 
			}
			
			//Print a country name only if there is any earthquake
			if (quakeCountryCounter >0) {
				println(nameCountry+" : "+ quakeCountryCounter);	
			}
		}
		
		int oceanQuakeCount=0;
		for (Marker f: quakeMarkers) {
			String location = (String) f.getProperty("country");
			
			if (location==null) {
				oceanQuakeCount++;
			}
		}
	
		println("OCEAN QUAKES: "+oceanQuakeCount);
		System.out.println("=====//=====");
		
	}
	
	// helper method to test whether a given earthquake is in a given country
	// This will also add the country property to the properties of the earthquake feature if 
	// it's in one of the countries.
	// You should not have to modify this code
	private boolean isInCountry(PointFeature earthquake, Marker country) {
		// getting location of feature
		Location checkLoc = earthquake.getLocation();

		// some countries represented it as MultiMarker
		// looping over SimplePolygonMarkers which make them up to use isInsideByLoc
		if(country.getClass() == MultiMarker.class) {
				
			// looping over markers making up MultiMarker
			for(Marker marker : ((MultiMarker)country).getMarkers()) {
					
				// checking if inside
				if(((AbstractShapeMarker)marker).isInsideByLocation(checkLoc)) {
					earthquake.addProperty("country", country.getProperty("name"));
						
					// return if is inside one
					return true;
				}
			}
		}
			
		// check if inside country represented by SimplePolygonMarker
		else if(((AbstractShapeMarker)country).isInsideByLocation(checkLoc)) {
			earthquake.addProperty("country", country.getProperty("name"));
			
			return true;
		}
		return false;
	}
	
	// Like a selector for a map provider.
	private AbstractMapProvider selectProvider (int p){	
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
}
