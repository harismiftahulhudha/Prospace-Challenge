package co.harismiftahulhudha.prospacetest.mvvm.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import co.harismiftahulhudha.prospacetest.databinding.FragmentBarcodeScannerBinding;
import co.harismiftahulhudha.prospacetest.mvvm.interfaces.BaseInterface;
import co.harismiftahulhudha.prospacetest.mvvm.viewmodels.BarcodeScannerViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

@AndroidEntryPoint
public class BarcodeScannerFragment extends BaseFragment implements BaseInterface, ZXingScannerView.ResultHandler {

    private FragmentBarcodeScannerBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBarcodeScannerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // ViewModels
    private BarcodeScannerViewModel viewModel;

    // Components

    // Variables
    private ZXingScannerView scannerView;
    private boolean isCheck = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents();
        subscribeListeners();
        subscribeObservers();

        scannerView.startCamera();
    }

    @Override
    public void initComponents() {
        viewModel = new ViewModelProvider(this).get(BarcodeScannerViewModel.class);
        scannerView = new ZXingScannerView(requireContext());
        scannerView.setAutoFocus(true);
        scannerView.setResultHandler(this);
        binding.frameCamera.addView(scannerView);
    }

    @Override
    public void subscribeListeners() {

    }

    @Override
    public void subscribeObservers() {
        viewModel.getDetailMachine().observe(getViewLifecycleOwner(), model -> {
            if (model != null && !isCheck) {
                isCheck = false;
                mainActivity.navController.navigate(BarcodeScannerFragmentDirections.actionBarcodeScannerFragmentToMachineDetailsFragment(model));
            }
        });
    }

    @Override
    public void handleResult(Result result) {
        binding.txtQrvalue.setText(result.getText());
        if (result.getText() != null) {
            isCheck = true;
            viewModel.getDetailMachine(Long.parseLong(result.getText()));
        }
    }

    @Override
    public void onDestroy() {
        scannerView.stopCamera();
        super.onDestroy();
    }
}