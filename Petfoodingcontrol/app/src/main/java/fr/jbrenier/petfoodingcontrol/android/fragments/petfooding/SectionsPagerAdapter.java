package fr.jbrenier.petfoodingcontrol.android.fragments.petfooding;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.android.fragments.petfooding.food.PetFoodFragment;
import fr.jbrenier.petfoodingcontrol.android.fragments.petfooding.weight.PetWeightFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_text_pet_fooding_food,
            R.string.tab_text_pet_fooding_weight};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        Fragment itemToShow = null;
        if (position == 0) {
            itemToShow = new PetFoodFragment();
        } else {
            itemToShow = new PetWeightFragment();
        }
        return itemToShow;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}