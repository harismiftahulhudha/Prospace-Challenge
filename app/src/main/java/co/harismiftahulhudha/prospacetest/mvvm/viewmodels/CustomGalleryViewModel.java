package co.harismiftahulhudha.prospacetest.mvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryFolderModel;
import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryModel;
import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;
import co.harismiftahulhudha.prospacetest.mvvm.repositories.CustomGalleryRepository;
import co.harismiftahulhudha.prospacetest.mvvm.repositories.MachineRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CustomGalleryViewModel extends ViewModel {

    private final MachineRepository machineRepository;
    private final CustomGalleryRepository repository;
    private SavedStateHandle state;
    private Event event;
    public MachineModel machineDetails;

    @Inject
    CustomGalleryViewModel(MachineRepository machineRepository, CustomGalleryRepository repository, SavedStateHandle state) {
        this.machineRepository = machineRepository;
        this. repository = repository;
        this.state = state;
        machineDetails = ((MachineModel) state.get("machineDetails"));
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void getFolders() {
        repository.getFolders();
    }

    public void getFiles(String bucket) {
        repository.getFiles(bucket);
    }

    public LiveData<List<CustomGalleryFolderModel>> folders() {
        return repository.folders;
    }

    public LiveData<List<CustomGalleryModel>> files() {
        return repository.files;
    }

    public void addSelectedFiles(CustomGalleryModel file) {
        repository.addSelectedFiles(file);
    }

    public void removeSelectedFiles(CustomGalleryModel file) {
        repository.removeSelectedFiles(file);
    }

    public void setSelectedFiles() {
        repository.setSelectedFiles(machineDetails.getThumbnail());
    }

    public LiveData<List<CustomGalleryModel>> getSelectedFiles() {
        return repository.getSelectedFiles();
    }

    public void updateMachine() {
        machineRepository.update(machineDetails);
    }

    public void onClickSave() {
        event.onClickSave();
    }

    public interface Event {
        void onClickSave();
    }
}
