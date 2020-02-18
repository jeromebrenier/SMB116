package fr.jbrenier.petfoodingcontrol.android.fragments.main.pets;

import android.content.Context;
import android.os.Bundle;

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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PetFragment extends Fragment {

    /** LOGGING */
    private static final String TAG = "PetFragment";

    private MainActivity mainActivity;
    private Observer<Boolean> userPetsArrayListChangeObserver;
    private MyPetRecyclerViewAdapter adapter;
    private OnListFragmentInteractionListener mListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_list_fragment, container, false);
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
        adapter = new MyPetRecyclerViewAdapter(this,
                mainActivity.getMainActivityViewModel(), mListener);
        recyclerView.setAdapter(adapter);
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
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Pet pet);
        void onDeletePetButtonClick(Pet pet);
    }
}
