package com.example.mostafa.myapplication1;

/**
 * Created by 679292 on 4/17/2016.
 */
public class WellData {
    public String location;
    public double depth;
    public double perfdepth;
    public double perfzone;
    public double stroke;
    public int strokepermin;

    public WellData()
    {}

    public WellData(String location, double depth, double perfdepth,
                    double perfzone, double stroke, int strokepermin)
    {
        this.location = location;
        this.depth = depth;
        this.perfdepth = perfdepth;
        this.perfzone = perfzone;
        this.stroke = stroke;
        this.strokepermin = strokepermin;

    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setDepth(double depth)
    {
        this.depth = depth;
    }

    public void setPerfdepth(double perfdepth)
    {
        this.perfdepth = perfdepth;
    }

    public void setPerfzone(double perfzone)
    {
        this.perfzone = perfzone;
    }

    public void setStroke(double stroke)
    {
        this.stroke = stroke;
    }
    public void setStrokepermin(int strokepermin)
    {
        this.strokepermin = strokepermin;
    }

    public String getLocation()
    {
        return location;
    }

    public double getDepth()
    {
        return depth;
    }

    public double getPerfdepth()
    {
        return perfdepth;
    }

    public double getPerfzone()
    {
        return perfzone;
    }

    public double getStroke()
    {
        return stroke;
    }

    public int getStrokepermin()
    {
        return strokepermin;
    }

    public String toString()
    {
        return "Location: " + location + "\n"
                + "Well Depth: " + depth + "\n"
                + "Perforation Depth: " + perfdepth +"\n"
                + "Perforation Zone: " + perfzone +"\n"
                + "Stroke: " + stroke +"\n"
                + "Stroke Per Minute: " + strokepermin;
    }

}
