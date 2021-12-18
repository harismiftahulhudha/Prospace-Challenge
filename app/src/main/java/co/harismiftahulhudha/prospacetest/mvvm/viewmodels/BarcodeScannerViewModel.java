package co.harismiftahulhudha.prospacetest.mvvm.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;
import co.harismiftahulhudha.prospacetest.mvvm.repositories.MachineRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class BarcodeScannerViewModel extends ViewModel {

    private final MachineRepository repository;
    @Inject
    BarcodeScannerViewModel(MachineRepository repository, SavedStateHandle state) {
        this.repository = repository;
    }

    public void getDetailMachine(long qrCode) {
        repository.getMachine(qrCode);
    }

    public LiveData<MachineModel> getDetailMachine() {
        return repository.detailMachine;
    }
}
