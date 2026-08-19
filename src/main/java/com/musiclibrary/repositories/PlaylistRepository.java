package com.musiclibrary.repositories;

import com.musiclibrary.models.Playlist;
import com.musiclibrary.utils.FileHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaylistRepository implements IRepository<Playlist> {
    private static final String FILE_PATH = "data/playlists.txt";
    private List<Playlist> playlists;

    public PlaylistRepository() {
        this.playlists = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public void add(Playlist entity) {
        if (getById(entity.getId()) != null) {
            throw new IllegalArgumentException("Playlist ID already exists!");
        }
        playlists.add(entity);
        saveToFile();
    }

    @Override
    public void update(Playlist entity) {
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getId().equals(entity.getId())) {
                playlists.set(i, entity);
                saveToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Playlist not found!");
    }

    @Override
    public void delete(String id) {
        playlists.removeIf(p -> p.getId().equals(id));
        saveToFile();
    }

    @Override
    public Playlist getById(String id) {
        return playlists.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Playlist> getAll() {
        return new ArrayList<>(playlists);
    }

    @Override
    public void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Playlist playlist : playlists) {
            // Format: id|name|description|songId1,songId2,...
            String songIdsStr = String.join(",", playlist.getSongIds());
            String line = String.format("%s|%s|%s|%s",
                    playlist.getId(), playlist.getName(), playlist.getDescription(), songIdsStr);
            lines.add(line);
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    @Override
    public void loadFromFile() {
        playlists.clear();
        List<String> lines = FileHandler.readLines(FILE_PATH);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                Playlist playlist = new Playlist(parts[0], parts[1], parts[2]);
                if (parts.length == 4 && !parts[3].trim().isEmpty()) {
                    List<String> ids = Arrays.asList(parts[3].split(","));
                    for (String id : ids) {
                        playlist.addSongId(id);
                    }
                }
                playlists.add(playlist);
            }
        }
    }
}
