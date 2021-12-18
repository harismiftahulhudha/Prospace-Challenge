package co.harismiftahulhudha.prospacetest.mvvm.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class CustomGalleryModel implements Parcelable {
    private long id;
    private Uri uri;
    private String filename;
    private boolean select;

    public CustomGalleryModel() {
    }

    public CustomGalleryModel(long id, Uri uri, String filename, boolean select) {
        this.id = id;
        this.uri = uri;
        this.filename = filename;
        this.select = select;
    }

    public CustomGalleryModel(long id, Uri uri, String filename) {
        this.id = id;
        this.uri = uri;
        this.filename = filename;
        this.select = false;
    }

    protected CustomGalleryModel(Parcel in) {
        id = in.readLong();
        uri = in.readParcelable(Uri.class.getClassLoader());
        filename = in.readString();
        select = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(uri, flags);
        dest.writeString(filename);
        dest.writeByte((byte) (select ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CustomGalleryModel> CREATOR = new Creator<CustomGalleryModel>() {
        @Override
        public CustomGalleryModel createFromParcel(Parcel in) {
            return new CustomGalleryModel(in);
        }

        @Override
        public CustomGalleryModel[] newArray(int size) {
            return new CustomGalleryModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
