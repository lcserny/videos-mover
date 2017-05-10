package net.cserny.videos.mover.ui.model;

import java.io.File;

/**
 * Created by leonardo on 10.05.2017.
 */
public class SubtitleDTO
{
    private File from;
    private File to;
    private String extension;

    public File getFrom() {
        return from;
    }

    public void setFrom(File from) {
        this.from = from;
    }

    public File getTo() {
        return to;
    }

    public void setTo(File to) {
        this.to = to;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
