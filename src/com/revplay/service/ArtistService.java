package com.revplay.service;

import com.revplay.dao.ArtistDAO;
import com.revplay.model.Artist;
import com.revplay.util.ValidationUtil;

public class ArtistService {

    private ArtistDAO artistDAO = new ArtistDAO();

    /* =====================================================
                          REGISTER
       ===================================================== */
    
    public void registerArtist(String name,
                               String email,
                               String password,
                               String bio,
                               String genre, 
                               String instagram_link,
                               String youtube_link) {

        email = email.trim().toLowerCase();

        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("❌ Invalid email format!");
            return;
        }

        if (!ValidationUtil.isStrongPassword(password)) {
            System.out.println("❌ Weak password!");
            return;
        }

        Artist artist = new Artist();
        artist.setArtistName(name);
        artist.setEmail(email);
        artist.setPassword(password);
        artist.setBio(bio);
        artist.setGenre(genre);
        artist.setInstagramLink(instagram_link);
        artist.setYoutubeLink(youtube_link);

        boolean success = artistDAO.registerArtist(artist);

        if (success) {
            System.out.println("✅ Artist registered successfully!");
        } else {
            System.out.println("❌ Artist registration failed!");
        }
    }

    /* =====================================================
                             LOGIN 
       ===================================================== */
    
    public Integer loginArtist(String email, String password) {

        Integer artistId = artistDAO.loginArtist(email, password);

        if (artistId != null) {
            System.out.println("✅ Artist login successful!");
            return artistId;
        } else {
            System.out.println("❌ Invalid artist credentials!");
            return null;
        }
    }
}
