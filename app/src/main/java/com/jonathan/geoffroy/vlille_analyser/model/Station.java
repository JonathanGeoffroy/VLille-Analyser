package com.jonathan.geoffroy.vlille_analyser.model;

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

    public Station(int id, String name) {
        this.id = id;
        this.name = name;
        this.star = false;
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
}
