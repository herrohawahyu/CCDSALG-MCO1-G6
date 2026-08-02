public class Mail 
{
    public Mail(String origin, String destinationCity, String schoolAddress) 
    {
        this.ORIGIN = origin;
        this.DESTINATION_CITY = destinationCity;
        this.SCHOOL_ADDRESS = schoolAddress;
    }

    public String getOriginCity() 
    { 
        return ORIGIN; 
    }

    public String getDestinationCity() 
    { 
        return DESTINATION_CITY; 
    }

    public String getSchoolAddress() 
    { 
        return SCHOOL_ADDRESS; 
    }

    private final String ORIGIN;
    private final String DESTINATION_CITY;
    private final String SCHOOL_ADDRESS;
}