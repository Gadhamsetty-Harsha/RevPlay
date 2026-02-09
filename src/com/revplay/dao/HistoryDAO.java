package com.revplay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.revplay.util.DBConnection;

public class HistoryDAO {

    public void addHistory(int userId, int songId) {

        String sql =
                "INSERT INTO listening_history (user_id, song_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, songId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getUserHistory(int userId) {

        List<String> history = new ArrayList<>();

        String sql =
            "SELECT s.title, ar.artist_name, h.listened_at " +
            "FROM listening_history h " +
            "JOIN songs s ON h.song_id = s.song_id " +
            "JOIN artists ar ON s.artist_id = ar.artist_id " +
            "WHERE h.user_id = ? " +
            "ORDER BY h.listened_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                history.add(
                    rs.getString("title") +
                    " | " +
                    rs.getString("artist_name") +
                    " | " +
                    rs.getTimestamp("listened_at")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return history;
    }
}
