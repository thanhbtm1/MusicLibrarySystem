package com.musiclibrary.models;

import java.util.Objects;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int durationSeconds;
    private int popularity;
    private boolean isFavorite;

    public Song(String id, String title, String artist, String album, String genre, int durationSeconds) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.durationSeconds = durationSeconds;
        this.popularity = 0;
        this.isFavorite = false;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbum() { return album; }
    public String getGenre() { return genre; }
    public int getDurationSeconds() { return durationSeconds; }
    public int getPopularity() { return popularity; }
    public boolean isFavorite() { return isFavorite; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbum(String album) { this.album = album; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
    public void setPopularity(int popularity) { this.popularity = popularity; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
