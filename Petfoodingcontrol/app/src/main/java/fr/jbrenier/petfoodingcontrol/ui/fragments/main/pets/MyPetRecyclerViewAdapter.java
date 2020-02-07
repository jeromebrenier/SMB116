package fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.ui.activities.main.MainActivityViewModel;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pet} and makes a call to the
 * specified {@link PetFragment.OnListFragmentInteractionListener}.
 */
public class MyPetRecyclerViewAdapter extends RecyclerView.Adapter<MyPetRecyclerViewAdapter.ViewHolder> {

    private PetFragment petFragment;
    private MainActivityViewModel mainActivityViewModel;

    private final PetFragment.OnListFragmentInteractionListener mListener;

    public MyPetRecyclerViewAdapter(PetFragment petFragment,
                                    MainActivityViewModel mainActivityViewModel,
                                    PetFragment.OnListFragmentInteractionListener listener) {
        this.petFragment = petFragment;
        this.mainActivityViewModel = mainActivityViewModel;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.pet = mainActivityViewModel.getUserPetsArrayList().get(position);
        mainActivityViewModel.getPetPhoto(holder.pet).observe(
                petFragment.getViewLifecycleOwner(), holder.mPetImageView::setImageBitmap);
        holder.mPetNameView.setText(
                mainActivityViewModel.getUserPetsArrayList().get(position).getName());
        holder.mPetStatusView.setText(R.string.pet_status_unknown);
/*        if (petFragment.getPetFragmentViewModel().getUserLogged() != null) {
           // setPetOwnedTxtVisibility(holder, holder.pet.getAuthorizedFeeders());
        }*/


        holder.mView.setOnClickListener(view -> {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.pet);
                }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    /**
     * Set the visibility of the TextView containing the indication of the ownership for the pet
     * to VISIBLE if the authorized feeders of the pet does not contains the user's email.
     * @param holder
     * @param authorizedFeeders
     */
/*    private void setPetOwnedTxtVisibility(ViewHolder holder, List<String> authorizedFeeders) {
        boolean isAuthorizedFeeder = authorizedFeeders.stream().anyMatch(
                authorizedFeeder -> authorizedFeeder.equals(
                        petFragment.getPetFragmentViewModel().getUserLogged().getEmail()));
        if (!isAuthorizedFeeder) {
            holder.mPetOwnedView.setVisibility(View.VISIBLE);
        }
    }*/

    @Override
    public int getItemCount() {
        return mainActivityViewModel.getUserPetsArrayList() == null ? 0 :
                mainActivityViewModel.getUserPetsArrayList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView mPetImageView;
        public final TextView mPetNameView;
        public final TextView mPetStatusView;
        public final TextView mPetOwnedView;

        public Pet pet;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPetImageView = view.findViewById(R.id.civ_pet_image);
            mPetNameView = view.findViewById(R.id.txt_pet_name);
            mPetStatusView = view.findViewById(R.id.txt_pet_fooding_status);
            mPetOwnedView = view.findViewById(R.id.txt_pet_is_owner);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPetNameView.getText() + "'";
        }
    }
}
