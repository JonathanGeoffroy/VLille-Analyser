package com.jonathan.geoffroy.vlille_analyser.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by geoffroy on 15/09/14.
 */
public class Station implements Serializable {
    private int id;
    private String name;
    private boolean star;
    private int nbBikes;
    private int nbFree;

    private LatLng position;

    public Station(int id, String name, boolean star, int nbBikes, int nbFree, LatLng position) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.nbBikes = nbBikes;
        this.nbFree = nbFree;
        this.position = position;
    }

    public Station(int id, String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.star = false;
        this.position = new LatLng(lat, lng);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public int getNbBikes() {
        return nbBikes;
    }

    public void setNbBikes(int nbBikes) {
        this.nbBikes = nbBikes;
    }

    public int getNbFree() {
        return nbFree;
    }

    public void setNbFree(int nbFree) {
        this.nbFree = nbFree;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
