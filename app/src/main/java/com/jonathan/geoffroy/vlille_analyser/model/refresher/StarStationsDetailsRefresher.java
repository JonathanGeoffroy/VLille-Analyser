package com.jonathan.geoffroy.vlille_analyser.model.refresher;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jonathan on 20/09/14.
 */
public class StarStationsDetailsRefresher extends StationDetailsRefresher {
    public StarStationsDetailsRefresher(StationsActivity activity) {
        super(activity);
    }

    @Override
    protected List<Station> getStationsToRefresh() {
        List<Station> stations = activity.getStations();
        List<Station> starredStations = new LinkedList<Station>();
        for(Station s : stations) {
            if(s.isStar()) {
                starredStations.add(s);
            }
        }
        return starredStations;
    }
}
