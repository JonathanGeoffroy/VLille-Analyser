package com.jonathan.geoffroy.vlille_analyser.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by geoffroy on 15/09/14.
 */
public class Station implements Parcelable {
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

    public Station(Parcel parcel) {
        id = parcel.readInt();
        name = parcel.readString();
        nbBikes = parcel.readInt();
        nbFree = parcel.readInt();
        star = parcel.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(nbBikes);
        parcel.writeInt(nbFree);
        parcel.writeByte((byte) (star ? 1 : 0));
    }
}
