package id.developer.tanitionary;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Naufal on 8/26/2016.
 */
public class ObjectSender implements Parcelable {
    String name, urlPhoto, email;

    public ObjectSender(String email, String name, String urlPhoto){
        this.email = email;
        this.name = name;
        this.urlPhoto = urlPhoto;
    }

    protected ObjectSender(Parcel in) {
        name = in.readString();
        urlPhoto = in.readString();
        email = in.readString();
    }

    public static final Creator<ObjectSender> CREATOR = new Creator<ObjectSender>() {
        @Override
        public ObjectSender createFromParcel(Parcel in) {
            return new ObjectSender(in);
        }

        @Override
        public ObjectSender[] newArray(int size) {
            return new ObjectSender[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(urlPhoto);
        dest.writeString(email);
    }
}
