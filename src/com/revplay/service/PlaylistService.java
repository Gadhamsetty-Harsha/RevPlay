package com.revplay.service;

import java.util.List;
import java.util.Scanner;
import com.revplay.dao.PlaylistDAO;

public class PlaylistService {

    private PlaylistDAO dao = new PlaylistDAO();

    public void createPlaylist(int userId, Scanner sc) {

        System.out.print("Playlist name: ");
        String name = sc.nextLine();

        System.out.print("Description: ");
        String desc = sc.nextLine();

        System.out.print("Public? (yes/no): ");
        boolean isPublic = sc.nextLine().equalsIgnoreCase("yes");

        dao.createPlaylist(userId, name, desc, isPublic);
    }

    public void managePlaylists(int userId, Scanner sc) {

    	List<String> playlists = dao.getUserPlaylists(userId);
    	List<Integer> playlistIds = dao.getUserPlaylistIds(userId);

        if (playlists.isEmpty()) {
            System.out.println("❌ No playlists found.");
            return;
        }

        while (true) {

            System.out.println("\n🎶 Your Playlists:");
            playlists.forEach(System.out::println);

            System.out.print("Enter playlist ID: ");
            if (!sc.hasNextInt()) {
                System.out.println("❌ Please enter a number");
                sc.nextLine();
                continue;
            }

            int pid = sc.nextInt();
            sc.nextLine();

            if (!playlistIds.contains(pid)) {
                System.out.println("❌ Invalid playlist ID.");
                continue;
            }

            System.out.println("1. Add Song");
            System.out.println("2. Remove Song");
            System.out.println("3. Show Songs");
            System.out.print("Choose option: ");

            if (!sc.hasNextInt()) {
                System.out.println("❌ Invalid choice!");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1 || choice == 2) {

                System.out.print("Enter Song ID: ");
                if (!sc.hasNextInt()) {
                    System.out.println("❌ Please enter a number");
                    sc.nextLine();
                    continue;
                }

                int songId = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    dao.addSongToPlaylist(pid, songId);
                } else {
                    dao.removeSongFromPlaylist(pid, songId);
                }

            } else if (choice == 3) {

                List<String> songs = dao.getSongsInPlaylist(pid);

                if (songs.isEmpty()) {
                    System.out.println("❌ No songs in this playlist.");
                } else {
                    System.out.println("\n🎵 Songs in Playlist:");
                    songs.forEach(System.out::println);
                }

            } else {
                System.out.println("❌ Invalid choice!");
            }


            return;
        }
    }
}