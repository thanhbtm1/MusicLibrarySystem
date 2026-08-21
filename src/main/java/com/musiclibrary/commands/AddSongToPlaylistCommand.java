package com.musiclibrary.commands;

import com.musiclibrary.controllers.PlaylistController;

public class AddSongToPlaylistCommand implements ICommand {
    private final PlaylistController controller;
    private final String playlistId;
    private final String songId;

    public AddSongToPlaylistCommand(PlaylistController controller, String playlistId, String songId) {
        this.controller = controller;
        this.playlistId = playlistId;
        this.songId = songId;
    }

    @Override
    public void execute() {
        controller.addSongToPlaylist(playlistId, songId);
    }

    @Override
    public void undo() {
        controller.removeSongFromPlaylist(playlistId, songId);
    }
}
