package fr.jbrenier.petfoodingcontrol.ui.fragments.main.pets;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Pet} and makes a call to the
 * specified {@link PetFragment.OnListFragmentInteractionListener}.
 */
public class MyPetRecyclerViewAdapter extends RecyclerView.Adapter<MyPetRecyclerViewAdapter.ViewHolder> {

    private final List<Pet> mUserPets;

    private final PetFragment.OnListFragmentInteractionListener mListener;

    public MyPetRecyclerViewAdapter(List<Pet> userPets,
                                    PetFragment.OnListFragmentInteractionListener listener) {
        mUserPets = userPets;
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
        holder.mIdView.setText(mUserPets.get(position).getId());
        holder.mContentView.setText(mUserPets.get(position).getName());

        holder.mView.setOnClickListener(view -> {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.pet);
                }
        });
    }

    @Override
    public int getItemCount() {
        return mUserPets == null ? 0 : mUserPets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Pet pet;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
