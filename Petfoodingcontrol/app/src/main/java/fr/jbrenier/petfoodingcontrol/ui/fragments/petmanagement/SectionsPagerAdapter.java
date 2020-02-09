package fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fr.jbrenier.petfoodingcontrol.R;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.feeders.PetFeedersFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.general.PetGeneralFragment;
import fr.jbrenier.petfoodingcontrol.ui.fragments.petmanagement.petfoodsettings.PetFoodSettingsFragment;

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
    private final PetGeneralFragment.OnSaveButtonClickListener listener;

    public SectionsPagerAdapter(Context context, PetGeneralFragment.OnSaveButtonClickListener
            listener, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.listener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment itemToShow = null;
        switch (position) {
            case 0 :
                PetGeneralFragment petGeneralFragment = new PetGeneralFragment();
                petGeneralFragment.setCallback(listener);
                itemToShow = petGeneralFragment;
                break;
            case 1 :
                itemToShow = new PetFoodSettingsFragment();
                break;
            case 2 :
                itemToShow = new PetFeedersFragment();
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