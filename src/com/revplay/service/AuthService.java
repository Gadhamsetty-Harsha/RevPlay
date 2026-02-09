package com.revplay.service;

import com.revplay.dao.UserDAO;
import com.revplay.dao.ArtistDAO;
import com.revplay.model.Artist;
import com.revplay.model.UserType;
import com.revplay.util.PasswordUtil;
import com.revplay.util.ValidationUtil;

public class AuthService {

    private UserDAO userDAO = new UserDAO();
    private ArtistDAO artistDAO = new ArtistDAO();

    public void register(UserType type, String name, String email,
                         String password, String securityQuestion, String securityAnswer, String bio, String genre, String instagram_link, String youtube_link) {

    	if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("❌ Invalid email format!");
            return;
        }

        if (!ValidationUtil.isStrongPassword(password)) {
            System.out.println("❌ Weak password!");
            return;
        }
    	
    	String hashedPassword = PasswordUtil.hashPassword(password);
    	
        if (type == UserType.USER) {
            userDAO.registerUser(name, email, hashedPassword, securityQuestion, securityAnswer);
        } else {
            Artist artist = new Artist();
            artist.setArtistName(name);
            artist.setEmail(email);
            artist.setPassword(hashedPassword);
            artist.setSecurityQuestion(securityQuestion);
            artist.setSecurityAnswer(securityAnswer);
            artist.setBio(bio);
            artist.setGenre(genre);
            artist.setInstagramLink(instagram_link);
            artist.setYoutubeLink(youtube_link);
            
        

            artistDAO.registerArtist(artist);
        }
    }

    public Integer login(UserType type, String email, String password) {
    	
    	String hashedPassword = PasswordUtil.hashPassword(password);

        if (type == UserType.USER) {
            return userDAO.loginUser(email, hashedPassword);
        } else {
            return artistDAO.loginArtist(email, hashedPassword);
        }
    }
}
