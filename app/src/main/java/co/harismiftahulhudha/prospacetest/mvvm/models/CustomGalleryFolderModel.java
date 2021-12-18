package co.harismiftahulhudha.prospacetest.mvvm.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomGalleryFolderModel implements Parcelable {
    private final long id;
    private final String title;

    public CustomGalleryFolderModel(long id, String title) {
        this.id = id;
        this.title = title;
    }

    protected CustomGalleryFolderModel(Parcel in) {
        id = in.readLong();
        title = in.readString();
    }

    public static final Creator<CustomGalleryFolderModel> CREATOR = new Creator<CustomGalleryFolderModel>() {
        @Override
        public CustomGalleryFolderModel createFromParcel(Parcel in) {
            return new CustomGalleryFolderModel(in);
        }

        @Override
        public CustomGalleryFolderModel[] newArray(int size) {
            return new CustomGalleryFolderModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomGalleryFolderModel) {
            final CustomGalleryFolderModel folder = (CustomGalleryFolderModel) o;
            return folder.id == this.id || folder.title.equals(this.title);
        } else {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        int result = String.valueOf(id).hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }
}
