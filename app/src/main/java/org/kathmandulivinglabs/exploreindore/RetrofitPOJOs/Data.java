package org.kathmandulivinglabs.exploreindore.RetrofitPOJOs;

public class Data
{
    private String amenity;

    private String[] tags;

    public String getAmenity ()
    {
        return amenity;
    }

    public void setAmenity (String amenity)
    {
        this.amenity = amenity;
    }

    public String[] getTags ()
    {
        return tags;
    }

    public void setTags (String[] tags)
    {
        this.tags = tags;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amenity = "+amenity+", tags = "+tags+"]";
    }
}