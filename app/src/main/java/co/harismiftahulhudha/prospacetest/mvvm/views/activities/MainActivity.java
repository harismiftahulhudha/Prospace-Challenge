package co.harismiftahulhudha.prospacetest.mvvm.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import java.util.Objects;

import co.harismiftahulhudha.prospacetest.R;
import co.harismiftahulhudha.prospacetest.databinding.ActivityMainBinding;
import co.harismiftahulhudha.prospacetest.mvvm.interfaces.BaseInterface;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements BaseInterface {
    
    // LayoutBinding
    private ActivityMainBinding binding;
    
    // ViewModels
    
    // Components
    
    // Variables
    public NavController navController;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        initComponents();
        subscribeListeners();
        subscribeObservers();
    }

    @Override
    public void initComponents() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.mainHostFragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();
    }

    @Override
    public void subscribeListeners() {

    }

    @Override
    public void subscribeObservers() {

    }
}