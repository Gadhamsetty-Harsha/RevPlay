package com.revplay.service;

import java.util.List;
import com.revplay.dao.SongDAO;
import com.revplay.dao.AlbumDAO;

public class SearchService {

    private SongDAO songDAO = new SongDAO();
    private AlbumDAO albumDAO = new AlbumDAO();

    public void searchSongByKeyword(String keyword) {
        List<String> songs = songDAO.searchSongsByKeyword(keyword);
        displayResults(songs);
    }

    public void searchSongByArtist(String artistName) {
        List<String> songs = songDAO.searchSongsByArtist(artistName);
        displayResults(songs);
    }

    // ✅ USER-side browse (by name, not ID)
    public void browseAlbumsByArtist(String artistName) {
        List<String> albums =
                albumDAO.getAlbumsByArtistName(artistName);
        displayResults(albums);
    }

    private void displayResults(List<String> results) {
        if (results.isEmpty()) {
            System.out.println("❌ No results found.");
        } else {
            System.out.println("🎧 Results:");
            for (String r : results) {
                System.out.println(" - " + r);
            }
        }
    }
}
