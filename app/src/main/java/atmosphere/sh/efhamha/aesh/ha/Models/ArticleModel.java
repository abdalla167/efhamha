package atmosphere.sh.efhamha.aesh.ha.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;


public class ArticleModel implements Parcelable {
    private String image_url, title, content, source, arch_id;




    private ArrayList user_likes;
    private ArrayList user_share;
    private ArrayList user_view;
    //private ArrayList user_comments;
    private HashMap user_comments;


    public ArticleModel(String image_url, String title, String content, String source, String arch_id, ArrayList<Integer> user_likes, ArrayList<Integer> user_share, ArrayList<Integer> user_view,HashMap<Integer,ArrayList<String>> user_comments){
        this.image_url = image_url;
        this.title = title;
        this.content = content;
        this.source = source;
        this.arch_id = arch_id;
        this.user_likes = user_likes;
        this.user_share = user_share;
        this.user_view = user_view;
        this.user_comments = user_comments;
    }

    protected ArticleModel(Parcel in) {
        image_url = in.readString();
        title = in.readString();
        content = in.readString();
        source = in.readString();
        arch_id = in.readString();
        user_likes = in.readArrayList(null);
        user_share = in.readArrayList(null);
        user_view = in.readArrayList(null);
        user_comments =in.readHashMap(HashMap.class.getClassLoader());




    }

    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }

    };

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSource() {
        return source;
    }

    public String getArch_id() {
        return arch_id;
    }

    public ArrayList<Integer> getUser_likes() {
        return user_likes;
    }

    public ArrayList<Integer> getUser_share() {
        return user_share;
    }

    public ArrayList<Integer> getUser_view() {
        return user_view;
    }


    public void setUser_comments(HashMap<Integer, ArrayList<String>> user_comments) {
        this.user_comments = user_comments;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap<Integer, ArrayList<String>> getUser_comments() {
        return user_comments;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setArch_id(String arch_id) {
        this.arch_id = arch_id;
    }

    public void setUser_likes(ArrayList<Integer> user_likes) {
        this.user_likes = user_likes;
    }

    public void setUser_share(ArrayList<Integer> user_share) {
        this.user_share = user_share;
    }

    public void setUser_view(ArrayList<Integer> user_view) {
        this.user_view = user_view;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image_url);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(source);
        dest.writeString(arch_id);
        dest.writeList(user_likes);
        dest.writeList(user_view);
        dest.writeList(user_share);

        dest.writeMap(user_comments);

    }
}
