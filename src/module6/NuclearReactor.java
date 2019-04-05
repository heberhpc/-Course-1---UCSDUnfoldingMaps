package module6;

/*Class that represent a Nuclear Reactor
**@ author Heber Henrique Pereira Coutinho*/

public class NuclearReactor{
	
	//IVARs
	private String ID;
	private String name;
	private String plant;
	private String country;
	private int totalPower;
	private double longitude;
	private double latitude;

	//CONSTRUCTOR
	public NuclearReactor (String ID, String name, String plant, String country,
			int totalPower, double longitude, double latitude){

		this.ID = ID;
		this.name = name;
		this.plant = plant;
		this.country = country;
		this.totalPower = totalPower;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getID(){
		return ID;
	}

	public String getName(){
		return name;
	}

	public String getPlant(){
		return plant;
	}

	public String getCountry(){
		return country;
	}

	public int getTotalPower(){
		return totalPower;
	}

	public double getlongitude(){
		return longitude;
	}

	public double getLatitude(){
		return latitude;
	}

	public String toString(){
		return ID+" - "+name+" - "+plant+" - "+country+" - "+totalPower+" - "+longitude+" - "+latitude;
	}

}

