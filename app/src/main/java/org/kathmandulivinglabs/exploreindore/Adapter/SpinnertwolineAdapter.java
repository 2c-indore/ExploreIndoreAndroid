package org.kathmandulivinglabs.exploreindore.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.kathmandulivinglabs.exploreindore.R;

import java.util.List;

public class SpinnertwolineAdapter extends ArrayAdapter {
    List<String> wardname;
    List<String> wardnumber;
    Context mContext;

    public SpinnertwolineAdapter(@NonNull Context context, List<String> wardname, List<String> wardnumber) {
        super(context, R.layout.spinner_twotv);
        this.wardname = wardname;
        this.wardnumber = wardnumber;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
      ViewHolder holder = new ViewHolder();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_twotv, null);
            holder.text1 = (TextView)convertView.findViewById(R.id.spinnerText1);
            holder.text2 = (TextView)convertView.findViewById(R.id.spinnerText2);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.text1.setText(wardname.get(position));
        holder.text2.setText(wardnumber.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getView(position,convertView,parent);
    }

    @Override
    public int getCount() {
        return wardname.size();
    }

    static class ViewHolder{
        TextView text1;
        TextView text2;
    }
}
