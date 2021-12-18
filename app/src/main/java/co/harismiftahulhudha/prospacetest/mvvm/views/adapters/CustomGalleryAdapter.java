package co.harismiftahulhudha.prospacetest.mvvm.views.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;

import co.harismiftahulhudha.prospacetest.databinding.ItemCustomGalleryBinding;
import co.harismiftahulhudha.prospacetest.mvvm.models.CustomGalleryModel;

public class CustomGalleryAdapter extends ListAdapter<CustomGalleryModel, CustomGalleryAdapter.ViewHolder> {

    private Listener listener;
    private final CustomGalleryAdapter.Mode mode;

    public CustomGalleryAdapter(CustomGalleryAdapter.Mode mode) {
        super(DIFF_CALLBACK);
        this.mode = mode;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCustomGalleryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomGalleryAdapter.ViewHolder holder, int position) {
        final CustomGalleryModel model = getItem(position);
        holder.bind(model);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCustomGalleryBinding binding;

        public ViewHolder(ItemCustomGalleryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CustomGalleryModel model) {
            try {
                final ParcelFileDescriptor parcelFileDescriptor = this.itemView.getContext().getContentResolver().openFileDescriptor(model.getUri(), "r");
                if (parcelFileDescriptor != null) {
                    final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
                    binding.imgFile.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (mode == Mode.GALLERY) {
                if (model.isSelect()) {
                    binding.viewOverlay.setVisibility(View.VISIBLE);
                    binding.imgIconSelect.setVisibility(View.VISIBLE);
                } else {
                    binding.viewOverlay.setVisibility(View.GONE);
                    binding.imgIconSelect.setVisibility(View.GONE);
                }
            }
            binding.getRoot().setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onClick(model, getAdapterPosition());
                }
            });
        }
    }

    public enum Mode {
        GALLERY, VIEW
    }

    public interface Listener {
        void onClick(CustomGalleryModel model, int position);
    }

    public static final DiffUtil.ItemCallback<CustomGalleryModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<CustomGalleryModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CustomGalleryModel oldItem, @NonNull CustomGalleryModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull CustomGalleryModel oldItem, @NonNull CustomGalleryModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
