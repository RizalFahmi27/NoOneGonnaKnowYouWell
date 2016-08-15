package id.developer.tanitionary;

/**
 * Created by Naufal on 8/13/2016.
 */
public class ObjectDiscussionLoaded{
    String title, time, desc, urlPic;
    Integer totalLike, totalComment;
    ObjectSender sender;

    ObjectDiscussionLoaded(String title, String time, String desc, String urlPic, Integer totalLike, Integer totalComment, ObjectSender sender){
        this.title = title;
        this.time = time;
        this.desc = desc;
        this.urlPic = urlPic;
        this.totalLike = totalLike;
        this.totalComment = totalComment;
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public Integer getTotalComment() {
        return totalComment;
    }

    public Integer getTotalLike() {
        return totalLike;
    }

    public ObjectSender getSender() {
        return sender;
    }

    public String getDesc() {
        return desc;
    }

    public String getUrlPic() {
        return urlPic;
    }
}

class ObjectSender{
    String name, urlPhoto;

    ObjectSender(String name, String urlPhoto){
        this.name = name;
        this.urlPhoto = urlPhoto;
    }

    public String getName() {
        return name;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }
}
