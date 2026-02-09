package com.revplay.main;

import java.util.Scanner;
import com.revplay.model.UserType;
import com.revplay.service.*;

public class RevPlayApp {

	private static UserType askUserType(Scanner sc) {

	    while (true) {
	        System.out.println("Select type:");
	        System.out.println("1. User");
	        System.out.println("2. Artist");
	        System.out.print("Choose option: ");

	        if (!sc.hasNextInt()) {
	            System.out.println("❌ Please enter a number");
	            sc.nextLine();
	            continue;
	        }

	        int choice = sc.nextInt();
	        sc.nextLine();

	        if (choice == 1) {
	            return UserType.USER;
	        }

	        if (choice == 2) {
	            return UserType.ARTIST;
	        }

	        System.out.println("❌ Invalid choice.");
	    }
	}
    
    private static int getIntInput(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("❌ Please enter a number");
            sc.nextLine();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        SearchService searchService = new SearchService();
        PlayerService playerService = new PlayerService();
        PlaylistService playlistService = new PlaylistService();
        HistoryService historyService = new HistoryService();

        // Artist services
        MusicService musicService = new MusicService();
        ArtistAlbumService albumService = new ArtistAlbumService();
        ArtistProfileService profileService = new ArtistProfileService();
        ArtistSongService songService = new ArtistSongService();

        boolean isLoggedIn = false;
        UserType loggedInType = null;

        Integer loggedInUserId = null;
        Integer loggedInArtistId = null;

        while (true) {

            /* =====================================================
                                BEFORE LOGIN
               ===================================================== */
        	
            if (!isLoggedIn) {

                System.out.println("\n█▀█ █▀▀ █ █ █▀█ █   ▄▀█ █▄█\r\n"
                		+ "█▀▄ ██▄ ▀▄▀ █▀▀ █▄▄ █▀█  █");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Forgot Password");
                System.out.println("4. Exit");
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
                        UserType regType = askUserType(sc);

                        System.out.print("Name: ");
                        String name = sc.nextLine();

                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        System.out.print("Password: ");
                        String password = sc.nextLine();

                        System.out.println("Choose a Security Question: ");
                        System.out.println("1. What is your favorite color?");
                        System.out.println("2. What is your first school name?");
                        System.out.println("3. What is your pet's name?");

                        int securityChoice;

                        while (true) {
                            securityChoice = getIntInput(sc);

                            if (securityChoice >= 1 && securityChoice <= 3) {
                                break;
                            }
                            System.out.println("❌ Invalid choice.");
                        }

                        String securityQuestion;

                        if (securityChoice == 1) {
                            securityQuestion = "What is your favorite color?";
                        } else if (securityChoice == 2) {
                            securityQuestion = "What is your first school name?";
                        } else {
                            securityQuestion = "What is your pet's name?";
                        }

                        String secA;
                        while (true) {
                            System.out.print("Security Answer: ");
                            secA = sc.nextLine().trim();

                            if (!secA.isEmpty()) {
                                break;
                            }
                            System.out.println("❌ Security Answer cannot be empty");
                        }



                        String bio = null;
                        String genre = null;
                        String instagram_link = null;
                        String youtube_link = null;

                        if (regType == UserType.ARTIST) {
                            System.out.print("Bio: ");
                            bio = sc.nextLine();
                            System.out.print("Genre: ");
                            genre = sc.nextLine();
                            System.out.println("Instagram Link: ");
                            instagram_link = sc.nextLine();
                            System.out.println("YouTube Link: ");
                            youtube_link = sc.nextLine();
                        }

                        authService.register(regType, name, email, password, securityQuestion, secA, bio, genre, instagram_link, youtube_link);
                    	break;

                    case 2:
                        UserType loginType = askUserType(sc);

                        System.out.print("Email: ");
                        email = sc.nextLine();

                        System.out.print("Password: ");
                        password = sc.nextLine();

                        Integer id = authService.login(loginType, email, password);

                        if (id != null) {
                            isLoggedIn = true;
                            loggedInType = loginType;

                            if (loginType == UserType.USER) {
                                loggedInUserId = id;
                                System.out.println("👋 Welcome to RevPlay!");
                            } else {
                                loggedInArtistId = id;
                                System.out.println("👋 Welcome, Artist!");
                            }
                        } else {
                            System.out.println("❌ Invalid email or password");
                        }

                        break;

                    case 3:
                        System.out.print("Email: ");
                        String fpEmail = sc.nextLine();

                        System.out.print("Security Answer: ");
                        String answer = sc.nextLine();

                        System.out.print("New Password: ");
                        String newPass = sc.nextLine();

                        userService.forgotPassword(fpEmail, answer, newPass);
                        break;

                    case 4:
                        System.out.println("Thank you for using RevPlay!");
                        System.exit(0);


                    default:
                        System.out.println("❌ Invalid choice!");
                }
            }

            /* =====================================================
                                   AFTER LOGIN
               ===================================================== */
            
            else {

                /* -------- USER MENU -------- */
                if (loggedInType == UserType.USER) {

                    System.out.println("\n===== 🎧 REVPLAY (USER) 🎶 =====");
                    System.out.println("1. Search Song by Keyword");
                    System.out.println("2. Browse Albums by Artist");
                    System.out.println("3. Play Song");
                    System.out.println("4. Create Playlist");
                    System.out.println("5. Manage My Playlists");
                    System.out.println("6. View Listening History");
                    System.out.println("7. Logout");
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
                            System.out.print("Enter keyword: ");
                            String keyword = sc.nextLine();
                            searchService.searchSongByKeyword(keyword);
                            break;

                        case 2:
                            System.out.print("Enter artist name: ");
                            String artistName = sc.nextLine();
                            searchService.browseAlbumsByArtist(artistName);
                            break;

                        case 3:
                            playerService.startPlayer(loggedInUserId, sc);
                            break;

                        case 4:
                            playlistService.createPlaylist(loggedInUserId, sc);
                            break;

                        case 5:
                            playlistService.managePlaylists(loggedInUserId, sc);
                            break;

                        case 6:
                            historyService.viewListeningHistory(loggedInUserId);
                            break;
                            
                        case 7:
                            isLoggedIn = false;
                            loggedInType = null;
                            loggedInUserId = null;
                            System.out.println("✅ Logged out successfully!");
                            break;

                        default:
                            System.out.println("❌ Invalid choice!");
                    }
                }

                /* =====================================================
                                    ARTIST MENU
                   ===================================================== */
                
                else {

                    System.out.println("\n===== 🎶 REVPLAY (ARTIST) 🎧 =====");
                    System.out.println("1. Create Album");
                    System.out.println("2. Upload Song");
                    System.out.println("3. View My Songs");
                    System.out.println("4. View My Albums");
                    System.out.println("5. See / Edit Profile");
                    System.out.println("6. Logout");
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
                            System.out.print("Album Name: ");
                            String albumName = sc.nextLine();

                            System.out.print("Release Date (yyyy-mm-dd): ");
                            String dateStr = sc.nextLine();

                            System.out.print("Genre: ");
                            String albumGenre = sc.nextLine();

                            albumService.createAlbum(
                                    loggedInArtistId,
                                    albumName,
                                    java.sql.Date.valueOf(dateStr),
                                    albumGenre
                            );
                            break;

                        case 2:
                            System.out.print("Song Title: ");
                            String title = sc.nextLine();

                            System.out.print("Genre: ");
                            String songGenre = sc.nextLine();

                            System.out.print("Duration (seconds): ");
                            int duration = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Release Date (yyyy-mm-dd): ");
                            String songDate = sc.nextLine();

                            System.out.print("Album ID (0 if none): ");
                            int albumId = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Song File Path: ");
                            String filePath = sc.nextLine();

                            musicService.uploadSong(
                                loggedInArtistId,
                                (albumId == 0) ? null : albumId,
                                title,
                                songGenre,
                                duration,
                                java.sql.Date.valueOf(songDate),
                                filePath
                            );
                            break;


                        case 3:
                            songService.viewMySongs(loggedInArtistId, sc);
                            break;

                        case 4:
                            albumService.viewMyAlbums(loggedInArtistId);
                            break;

                        case 5:
                            profileService.viewAndEditProfile(loggedInArtistId, sc);
                            break;

                        case 6:
                            isLoggedIn = false;
                            loggedInType = null;
                            loggedInArtistId = null;
                            System.out.println("✅ Logged out successfully!");
                            break;

                        default:
                            System.out.println("❌ Invalid choice!");
                    }
                }
            }
        }
    }
}
