package co.harismiftahulhudha.prospacetest.mvvm.views.fragments;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import co.harismiftahulhudha.prospacetest.R;
import co.harismiftahulhudha.prospacetest.databinding.FragmentCustomGalleryBinding;
import co.harismiftahulhudha.prospacetest.mvvm.interfaces.BaseInterface;
import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryFolderModel;
import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryModel;
import co.harismiftahulhudha.prospacetest.mvvm.viewmodels.CustomGalleryViewModel;
import co.harismiftahulhudha.prospacetest.mvvm.views.adapters.CustomGalleryAdapter;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CustomGalleryFragment extends BaseFragment implements BaseInterface {

    private static final String TAG = "CustomGalleryFragment";

    private FragmentCustomGalleryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCustomGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // ViewModels
    private CustomGalleryViewModel viewModel;

    // Components
    private CustomGalleryAdapter adapter;

    // Variables
    private ArrayAdapter<String> spinnerFolderAdapter;
    private final List<String> folderName = new ArrayList<>();
    private final List<Long> folderId = new ArrayList<>();
    private final List<CustomGalleryModel> selectedFiles = new ArrayList<>();
    private MenuItem menuItem;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initComponents();
        subscribeListeners();
        subscribeObservers();

        viewModel.getFolders();
        viewModel.setSelectedFiles();
    }

    @Override
    public void initComponents() {
        viewModel = new ViewModelProvider(this).get(CustomGalleryViewModel.class);
        viewModel.setEvent(() -> {
            mainActivity.navController.popBackStack();
        });
        setHasOptionsMenu(true);
        spinnerFolderAdapter = new ArrayAdapter(requireContext(), R.layout.custom_item_gallery_spinner_selected, folderName);
        spinnerFolderAdapter.setDropDownViewResource(R.layout.custom_item_spinner_dropdown);
        binding.spinnerFolder.setAdapter(spinnerFolderAdapter);

        binding.rvFiles.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.rvFiles.setHasFixedSize(true);
        binding.rvFiles.setItemViewCacheSize(20);
        binding.rvFiles.addItemDecoration(new GridSpacingItemDecorationHelper(3, 5, true));
        adapter = new CustomGalleryAdapter(CustomGalleryAdapter.Mode.GALLERY);
        binding.rvFiles.setAdapter(adapter);
    }

    @Override
    public void subscribeListeners() {
        adapter.setListener((model, position) -> {
            final List<CustomGalleryModel> tmp = new ArrayList<>(adapter.getCurrentList());
            if (selectedFiles.size() == 10) {
                if (model.isSelect()) {
                    model.setSelect(false);
                    tmp.set(position, model);
                    adapter.submitList(tmp);
                    adapter.notifyItemChanged(position);
                    viewModel.removeSelectedFiles(model);
                }
            } else {
                model.setSelect(!model.isSelect());
                tmp.set(position, model);
                adapter.submitList(tmp);
                adapter.notifyItemChanged(position);
                if (model.isSelect()) {
                    viewModel.addSelectedFiles(model);
                } else {
                    viewModel.removeSelectedFiles(model);
                }
            }
        });
        binding.spinnerFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    viewModel.getFiles(String.valueOf(folderId.get(position)));
                } else {
                    viewModel.getFiles(folderName.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void subscribeObservers() {
        viewModel.folders().observe(getViewLifecycleOwner(), customGalleryFolderModels -> {
            if (customGalleryFolderModels != null) {
                folderName.clear();
                folderId.clear();
                for (CustomGalleryFolderModel model : customGalleryFolderModels) {
                    folderName.add(model.getTitle());
                    folderId.add(model.getId());
                }
                spinnerFolderAdapter.notifyDataSetChanged();
            }
        });
        viewModel.files().observe(getViewLifecycleOwner(), customGalleryModels -> {
            if (customGalleryModels != null) {
                final List<CustomGalleryModel> tmp = new ArrayList<>();
                for (CustomGalleryModel model: customGalleryModels) {
                    if (selectedFiles.size() > 0) {
                        for (CustomGalleryModel selected : selectedFiles) {
                            if (model.getId() == selected.getId()) {
                                model.setSelect(true);
                            }
                        }
                    }
                    tmp.add(model);
                }
                adapter.submitList(tmp);
            }
        });
        viewModel.getSelectedFiles().observe(getViewLifecycleOwner(), customGalleryModels -> {
            if (customGalleryModels != null) {
                selectedFiles.clear();
                selectedFiles.addAll(customGalleryModels);
                for (CustomGalleryModel model : customGalleryModels) {
                    Log.d(TAG, "subscribeObservers: " + model.getFilename());
                }
                Log.d(TAG, "subscribeObservers: ========================");
                if (menuItem != null) {
                    viewModel.machineDetails.setThumbnail(selectedFiles);
                    viewModel.updateMachine();
                    @SuppressLint("StringFormatMatches") String save = String.format(getResources().getString(R.string.save_thumbnail), selectedFiles.size());
                    menuItem.setTitle(save);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.gallery_menu, menu);
        menuItem = menu.findItem(R.id.actionSaveGallery);
        @SuppressLint("StringFormatMatches") String save = String.format(getResources().getString(R.string.save_thumbnail), selectedFiles.size());
        menuItem.setTitle(save);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.actionSaveGallery) {
            viewModel.onClickSave();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class GridSpacingItemDecorationHelper extends RecyclerView.ItemDecoration {
        private final int spanCount;
        private final int spacing;
        private final boolean includeEdge;

        public GridSpacingItemDecorationHelper(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            final int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;
                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}