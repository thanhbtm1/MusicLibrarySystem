package com.musiclibrary.controllers;

import com.musiclibrary.models.Song;
import com.musiclibrary.repositories.IRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaybackController {
    private final IRepository<Song> songRepository;
    private final List<String> recentlyPlayedHistory;
    
    private boolean shuffleMode;
    private boolean repeatMode;

    public PlaybackController(IRepository<Song> songRepository) {
        this.songRepository = songRepository;
        this.recentlyPlayedHistory = new ArrayList<>();
        this.shuffleMode = false;
        this.repeatMode = false;
    }

    public void playSong(String songId) {
        Song song = songRepository.getById(songId);
        if (song != null) {
            System.out.println("Now playing: " + song.getTitle() + " by " + song.getArtist());
            // Update popularity and history
            song.setPopularity(song.getPopularity() + 1);
            songRepository.update(song);
            
            addToHistory(songId);
        } else {
            System.out.println("Cannot play: Song not found.");
        }
    }

    public void playPlaylist(List<String> songIds) {
        if (songIds == null || songIds.isEmpty()) {
            System.out.println("Playlist is empty.");
            return;
        }

        List<String> queue = new ArrayList<>(songIds);
        if (shuffleMode) {
            Collections.shuffle(queue);
        }

        do {
            for (String songId : queue) {
                playSong(songId);
            }
        } while (repeatMode);
    }

    private void addToHistory(String songId) {
        // Simple history tracking (Medium req). Hard req will limit storage size later.
        recentlyPlayedHistory.remove(songId); // remove if exists to push to front
        recentlyPlayedHistory.add(0, songId); // add to top
    }

    public void showRecentlyPlayed() {
        System.out.println("--- Recently Played History ---");
        if (recentlyPlayedHistory.isEmpty()) {
            System.out.println("No history found.");
            return;
        }
        for (String songId : recentlyPlayedHistory) {
            Song song = songRepository.getById(songId);
            if (song != null) {
                System.out.println("- " + song.getTitle());
            }
        }
    }

    public void toggleShuffle() {
        this.shuffleMode = !this.shuffleMode;
        System.out.println("Shuffle mode is now: " + (shuffleMode ? "ON" : "OFF"));
    }

    public void toggleRepeat() {
        this.repeatMode = !this.repeatMode;
        System.out.println("Repeat mode is now: " + (repeatMode ? "ON" : "OFF"));
    }
}
