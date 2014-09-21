package com.jonathan.geoffroy.vlille_analyser.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jonathan.geoffroy.vlille_analyser.R;
import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import java.util.List;

/**
 * Created by geoffroy on 15/09/14.
 */
public class StationAdapter extends BaseAdapter {
    private List<Station> list;
    private LayoutInflater layoutInflater;
    private StationsActivity context;

    public StationAdapter(Context c) {
        layoutInflater = LayoutInflater.from(c);
        StationsActivity activity = (StationsActivity) c;
        list = activity.getStations();
        context = activity;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.station_item_list, null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name_textview);
            viewHolder.nbBikes = (TextView) convertView.findViewById(R.id.nbbikes_textview);
            viewHolder.nbFree = (TextView) convertView.findViewById(R.id.nbfree_textview);
            viewHolder.star = (CheckBox) convertView.findViewById(R.id.star_toggle);

            viewHolder.star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    context.onStarChanged(list.get(position), isChecked);
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Station station = list.get(position);
        viewHolder.name.setText(station.getName());
        viewHolder.star.setChecked(station.isStar());
        viewHolder.nbBikes.setText(String.valueOf(station.getNbBikes()));
        viewHolder.nbFree.setText(String.valueOf(station.getNbFree()));

        return convertView;
    }

    private class ViewHolder {
        private TextView name;
        private CheckBox star;
        private TextView nbBikes;
        private TextView nbFree;

        public ViewHolder() {
        }
    }


}