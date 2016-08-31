package id.developer.tanitionary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Naufal on 8/26/2016.
 */
public class ObjectCommend implements Parcelable {

    private Integer idCommend;
    private String textCommend, dateCommend, urlPhotoCommend;
    private ObjectSender sender;

    public ObjectCommend(Integer idCommend, String textCommend, String dateCommend, String urlPhoto, ObjectSender sender){
        this.idCommend = idCommend;
        this.textCommend = textCommend;
        this.dateCommend = dateCommend;
        this.urlPhotoCommend = urlPhoto;
        this.sender = sender;
    }

    protected ObjectCommend(Parcel in) {
        idCommend = in.readInt();
        textCommend = in.readString();
        dateCommend = in.readString();
        urlPhotoCommend = in.readString();
        sender = in.readParcelable(ObjectSender.class.getClassLoader());
    }

    public static final Creator<ObjectCommend> CREATOR = new Creator<ObjectCommend>() {
        @Override
        public ObjectCommend createFromParcel(Parcel in) {
            return new ObjectCommend(in);
        }

        @Override
        public ObjectCommend[] newArray(int size) {
            return new ObjectCommend[size];
        }
    };

    public ObjectSender getSender() {
        return sender;
    }

    public Integer getIdCommend() {
        return idCommend;
    }

    public String getTextCommend() {
        return textCommend;
    }

    public String getDateCommend() {
        return dateCommend;
    }

    public String getUrlPhotoCommend() {
        return urlPhotoCommend;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCommend);
        dest.writeString(textCommend);
        dest.writeString(dateCommend);
        dest.writeString(urlPhotoCommend);
        dest.writeParcelable(sender, flags);
    }
}
