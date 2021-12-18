package co.harismiftahulhudha.prospacetest.mvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryModel;
import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;
import co.harismiftahulhudha.prospacetest.mvvm.repositories.MachineRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateOrEditMachineViewModel extends ViewModel {
    private final MachineRepository repository;
    private SavedStateHandle state;
    public final MachineModel machineDetails;
    private Event event;

    @Inject
    CreateOrEditMachineViewModel(MachineRepository repository, SavedStateHandle state) {
        this.repository = repository;
        this.state = state;
        machineDetails = ((MachineModel) state.get("machineDetails"));
        if (!state.contains("stored")) {
            setStored("empty");
        }
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setName(String value) {
        state.set("name", value);
        onInputChange();
    }

    public String getName() {
        return (String) state.get("name");
    }

    public void setType(String value) {
        state.set("type", value);
        onInputChange();
    }

    public String getType() {
        return (String) state.get("type");
    }

    public void setQrcode(String value) {
        state.set("qrcode", value);
        onInputChange();
    }

    public String getQrcode() {
        return (String) state.get("qrcode");
    }

    public void setLastMaintenanceDate(String value) {
        state.set("lastMaintenanceDate", value);
        onInputChange();
    }

    public String getLastMaintenanceDate() {
        return (String) state.get("lastMaintenanceDate");
    }

    public String getStored() {
        return (String) state.get("stored");
    }

    public void setStored(String value) {
        state.set("stored", value);
    }

    public void onInputChange() {
        if (getName() != null && !getName().equals("") &&
                getType() != null && !getType().equals("") &&
                getQrcode() != null && !getQrcode().equals("") &&
                getLastMaintenanceDate() != null && !getLastMaintenanceDate().equals("")) {
            event.setButtonActive();
        } else {
            event.setButtonDisable();
        }
    }

    public void save() {
        if (getName() != null && !getName().equals("") &&
                getType() != null && !getType().equals("") &&
                getQrcode() != null && !getQrcode().equals("") &&
                getLastMaintenanceDate() != null && !getLastMaintenanceDate().equals("")) {
            try {
                setStored("empty");
                MachineModel model = new MachineModel();
                List<CustomGalleryModel> list = new ArrayList<>();
                model.setName(getName());
                model.setType(getType());
                model.setQrCodeNumber(Long.parseLong(getQrcode()));
                model.setThumbnail(list);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = sdf.parse(getLastMaintenanceDate());
                model.setLastMaintenanceDate(date);
                if (machineDetails != null) {
                    model.setId(machineDetails.getId());
                }
                repository.insert(model);
                setStored("stored");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public LiveData<MachineModel> lastMachine() {
        return repository.lastMachine;
    }

    public void onClickSave() {
        event.onClickSave();
    }

    public void onSetData() {
        if (machineDetails != null) {
            event.onSetData(machineDetails);
        }
    }

    public interface Event {
        void setButtonDisable();
        void setButtonActive();
        void onClickSave();
        void onSetData(MachineModel model);
    }
}
