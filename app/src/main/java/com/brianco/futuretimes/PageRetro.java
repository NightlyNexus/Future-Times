package com.brianco.futuretimes;

import com.google.gson.annotations.SerializedName;

public class PageRetro {

    @SerializedName("mediaLink") private String mediaLink;

    @SerializedName("contentType") private String contentType;

    @SerializedName("metadata") private Metadata metadata;

    public PageRetro(String mediaLink, String contentType, Metadata metadata) {
        this.mediaLink = mediaLink;
        this.contentType = contentType;
        this.metadata = metadata;
    }

    public boolean isPic() {
        return contentType.startsWith("image");
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof PageRetro)) return false;
        PageRetro o = (PageRetro) other;
        return (o.metadata.volume == this.metadata.volume && o.metadata.page == this.metadata.page);
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public String getContributor() {
        return metadata.contributor;
    }

    public String getDate() {
        return metadata.date;
    }

    public String getPublisher() {
        return metadata.publisher;
    }

    public String getSource() {
        return metadata.source;
    }

    public String getCreator() {
        return metadata.creator;
    }

    public String getDescription() {
        return metadata.description;
    }

    public String getTitle() {
        return metadata.title;
    }

    public int getVolume() {
        return metadata.volume;
    }

    public int getPage() {
        return metadata.page;
    }

    public String getTranscriptLink() {
        return metadata.transcriptLink;
    }

    private static class Metadata {
        @SerializedName("Contributor") private String contributor;
        @SerializedName("Date") private String date;
        @SerializedName("Publisher") private String publisher;
        @SerializedName("Source") private String source;
        @SerializedName("Creator") private String creator;
        @SerializedName("Description") private String description;
        @SerializedName("Title") private String title;
        @SerializedName("Volume") private int volume;
        @SerializedName("Page") private int page;
        @SerializedName("transcript_link") private String transcriptLink;
    }
}
