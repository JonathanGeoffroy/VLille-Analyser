package com.jonathan.geoffroy.vlille_analyser.model.db;

import android.provider.BaseColumns;

/**
 * Contrat de la base de données
 * Permet de formatter les données pour les ajouter / récupérer dans la base de données
 * Created by jonathan on 20/09/14.
 */
public class StationContract {
    public StationContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class StationEntry implements BaseColumns {
        public static final String TABLE_NAME = "Station";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String NBBIKES = "nbBikes";
        public static final String NBFREE = "nbFree";
        public static final String STAR = "star";
        public static final String LAT = "latitude";
        public static final String LNG = "longitude";

    }

}
