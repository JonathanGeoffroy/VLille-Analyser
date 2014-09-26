package com.jonathan.geoffroy.vlille_analyser.model.refresher;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import java.util.List;

/**
 * Created by jonathan on 20/09/14.
 */
public class AllStationsDetailsRefresher extends StationDetailsRefresher {
    private static final int DEFAULT_NB_STATIONS_TO_REFRESH = 20;
    private int stationPosition;
    private int nbStationsToRefresh;
    public AllStationsDetailsRefresher(StationsActivity activity) {
        super(activity);
        stationPosition = 0;
        nbStationsToRefresh = DEFAULT_NB_STATIONS_TO_REFRESH;
    }

    @Override
    protected List<Station> getStationsToRefresh() {
        List<Station> allStations = activity.getStations();
        int size = allStations.size();

        /*
         * Fix bug: si on rafraichi au moment où on change la taille de la liste (exemple: on n'affiche que les favoris).
         * Raison:
         *  Les Timers Android ne peuvent être arretés si ils sont déjà prévu.
         *  Or la taille de la liste peut être modifié par l'utilisateur.
         *  Donc stationPosition peut être supérieur à la taille de la liste.
         *  D'où: IllegalArgumentException
         *  Solution: repartir de la première station
         */
        if (stationPosition >= size) {
            stationPosition = 0;
        }

        int lastStationToRefresh = Math.min(stationPosition + nbStationsToRefresh, size);
        List<Station> toRefresh = allStations.subList(stationPosition, lastStationToRefresh);
        stationPosition = lastStationToRefresh % allStations.size();

        return toRefresh;
    }
}
