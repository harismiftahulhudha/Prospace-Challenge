package co.harismiftahulhudha.prospacetest.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;

@Dao
public interface MachineDao {

    @Query("SELECT * FROM machine ORDER BY name ASC")
    LiveData<List<MachineModel>> listSortByName();

    @Query("SELECT * FROM machine ORDER BY type ASC")
    LiveData<List<MachineModel>> listSortByType();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MachineModel model);

    @Update
    void update(MachineModel model);

    @Delete
    void delete(MachineModel model);

    @Query("SELECT * FROM machine ORDER BY id DESC LIMIT 1")
    MachineModel lastMachine();

    @Query("SELECT * FROM machine WHERE id = :id")
    MachineModel getMachine(int id);

    @Query("SELECT * FROM machine WHERE qrCodeNumber = :qrCodeNumber LIMIT 1")
    MachineModel getMachine(long qrCodeNumber);
}
