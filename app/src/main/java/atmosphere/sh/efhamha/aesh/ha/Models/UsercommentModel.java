package atmosphere.sh.efhamha.aesh.ha.Models;

public class UsercommentModel {

    private String image_url, username, contentcomment;

    public UsercommentModel(String image_url, String username, String contentcomment) {
        this.image_url = image_url;
        this.username = username;
        this.contentcomment = contentcomment;
    }

    public UsercommentModel() {
    }

    public String getImage_url() {
        return image_url;
    }

    public String getUsername() {
        return username;
    }

    public String getContentcomment() {
        return contentcomment;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContentcomment(String contentcomment) {
        this.contentcomment = contentcomment;
    }
}
