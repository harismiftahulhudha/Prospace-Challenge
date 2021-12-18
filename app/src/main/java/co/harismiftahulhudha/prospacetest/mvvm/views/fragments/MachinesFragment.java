package co.harismiftahulhudha.prospacetest.mvvm.views.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.harismiftahulhudha.prospacetest.R;
import co.harismiftahulhudha.prospacetest.databinding.FragmentMachinesBinding;
import co.harismiftahulhudha.prospacetest.mvvm.interfaces.BaseInterface;
import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;
import co.harismiftahulhudha.prospacetest.mvvm.repositories.MachineRepository;
import co.harismiftahulhudha.prospacetest.mvvm.viewmodels.MachinesViewModel;
import co.harismiftahulhudha.prospacetest.mvvm.views.adapters.MachineAdapter;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MachinesFragment extends BaseFragment implements BaseInterface {

    private FragmentMachinesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMachinesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // ViewModels
    private MachinesViewModel viewModel;

    // Components
    private MachineAdapter adapter;

    // Variables
    final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            mainActivity.navController.navigate(MachinesFragmentDirections.actionMachinesFragmentToMachineDetailsFragment(tmpModel));
        }
    });
    private MachineModel tmpModel;
    final ActivityResultLauncher<String> requestCameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> mainActivity.navController.navigate(MachinesFragmentDirections.actionMachinesFragmentToBarcodeScannerFragment()));


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents();
        subscribeListeners();
        subscribeObservers();
    }

    @Override
    public void initComponents() {
        viewModel = new ViewModelProvider(this).get(MachinesViewModel.class);
        viewModel.setEvent(new MachinesViewModel.Event() {
            @Override
            public void onClickMachine(MachineModel model, int position) {
                tmpModel = model;
                if (isReadExternalStorageGranted()) {
                    mainActivity.navController.navigate(MachinesFragmentDirections.actionMachinesFragmentToMachineDetailsFragment(model));
                } else {
                    requestPermission();
                }
            }

            @Override
            public void onClickMenuMachine(MachineModel model, int position, ImageView imageView) {
                showPopupMenu(model, position, imageView);
            }

            @Override
            public void onClickCreateMachine() {
                mainActivity.navController.navigate(MachinesFragmentDirections.actionMachinesFragmentToCreateOrEditFragment(null));
            }

            @Override
            public void onClickEditMachine(MachineModel model, int position) {
                mainActivity.navController.navigate(MachinesFragmentDirections.actionMachinesFragmentToCreateOrEditFragment(model));
            }

            @Override
            public void onClickDeleteMachine(MachineModel model, int position) {
                showAlertDelete(model, position);
            }
        });
        setHasOptionsMenu(true);
        binding.rvMachines.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMachines.setHasFixedSize(true);
        adapter = new MachineAdapter();
        binding.rvMachines.setAdapter(adapter);
    }

    @Override
    public void subscribeListeners() {
        adapter.setListener(new MachineAdapter.Listener() {
            @Override
            public void onClick(MachineModel model, int position) {
                viewModel.onClickMachine(model, position);
            }

            @Override
            public void onClickMenu(MachineModel model, int position, ImageView imageView) {
                viewModel.onClickMenuMachine(model, position, imageView);
            }
        });
        binding.fabCreateMachine.setOnClickListener(v -> viewModel.onClickCreateMachine());
    }

    @Override
    public void subscribeObservers() {
        viewModel.sortOrder().observe(getViewLifecycleOwner(), sortOrder -> {
            if (sortOrder != null) {
                viewModel.listMachine(sortOrder).observe(getViewLifecycleOwner(), machineModels -> adapter.submitList(machineModels));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        final MenuItem itemSortByName = menu.findItem(R.id.actionSortByName);
        final MenuItem itemSortByType = menu.findItem(R.id.actionSortByType);
        if (viewModel.sortOrder.getValue() == MachineRepository.SortOrder.NAME) {
            itemSortByName.setChecked(true);
        } else {
            itemSortByType.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionSortByName) {
            viewModel.sortOrder.setValue(MachineRepository.SortOrder.NAME);
            item.setChecked(true);
            return true;
        } else if (item.getItemId() == R.id.actionSortByType) {
            viewModel.sortOrder.setValue(MachineRepository.SortOrder.TYPE);
            item.setChecked(true);
            return true;
        } else if (item.getItemId() == R.id.actionBarcode) {
            if (isCameraGranted()) {
                mainActivity.navController.navigate(MachinesFragmentDirections.actionMachinesFragmentToBarcodeScannerFragment());
            } else {
                requestCameraPermission();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopupMenu(MachineModel model, int position, ImageView imageView) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), imageView);
        popupMenu.getMenuInflater().inflate(R.menu.item_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.actionEdit) {
                viewModel.onClickEditMachine(model, position);
                return true;
            } else if (item.getItemId() == R.id.actionDelete) {
                viewModel.onClickDeleteMachine(model, position);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showAlertDelete(MachineModel model, int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("Delete Image Machine");
        alertDialog.setMessage("Are you sure want to delete " + model.getName() + " ?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            viewModel.deleteMachine(model);
            List<MachineModel> models = new ArrayList<>(adapter.getCurrentList());
            models.remove(position);
            adapter.submitList(models);
            Toast.makeText(requireContext(), "Successfully Delete Image Machine", Toast.LENGTH_SHORT).show();
        });
        alertDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        Dialog dialog = alertDialog.create();
        dialog.show();
    }

    private boolean isReadExternalStorageGranted() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private boolean isCameraGranted() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
    }
}