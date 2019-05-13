package atmosphere.sh.efhamha.aesh.ha.Models;

public class ArticleModel
{
    String title,content;
    int imageurl;

    public ArticleModel() {
    }

    public ArticleModel(String title, String content, int imageurl) {
        this.title = title;
        this.content = content;
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImageurl() {
        return imageurl;
    }

    public void setImageurl(int imageurl) {
        this.imageurl = imageurl;
    }
}
