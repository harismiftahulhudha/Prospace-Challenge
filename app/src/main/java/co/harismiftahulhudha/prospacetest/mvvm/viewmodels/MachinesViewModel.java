package co.harismiftahulhudha.prospacetest.mvvm.viewmodels;

import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;
import co.harismiftahulhudha.prospacetest.mvvm.repositories.MachineRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MachinesViewModel extends ViewModel {

    private final MachineRepository repository;
    private SavedStateHandle state;
    private Event event;

    @Inject
    MachinesViewModel(MachineRepository repository, SavedStateHandle state) {
        this. repository = repository;
        this.state = state;
        sortOrder.setValue(MachineRepository.SortOrder.NAME);
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void deleteMachine(MachineModel model) {
        repository.delete(model);
    }

    public MutableLiveData<MachineRepository.SortOrder> sortOrder = new MediatorLiveData<>();

    public LiveData<List<MachineModel>> listMachine(MachineRepository.SortOrder sortBy) {
        return repository.listMachine(sortBy);
    }

    public LiveData<MachineRepository.SortOrder> sortOrder() {
        return sortOrder;
    }

    public void onClickMachine(MachineModel model, int position) {
        event.onClickMachine(model, position);
    }

    public void onClickMenuMachine(MachineModel model, int position, ImageView imageView) {
        event.onClickMenuMachine(model, position, imageView);
    }

    public void onClickCreateMachine() {
        event.onClickCreateMachine();
    }

    public void onClickEditMachine(MachineModel model, int position) {
        event.onClickEditMachine(model, position);
    }

    public void onClickDeleteMachine(MachineModel model, int position) {
        event.onClickDeleteMachine(model, position);
    }

    public interface Event {
        void onClickMachine(MachineModel model, int position);
        void onClickMenuMachine(MachineModel model, int position, ImageView imageView);
        void onClickCreateMachine();
        void onClickEditMachine(MachineModel model, int position);
        void onClickDeleteMachine(MachineModel model, int position);
    }
}
