package com.revplay.model;

public class Artist {

    private int artistId;
    private String artistName;
    private String email;
    private String password;
    private String SecurityQuestion;
    private String SecurityAnswer;
    public String getSecurityQuestion() {
		return SecurityQuestion;
	}
	public void setSecurityQuestion(String securityQuestion) {
		SecurityQuestion = securityQuestion;
	}
	public String getSecurityAnswer() {
		return SecurityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		SecurityAnswer = securityAnswer;
	}
	private String bio;
    private String genre;
    private String instagramLink;
    private String youtubeLink;

    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getInstagramLink() { return instagramLink; }
    public void setInstagramLink(String instagramLink) { this.instagramLink = instagramLink; }

    public String getYoutubeLink() { return youtubeLink; }
    public void setYoutubeLink(String youtubeLink) { this.youtubeLink = youtubeLink; }
}
