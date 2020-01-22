package fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.utils.ImageUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pet} and makes a call to the
 * specified {@link PetFragment.OnListFragmentInteractionListener}.
 */
public class MyPetRecyclerViewAdapter extends RecyclerView.Adapter<MyPetRecyclerViewAdapter.ViewHolder> {

    private final PetFoodingControlRepository petFoodingControlRepository;
    private final List<Pet> mUserPets;
    private User userLogged = null;

    /** Manages disposables */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final PetFragment.OnListFragmentInteractionListener mListener;

    public MyPetRecyclerViewAdapter(PetFragment petFragment,
                                    PetFragment.OnListFragmentInteractionListener listener) {
        this.petFoodingControlRepository = petFragment.petService.getPfcRepository();
        mUserPets = petFragment.getPetFragmentViewModel().getUserPets();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.pet = mUserPets.get(position);
        Disposable disposable = petFoodingControlRepository.getPetPhoto(holder.pet).subscribe(
                photo -> holder.mPetImageView.setImageBitmap(
                        ImageUtils.getBitmapFromBase64String(photo.getImage()))
        );
        compositeDisposable.add(disposable);
        holder.mPetNameView.setText(mUserPets.get(position).getName());
        holder.mPetStatusView.setText(R.string.pet_status_unknown);
        if (userLogged != null) {
           // setPetOwnedTxtVisibility(holder, holder.pet.getAuthorizedFeeders());
        }

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
        compositeDisposable.dispose();
        super.onDetachedFromRecyclerView(recyclerView);
    }

    /**
     * Set the visibility of the TextView containing the indication of the ownership for the pet
     * to VISIBLE if the authorized feeders of the pet does not contains the user's email.
     * @param holder
     * @param authorizedFeeders
     */
    private void setPetOwnedTxtVisibility(ViewHolder holder, List<String> authorizedFeeders) {
        boolean isAuthorizedFeeder = authorizedFeeders.stream().anyMatch(
                authorizedFeeder -> authorizedFeeder.equals(userLogged.getEmail()));
        if (!isAuthorizedFeeder) {
            holder.mPetOwnedView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mUserPets == null ? 0 : mUserPets.size();
    }

    public void setUserLogged(User userLogged) {
        this.userLogged = userLogged;
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
            mPetImageView = (CircleImageView) view.findViewById(R.id.civ_pet_image);
            mPetNameView = (TextView) view.findViewById(R.id.txt_pet_name);
            mPetStatusView = (TextView) view.findViewById(R.id.txt_pet_fooding_status);
            mPetOwnedView = (TextView) view.findViewById(R.id.txt_pet_is_owner);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPetNameView.getText() + "'";
        }
    }
}
