package atmosphere.sh.efhamha.aesh.ha.Models;

public class CommentModel {

    private String image_url,username,contentcomment;

    public CommentModel() {
    }

    public CommentModel(String image_url, String username, String contentcomment) {
        this.image_url = image_url;
        this.username = username;
        this.contentcomment = contentcomment;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContentcomment() {
        return contentcomment;
    }

    public void setContentcomment(String contentcomment) {
        this.contentcomment = contentcomment;
    }
}
