package fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.feeders;

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
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetData;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementActivity;
import fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.PetManagementFragment;

public class PetFeedersFragment extends PetManagementFragment implements PetData {

    /** LOGGING */
    private static final String TAG = "PetFeedersFragment";

    private PetManagementActivity petManagementActivity;
    private onRemoveFeederButtonClickListener mListener;
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
        View view = inflater.inflate(R.layout.pet_feeders_fragment_list, container, false);
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
        adapter = new MyPetFeedersRecyclerViewAdapter(
                petManagementActivity.getPetManagementViewModel(), mListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onRemoveFeederButtonClickListener) {
            mListener = (onRemoveFeederButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onRemoveFeederButtonClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onRemoveFeederButtonClickListener {
        void onRemoveFeederButtonClick(Feeder feeder, MyPetFeedersRecyclerViewAdapter adapter);
    }

    @Override
    public void loadPetData(PetManagementActivity pMA) {
        Log.i(TAG, "PetFeedersFragment loadPetData");
    }

    @Override
    public void savePetData(PetManagementActivity pMA) {
        Log.i(TAG, "PetFeedersFragment savePetData");
    }
}
