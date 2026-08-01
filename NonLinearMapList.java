public class NonLinearMapList 
{
    public NonLinearMapList(String city, String place1, String place2, double distance) 
    {
        this.city = city;
        this.place1 = place1;
        this.place2 = place2;
        this.distance = distance;
    }
    
    public String getCity() 
    { 
        return city; 
    }

    public String getPlace1() 
    { 
        return place1; 
    }
    
    public String getPlace2()
    { 
        return place2; 
    }

    public double getDistance()
    {
        return distance;
    }


    private String city;
    private String place1;
    private String place2;
    private double distance;
}