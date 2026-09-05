package com.musiclibrary;

import com.musiclibrary.commands.AddSongToPlaylistCommand;
import com.musiclibrary.commands.CommandManager;
import com.musiclibrary.commands.ICommand;
import com.musiclibrary.commands.RemoveSongFromPlaylistCommand;
import com.musiclibrary.controllers.PlaybackController;
import com.musiclibrary.controllers.PlaylistController;
import com.musiclibrary.controllers.SongController;
import com.musiclibrary.controllers.StatisticsController;
import com.musiclibrary.models.Playlist;
import com.musiclibrary.models.Song;
import com.musiclibrary.repositories.PlaylistRepository;
import com.musiclibrary.repositories.SongRepository;

import java.util.Scanner;

public class Main {
    private static SongController songController;
    private static PlaylistController playlistController;
    private static PlaybackController playbackController;
    private static StatisticsController statisticsController;
    private static CommandManager commandManager;

    public static void main(String[] args) {
        // Initialize dependencies
        SongRepository songRepository = new SongRepository();
        PlaylistRepository playlistRepository = new PlaylistRepository();
        
        songController = new SongController(songRepository);
        playlistController = new PlaylistController(playlistRepository, songRepository);
        playbackController = new PlaybackController(songRepository);
        statisticsController = new StatisticsController(songRepository);
        commandManager = new CommandManager();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to Music Library System");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add a new Song");
            System.out.println("2. Create a new Playlist");
            System.out.println("3. Add Song to Playlist");
            System.out.println("4. Remove Song from Playlist");
            System.out.println("5. View Playlist Details");
            System.out.println("6. Play a Song");
            System.out.println("7. Show Recently Played");
            System.out.println("8. Show Top Played Songs");
            System.out.println("9. Generate Playlist by Rules");
            System.out.println("10. Undo last Playlist action");
            System.out.println("11. Redo last Playlist action");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        addSongFlow(scanner);
                        break;
                    case 2:
                        createPlaylistFlow(scanner);
                        break;
                    case 3:
                        addSongToPlaylistFlow(scanner);
                        break;
                    case 4:
                        removeSongFromPlaylistFlow(scanner);
                        break;
                    case 5:
                        System.out.print("Enter Playlist ID: ");
                        playlistController.viewPlaylistDetails(scanner.nextLine());
                        break;
                    case 6:
                        System.out.print("Enter Song ID to play: ");
                        playbackController.playSong(scanner.nextLine());
                        break;
                    case 7:
                        playbackController.showRecentlyPlayed();
                        break;
                    case 8:
                        statisticsController.showMostPlayedSongs(10);
                        break;
                    case 9:
                        generatePlaylistFlow(scanner);
                        break;
                    case 10:
                        commandManager.undo();
                        System.out.println("Undo completed.");
                        break;
                    case 11:
                        commandManager.redo();
                        System.out.println("Redo completed.");
                        break;
                    case 0:
                        running = false;
                        System.out.println("Exiting system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void addSongFlow(Scanner scanner) {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Artist: ");
        String artist = scanner.nextLine();
        System.out.print("Enter Album: ");
        String album = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter Duration (seconds): ");
        int duration = Integer.parseInt(scanner.nextLine());

        Song newSong = new Song(id, title, artist, album, genre, duration);
        songController.addSong(newSong);
        System.out.println("Song added successfully.");
    }

    private static void createPlaylistFlow(Scanner scanner) {
        System.out.print("Enter Playlist ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Description: ");
        String desc = scanner.nextLine();

        Playlist p = new Playlist(id, name, desc);
        playlistController.createPlaylist(p);
        System.out.println("Playlist created successfully.");
    }

    private static void addSongToPlaylistFlow(Scanner scanner) {
        System.out.print("Enter Playlist ID: ");
        String pId = scanner.nextLine();
        System.out.print("Enter Song ID: ");
        String sId = scanner.nextLine();

        ICommand cmd = new AddSongToPlaylistCommand(playlistController, pId, sId);
        commandManager.executeCommand(cmd);
        System.out.println("Song added to playlist.");
    }

    private static void removeSongFromPlaylistFlow(Scanner scanner) {
        System.out.print("Enter Playlist ID: ");
        String pId = scanner.nextLine();
        System.out.print("Enter Song ID: ");
        String sId = scanner.nextLine();

        ICommand cmd = new RemoveSongFromPlaylistCommand(playlistController, pId, sId);
        commandManager.executeCommand(cmd);
        System.out.println("Song removed from playlist.");
    }

    private static void generatePlaylistFlow(Scanner scanner) {
        System.out.print("Enter new Playlist Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter desired Genre (or leave blank): ");
        String genre = scanner.nextLine();
        if (genre.trim().isEmpty()) genre = null;
        System.out.print("Enter desired Artist (or leave blank): ");
        String artist = scanner.nextLine();
        if (artist.trim().isEmpty()) artist = null;
        System.out.print("Enter Maximum Duration (seconds): ");
        int duration = Integer.parseInt(scanner.nextLine());

        Playlist generated = playlistController.generatePlaylistByRules(name, genre, artist, duration);
        System.out.println("Generated Playlist ID: " + generated.getId());
    }
}
