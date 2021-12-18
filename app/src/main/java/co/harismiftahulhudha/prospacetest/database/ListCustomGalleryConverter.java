package co.harismiftahulhudha.prospacetest.database;

import android.net.Uri;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryModel;

public class ListCustomGalleryConverter {
    @TypeConverter
    public List<CustomGalleryModel> fromString(String value) {
        List<CustomGalleryModel> list = new ArrayList<>();
        if (!value.equals("")) {
            for (String split : value.split(";")) {
                CustomGalleryModel model = new CustomGalleryModel();
                String[] splitVal = split.split(",");
                model.setId(Long.parseLong(splitVal[0]));
                model.setUri(Uri.parse(splitVal[1]));
                model.setFilename(splitVal[2]);
                if (splitVal[3].equals("true")) {
                    model.setSelect(true);
                } else {
                    model.setSelect(false);
                }
                list.add(model);
            }
        }
        return list;
    }

    @TypeConverter
    public String toString(List<CustomGalleryModel> list) {
        StringBuilder str = new StringBuilder();
        int index = 0;
        for (CustomGalleryModel model : list) {
            String objStr = model.getId() +
                    "," +
                    model.getUri().toString() +
                    "," +
                    model.getFilename() +
                    "," +
                    model.isSelect();
            str.append(objStr);
            if (index < list.size() - 1) {
                str.append(";");
            }
            index++;
        }
        return str.toString();
    }
}
