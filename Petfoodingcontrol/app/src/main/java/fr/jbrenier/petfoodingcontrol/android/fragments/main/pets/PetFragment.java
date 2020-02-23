package fr.jbrenier.petfoodingcontrol.android.fragments.main.pets;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.android.activities.main.MainActivity;

/**
 * A fragment representing the list of Pets of the user.
 */
public class PetFragment extends Fragment {

    private MainActivity mainActivity;
    private Observer<Boolean> userPetsArrayListChangeObserver;
    private MyPetRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PetFragment() {
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPetsArrayListChangeObserver = bool -> adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userPetsArrayListChangeObserver = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_list_fragment, container, false);
        // Toolbar title
        mainActivity.setToolBarTitle(R.string.menu_pets);
        //show the add a pet button
        if (mainActivity.findViewById(R.id.main_addPet).getVisibility() != View.VISIBLE) {
            mainActivity.findViewById(R.id.main_addPet).setVisibility(View.VISIBLE);
        }
         // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            setAdapter(recyclerView);
        }
        // show the add a pet button if invisible
        if (mainActivity.findViewById(R.id.main_addPet).getVisibility() == View.INVISIBLE) {
            mainActivity.findViewById(R.id.main_addPet).setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void setAdapter(RecyclerView recyclerView) {
        adapter = new MyPetRecyclerViewAdapter(this,
                mainActivity.getMainActivityViewModel(), mainActivity);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onStart() {
        mainActivity.getMainActivityViewModel().getUserPetsArrayListChanged().observe(this,
                userPetsArrayListChangeObserver);
        super.onStart();
    }

    @Override
    public void onStop() {
        mainActivity.getMainActivityViewModel().getUserPetsArrayListChanged().removeObserver(
                userPetsArrayListChangeObserver);
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    /**
     * Interface to be implemented for interactions with the Pet card.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Pet pet);
        void onDeletePetButtonClick(Pet pet);
    }
}
