package org.kathmandulivinglabs.preparepokhara.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.transform.MapEntry;
import com.github.ybq.android.spinkit.style.Wave;

import org.kathmandulivinglabs.preparepokhara.Activity.MainActivity;
import org.kathmandulivinglabs.preparepokhara.FilterParcel;
import org.kathmandulivinglabs.preparepokhara.R;
import org.kathmandulivinglabs.preparepokhara.Realmstore.Bank;
import org.kathmandulivinglabs.preparepokhara.Realmstore.FilterSchema;
import org.kathmandulivinglabs.preparepokhara.Realmstore.Ward;
import org.kathmandulivinglabs.preparepokhara.View.ProgressDialogFragment;
import org.kathmandulivinglabs.preparepokhara.View.RangeWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.apptik.widget.MultiSlider;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Bhawak on 3/11/2018.
 */

public class InsightFragment  extends Fragment {
    onInsightSelected mCallback;
    Boolean ags;
    private LinearLayout parentFilterLayout;
    FilterParcel mapfilter=new FilterParcel();
    private ProgressDialogFragment progressDialogFragment;

    private static Map<String, Boolean> filter = new HashMap<>();
    private static String selectedType = MainActivity.def_type;
    Map<String, String> filter_param;
    private String wardid="all";
    public interface onInsightSelected{
        public void onInsight(Boolean vals);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (onInsightSelected) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement onInsightSelected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.filter_root, container, false);
        Button applyFilter = v.findViewById(R.id.apply_filter);
        parentFilterLayout = v.findViewById(R.id.filter_parent);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        filter_param = new HashMap<>();
        if (getArguments() != null) {
            selectedType=getArguments().getString("selectedType","attractions");
//            if(mapfilter.getFilter_parameter()!=null){
//                filter_param = mapfilter.getFilter_parameter();
//            }

        }

        Realm realm = Realm.getDefaultInstance();
        RealmQuery<FilterSchema> query = realm.where(FilterSchema.class).equalTo("amenity", selectedType);
        RealmResults<FilterSchema> results = query.findAll();
        int spinner_size = realm.where(FilterSchema.class).equalTo("amenity", selectedType).contains("type","single-select").findAll().size();
        int size = results.size();
        int range_size = realm.where(FilterSchema.class).equalTo("amenity", selectedType).contains("type","range").findAll().size();
        View[] title_view, switch_view,spinner_view,range_view,checkbox_view;
        TextView[] title_text, switch_text;
        RangeWidget[] range_bar;
        android.support.v7.widget.AppCompatCheckBox[] checkbox_btn;
        android.support.v7.widget.SwitchCompat[] switch_btn = new SwitchCompat[0];
        android.support.v7.widget.AppCompatSpinner[] spinner_spinner;

        title_view = new View[size];
        title_text = new TextView[size];

        spinner_view = new View[spinner_size];
        spinner_spinner = new AppCompatSpinner[spinner_size];

        range_view = new View[range_size];
        range_bar = new RangeWidget[range_size];
        int i =0,j=0,k=0;
        String[] range = new String[range_size];
        if(parentFilterLayout!=null)
        parentFilterLayout.removeAllViews();
        Map<String,String> switch_selected = new HashMap<>();
        Map<String,String> check_selected = new HashMap<>();
        for(FilterSchema fr : results){
            LayoutInflater title_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            title_view[i]=title_inflater.inflate(R.layout.filter_title, null);
            title_text[i] = title_view[i].findViewById(R.id.filter_title);
            title_text[i].setText(fr.getLabel());
            parentFilterLayout.addView(title_view[i]);
            switch (fr.getType()){
                case "single-select": LayoutInflater single_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        spinner_view[k]=single_inflater.inflate(R.layout.filter_spinner, null);
                                        spinner_spinner[k] = spinner_view[k].findViewById(R.id.spinner_content);
                                        populateWardSpinner(spinner_spinner[k]);
                                        parentFilterLayout.addView(spinner_view[k]);
                                        k++;
                                        break;
                case "range":LayoutInflater range_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                range_view[j]=range_inflater.inflate(R.layout.filter_range,null );
                                range_bar[j] = range_view[j].findViewById(R.id.range_slider);
                                range_bar[j].setRange(fr.getMin(),fr.getMax());
                                range[j]=fr.getDbkey();
                                parentFilterLayout.addView(range_view[j]);
                                j++;
                                break;
                case "multi-select":
                    if(fr.get_boolean()!=null && fr.get_boolean()){
                        RealmList<String> switchValue = fr.getOption_lable();
                        int switch_size = switchValue.size();
                        switch_view =new View[switch_size];
                        switch_btn = new SwitchCompat[switch_size];
                        switch_text = new TextView[switch_size];
                        int switch_num = 0;
                        for (String switch_list:switchValue
                             ) {
                            LayoutInflater switch_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            switch_view[switch_num]=switch_inflater.inflate(R.layout.filter_switch,null );
                            switch_btn[switch_num] = switch_view[switch_num].findViewById(R.id.switch_button);
                            switch_text[switch_num]=switch_view[switch_num].findViewById(R.id.switch_text);
                            switch_text[switch_num].setText(switch_list);
                            //switch_selected.put(fr.getOption_key().get(switch_num),"no");
                            int finalSwitch_num = switch_num;
                            switch_btn[switch_num].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                               @Override
                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                   if(isChecked){
                                       switch_selected.put(fr.getOption_key().get(finalSwitch_num),"yes");
                                   }
                                   else{
                                       switch_selected.remove(fr.getOption_key().get(finalSwitch_num));
                                   }
                               }
                           });
                            parentFilterLayout.addView(switch_view[switch_num]);
                            switch_num++;
                        }
                    }
                    else {
                        RealmList<String> checkValue = fr.getOption_lable();
                        int check_size = checkValue.size();
                        checkbox_view = new View[check_size];
                        checkbox_btn = new AppCompatCheckBox[check_size];
                        int check_num =0;
                        for(String check_list:checkValue){
                            LayoutInflater check_inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            checkbox_view[check_num]=check_inflater.inflate(R.layout.filter_checkbox,null );
                            checkbox_btn[check_num] = checkbox_view[check_num].findViewById(R.id.checkbox_button);
                            checkbox_btn[check_num].setText(check_list);
                            checkbox_btn[check_num].setTag(fr.getOption_key().get(check_num));
                            check_selected.put(check_list,fr.getOption_key().get(check_num));
                            int finalCheck_num = check_num;
                            checkbox_btn[check_num].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked){
                                        check_selected.put(check_list,fr.getOption_key().get(finalCheck_num));
                                    }
                                    else{
                                        check_selected.remove(check_list);
                                    }
                                }
                            });
                            parentFilterLayout.addView(checkbox_view[check_num]);
                            check_num++;
                        }
                    }
                    break;
                default:break;
            }
            i++;
        }
        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    for(int i=0;i<range_size;i++) {
                       filter_param.put(range[i]+"max", String.valueOf(range_bar[i].getMaxValue().intValue()));
                        filter_param.put(range[i]+"min", String.valueOf(range_bar[i].getMinValue().intValue()));
                    }
                for(Map.Entry<String,String> switch_vals:switch_selected.entrySet() ){
                        filter_param.put(switch_vals.getKey(), switch_vals.getValue());
                }
                for(Map.Entry<String,String> check_vals:check_selected.entrySet()){
                        filter_param.put(check_vals.getKey()+"check",check_vals.getValue());
                }
                filter_param.put("wardid",wardid);
                MainActivity.filter_param = filter_param;
                mapfilter.setFilter_parameter(filter_param);

                    ags=true;
                    MapFragment mp = new MapFragment();
//                Bundle args = new Bundle();
//                args.putParcelable("FilterValue",mapfilter);
//                mp.setArguments(args);
//                getFragmentManager().beginTransaction().add(R.id.container,mp ).commit();
                Intent i = new Intent(getActivity().getBaseContext(),MainActivity.class);
                i.putExtra("FilterValue",mapfilter);
                mCallback.onInsight(ags);
               // getActivity().startActivity(i);
            }
        });


//        Fragment MapFragment = new MapFragment();
//        Bundle bundel = new Bundle();
//        bundel.putParcelable("filterdata",mapfilter);
//        MapFragment.setArguments(bundel);
        return v;
    }


    public void updateView(){
        Fragment frg = null;
        String tagfeature = "android:switcher:" + R.id.viewpager + ":" + 1;
        frg = (InsightFragment) getFragmentManager().findFragmentByTag(tagfeature);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void populateWardSpinner(AppCompatSpinner sItems){
        int wardId;
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Ward> ward = realm.where(Ward.class).findAll().sort("number");
        List<String> wardArray =  new ArrayList<String>();
        wardArray.add("All Wards");
        for(int i =0 ; i<ward.size();i++) {
            wardArray.add(ward.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_dropdown_item, wardArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


//        if(adapter!=null)
//            adapter.clear();
        sItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();
       // sItems.setSelection(wardId,true);
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                wardId_hos=position;
                if(position>0){
                    wardid=ward.get(position-1).getOsmID();
                }
                else wardid="all";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}


