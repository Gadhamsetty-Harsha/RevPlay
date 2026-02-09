package com.revplay.service;

import java.util.Scanner;
import com.revplay.dao.ArtistDAO;
import com.revplay.model.Artist;

public class ArtistProfileService {

    private ArtistDAO artistDAO = new ArtistDAO();

    public void viewAndEditProfile(int artistId, Scanner sc) {

        while (true) {

            Artist artist = artistDAO.getArtistById(artistId);

            if (artist == null) {
                System.out.println("❌ Artist not found!");
                return;
            }

            System.out.println("\n===== ARTIST PROFILE =====");
            System.out.println("Name      : " + artist.getArtistName());
            System.out.println("Bio       : " + artist.getBio());
            System.out.println("Genre     : " + artist.getGenre());
            System.out.println("Instagram : " + artist.getInstagramLink());
            System.out.println("YouTube   : " + artist.getYoutubeLink());

            System.out.println("\n1. Update Bio");
            System.out.println("2. Update Genre");
            System.out.println("3. Update Instagram Link");
            System.out.println("4. Update YouTube Link");
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
                    System.out.print("Enter new bio: ");
                    artistDAO.updateArtistProfile(
                            artistId,
                            sc.nextLine(),
                            artist.getGenre(),
                            artist.getInstagramLink(),
                            artist.getYoutubeLink()
                    );
                    break;

                case 2:
                    System.out.print("Enter new genre: ");
                    artistDAO.updateArtistProfile(
                            artistId,
                            artist.getBio(),
                            sc.nextLine(),
                            artist.getInstagramLink(),
                            artist.getYoutubeLink()
                    );
                    break;

                case 3:
                    System.out.print("Enter new Instagram link: ");
                    artistDAO.updateArtistProfile(
                            artistId,
                            artist.getBio(),
                            artist.getGenre(),
                            sc.nextLine(),
                            artist.getYoutubeLink()
                    );
                    break;

                case 4:
                    System.out.print("Enter new YouTube link: ");
                    artistDAO.updateArtistProfile(
                            artistId,
                            artist.getBio(),
                            artist.getGenre(),
                            artist.getInstagramLink(),
                            sc.nextLine()
                    );
                    break;

                case 5:
                    return;

                default:
                    System.out.println("❌ Invalid choice");
            }
        }
    }
}
