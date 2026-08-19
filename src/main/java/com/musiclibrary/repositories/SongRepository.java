package com.musiclibrary.repositories;

import com.musiclibrary.models.Song;
import com.musiclibrary.utils.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class SongRepository implements IRepository<Song> {
    private static final String FILE_PATH = "data/songs.txt";
    private List<Song> songs;

    public SongRepository() {
        this.songs = new ArrayList<>();
        loadFromFile();
    }

    @Override
    public void add(Song entity) {
        if (getById(entity.getId()) != null) {
            throw new IllegalArgumentException("Song ID already exists!");
        }
        songs.add(entity);
        saveToFile();
    }

    @Override
    public void update(Song entity) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId().equals(entity.getId())) {
                songs.set(i, entity);
                saveToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Song not found!");
    }

    @Override
    public void delete(String id) {
        songs.removeIf(s -> s.getId().equals(id));
        saveToFile();
    }

    @Override
    public Song getById(String id) {
        return songs.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Song> getAll() {
        return new ArrayList<>(songs);
    }

    @Override
    public void saveToFile() {
        List<String> lines = new ArrayList<>();
        for (Song song : songs) {
            // Format: id|title|artist|album|genre|durationSeconds|popularity|isFavorite
            String line = String.format("%s|%s|%s|%s|%s|%d|%d|%b",
                    song.getId(), song.getTitle(), song.getArtist(), song.getAlbum(),
                    song.getGenre(), song.getDurationSeconds(), song.getPopularity(), song.isFavorite());
            lines.add(line);
        }
        FileHandler.writeLines(FILE_PATH, lines);
    }

    @Override
    public void loadFromFile() {
        songs.clear();
        List<String> lines = FileHandler.readLines(FILE_PATH);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 8) {
                Song song = new Song(parts[0], parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
                song.setPopularity(Integer.parseInt(parts[6]));
                song.setFavorite(Boolean.parseBoolean(parts[7]));
                songs.add(song);
            }
        }
    }
}
