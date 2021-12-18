package co.harismiftahulhudha.prospacetest.mvvm.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

import co.harismiftahulhudha.prospacetest.database.DateConverter;
import co.harismiftahulhudha.prospacetest.database.ListCustomGalleryConverter;

@Entity(tableName = "machine")
@TypeConverters(ListCustomGalleryConverter.class)
public class MachineModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String type;
    private List<CustomGalleryModel> thumbnail;
    private Long qrCodeNumber;
    private Date lastMaintenanceDate;

    public MachineModel() {
    }

    protected MachineModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readString();
        thumbnail = in.createTypedArrayList(CustomGalleryModel.CREATOR);
        if (in.readByte() == 0) {
            qrCodeNumber = null;
        } else {
            qrCodeNumber = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeTypedList(thumbnail);
        if (qrCodeNumber == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(qrCodeNumber);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MachineModel> CREATOR = new Creator<MachineModel>() {
        @Override
        public MachineModel createFromParcel(Parcel in) {
            return new MachineModel(in);
        }

        @Override
        public MachineModel[] newArray(int size) {
            return new MachineModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CustomGalleryModel> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(List<CustomGalleryModel> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getQrCodeNumber() {
        return qrCodeNumber;
    }

    public void setQrCodeNumber(Long qrCodeNumber) {
        this.qrCodeNumber = qrCodeNumber;
    }

    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(Date lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public static Creator<MachineModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "MachineModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", thumbnail=" + thumbnail +
                ", qrCodeNumber=" + qrCodeNumber +
                ", lastMaintenanceDate=" + lastMaintenanceDate +
                '}';
    }
}
