package fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.entities.user.User;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetData;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetManagementActivity;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.PetManagementFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PetFeedersFragment extends PetManagementFragment implements PetData {

    /** LOGGING */
    private static final String TAG = "PetFeedersFragment";

    private PetManagementActivity petManagementActivity;
    private OnListFragmentInteractionListener mListener;
    private MyPetFeedersRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PetFeedersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_feeders_list, container, false);
        petManagementActivity = (PetManagementActivity)getActivity();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            setAdapter(recyclerView);
        }
        return view;
    }

    private void setAdapter(RecyclerView recyclerView) {
        adapter = new MyPetFeedersRecyclerViewAdapter(this,
                petManagementActivity.getPetManagementViewModel(), mListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showAddAFeederButtonIfVisible();
    }

    /**
     * Show the add a feeder floating button if not visible.
     */
    private void showAddAFeederButtonIfVisible() {
        if (petManagementActivity.findViewById(R.id.add_a_feeder).getVisibility() != View.VISIBLE) {
            petManagementActivity.findViewById(R.id.add_a_feeder).setVisibility(View.VISIBLE);
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
        void onListFragmentInteraction(User feeder);
    }

    @Override
    public void loadPetData() {
        Log.i(TAG, "loadPetData");
    }

    @Override
    public void savePetData() {
        Log.i(TAG, "savePetData");
    }
}
