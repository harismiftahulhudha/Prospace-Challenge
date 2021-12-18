package co.harismiftahulhudha.prospacetest.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Provider;

import co.harismiftahulhudha.prospacetest.database.dao.MachineDao;
import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryModel;
import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;

@Database(entities = {MachineModel.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";

    public abstract MachineDao machineDao();

    public static class Callback extends RoomDatabase.Callback {
        private final Provider<AppDatabase> database;
        @Inject Callback(Provider<AppDatabase> database) {
            this.database = database;
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            Log.d(TAG, "onCreate: ");
            final MachineDao dao = database.get().machineDao();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                List<CustomGalleryModel> list = new ArrayList<>();

                MachineModel model = new MachineModel();
                model.setName("Test 1");
                model.setType("photo");
                model.setQrCodeNumber(0L);
                model.setThumbnail(list);
                model.setLastMaintenanceDate(new Date());
                dao.insert(model);

                model.setName("Test 2");
                model.setType("video");
                model.setQrCodeNumber(0L);
                model.setThumbnail(list);
                model.setLastMaintenanceDate(new Date());
                dao.insert(model);

                model.setName("Test 3");
                model.setType("png");
                model.setQrCodeNumber(0L);
                model.setThumbnail(list);
                model.setLastMaintenanceDate(new Date());
                dao.insert(model);
            });
        }
    }
}
