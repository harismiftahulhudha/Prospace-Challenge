package co.harismiftahulhudha.prospacetest.mvvm.viewmodels;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailImageViewModel extends ViewModel {

    private Event event;
    public String uri;

    @Inject
    DetailImageViewModel(SavedStateHandle state) {
        uri = ((String) state.get("uri"));
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void onSetImage() {
        event.onSetImage(uri);
    }

    public interface Event {
        void onSetImage(String uri);
    }
}
