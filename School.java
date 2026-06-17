public class School 
{
    public School(String city, String address, double distancePO) 
    {
        this.address = address;
        this.city = city;
        this.distancePO = distancePO;
    }

    public String getAddress() 
    { 
        return address; 
    }

    public String getCity() 
    { 
        return city; 
    }
    
    public double getDistancePO()
    { 
        return distancePO; 
    }


    private String address;
    private String city;
    private double distancePO;
}