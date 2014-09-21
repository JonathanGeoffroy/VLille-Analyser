package com.jonathan.geoffroy.vlille_analyser.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jonathan.geoffroy.vlille_analyser.R;
import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.model.request.StationDetailsTask;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;
import com.jonathan.geoffroy.vlille_analyser.view.adapter.StationAdapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class StationFragment extends Fragment implements AbsListView.OnItemClickListener {
    private static final String ARG_STATIONS = "stations";
    private List<Station> stations;

    private OnStationFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private BaseAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StationFragment() {
    }

    // TODO: Rename and change types of parameters
    public static StationFragment newInstance() {
        StationFragment fragment = new StationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mAdapter = new StationAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        // Call onAutomaticRefreshStateChanged when user clicked on automatic_refresh checkbox
        CheckBox automaticRefreshCheckbox = (CheckBox) view.findViewById(R.id.auto_refreshing_cb);
        automaticRefreshCheckbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mListener.onAutomaticRefreshChanged(isChecked);
                    }
                }
        );
        mListener.onAutomaticRefreshChanged(automaticRefreshCheckbox.isChecked());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStationFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Click", "item " + position + " is cliked");
        StationsActivity activity = (StationsActivity) getActivity();
        Station station = (Station) mListView.getAdapter().getItem(position);
        new StationDetailsTask(activity, station).execute();
    }

    public void notifyStationsChanged() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStationFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onStarChanged(Station position, boolean isChecked);

        public void onAutomaticRefreshChanged(boolean isChecked);
    }

}
