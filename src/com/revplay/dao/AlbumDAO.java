package com.revplay.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revplay.model.Album;
import com.revplay.util.DBConnection;

public class AlbumDAO {

    /* =====================================================
                   CREATE ALBUM (ARTIST SIDE)
       ===================================================== */
	
    public boolean createAlbum(int artistId,
                               String albumName,
                               Date releaseDate,
                               String genre) {

        String sql =
                "INSERT INTO albums (artist_id, album_name, release_date, genre) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ps.setString(2, albumName);
            ps.setDate(3, releaseDate);
            ps.setString(4, genre);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =====================================================
           VIEW ALBUMS BY ARTIST ID (ARTIST MANAGEMENT)
       ===================================================== */
    public List<Album> getAlbumsByArtistId(int artistId) {

        List<Album> albums = new ArrayList<>();

        String sql =
                "SELECT album_id, album_name, release_date, genre " +
                        "FROM albums WHERE artist_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Album album = new Album();
                album.setAlbumId(rs.getInt("album_id"));
                album.setAlbumName(rs.getString("album_name"));
                album.setReleaseDate(rs.getDate("release_date"));
                album.setGenre(rs.getString("genre"));
                albums.add(album);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return albums;
    }

    /* =====================================================
                UPDATE ALBUM INFO (ARTIST SIDE)
       ===================================================== */
    public boolean updateAlbum(int albumId,
                               int artistId,
                               String albumName,
                               String genre) {

        String sql =
                "UPDATE albums SET album_name=?, genre=? " +
                        "WHERE album_id=? AND artist_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, albumName);
            ps.setString(2, genre);
            ps.setInt(3, albumId);
            ps.setInt(4, artistId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =====================================================
             DELETE ALBUM (SAFE – SONGS BECOME SINGLES)
       ===================================================== */
    public boolean deleteAlbum(int albumId, int artistId) {

        try (Connection con = DBConnection.getConnection()) {

            // Step 1: Detach songs from album (album_id -> NULL)
            String detachSql =
                    "UPDATE songs SET album_id = NULL WHERE album_id = ?";
            try (PreparedStatement ps = con.prepareStatement(detachSql)) {
                ps.setInt(1, albumId);
                ps.executeUpdate();
            }

            // Step 2: Delete album
            String deleteSql =
                    "DELETE FROM albums WHERE album_id = ? AND artist_id = ?";
            try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
                ps.setInt(1, albumId);
                ps.setInt(2, artistId);
                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =====================================================
         SEARCH / BROWSE ALBUMS BY ARTIST NAME (USER SIDE)
       ===================================================== */
    public List<String> getAlbumsByArtistName(String artistName) {

        List<String> albums = new ArrayList<>();

        String sql = "SELECT al.album_name, al.genre, al.release_date FROM albums al JOIN artists ar ON al.artist_id = ar.artist_id WHERE ar.artist_name LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + artistName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String row =
                		" Song Name: " + rs.getString("album_name") +
                        " | Genre: " + rs.getString("genre") +
                        " | Release Date: " + rs.getDate("release_date");
                albums.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return albums;
    }
}
