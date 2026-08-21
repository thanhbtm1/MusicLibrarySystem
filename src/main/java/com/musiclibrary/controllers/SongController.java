package com.musiclibrary.controllers;

import com.musiclibrary.models.Song;
import com.musiclibrary.repositories.IRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SongController {
    private final IRepository<Song> songRepository;

    public SongController(IRepository<Song> songRepository) {
        this.songRepository = songRepository;
    }

    public void addSong(Song song) {
        songRepository.add(song);
    }

    public void updateSong(Song song) {
        songRepository.update(song);
    }

    public void deleteSong(String id) {
        songRepository.delete(id);
    }

    public Song getSong(String id) {
        return songRepository.getById(id);
    }

    public List<Song> getAllSongs() {
        return songRepository.getAll();
    }

    // Search and Sort
    public List<Song> searchSongs(String query) {
        String lowerQuery = query.toLowerCase();
        return songRepository.getAll().stream()
                .filter(s -> s.getTitle().toLowerCase().contains(lowerQuery) ||
                        s.getArtist().toLowerCase().contains(lowerQuery) ||
                        s.getAlbum().toLowerCase().contains(lowerQuery) ||
                        s.getGenre().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public List<Song> sortSongsByTitle() {
        return songRepository.getAll().stream()
                .sorted(Comparator.comparing(Song::getTitle))
                .collect(Collectors.toList());
    }

    public List<Song> sortSongsByArtist() {
        return songRepository.getAll().stream()
                .sorted(Comparator.comparing(Song::getArtist))
                .collect(Collectors.toList());
    }

    // Mark or unmark favorite
    public void toggleFavorite(String id) {
        Song song = songRepository.getById(id);
        if (song != null) {
            song.setFavorite(!song.isFavorite());
            songRepository.update(song);
        } else {
            throw new IllegalArgumentException("Song not found!");
        }
    }
}
