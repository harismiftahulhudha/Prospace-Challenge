package co.harismiftahulhudha.prospacetest.mvvm.views.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import co.harismiftahulhudha.prospacetest.mvvm.views.activities.MainActivity;

public abstract class BaseFragment extends Fragment {
    public MainActivity mainActivity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }
}
