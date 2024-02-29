package com.tdtu.starrail.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Seat implements Parcelable {

    private int id;
    private String seatNumber;
    private boolean isSelected;

    public Seat() {
    }

    public Seat(int id, String seatNumber, boolean isSelected) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // Parcelable implementation
    protected Seat(Parcel in) {
        id = in.readInt();
        seatNumber = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(seatNumber);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Seat> CREATOR = new Creator<Seat>() {
        @Override
        public Seat createFromParcel(Parcel in) {
            return new Seat(in);
        }

        @Override
        public Seat[] newArray(int size) {
            return new Seat[size];
        }
    };

    // Rest of the class, including constructors, getters, and setters
    // ...
}
