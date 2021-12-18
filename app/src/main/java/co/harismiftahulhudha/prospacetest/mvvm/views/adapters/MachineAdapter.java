package co.harismiftahulhudha.prospacetest.mvvm.views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.harismiftahulhudha.prospacetest.databinding.ItemMachineBinding;
import co.harismiftahulhudha.prospacetest.mvvm.models.MachineModel;

public class MachineAdapter extends ListAdapter<MachineModel, MachineAdapter.ViewHolder> {

    private Listener listener;

    public MachineAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MachineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMachineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MachineAdapter.ViewHolder holder, int position) {
        final MachineModel model = getItem(position);
        holder.bind(model);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMachineBinding binding;

        public ViewHolder(ItemMachineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MachineModel model) {
            binding.txtName.setText(model.getName());
            binding.txtType.setText(model.getType());
            binding.imgMenu.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onClickMenu(model, getAdapterPosition(), binding.imgMenu);
                }
            });
            binding.getRoot().setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onClick(model, getAdapterPosition());
                }
            });
        }
    }

    public interface Listener {
        void onClick(MachineModel model, int position);
        void onClickMenu(MachineModel model, int position, ImageView imageView);
    }

    public static final DiffUtil.ItemCallback<MachineModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<MachineModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull MachineModel oldItem, @NonNull MachineModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull MachineModel oldItem, @NonNull MachineModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
