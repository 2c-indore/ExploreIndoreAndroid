package org.kathmandulivinglabs.exploreindore.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import org.kathmandulivinglabs.exploreindore.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Bhawak on 6/10/18.
 */
public class ExpandableMenuAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ExpandedMenuModel> mListDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<ExpandedMenuModel, List<String>> mListDataChild;
    ExpandableListView expandList;

    public ExpandableMenuAdapter(Context context, List<ExpandedMenuModel> listDataHeader, HashMap<ExpandedMenuModel, List<String>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }
    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;
//        if ((groupPosition != 10)&&(groupPosition != 11)&&(groupPosition != 12)) {
            childCount = this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                    .size();
//        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpandedMenuModel headerTitle = (ExpandedMenuModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listheader, null);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.submenu);
        ImageView headerIcon = (ImageView) convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.NORMAL);
        lblListHeader.setText(headerTitle.getIconName());
//        if ((groupPosition == 10)||(groupPosition == 11)||(groupPosition == 12)) {
//            headerIcon.setImageResource(headerTitle.getIconImg());
//        }
       // headerIcon.setImageResource(headerTitle.getIconImg());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_submenu, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.submenu);

        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
