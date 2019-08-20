package org.kathmandulivinglabs.exploreindore.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.kathmandulivinglabs.exploreindore.Fragment.MapFragment;
import org.kathmandulivinglabs.exploreindore.R;

import java.util.ArrayList;
import java.util.Locale;

public class SearchListAdapter extends BaseAdapter {
    public static String searchText = "";
    private Context context;
    private ArrayList<MapFragment.Search> items;

    public SearchListAdapter(Context context, ArrayList<MapFragment.Search> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MapFragment.Search search = items.get(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.layout_search_item, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        if (searchText.length() > 0) {
            setHighlightText(txtTitle, search.toString(), searchText);
        } else {
            txtTitle.setText(search.toString());
        }
        return convertView;
    }

    private void setHighlightText(TextView textView, String originalText, String searchText) {
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(originalText);
        int i = originalText.toLowerCase(Locale.getDefault()).indexOf(searchText);
        if (i != -1) {
            spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimaryDark)), i,
                    i + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanText.setSpan(new StyleSpan(Typeface.BOLD), i,
                    i + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spanText, TextView.BufferType.SPANNABLE);
        } else {
            textView.setText(originalText, TextView.BufferType.NORMAL);
        }
    }
}
