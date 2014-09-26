package com.jonathan.geoffroy.vlille_analyser.model.request;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Exécute <code>StationDetailsParser.execute()</code> dans un Thread secondaire, afin de ne pas bloquer l'interface graphique<br/>
 * Created by geoffroy on 19/09/14.
 */
public class StationDetailsTask extends StationTask {
    private Station station;

    public StationDetailsTask(StationsActivity activity, Station station) {
        super(activity);
        this.station = station;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        StationDetailsParser stationDetailParser = new StationDetailsParser(new DefaultHttpClient());
        try {
            stationDetailParser.execute(station);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.notifyStationUpdated(station);
    }
}
