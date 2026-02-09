package com.revplay.service;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import com.revplay.dao.AlbumDAO;
import com.revplay.dao.SongDAO;
import com.revplay.model.Album;
import com.revplay.model.Song;

public class ArtistAlbumService {

    private AlbumDAO albumDAO = new AlbumDAO();
    private SongDAO songDAO = new SongDAO();

    /* =====================================================
              CREATE ALBUM (CALLED FROM RevPlayApp)
       ===================================================== */
    public void createAlbum(int artistId,
                            String albumName,
                            Date releaseDate,
                            String genre) {

        boolean success = albumDAO.createAlbum(
                artistId, albumName, releaseDate, genre);

        if (success) {
            System.out.println("✅ Album created successfully!");
        } else {
            System.out.println("❌ Album creation failed!");
        }
    }

    /* =====================================================
                     VIEW & MANAGE ALBUMS
       ===================================================== */
    @SuppressWarnings("resource")
	public void viewMyAlbums(int artistId) {

        Scanner sc = new Scanner(System.in);

        List<Album> albums = albumDAO.getAlbumsByArtistId(artistId);

        if (albums.isEmpty()) {
            System.out.println("❌ No albums created yet.");
            return;
        }

        System.out.println("\n===== MY ALBUMS =====");
        for (Album a : albums) {
            System.out.println(
                    a.getAlbumId() + " | " +
                            a.getAlbumName() + " | " +
                            a.getGenre()
            );
        }

        System.out.print("\nEnter Album ID to manage (0 to Back): ");
        if (!sc.hasNextInt()) {
            System.out.println("❌ Invalid choice!");
            sc.nextLine();
            return;
        }
        int albumId = sc.nextInt();
        sc.nextLine();

        if (albumId != 0) {
            manageAlbum(albumId, artistId, sc);
        }
    }

    /* =====================================================
                     MANAGE SINGLE ALBUM
       ===================================================== */
    private void manageAlbum(int albumId, int artistId, Scanner sc) {

        while (true) {

            System.out.println("\n===== MANAGE ALBUM =====");
            System.out.println("1. View Songs in Album");
            System.out.println("2. Update Album Info");
            System.out.println("3. Remove Song from Album");
            System.out.println("4. Delete Album");
            System.out.println("5. Back");
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
                    List<Song> songs =
                            songDAO.getSongsByAlbumId(albumId);

                    if (songs.isEmpty()) {
                        System.out.println("(No songs in this album)");
                    } else {
                        for (Song s : songs) {
                            System.out.println(
                                    s.getSongId() + " | " + s.getTitle());
                        }
                    }
                    break;

                case 2:
                    System.out.print("New Album Name: ");
                    String name = sc.nextLine();

                    System.out.print("New Genre: ");
                    String genre = sc.nextLine();

                    boolean updated =
                            albumDAO.updateAlbum(
                                    albumId, artistId, name, genre);

                    if (updated) {
                        System.out.println("✅ Album updated!");
                    } else {
                        System.out.println("❌ Update failed!");
                    }
                    break;

                case 3:
                    System.out.print("Enter Song ID to remove: ");
                    int songId = sc.nextInt();
                    sc.nextLine();

                    songDAO.removeSongFromAlbum(songId, artistId);
                    System.out.println("✅ Song removed from album.");
                    break;

                case 4:
                    boolean deleted =
                            albumDAO.deleteAlbum(albumId, artistId);

                    if (deleted) {
                        System.out.println(
                                "🗑 Album deleted (songs preserved as singles).");
                    } else {
                        System.out.println("❌ Delete failed!");
                    }
                    return;

                case 5:
                    return;

                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }
}
