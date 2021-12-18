package co.harismiftahulhudha.prospacetest.mvvm.views.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import co.harismiftahulhudha.prospacetest.databinding.FragmentCreateOrEditBinding;
import co.harismiftahulhudha.prospacetest.mvvm.interfaces.BaseInterface;
import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;
import co.harismiftahulhudha.prospacetest.mvvm.viewmodels.CreateOrEditMachineViewModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateOrEditFragment extends BaseFragment implements BaseInterface {

    private FragmentCreateOrEditBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateOrEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // ViewModels
    private CreateOrEditMachineViewModel viewModel;

    // Components

    // Variables

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents();
        subscribeListeners();
        subscribeObservers();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initComponents() {
        viewModel = new ViewModelProvider(this).get(CreateOrEditMachineViewModel.class);
        viewModel.setEvent(new CreateOrEditMachineViewModel.Event() {
            @Override
            public void setButtonDisable() {
                binding.btnSave.setEnabled(false);
            }

            @Override
            public void setButtonActive() {
                binding.btnSave.setEnabled(true);
            }

            @Override
            public void onClickSave() {
                viewModel.save();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSetData(MachineModel model) {
                binding.txtTitle.setText("Edit Image Machine");
                binding.inputName.setText(model.getName());
                viewModel.setName(model.getName());
                binding.inputType.setText(model.getType());
                viewModel.setType(model.getType());
                binding.inputQrcode.setText(model.getQrCodeNumber().toString());
                viewModel.setQrcode(model.getQrCodeNumber().toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String date = simpleDateFormat.format(model.getLastMaintenanceDate());
                binding.txtLastMaintanceDate.setText(date);
                viewModel.setLastMaintenanceDate(date);
            }
        });
        binding.txtTitle.setText("Create Image Machine");
        viewModel.onSetData();
        if (viewModel.machineDetails != null) {
            binding.inputName.setText(viewModel.machineDetails.getName());
            binding.inputType.setText(viewModel.machineDetails.getType());
        } else {
            viewModel.onInputChange();
        }
    }

    @Override
    public void subscribeListeners() {
        binding.inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setName(binding.inputName.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setType(binding.inputType.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.inputQrcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputQrcode.removeTextChangedListener(this);
                viewModel.setQrcode(binding.inputQrcode.getText().toString().trim().replaceAll("[^0-9]+",""));
                binding.inputQrcode.setText(viewModel.getQrcode());
                binding.inputQrcode.setSelection(binding.inputQrcode.getText().toString().length());
                binding.inputQrcode.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.txtLastMaintanceDate.setOnClickListener(v -> showDatePickerDialog());
        binding.btnSave.setOnClickListener(v -> viewModel.onClickSave());
    }

    @Override
    public void subscribeObservers() {
        viewModel.lastMachine().observe(getViewLifecycleOwner(), machineModel -> {
            if (!viewModel.getStored().equals("empty")) {
                if (viewModel.machineDetails == null) {
                    Toast.makeText(requireContext(), "Successfully Create Image Machine", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Successfully Edit Image Machine", Toast.LENGTH_SHORT).show();
                }
                mainActivity.navController.popBackStack();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar newCalendar = Calendar.getInstance();
        if (viewModel.getLastMaintenanceDate() != null && !viewModel.getLastMaintenanceDate().equals("")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = sdf.parse(viewModel.getLastMaintenanceDate());
                newCalendar.setTime(Objects.requireNonNull(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        final DatePickerDialog  StartTime = new DatePickerDialog(requireContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate = simpleDateFormat.format(newDate.getTime());
            binding.txtLastMaintanceDate.setText(formattedDate);
            viewModel.setLastMaintenanceDate(formattedDate);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        StartTime.show();
    }
}