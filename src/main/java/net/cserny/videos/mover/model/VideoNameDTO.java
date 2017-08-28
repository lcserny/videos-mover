package net.cserny.videos.mover.model;

/**
 * Created by Leonardo Cserny on 20.10.2016.
 */
public class VideoNameDTO
{
    private String name;
    private Integer year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFormattedName() {
        return year != null ? String.format("%s (%d)", name, year) : name;
    }
}
