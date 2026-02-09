package com.revplay.service;

import java.sql.Date;

import com.revplay.dao.SongDAO;

public class MusicService {

    private SongDAO songDAO = new SongDAO();

    // UPLOAD SONG (CORRECT PARAMETER PASSING)
    public void uploadSong(int artistId,
                           Integer albumId,
                           String title,
                           String genre,
                           int duration,
                           Date releaseDate,
                           String filePath) {

        boolean success = songDAO.uploadSong(
                artistId,
                albumId,
                title,
                genre,
                duration,
                releaseDate,
                filePath
        );

        if (success) {
            System.out.println("✅ Song uploaded successfully!");
        } else {
            System.out.println("❌ Song upload failed!");
        }
    }
}
