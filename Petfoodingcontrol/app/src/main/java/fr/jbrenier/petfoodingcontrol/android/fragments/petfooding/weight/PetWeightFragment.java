package fr.jbrenier.petfoodingcontrol.android.fragments.petfooding.weight;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.android.activities.petfooding.PetFoodingActivity;
import fr.jbrenier.petfoodingcontrol.android.activities.petfooding.PetFoodingViewModel;

import static fr.jbrenier.petfoodingcontrol.BR.petfoodingactivity;
import static fr.jbrenier.petfoodingcontrol.BR.petweightfragmentviewmodel;

/**
 * The Pet weight fragment.
 * @author Jérôme Brenier
 */
public class PetWeightFragment extends Fragment {

    private PetFoodingActivity petFoodingActivity;
    private ViewDataBinding binding;

    public PetWeightFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.pet_weight_fragment, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PetFoodingViewModel pfcVM = petFoodingActivity.getPetFoodingViewModel();
        binding.setVariable(petweightfragmentviewmodel, pfcVM);
        binding.setVariable(petfoodingactivity, getActivity());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        petFoodingActivity = (PetFoodingActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        petFoodingActivity = null;
    }

}
