package fr.jbrenier.petfoodingcontrol.ui.fragments.petfooding.food;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.activities.petfooding.PetFoodingActivity;
import fr.jbrenier.petfoodingcontrol.ui.activities.petfooding.PetFoodingViewModel;

import static fr.jbrenier.petfoodingcontrol.BR.petfoodfragmentviewmodel;
import static fr.jbrenier.petfoodingcontrol.BR.petfoodingactivity;

/**
 * The Pet food fragment.
 * @author Jérôme Brenier
 */
public class PetFoodFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ViewDataBinding binding;

    public PetFoodFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.pet_food_fragment, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PetFoodingViewModel pfcVM = ((PetFoodingActivity) getActivity()).getPetFoodingViewModel();
        binding.setVariable(petfoodfragmentviewmodel, pfcVM);
        binding.setVariable(petfoodingactivity, getActivity());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
