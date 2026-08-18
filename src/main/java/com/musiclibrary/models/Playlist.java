package com.musiclibrary.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist {
    private String id;
    private String name;
    private String description;
    private List<String> songIds;

    public Playlist(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songIds = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getSongIds() { return songIds; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    
    public void addSongId(String songId) {
        if (!songIds.contains(songId)) {
            songIds.add(songId);
        }
    }

    public void removeSongId(String songId) {
        songIds.remove(songId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
