package module6;

import de.fhpotsdam.unfolding.geo.Location;
import processing.core.PConstants;
import processing.core.PGraphics;


public class NuclearReactorMarker extends CommonMarker{

	private NuclearReactor reactor;
	
	public NuclearReactorMarker(Location location, NuclearReactor reactor) {
		super(location);
		this.reactor = reactor;

	}

	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(255,0,0);
		pg.rect(x,  y, 8, 8);
		pg.fill(255,255,0);
		pg.rect(x+2,  y+2, 4, 4);
		pg.popStyle();
		
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		
		String title = reactor.toString();
		pg.pushStyle();
		float textW = pg.textWidth(title);
		
		pg.fill(255,255,0);
		pg.rect(x,y+5,textW+6,20);
		
		pg.fill(0);
		pg.text(title, x+3, y+20);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.popStyle();
		
		
	}
	
	public String getID() {
		return reactor.getID();
	}
	
	public String getName() {
		return reactor.getName();
	}
	
	public String getPlant() {
		return reactor.getPlant();
	}
	
	public String getCountry() {
		return reactor.getCountry();
	}
	
}
