package org.kathmandulivinglabs.preparepokhara.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kathmandulivinglabs.preparepokhara.Adapter.FragmentAdapter;
import org.kathmandulivinglabs.preparepokhara.Customclass.CustomViewPager;
import org.kathmandulivinglabs.preparepokhara.R;

/**
 * Created by Bhawak on 3/11/2018.
 */

public class HomeFragment extends Fragment {
    public HomeFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_view, container, false);
        CustomViewPager viewPager = (CustomViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) v.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);

        return v;
    }
    // Add Fragments to Tabs
    private void setupViewPager(CustomViewPager viewPager) {


//        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager());
//        adapter.addFragment(new MapFragment(),"Map");
//        adapter.addFragment(new InsightFragment(),"Map Feature");
//        viewPager.setAdapter(adapter);

    }

}
