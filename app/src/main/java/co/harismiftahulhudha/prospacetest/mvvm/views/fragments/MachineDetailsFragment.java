package co.harismiftahulhudha.prospacetest.mvvm.views.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Locale;

import co.harismiftahulhudha.prospacetest.R;
import co.harismiftahulhudha.prospacetest.databinding.FragmentMachineDetailsBinding;
import co.harismiftahulhudha.prospacetest.mvvm.interfaces.BaseInterface;
import co.harismiftahulhudha.prospacetest.mvvm.viewmodels.MachineDetailsViewModel;
import co.harismiftahulhudha.prospacetest.mvvm.views.adapters.CustomGalleryAdapter;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MachineDetailsFragment extends BaseFragment implements BaseInterface {

    private static final String TAG = "MachineDetailsFragment";

    private FragmentMachineDetailsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMachineDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // ViewModels
    private MachineDetailsViewModel viewModel;

    // Components
    private CustomGalleryAdapter adapter;

    // Variables

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents();
        subscribeListeners();
        subscribeObservers();

        // Variables
        assert viewModel.machineDetails != null;
        viewModel.getDetailMachine(viewModel.machineDetails.getId());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initComponents() {
        viewModel = new ViewModelProvider(this).get(MachineDetailsViewModel.class);
        viewModel.setEvent(new MachineDetailsViewModel.Event() {
            @Override
            public void onSetData() {
                assert viewModel.machineDetails != null;
                binding.txtId.setText(String.valueOf(viewModel.machineDetails.getId()));
                binding.txtName.setText(viewModel.machineDetails.getName());
                binding.txtType.setText(viewModel.machineDetails.getType());
                binding.txtQrcode.setText(viewModel.machineDetails.getQrCodeNumber().toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = simpleDateFormat.format(viewModel.machineDetails.getLastMaintenanceDate());
                binding.txtLastMaintanceDate.setText(date);

                binding.rvGallery.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                binding.rvGallery.setHasFixedSize(true);
                binding.rvGallery.setItemViewCacheSize(20);
                binding.rvGallery.addItemDecoration(new CustomGalleryFragment.GridSpacingItemDecorationHelper(3, 5, true));
                adapter = new CustomGalleryAdapter(CustomGalleryAdapter.Mode.VIEW);
                adapter.setListener((model, position) -> {
                    viewModel.onClickDetailImage(model.getUri());
                });
                binding.rvGallery.setAdapter(adapter);
                adapter.submitList(viewModel.machineDetails.getThumbnail());
                viewModel.setSelectedFiles(viewModel.machineDetails.getThumbnail());
            }

            @Override
            public void onClickGallery() {
                mainActivity.navController.navigate(MachineDetailsFragmentDirections.actionGlobalCustomGalleryFragment(viewModel.machineDetails));
            }

            @Override
            public void onClickDetailImage(Uri uri) {
                mainActivity.navController.navigate(MachineDetailsFragmentDirections.actionMachineDetailsFragmentToDetailImageFragment(uri.toString()));
            }
        });
        setHasOptionsMenu(true);
    }

    @Override
    public void subscribeListeners() {

    }

    @Override
    public void subscribeObservers() {
        viewModel.getDetailMachine().observe(getViewLifecycleOwner(), model -> {
            viewModel.machineDetails = model;
            viewModel.onSetData();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionOpenGallery) {
            viewModel.onClickGallery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}