package com.jonathan.geoffroy.vlille_analyser.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jonathan.geoffroy.vlille_analyser.R;
import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoogleMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoogleMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoogleMapFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private MapView mapView;
    private List<Marker> markers;

    public GoogleMapFragment() {
        markers = new LinkedList<Marker>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GoogleMapFragment.
     */
    public static GoogleMapFragment newInstance() {
        GoogleMapFragment fragment = new GoogleMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_google_map, container, false);

        mapView = (MapView) v.findViewById(R.id.googleMap);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(this.getActivity());

        return v;
    }

    public void notifyStationsChanged() {
        addStations();
    }

    public void notifyStationUpdated(Station station) {
        StationsActivity activity = (StationsActivity) getActivity();
        List<Station> stations = activity.getStations();
        int position = stations.indexOf(station);
        if (position < markers.size()) {
            Marker marker = markers.get(position);
            marker.setSnippet(station.getNbBikes() + "bikes, " + station.getNbFree() + " free");
        } else {
            addMarker(mapView.getMap(), station);
        }
    }

    private void addStations() {
        StationsActivity activity = (StationsActivity) getActivity();
        List<Station> stations = activity.getStations();

        GoogleMap map = mapView.getMap();
        map.clear();
        markers.clear();
        Marker marker;
        for (Station station : stations) {
            addMarker(map, station);
        }
    }

    private void addMarker(GoogleMap map, Station station) {
        Marker marker = map.addMarker(
                new MarkerOptions()
                        .position(station.getPosition())
                        .title(station.getName())
                        .snippet(station.getNbBikes() + "bikes, " + station.getNbFree() + " free")
        );
        markers.add(marker);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
    public interface OnFragmentInteractionListener {
    }

}
