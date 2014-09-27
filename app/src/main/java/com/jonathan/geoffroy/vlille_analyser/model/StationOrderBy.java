package com.jonathan.geoffroy.vlille_analyser.model;

import com.jonathan.geoffroy.vlille_analyser.R;

/**
 * Enumération des façon d'ordonner la liste des stations.
 * Created by geoffroy on 25/09/14.
 */
public enum StationOrderBy {
    NAME(R.id.orderBy_name, "NAME"), STAR(R.id.orderBy_starFirst, "STAR DESC");

    /**
     * L'id du bouton radio permettant de choisir le type d'ordre
     */
    private final int id;

    /**
     * Le type d'ordre associé. <br/>
     * Propriété SQL <code>ORDER BY</code>
     */
    private String order;

    StationOrderBy(int id, String order) {
        this.id = id;
        this.order = order;
    }

    public static StationOrderBy valueOf(int id) {
        for(StationOrderBy o : StationOrderBy.values()) {
            if(o.id == id) {
                return o;
            }
        }
        return null;
    }

    public String getOrder() {
        return order;
    }
}
