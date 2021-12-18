package co.harismiftahulhudha.prospacetest.mvvm.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryModel;
import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;
import co.harismiftahulhudha.prospacetest.mvvm.repositories.CustomGalleryRepository;
import co.harismiftahulhudha.prospacetest.mvvm.repositories.MachineRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MachineDetailsViewModel extends ViewModel {

    private final CustomGalleryRepository customGalleryRepository;
    private final MachineRepository repository;
    public MachineModel machineDetails;
    private Event event;

    @Inject
    MachineDetailsViewModel(CustomGalleryRepository customGalleryRepository, MachineRepository repository, SavedStateHandle state) {
        this.customGalleryRepository = customGalleryRepository;
        this.repository = repository;
        machineDetails = ((MachineModel) state.get("machineDetails"));
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void onSetData() {
        if (machineDetails != null) {
            event.onSetData();
        }
    }

    public void onClickGallery() {
        if (machineDetails != null) {
            event.onClickGallery();
        }
    }

    public void onClickDetailImage(Uri uri) {
        if (machineDetails != null) {
            event.onClickDetailImage(uri);
        }
    }

    public void setSelectedFiles(List<CustomGalleryModel> files) {
        customGalleryRepository.setSelectedFiles(files);
    }

    public void getDetailMachine(int id) {
        repository.getMachine(id);
    }

    public LiveData<MachineModel> getDetailMachine() {
        return repository.detailMachine;
    }

    public interface Event {
        void onSetData();
        void onClickGallery();
        void onClickDetailImage(Uri uri);
    }
}
