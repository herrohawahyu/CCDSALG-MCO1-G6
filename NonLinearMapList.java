public class NonLinearMapList 
{
    public NonLinearMapList(String city, String place1, String place2, double distance) 
    {
        this.CITY = city;
        this.PLACE1 = place1;
        this.PLACE2 = place2;
        this.DISTANCE = distance;
    }
    
    public String getCity() 
    { 
        return CITY; 
    }

    public String getPlace1() 
    { 
        return PLACE1; 
    }
    
    public String getPlace2()
    { 
        return PLACE2; 
    }

    public double getDistance()
    {
        return DISTANCE;
    }


    private final String CITY;
    private final String PLACE1;
    private final String PLACE2;
    private final double DISTANCE;
}