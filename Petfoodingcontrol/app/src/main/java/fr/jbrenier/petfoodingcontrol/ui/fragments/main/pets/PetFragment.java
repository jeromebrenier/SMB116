package fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivity;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PetFragment extends Fragment {

    private PetFragmentViewModel petFragmentViewModel;
    private MainActivity mainActivity;
    private Observer<List<Pet>> userPetObserver;
    private MyPetRecyclerViewAdapter adapter;
    private OnListFragmentInteractionListener mListener;

    @Inject
    PetService petService;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PetFragment() {
    }

    @SuppressWarnings("unused")
    public static PetFragment newInstance() {
        PetFragment fragment = new PetFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);
        ((PetFoodingControl) getActivity().getApplicationContext()).getAppComponent()
                .inject(this);
        petFragmentViewModel = ViewModelProviders.of(this).get(PetFragmentViewModel.class);
        mainActivity = (MainActivity)getActivity();
        // Toolbar title
        mainActivity.setToolBarTitle(R.string.menu_pets);
        //show the add a pet button
        if (getActivity().findViewById(R.id.main_addPet).getVisibility() != View.VISIBLE) {
            getActivity().findViewById(R.id.main_addPet).setVisibility(View.VISIBLE);
        }
         // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            setAdapter(recyclerView);
        }
        // show the add a pet button if invisible
        if (getActivity().findViewById(R.id.main_addPet).getVisibility() == View.INVISIBLE) {
            getActivity().findViewById(R.id.main_addPet).setVisibility(View.VISIBLE);
        }
        return view;
    }


    private void setAdapter(RecyclerView recyclerView) {
        if (petService.getPfcRepository().getUserPets() != null) {
            petFragmentViewModel.refresh(petService.getPfcRepository().getUserPets().getValue());
        }
        adapter = new MyPetRecyclerViewAdapter(this, mListener);
        adapter.setUserLogged(petService.getPfcRepository().getUserLogged().getValue());
        recyclerView.setAdapter(adapter);
        userPetObserver = list -> {
            petFragmentViewModel.refresh(list);
            adapter.notifyDataSetChanged();
        };
        petService.getPfcRepository().getUserLogged().observe(getViewLifecycleOwner(), user -> {
            adapter.setUserLogged(user);
            adapter.notifyDataSetChanged();
            if (user == null) {
                observeUserPets(true);
            } else {
                observeUserPets(false);
            }
        });
    }

    private void observeUserPets(boolean status) {
        if (status) {
            petService.getPfcRepository().getUserPets().observe(getViewLifecycleOwner(),
                    userPetObserver);
        } else {
            petService.getPfcRepository().getUserPets().removeObserver(userPetObserver);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public PetFragmentViewModel getPetFragmentViewModel() {
        return petFragmentViewModel;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Pet pet);
    }
}
