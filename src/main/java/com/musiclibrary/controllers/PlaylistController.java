package com.musiclibrary.controllers;

import com.musiclibrary.models.Playlist;
import com.musiclibrary.models.Song;
import com.musiclibrary.repositories.IRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class PlaylistController {
    private final IRepository<Playlist> playlistRepository;
    private final IRepository<Song> songRepository;

    public PlaylistController(IRepository<Playlist> playlistRepository, IRepository<Song> songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    public void createPlaylist(Playlist playlist) {
        playlistRepository.add(playlist);
    }

    public void updatePlaylist(Playlist playlist) {
        playlistRepository.update(playlist);
    }

    public void deletePlaylist(String id) {
        playlistRepository.delete(id);
    }

    public Playlist getPlaylist(String id) {
        return playlistRepository.getById(id);
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.getAll();
    }

    // Search within playlist
    public List<Song> searchSongsInPlaylist(String playlistId, String query) {
        Playlist playlist = playlistRepository.getById(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        String lowerQuery = query.toLowerCase();
        List<Song> matchedSongs = new ArrayList<>();
        
        for (String songId : playlist.getSongIds()) {
            Song song = songRepository.getById(songId);
            if (song != null) {
                if (song.getTitle().toLowerCase().contains(lowerQuery) ||
                    song.getArtist().toLowerCase().contains(lowerQuery) ||
                    song.getAlbum().toLowerCase().contains(lowerQuery)) {
                    matchedSongs.add(song);
                }
            }
        }
        return matchedSongs;
    }

    // Playlist duration statistics
    public int getPlaylistDuration(String playlistId) {
        Playlist playlist = playlistRepository.getById(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist not found");
        }
        int totalSeconds = 0;
        for (String songId : playlist.getSongIds()) {
            Song song = songRepository.getById(songId);
            if (song != null) {
                totalSeconds += song.getDurationSeconds();
            }
        }
        return totalSeconds;
    }

    public void viewPlaylistDuration(String playlistId) {
        try {
            int duration = getPlaylistDuration(playlistId);
            int minutes = duration / 60;
            int seconds = duration % 60;
            System.out.printf("Playlist '%s' total duration: %d minutes %d seconds%n", 
                    playlistRepository.getById(playlistId).getName(), minutes, seconds);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Add or remove songs from playlist
    public void addSongToPlaylist(String playlistId, String songId) {
        Playlist playlist = playlistRepository.getById(playlistId);
        if (playlist == null) throw new IllegalArgumentException("Playlist not found");
        if (songRepository.getById(songId) == null) throw new IllegalArgumentException("Song not found");

        playlist.addSongId(songId);
        playlistRepository.update(playlist);
    }

    public void removeSongFromPlaylist(String playlistId, String songId) {
        Playlist playlist = playlistRepository.getById(playlistId);
        if (playlist == null) throw new IllegalArgumentException("Playlist not found");

        playlist.removeSongId(songId);
        playlistRepository.update(playlist);
    }

    // View playlist details
    public void viewPlaylistDetails(String playlistId) {
        Playlist playlist = playlistRepository.getById(playlistId);
        if (playlist == null) {
            System.out.println("Playlist not found.");
            return;
        }
        System.out.println("Playlist: " + playlist.getName() + " | Description: " + playlist.getDescription());
        System.out.println("Songs:");
        List<String> songIds = playlist.getSongIds();
        if (songIds.isEmpty()) {
            System.out.println("  (No songs in this playlist)");
            return;
        }

        for (String songId : songIds) {
            Song song = songRepository.getById(songId);
            if (song != null) {
                System.out.printf("  - [%s] %s by %s (%d sec)%n", 
                    song.getId(), song.getTitle(), song.getArtist(), song.getDurationSeconds());
            } else {
                System.out.printf("  - [Unknown Song ID: %s]%n", songId);
            }
        }
    }
}
