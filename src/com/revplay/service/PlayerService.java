package com.revplay.service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Scanner;

import javazoom.jl.player.Player;

import com.revplay.dao.HistoryDAO;
import com.revplay.dao.SongDAO;
import com.revplay.model.Song;

public class PlayerService {

    private SongDAO songDAO = new SongDAO();
    private HistoryDAO historyDAO = new HistoryDAO();

    private Player player;
    private Thread playThread;
    private boolean isRepeat = false;

    /* =====================================================
                       SEARCH & SELECT
       ===================================================== */
    public void startPlayer(int userId, Scanner sc) {

        System.out.print("Enter song name or artist keyword: ");
        String keyword = sc.nextLine();

        List<Song> songs = songDAO.searchSongsForPlayback(keyword);

        if (songs.isEmpty()) {
            System.out.println("❌ No songs found.");
            return;
        }

        System.out.println("\n🎧 Available Songs:");
        for (Song s : songs) {
            System.out.println(
                    s.getSongId() + " | " +
                    s.getTitle() + " | " +
                    s.getArtistName()
            );
        }

        System.out.print("\nEnter Song ID to play: ");
        if (!sc.hasNextInt()) {
            System.out.println("❌ Please enter a number only");
            sc.nextLine();
            return;
        }
        int songId = sc.nextInt();
        sc.nextLine();

        Song song = songDAO.getSongForPlayback(songId);
        if (song == null) {
            System.out.println("❌ Invalid song ID.");
            return;
        }

        playSong(userId, song, sc);
    }

    /* =====================================================
                         PLAYER MENU
       ===================================================== */
    private void playSong(int userId, Song song, Scanner sc) {

        boolean playing = true;

        while (playing) {

            System.out.println("\n1. Play");
            System.out.println("2. Stop");
            System.out.println("3. Repeat");
            System.out.println("4. Exit Player");
            System.out.print("Choose option: ");

            if (!sc.hasNextInt()) {
                System.out.println("❌ Invalid choice!");
                sc.nextLine();
                continue;
            }
            int choice = sc.nextInt();
            sc.nextLine();



            switch (choice) {

                case 1:
                    startPlayback(userId, song);
                    break;

                case 2:
                    stopPlayback();
                    break;

                case 3:
                	isRepeat = true;
                    startPlayback(userId, song);
                    break;

                case 4:
                    stopPlayback();
                    playing = false;
                    break;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }


    /* =====================================================
                         REAL MP3 PLAY
       ===================================================== */
    private void startPlayback(int userId, Song song) {

        try {
            // Stop current playback if running
            if (player != null) {
                player.close();
            }

            // Print correct message based on user intent
            if (isRepeat) {
                System.out.println("🔁 Playback Repeated");
                isRepeat = false;
            }

            FileInputStream fis = new FileInputStream(song.getFilePath());
            player = new Player(fis);

            playThread = new Thread(() -> {
                try {
                    player.play();
                } catch (Exception ignored) {}
            });

            playThread.start();

            songDAO.incrementPlayCount(song.getSongId());
            if (userId > 0) {
                historyDAO.addHistory(userId, song.getSongId());
            }

            System.out.println("▶ Now Playing: " + song.getTitle());

        } catch (Exception e) {
            System.out.println("❌ Unable to play song.");
            e.printStackTrace();
        }
    }


    /* =====================================================
                          STOP
       ===================================================== */
    private void stopPlayback() {
        try {
            if (player != null) {
                player.close();
                System.out.println("⏹ Playback stopped");
                player = null;
            }
            if (playThread != null) {
                playThread.interrupt();
                playThread = null;
            }
        } catch (Exception ignored) {}
    }


}
