package com.mhk.movieappadminpanel;

public class MovieModel {
    String movieName,movieImageLink,movieVideoLink,movieCategory,movieSeries;

    public MovieModel() {
    }

    public MovieModel(String movieName, String movieImageLink, String movieVideoLink, String movieCategory, String movieSeries) {
        this.movieName = movieName;
        this.movieImageLink = movieImageLink;
        this.movieVideoLink = movieVideoLink;
        this.movieCategory = movieCategory;
        this.movieSeries = movieSeries;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImageLink() {
        return movieImageLink;
    }

    public void setMovieImageLink(String movieImageLink) {
        this.movieImageLink = movieImageLink;
    }

    public String getMovieVideoLink() {
        return movieVideoLink;
    }

    public void setMovieVideoLink(String movieVideoLink) {
        this.movieVideoLink = movieVideoLink;
    }

    public String getMovieCategory() {
        return movieCategory;
    }

    public void setMovieCategory(String movieCategory) {
        this.movieCategory = movieCategory;
    }

    public String getMovieSeries() {
        return movieSeries;
    }

    public void setMovieSeries(String movieSeries) {
        this.movieSeries = movieSeries;
    }
}
