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
public class MyPetRecyclerViewAdapter extends RecyclerView.Adapter<MyPetRecyclerViewAdapter.ViewHolder> {

    private PetFragment petFragment;
    private MainActivityViewModel mainActivityViewModel;
    private RecyclerView recyclerView;

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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.pet = mainActivityViewModel.getUserPetsArrayList().get(position);
        mainActivityViewModel.getPetPhoto(holder.pet).observe(
                petFragment.getViewLifecycleOwner(), holder.mPetImageView::setImageBitmap);
        holder.mPetNameView.setText(
                mainActivityViewModel.getUserPetsArrayList().get(position).getName());
        mainActivityViewModel.getPetStatus(holder.pet).observe(
                petFragment.getViewLifecycleOwner(), status -> {
                    String statusToShow;
                    int textColor;
                    if (status > 0) {
                        statusToShow = recyclerView.getResources()
                                .getString(R.string.pet_status_can_be_fed);
                        textColor = Color.GREEN;
                    } else {
                        statusToShow = recyclerView.getResources()
                                .getString(R.string.pet_status_fed);
                        textColor = Color.RED;
                    }
                    holder.mPetStatusView.setText(statusToShow);
                    holder.mPetStatusView.setTextColor(textColor);
                }
        );
        PetFoodingControl pfc = (PetFoodingControl) petFragment.getActivity().getApplication();
        User userLogged = pfc.getUserLogged().getValue();
        if (userLogged != null) {
            if (holder.pet.getUserId().equals(userLogged.getUserId())) {
                holder.mPetOwnedView.setVisibility(View.VISIBLE);
            }
        }

        holder.mView.setOnClickListener(view -> {
            if (null != mListener) {
                mListener.onListFragmentInteraction(holder.pet);
            }
        });

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView mPetImageView;
        public final TextView mPetNameView;
        public final TextView mPetStatusView;
        public final TextView mPetOwnedView;
        public final ImageButton mDeleteButton;

        public Pet pet;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPetImageView = view.findViewById(R.id.civ_pet_image);
            mPetNameView = view.findViewById(R.id.txt_pet_name);
            mPetStatusView = view.findViewById(R.id.txt_pet_fooding_status);
            mPetOwnedView = view.findViewById(R.id.txt_pet_is_owner);
            mDeleteButton = view.findViewById(R.id.btn_delete_pet);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPetNameView.getText() + "'";
        }
    }
}
