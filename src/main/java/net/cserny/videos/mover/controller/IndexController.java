package net.cserny.videos.mover.controller;

import net.cserny.videos.mover.VideosMoverApplication;
import net.cserny.videos.mover.dao.Video;
import net.cserny.videos.mover.dao.VideoRepo;
import net.cserny.videos.mover.service.VideoMover;
import net.cserny.videos.mover.service.VideoOutputPathResolver;
import net.cserny.videos.mover.service.VideoScanner;
import net.cserny.videos.mover.service.provider.SystemPathProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by leonardo on 01.07.2017.
 */
@Controller
public class IndexController
{
    @Autowired
    private VideoScanner videoScanner;
    @Autowired
    private SystemPathProvider pathProvider;
    @Autowired
    private VideoRepo videoRepo;
    @Autowired
    private VideoOutputPathResolver videoOutputPathResolver;
    @Autowired
    private VideoMover videoMover;

    @RequestMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("downloadsPath", pathProvider.getDownloadsPath());
        model.addAttribute("moviesPath", pathProvider.getMoviePath());
        model.addAttribute("tvShowPath", pathProvider.getTvShowPath());
        model.addAttribute("videos", videoRepo.findAllSorted());
        model.addAttribute("totalVideos", videoRepo.countVideos());
        return "index";
    }

    @RequestMapping("/move-videos")
    public String moveVideos(Model model, @RequestParam("tvShowChecked") String[] tvShowChecked, @RequestParam("movieChecked") String[] movieChecked) {
        for (String idString : tvShowChecked) {
            Long id = Long.valueOf(idString);
            Video video = videoRepo.findOne(id);
            if (video != null) {
                videoMover.moveTvShow(video);
                videoRepo.delete(id);
            }
        }

        for (String idString : movieChecked) {
            Long id = Long.valueOf(idString);
            Video video = videoRepo.findOne(id);
            if (video != null) {
                videoMover.moveMovie(video);
                videoRepo.delete(id);
            }
        }

        return showIndex(model);
    }

    @RequestMapping("/load-videos")
    public String loadVideos(Model model) {
        videoRepo.deleteAll();
        for (File videoFile : videoScanner.scan(pathProvider.getDownloadsPath())) {
            Video video = new Video();
            video.setName(videoFile.getName());
            video.setFilePath(videoFile.getPath());
            video.setTvOutput(videoOutputPathResolver.resolveTvShow(videoFile.getName()));
            video.setMovieOutput(videoOutputPathResolver.resolveMovies(videoFile.getName()));
            videoRepo.saveAndFlush(video);
        }

        return showIndex(model);
    }

    @RequestMapping("/change-paths")
    public String changePaths(Model model, String downloadsPath, String moviesPath, String tvShowPath) {
        pathProvider.setDownloadsPath(downloadsPath);
        pathProvider.setMoviePath(moviesPath);
        pathProvider.setTvShowPath(tvShowPath);

        return showIndex(model);
    }
}
