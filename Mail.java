public class Mail 
{
    public Mail(String origin, String destinationCity, String schoolAddress) 
    {
        this.origin = origin;
        this.destinationCity = destinationCity;
        this.schoolAddress = schoolAddress;
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
    
    private String origin;
    private String destinationCity;
    private String schoolAddress;
}