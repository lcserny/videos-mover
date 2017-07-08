package net.cserny.videos.mover.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by leonardo on 01.07.2017.
 */
public interface VideoRepo extends JpaRepository<Video, Long>
{
    @Query("SELECT count(*) from Video")
    public int countVideos();

    @Query("SELECT v from Video v ORDER BY LOWER(v.name) ASC")
    public List<Video> findAllSorted();
}
