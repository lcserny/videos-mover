package net.cserny.videos.mover.dao;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by leonardo on 01.07.2017.
 */
@Entity
@Table(name = "VIDEOS")
public class Video implements Serializable
{
    private long id;
    private String name;
    private String filePath;
    private String tvOutput;
    private String movieOutput;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column
    public String getTvOutput() {
        return tvOutput;
    }

    public void setTvOutput(String tvOutput) {
        this.tvOutput = tvOutput;
    }

    @Column
    public String getMovieOutput() {
        return movieOutput;
    }

    public void setMovieOutput(String movieOutput) {
        this.movieOutput = movieOutput;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", tvOutput='" + tvOutput + '\'' +
                ", movieOutput='" + movieOutput + '\'' +
                '}';
    }
}
