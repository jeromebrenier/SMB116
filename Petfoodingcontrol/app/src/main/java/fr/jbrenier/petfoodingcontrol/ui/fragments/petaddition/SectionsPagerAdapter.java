package fr.jbrenier.petfoodingcontrol.ui.fragments.petaddition;

import android.content.Context;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petaddition.feeders.PetFeedersFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petaddition.general.PetGeneralFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petfoodsettings.PetFoodSettingsFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_text_pet_general,
            R.string.tab_text_pet_food,
            R.string.tab_text_pet_feeders};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment itemToShow = null;
        switch (position) {
            case 0 :
                itemToShow = PetGeneralFragment.newInstance();
                break;
            case 1 :
                itemToShow = PetFoodSettingsFragment.newInstance();
                break;
            case 2 :
                itemToShow = new PetFeedersFragment();
        }
        return itemToShow;
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // return PlaceholderFragment.newInstance(position + 1);
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