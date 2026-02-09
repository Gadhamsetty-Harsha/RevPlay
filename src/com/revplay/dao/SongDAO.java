package com.revplay.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revplay.model.Song;
import com.revplay.util.DBConnection;

public class SongDAO {

    /* =====================================================
       INSERT SONG (ONLY PLACE WHERE INSERT IS USED)
       ===================================================== */
    public boolean uploadSong(int artistId, Integer albumId,
                              String title, String genre,
                              int duration, Date releaseDate, String filePath) {

        String sql =
                "INSERT INTO songs (artist_id, album_id, title, genre, duration, release_date, play_count, filepath) " +
                        "VALUES (?, ?, ?, ?, ?, ?, 0, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);

            if (albumId == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, albumId);
            }

            ps.setString(3, title);
            ps.setString(4, genre);
            ps.setInt(5, duration);
            ps.setDate(6, releaseDate);
            ps.setString(7, filePath);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =====================================================
       VIEW ALL SONGS BY ARTIST (ALBUM + NON-ALBUM)
       ===================================================== */
    public List<Song> getSongsByArtistId(int artistId) {

        List<Song> songs = new ArrayList<>();

        String sql =
        	    "SELECT s.song_id, s.title, s.genre, s.duration, s.play_count, s.filePath, a.album_name " +
        	    "FROM songs s " +
        	    "LEFT JOIN albums a ON s.album_id = a.album_id " +
        	    "WHERE s.artist_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setGenre(rs.getString("genre"));
                song.setDuration(rs.getInt("duration"));
                song.setPlayCount(rs.getInt("play_count"));
                song.setFilePath(rs.getString("filePath"));

                String albumName = rs.getString("album_name");
                song.setAlbumName(
                        albumName == null ? "Single" : albumName
                );

                songs.add(song);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    /* =====================================================
                        VIEW SONGS BY ALBUM
       ===================================================== */
    public List<Song> getSongsByAlbumId(int albumId) {

        List<Song> songs = new ArrayList<>();

        String sql =
                "SELECT song_id, title, play_count FROM songs WHERE album_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, albumId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setPlayCount(rs.getInt("play_count"));
                songs.add(song);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs;
    }

    /* =====================================================
          UPDATE SONG (NO INSERT – FIXES DUPLICATE ISSUE)
       ===================================================== */
    public boolean updateSong(int songId, int artistId,
                              String title, String genre, int duration, String filePath) {

        String sql =
                "UPDATE songs SET title=?, genre=?, duration=?, filePath=? " +
                        "WHERE song_id=? AND artist_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, genre);
            ps.setInt(3, duration);
            ps.setString(4, filePath);
            ps.setInt(5, songId);
            ps.setInt(6, artistId);
            

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =====================================================
            REMOVE SONG FROM ALBUM (SET album_id = NULL)
       ===================================================== */
    public void removeSongFromAlbum(int songId, int artistId) {

        String sql =
                "UPDATE songs SET album_id = NULL WHERE song_id = ? AND artist_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, songId);
            ps.setInt(2, artistId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
             DELETE SONG (SAFE – ARTIST OWNERSHIP CHECK)
       ===================================================== */
    public void deleteSong(int songId, int artistId) {

        String sql =
                "DELETE FROM songs WHERE song_id = ? AND artist_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, songId);
            ps.setInt(2, artistId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<String> searchSongsByKeyword(String keyword) {

        List<String> results = new ArrayList<>();

        String sql = "SELECT s.title, ar.artist_name FROM songs s JOIN artists ar ON s.artist_id = ar.artist_id WHERE s.title LIKE ? OR s.genre LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String row =
                        rs.getString("title") +
                                " | Artist: " +
                                rs.getString("artist_name");
                results.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
    public List<String> searchSongsByArtist(String artistName) {

        List<String> results = new ArrayList<>();

        String sql = "SELECT s.title, s.genre FROM songs s JOIN artists ar ON s.artist_id = ar.artist_idWHERE ar.artist_name LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + artistName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String row =
                        rs.getString("title") +
                                " | Genre: " +
                                rs.getString("genre");
                results.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
    public void incrementPlayCount(int songId) {

        String sql = "UPDATE songs SET play_count = play_count + 1 WHERE song_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, songId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Song> searchSongsForPlayback(String keyword) {

        List<Song> songs = new ArrayList<>();

        String sql = "SELECT s.song_id, s.title, ar.artist_name FROM songs s JOIN artists ar ON s.artist_id = ar.artist_id WHERE s.title LIKE ? OR ar.artist_name LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setArtistName(rs.getString("artist_name")); // add field if not present
                songs.add(song);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return songs;
    }

    /* =====================================================
                       getSongForPlayback
       ===================================================== */
    
    public Song getSongForPlayback(int songId) {

        String sql = "SELECT song_id, title, filePath FROM songs WHERE song_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, songId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setFilePath(rs.getString("filePath"));
                return song;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
}
