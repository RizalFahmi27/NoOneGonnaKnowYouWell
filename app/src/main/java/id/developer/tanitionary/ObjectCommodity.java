package id.developer.tanitionary;

/**
 * Created by Naufal on 8/29/2016.
 */
public class ObjectCommodity implements Comparable<ObjectCommodity> {

    String name, urlPic, recentPrice, yesterdayPrice;

    public ObjectCommodity(String name, String urlPic, String recentPrice, String yesterdayPrice){
        this.name = name;
        this.urlPic = urlPic;
        this.recentPrice = recentPrice;
        this.yesterdayPrice = yesterdayPrice;
    }

    public String getRecentPrice() {
        return recentPrice;
    }

    public String getYesterdayPrice() {
        return yesterdayPrice;
    }

    public String getName() {
        return name;
    }

    public String getUrlPic() {
        return urlPic;
    }

    @Override
    public int compareTo(ObjectCommodity another) {
        return this.getName().compareTo(another.getName());
    }
}
