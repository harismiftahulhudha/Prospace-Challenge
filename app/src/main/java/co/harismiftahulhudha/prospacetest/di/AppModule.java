package co.harismiftahulhudha.prospacetest.di;

import static co.harismiftahulhudha.prospacetest.BuildConfig.DATABASE_NAME;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import javax.inject.Singleton;

import co.harismiftahulhudha.prospacetest.BuildConfig;
import co.harismiftahulhudha.prospacetest.database.AppDatabase;
import co.harismiftahulhudha.prospacetest.database.dao.MachineDao;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public static AppDatabase provideDatabase(Application app, AppDatabase.Callback callback) {
        if (BuildConfig.DEBUG) {
            return Room.databaseBuilder(app, AppDatabase.class, DATABASE_NAME)
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        } else {
            return Room.databaseBuilder(app, AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
    }

    @Provides
    @Singleton
    public static MachineDao provideMachineDao(AppDatabase db) {
        return db.machineDao();
    }
}
