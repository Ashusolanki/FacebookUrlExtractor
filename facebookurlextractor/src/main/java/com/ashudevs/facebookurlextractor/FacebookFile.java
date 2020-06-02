package com.ashudevs.facebookurlextractor;

public class FacebookFile {
    private String sdUrl;
    private String hdUrl;
    private String ext;
    private String filename;
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSdUrl() {
        return sdUrl;
    }

    public void setSdUrl(String sdUrl) {
        this.sdUrl = sdUrl;
    }

    public String getHdUrl() {
        return hdUrl;
    }

    public void setHdUrl(String hdUrl) {
        this.hdUrl = hdUrl;
    }
}
