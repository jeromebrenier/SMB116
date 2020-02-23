package fr.jbrenier.petfoodingcontrol.android.fragments.petmanagement.feeders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.android.activities.petmanagement.PetManagementViewModel;
/**
 * {@link RecyclerView.Adapter} that can display a {@link Feeder} and makes a call to the
 * specified {@link PetFeedersFragment.onRemoveFeederButtonClickListener}.
 */
public class MyPetFeedersRecyclerViewAdapter extends
        RecyclerView.Adapter<MyPetFeedersRecyclerViewAdapter.ViewHolder> {

    private final PetFeedersFragment.onRemoveFeederButtonClickListener mListener;
    private PetManagementViewModel petManagementViewModel;

    MyPetFeedersRecyclerViewAdapter(PetManagementViewModel petManagementViewModel,
                                           PetFeedersFragment.onRemoveFeederButtonClickListener
                                                   listener) {
        this.petManagementViewModel = petManagementViewModel;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_feeders_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.feeder = petManagementViewModel.getPetFeedersArrayList().get(position);
        holder.mFeederNameView.setText(holder.feeder.getDisplayedName());
        holder.mFeederEmailView.setText(holder.feeder.getEmail());
        holder.mRemoveFeederButton.setOnClickListener(view -> {
            if (null != mListener) {
                mListener.onRemoveFeederButtonClick(holder.feeder, this);
            }
        });
    }

    @Override
    public int getItemCount() {
        return petManagementViewModel.getPetFeedersArrayList() == null ? 0 :
                petManagementViewModel.getPetFeedersArrayList().size();
    }

    /**
     * The view holder representing a pet feeder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mFeederNameView;
        final TextView mFeederEmailView;
        final ImageButton mRemoveFeederButton;
        public Feeder feeder;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mFeederNameView = view.findViewById(R.id.txt_pet_feeder_name);
            mFeederEmailView = view.findViewById(R.id.txt_pet_feeder_email);
            mRemoveFeederButton = view.findViewById(R.id.btn_delete_feeder);
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + mFeederEmailView.getText() + "'";
        }
    }
}
