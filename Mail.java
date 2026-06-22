public class Mail 
{
    public Mail(String origin, String destinationCity, String schoolAddress, double distance) 
    {
        this.origin = origin;
        this.destinationCity = destinationCity;
        this.schoolAddress = schoolAddress;
        this.distance = distance;
    }

    public String getOriginCity() 
    { 
        return origin; 
    }

    public String getDestinationCity() 
    { 
        return destinationCity; 
    }

    public String getSchoolAddress() 
    { 
        return schoolAddress; 
    }
    
    public double getDistance() 
    { 
        return distance; 
    }
    
    private String origin;
    private String destinationCity;
    private String schoolAddress;
    private double distance;
}