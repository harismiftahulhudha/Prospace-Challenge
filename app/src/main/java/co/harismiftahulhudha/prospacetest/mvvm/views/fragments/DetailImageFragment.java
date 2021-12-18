package co.harismiftahulhudha.prospacetest.mvvm.views.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileNotFoundException;

import co.harismiftahulhudha.prospacetest.databinding.FragmentDetailImageBinding;
import co.harismiftahulhudha.prospacetest.mvvm.interfaces.BaseInterface;
import co.harismiftahulhudha.prospacetest.mvvm.viewmodels.DetailImageViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailImageFragment extends BaseFragment implements BaseInterface {

    private FragmentDetailImageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // ViewModels
    private DetailImageViewModel viewModel;

    // Components

    // Variables

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents();
        subscribeListeners();
        subscribeObservers();

        viewModel.onSetImage();
    }

    @Override
    public void initComponents() {
        viewModel = new ViewModelProvider(this).get(DetailImageViewModel.class);
        viewModel.setEvent(uri -> {
            try {
                final ParcelFileDescriptor parcelFileDescriptor = requireContext().getContentResolver().openFileDescriptor(Uri.parse(uri), "r");
                if (parcelFileDescriptor != null) {
                    final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
                    binding.zoomImg.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void subscribeListeners() {

    }

    @Override
    public void subscribeObservers() {

    }
}