package com.musiclibrary.controllers;

import com.musiclibrary.models.Song;
import com.musiclibrary.repositories.IRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsController {
    private final IRepository<Song> songRepository;

    public StatisticsController(IRepository<Song> songRepository) {
        this.songRepository = songRepository;
    }

    // Most played songs statistics
    public List<Song> getMostPlayedSongs(int limit) {
        // 'popularity' represents play count in our model.
        return songRepository.getAll().stream()
                .filter(s -> s.getPopularity() > 0)
                .sorted(Comparator.comparingInt(Song::getPopularity).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void showMostPlayedSongs(int limit) {
        System.out.println("--- Top " + limit + " Most Played Songs ---");
        List<Song> topSongs = getMostPlayedSongs(limit);
        if (topSongs.isEmpty()) {
            System.out.println("No songs have been played yet.");
            return;
        }
        int rank = 1;
        for (Song song : topSongs) {
            System.out.printf("%d. %s by %s (Played %d times)%n",
                    rank++, song.getTitle(), song.getArtist(), song.getPopularity());
        }
    }

    public List<Song> rankSongs() {
        // Multi-criteria ranking: Play count (popularity) + Favorite status
        // Assume 1 play = 1 point, Favorite = 50 points
        return songRepository.getAll().stream()
                .sorted((s1, s2) -> {
                    int score1 = s1.getPopularity() + (s1.isFavorite() ? 50 : 0);
                    int score2 = s2.getPopularity() + (s2.isFavorite() ? 50 : 0);
                    return Integer.compare(score2, score1);
                })
                .collect(Collectors.toList());
    }
}
