package com.jonathan.geoffroy.vlille_analyser.model.request;

import android.os.AsyncTask;

import com.jonathan.geoffroy.vlille_analyser.view.activity.StationsActivity;

/**
 * Created by geoffroy on 19/09/14.
 */
public abstract class StationTask extends AsyncTask<Void, Void, Void> {
    protected StationsActivity activity;

    protected StationTask(StationsActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.notifyStationsChanged();
    }
}
