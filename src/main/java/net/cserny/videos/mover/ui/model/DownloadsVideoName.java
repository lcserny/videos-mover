package net.cserny.videos.mover.ui.model;

/**
 * Created by Leonardo Cserny on 20.10.2016.
 */
public class DownloadsVideoName
{
    private String name;
    private Integer year;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getYear()
    {
        return year;
    }

    public void setYear(Integer year)
    {
        this.year = year;
    }

    public String getFormattedName()
    {
        String formattedName = name;
        if (year != null) {
            formattedName += " (" + year + ")";
        }
        return formattedName;
    }
}
