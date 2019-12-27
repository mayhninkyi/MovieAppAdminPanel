package com.mhk.movieappadminpanel;

public class SeriesModel {
    public String series_name,series_img_link,series_category,series_vid_link;

    public SeriesModel() {
    }

    public SeriesModel(String series_name, String series_img_link, String series_category, String series_vid_link) {
        this.series_name = series_name;
        this.series_img_link = series_img_link;
        this.series_category = series_category;
        this.series_vid_link = series_vid_link;
    }

    public String getSeries_name() {
        return series_name;
    }

    public void setSeries_name(String series_name) {
        this.series_name = series_name;
    }

    public String getSeries_img_link() {
        return series_img_link;
    }

    public void setSeries_img_link(String series_img_link) {
        this.series_img_link = series_img_link;
    }

    public String getSeries_category() {
        return series_category;
    }

    public void setSeries_category(String series_category) {
        this.series_category = series_category;
    }

    public String getSeries_vid_link() {
        return series_vid_link;
    }

    public void setSeries_vid_link(String series_vid_link) {
        this.series_vid_link = series_vid_link;
    }
}
