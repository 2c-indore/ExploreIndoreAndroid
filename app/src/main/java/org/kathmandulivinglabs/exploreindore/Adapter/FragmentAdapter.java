package org.kathmandulivinglabs.exploreindore.Adapter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.kathmandulivinglabs.exploreindore.Activity.MainActivity;
import org.kathmandulivinglabs.exploreindore.Fragment.InsightFragment;
import org.kathmandulivinglabs.exploreindore.Fragment.MapFragment;

import java.lang.ref.WeakReference;
import java.util.List;

import java.util.ArrayList;

/**
 * Created by Bhawak on 3/11/2018.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final Bundle fragmentBundle;
    private FragmentManager mFragmentManager;

    public FragmentAdapter(FragmentManager fm, Bundle data) {
        super(fm);
        fragmentBundle = data;
        this.mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment fr = new Fragment();
        fr.setArguments(this.fragmentBundle);
        return  mFragmentList.get(position);
    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public int getItemPosition(Object object) {
        if(object instanceof InsightFragment) {
            Log.d("inside", "getItemPosition: ");
            ((InsightFragment) object).updateView();
            return super.getItemPosition(object);
        }
        else if(object instanceof MapFragment && MainActivity.updateMapView) {
            MainActivity.updateMapView=false;
            return POSITION_NONE;
        }
       else return POSITION_UNCHANGED;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return mFragmentTitleList.get(position);
    }

}
