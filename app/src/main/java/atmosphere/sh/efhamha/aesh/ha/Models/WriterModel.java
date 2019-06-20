package atmosphere.sh.efhamha.aesh.ha.Models;

public class WriterModel {

    String id,name,imgurl;

    public WriterModel(String id, String name ,String imgurl) {
        this.id = id;
        this.name = name;
        this.imgurl=imgurl;
    }

    public WriterModel() {
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    }
