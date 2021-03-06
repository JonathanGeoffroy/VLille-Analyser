package com.jonathan.geoffroy.vlille_analyser.model.request;

import android.widget.Toast;

import com.jonathan.geoffroy.vlille_analyser.R;
import com.jonathan.geoffroy.vlille_analyser.model.Station;
import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Exécute <code>AllStationParser.execute()</code> dans un Thread secondaire, afin de ne pas bloquer l'interface graphique<br/>
 * Created by geoffroy on 19/09/14.
 */
public class AllStationsTask extends StationTask {

    private final List<Station> stations;

    public AllStationsTask(StationsActivity activity, List<Station> stations) {
        super(activity);
        this.stations = stations;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        AllStationsParser parser = new AllStationsParser(new DefaultHttpClient());
        try {
            parser.execute(stations);
        } catch (Exception e) {
            e.printStackTrace();
            this.cancel(true);
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(activity, R.string.parse_problem, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.notifyStationsAdded();
    }
}
