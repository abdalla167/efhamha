package atmosphere.sh.efhamha.aesh.ha.Models;

public class NotificationModel
{
    String article_image,article_title,article_day,article_month,article_year;

    public NotificationModel() {
    }

    public NotificationModel(String article_image, String article_title, String article_day, String article_month, String article_year) {
        this.article_image = article_image;
        this.article_title = article_title;
        this.article_day = article_day;
        this.article_month = article_month;
        this.article_year = article_year;
    }

    public String getArticle_image() {
        return article_image;
    }

    public void setArticle_image(String article_image) {
        this.article_image = article_image;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_day() {
        return article_day;
    }

    public void setArticle_day(String article_day) {
        this.article_day = article_day;
    }

    public String getArticle_month() {
        return article_month;
    }

    public void setArticle_month(String article_month) {
        this.article_month = article_month;
    }

    public String getArticle_year() {
        return article_year;
    }

    public void setArticle_year(String article_year) {
        this.article_year = article_year;
    }
}
