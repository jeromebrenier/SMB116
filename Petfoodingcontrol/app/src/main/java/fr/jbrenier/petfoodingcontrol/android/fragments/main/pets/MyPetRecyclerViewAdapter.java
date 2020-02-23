package fr.jbrenier.petfoodingcontrol.android.fragments.main.pets;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.android.activities.main.MainActivityViewModel;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pet} and makes a call to the
 * specified {@link PetFragment.OnListFragmentInteractionListener}.
 */
public class MyPetRecyclerViewAdapter
        extends RecyclerView.Adapter<MyPetRecyclerViewAdapter.ViewHolder> {

    private PetFragment petFragment;
    private MainActivityViewModel mainActivityViewModel;
    private RecyclerView recyclerView;

    private final PetFragment.OnListFragmentInteractionListener mListener;

    MyPetRecyclerViewAdapter(PetFragment petFragment,
                                    MainActivityViewModel mainActivityViewModel,
                                    PetFragment.OnListFragmentInteractionListener listener) {
        this.petFragment = petFragment;
        this.mainActivityViewModel = mainActivityViewModel;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pet_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.pet = mainActivityViewModel.getUserPetsArrayList().get(position);
        setPetPhoto(holder);
        setPetName(holder);
        setPetStatus(holder);
        setPetOwnerIndicator(holder);
        setPetCardOnClickListener(holder);
        setPetCardOnDeleteButtonLongClickListener(holder);
    }

    /**
     * Set the pet photo in the card ImageView.
     * @param holder the view holder for the pet
     */
    private void setPetPhoto(ViewHolder holder) {
        mainActivityViewModel.getPetPhoto(holder.pet).observe(
                petFragment.getViewLifecycleOwner(), holder.mPetImageView::setImageBitmap);
    }

    /**
     * Set the pet name in the card corresponding TextView.
     * @param holder the view holder for the pet
     */
    private void setPetName(ViewHolder holder) {
        holder.mPetNameView.setText(holder.pet.getName());
    }

    /**
     * Ste the pet status of fooding int the dedicated part of the card.
     * @param holder the view holder for the pet
     */
    private void setPetStatus(ViewHolder holder) {
        if (holder.pet.getFoodSettings() != null &&
                holder.pet.getFoodSettings().getDailyQuantity() != null) {
            holder.mPetStatusView.setText(
                    recyclerView.getResources().getString(R.string.pet_status_can_be_fed));
            holder.mPetStatusView.setTextColor(Color.rgb(58, 147, 135));
        }
        mainActivityViewModel.getPetStatus(holder.pet).observe(
                petFragment.getViewLifecycleOwner(), status -> {
                    if (status <= 0) {
                        holder.mPetStatusView.setText(recyclerView.getResources()
                                .getString(R.string.pet_status_fed));
                        holder.mPetStatusView.setTextColor(Color.RED);
                    }
                }
        );
    }

    /**
     * Set the pet ownership indicator in the corresponding TextView of the card.
     * @param holder the view holder for the pet
     */
    private void setPetOwnerIndicator(ViewHolder holder) {
        PetFoodingControl pfc = null;
        if (petFragment.getActivity() != null) {
            pfc = (PetFoodingControl) petFragment.getActivity().getApplication();
        }
        User userLogged = null;
        if (pfc != null) {
            userLogged = pfc.getUserLogged().getValue();
        }
        if (userLogged != null) {
            if (holder.pet.getUserId().equals(userLogged.getUserId())) {
                holder.mPetOwnedView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Set the onClick listener on the card.
     * @param holder the view holder for the pet
     */
    private void setPetCardOnClickListener(ViewHolder holder) {
        holder.mView.setOnClickListener(view -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.pet);
            }
        });
    }

    /**
     * Set the long click listener on the deletion (cross) button of card.
     * @param holder the view holder for the pet
     */
    private void setPetCardOnDeleteButtonLongClickListener(ViewHolder holder) {
        holder.mDeleteButton.setOnLongClickListener(view -> {
            if (null != mListener) {
                mListener.onDeletePetButtonClick(holder.pet);
            }
            return true;
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mainActivityViewModel.getUserPetsArrayList() == null ? 0 :
                mainActivityViewModel.getUserPetsArrayList().size();
    }

    /**
     * The view holder for a pet.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final CircleImageView mPetImageView;
        final TextView mPetNameView;
        final TextView mPetStatusView;
        final TextView mPetOwnedView;
        final ImageButton mDeleteButton;

        public Pet pet;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPetImageView = view.findViewById(R.id.civ_pet_image);
            mPetNameView = view.findViewById(R.id.txt_pet_name);
            mPetStatusView = view.findViewById(R.id.txt_pet_fooding_status);
            mPetOwnedView = view.findViewById(R.id.txt_pet_is_owner);
            mDeleteButton = view.findViewById(R.id.btn_delete_pet);
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + mPetNameView.getText() + "'";
        }
    }
}
