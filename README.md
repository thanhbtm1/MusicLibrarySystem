# Music Library Management System

A simple, pure Java (No DB, No Frameworks) console-based application to manage a music library.
Data is stored locally in text files under the `data/` directory.

## Features

- **Core Data Management:** Add, update, delete, and view songs and playlists.
- **Playback System:** Play songs/playlists, shuffle, repeat, and track recently played history.
- **Smart Playlists:** Auto-generate playlists based on genre, artist, and max duration.
- **Analytics:** View playlist total duration and most played songs.
- **Undo/Redo:** Playlist actions (adding/removing songs) can be undone and redone (Command Pattern).

## Architecture

The system follows a strict Model-View-Controller (MVC) and Repository pattern:
- `models/`: Pure data objects (`Song`, `Playlist`).
- `repositories/`: File-based data storage implementations using `FileHandler`.
- `controllers/`: Business logic.
- `commands/`: Implementation of the Command pattern for Undo/Redo mechanisms.


