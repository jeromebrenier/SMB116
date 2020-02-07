package fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement.PetManagementViewModel;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders.PetFeedersFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Feeder} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPetFeedersRecyclerViewAdapter extends RecyclerView.Adapter<MyPetFeedersRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private PetFeedersFragment petFeedersFragment;
    private PetManagementViewModel petManagementViewModel;

    public MyPetFeedersRecyclerViewAdapter(PetFeedersFragment petFeedersFragment,
                                           PetManagementViewModel petManagementViewModel,
                                           OnListFragmentInteractionListener listener) {
        this.petFeedersFragment = petFeedersFragment;
        this.petManagementViewModel = petManagementViewModel;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_feeders_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.feeder = petManagementViewModel.getPetFeedersArrayList().get(position);
        holder.mFeederNameView.setText(
                petManagementViewModel.getPetFeedersArrayList().get(position).getDisplayedName());
        holder.mFeederEmailView.setText(
                petManagementViewModel.getPetFeedersArrayList().get(position).getEmail());

        holder.mView.setOnClickListener(view -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.feeder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petManagementViewModel.getPetFeedersArrayList() == null ? 0 :
                petManagementViewModel.getPetFeedersArrayList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFeederNameView;
        public final TextView mFeederEmailView;
        public Feeder feeder;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFeederNameView = (TextView) view.findViewById(R.id.txt_pet_feeder_name);
            mFeederEmailView = (TextView) view.findViewById(R.id.txt_pet_feeder_email);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFeederEmailView.getText() + "'";
        }
    }
}
