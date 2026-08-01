public class PostOfficeList 
{
    public PostOfficeList(String postOffice1, String postOffice2, double distance) 
    {
        this.postOffice1 = postOffice1;
        this.postOffice2 = postOffice2;
        this.distance = distance;
    }

    public String getPostOffice1() 
    { 
        return postOffice1; 
    }
    
    public String getPostOffice2()
    { 
        return postOffice2; 
    }

    public double getDistance()
    {
        return distance;
    }


    private String postOffice1;
    private String postOffice2;
    private double distance;
}