package com.brianco.futuretimes;

import java.io.Serializable;

public class Page implements Comparable<Page>, Serializable {

    private final String pdfLink;
    private final String picLink;
    private final String transcriptLink;
    private final String contributor;
    private final String date;
    private final String publisher;
    private final String source;
    private final String creator;
    private final String description;
    private final String title;
    private final int volume;
    private final int page;

    public Page(String pdfLink, String picLink, String transcriptLink, String contributor, String date, String publisher, String source, String creator, String description, String title, int volume, int page) {
        this.pdfLink = pdfLink;
        this.picLink = picLink;
        this.transcriptLink = transcriptLink;
        this.contributor = contributor;
        this.date = date;
        this.publisher = publisher;
        this.source = source;
        this.creator = creator;
        this.description = description;
        this.title = title;
        this.volume = volume;
        this.page = page;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Page)) return false;
        Page o = (Page) other;
        return (o.volume == this.volume && o.page == this.page);
    }

    public int getPage() {
        return page;
    }

    public int getVolume() {
        return volume;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() {
        return creator;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public String getPicLink() {
        return picLink;
    }

    public String getTranscriptLink() {
        return transcriptLink;
    }

    public String getContributor() {
        return contributor;
    }

    public String getDate() {
        return date;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSource() {
        return source;
    }

    @Override
    public int compareTo(Page another) {
        if (this.volume > another.volume) {
            return 1;
        }
        if (this.volume < another.volume) {
            return -1;
        }
        return this.page - another.page;
    }
}
