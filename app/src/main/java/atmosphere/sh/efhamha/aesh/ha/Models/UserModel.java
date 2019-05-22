package atmosphere.sh.efhamha.aesh.ha.Models;

public class UserModel {
    private String userId, userName, email, imageUrl;

    public UserModel(String userId, String userName, String email, String imageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.imageUrl = imageUrl;
    }
    public UserModel()
    {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
