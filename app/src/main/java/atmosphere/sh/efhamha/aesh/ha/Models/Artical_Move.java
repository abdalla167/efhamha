package atmosphere.sh.efhamha.aesh.ha.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class  Artical_Move  implements Serializable {


private String title,content,source,article_time,article_day,article_month,article_year,wordfile,caption;
        int type;
private ArrayList<String> image_url;

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

public String getSource() {
        return source;
        }

public void setSource(String source) {
        this.source = source;
        }

public String getArticle_time() {
        return article_time;
        }

public void setArticle_time(String article_time) {
        this.article_time = article_time;
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

public String getWordfile() {
        return wordfile;
        }

public void setWordfile(String wordfile) {
        this.wordfile = wordfile;
        }

public int getType() {
        return type;
        }

public void setType(int type) {
        this.type = type;
        }


public ArrayList<String> getImage_url() {
        return image_url;
        }
//
public void setImage_url(ArrayList<String> image_url) {
        this.image_url = image_url;
        }


///
public Artical_Move(ArrayList<String> image_url, String title, String content, String source, String article_time, String article_day, String article_month, String article_year ,String wordfile, int type,String caption) {
        this.image_url = image_url;
        this.title = title;
        this.content = content;
        this.source = source;
        this.article_time = article_time;
        this.article_day = article_day;
        this.article_month = article_month;
        this.article_year = article_year;
        this.type = type;
        this.wordfile=wordfile;
this.caption=caption;
}
        }
