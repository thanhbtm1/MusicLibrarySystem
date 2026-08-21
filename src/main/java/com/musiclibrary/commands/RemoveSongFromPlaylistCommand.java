package com.musiclibrary.commands;

import com.musiclibrary.controllers.PlaylistController;

public class RemoveSongFromPlaylistCommand implements ICommand {
    private final PlaylistController controller;
    private final String playlistId;
    private final String songId;

    public RemoveSongFromPlaylistCommand(PlaylistController controller, String playlistId, String songId) {
        this.controller = controller;
        this.playlistId = playlistId;
        this.songId = songId;
    }

    @Override
    public void execute() {
        controller.removeSongFromPlaylist(playlistId, songId);
    }

    @Override
    public void undo() {
        controller.addSongToPlaylist(playlistId, songId);
    }
}
