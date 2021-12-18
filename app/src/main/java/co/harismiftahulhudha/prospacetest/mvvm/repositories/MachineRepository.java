package co.harismiftahulhudha.prospacetest.mvvm.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import co.harismiftahulhudha.prospacetest.database.dao.MachineDao;
import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;
import co.harismiftahulhudha.prospacetest.mvvm.viewmodels.MachinesViewModel;

@Singleton
public class MachineRepository {

    private MachineDao dao;

    @Inject
    public MachineRepository(MachineDao dao) {
        this.dao = dao;
    }

    public MutableLiveData<MachineModel> lastMachine = new MediatorLiveData<>();
    public MutableLiveData<MachineModel> detailMachine = new MediatorLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<List<MachineModel>> listMachine(SortOrder sortBy) {
        if (sortBy == SortOrder.TYPE) {
            return dao.listSortByType();
        } else {
            return dao.listSortByName();
        }
    }

    public void insert(MachineModel model) {
        executor.execute(() -> {
            dao.insert(model);
            lastMachine.postValue(dao.lastMachine());
        });
    }

    public void update(MachineModel model) {
        executor.execute(() -> {
            dao.update(model);
            detailMachine.postValue(dao.getMachine(model.getId()));
        });
    }

    public void delete(MachineModel model) {
        executor.execute(() -> {
            dao.delete(model);
        });
    }

    public void getMachine(int id) {
        executor.execute(() -> {
            detailMachine.postValue(dao.getMachine(id));
        });
    }
    public void getMachine(long qrCode) {
        executor.execute(() -> {
            detailMachine.postValue(dao.getMachine(qrCode));
        });
    }

    public enum SortOrder {
        NAME, TYPE
    }
}
