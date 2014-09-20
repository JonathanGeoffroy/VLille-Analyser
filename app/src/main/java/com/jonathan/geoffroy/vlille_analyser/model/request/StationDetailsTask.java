package com.jonathan.geoffroy.vlille_analyser.model.request;

import android.util.Log;

import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
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
        Log.i("VLille-Http", "try to find info for " + station.getName());
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
}
