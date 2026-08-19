package com.musiclibrary.controllers;

import com.musiclibrary.models.Playlist;
import com.musiclibrary.models.Song;
import com.musiclibrary.repositories.IRepository;
import java.util.List;
import java.util.stream.Collectors;

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

    // Day 3: Add or remove songs from playlist
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

    // Day 3: View playlist details
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
