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

import java.util.List;
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
            System.out.println("12. View all Songs");
            System.out.println("13. View all Playlists");
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
                    case 12:
                        viewAllSongs();
                        break;
                    case 13:
                        viewAllPlaylists();
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
        String id;
        while (true) {
            System.out.print("Enter ID (format Sxxx, e.g., S001): ");
            id = scanner.nextLine().trim();
            if (!id.matches("^S\\d{3}$")) {
                System.out.println("Invalid format. ID must start with 'S' followed by 3 digits.");
                continue;
            }
            if (songController.getSong(id) != null) {
                System.out.println("Error: Song ID already exists. Please choose a different ID.");
                continue;
            }
            break;
        }

        String title;
        while (true) {
            System.out.print("Enter Title: ");
            title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter again.");
                continue;
            }
            break;
        }

        String artist;
        while (true) {
            System.out.print("Enter Artist: ");
            artist = scanner.nextLine().trim();
            if (artist.isEmpty()) {
                System.out.println("Artist cannot be empty. Please enter again.");
                continue;
            }
            break;
        }

        System.out.print("Enter Album: ");
        String album = scanner.nextLine().trim();
        
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine().trim();
        
        int duration;
        while (true) {
            System.out.print("Enter Duration (seconds): ");
            try {
                duration = Integer.parseInt(scanner.nextLine().trim());
                if (duration <= 0) {
                    System.out.println("Duration must be a positive number.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        Song newSong = new Song(id, title, artist, album, genre, duration);
        songController.addSong(newSong);
        System.out.println("Song added successfully.");
    }

    private static void createPlaylistFlow(Scanner scanner) {
        String id;
        while (true) {
            System.out.print("Enter Playlist ID (format Pxxx, e.g., P001): ");
            id = scanner.nextLine().trim();
            if (!id.matches("^P\\d{3}$")) {
                System.out.println("Invalid format. ID must start with 'P' followed by 3 digits.");
                continue;
            }
            if (playlistController.getPlaylist(id) != null) {
                System.out.println("Error: Playlist ID already exists. Please choose a different ID.");
                continue;
            }
            break;
        }

        String name;
        while (true) {
            System.out.print("Enter Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter again.");
                continue;
            }
            break;
        }

        System.out.print("Enter Description: ");
        String desc = scanner.nextLine().trim();

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
        String name;
        while (true) {
            System.out.print("Enter new Playlist Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter again.");
                continue;
            }
            break;
        }

        System.out.print("Enter desired Genre (or leave blank): ");
        String genre = scanner.nextLine().trim();
        if (genre.isEmpty()) genre = null;
        
        System.out.print("Enter desired Artist (or leave blank): ");
        String artist = scanner.nextLine().trim();
        if (artist.isEmpty()) artist = null;
        
        int duration;
        while (true) {
            System.out.print("Enter Maximum Duration (seconds): ");
            try {
                duration = Integer.parseInt(scanner.nextLine().trim());
                if (duration <= 0) {
                    System.out.println("Duration must be a positive number.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        Playlist generated = playlistController.generatePlaylistByRules(name, genre, artist, duration);
        System.out.println("Generated Playlist ID: " + generated.getId());
    }

    private static void viewAllSongs() {
        System.out.println("\n--- ALL SONGS ---");
        List<Song> songs = songController.getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("No songs available.");
        } else {
            for (Song song : songs) {
                System.out.printf("[%s] %s by %s | Album: %s | Genre: %s | Duration: %ds%n",
                        song.getId(), song.getTitle(), song.getArtist(), song.getAlbum(), song.getGenre(), song.getDurationSeconds());
            }
        }
    }

    private static void viewAllPlaylists() {
        System.out.println("\n--- ALL PLAYLISTS ---");
        List<Playlist> playlists = playlistController.getAllPlaylists();
        if (playlists.isEmpty()) {
            System.out.println("No playlists available.");
        } else {
            for (Playlist playlist : playlists) {
                System.out.printf("[%s] %s | Description: %s%n",
                        playlist.getId(), playlist.getName(), playlist.getDescription());
            }
        }
    }
}
