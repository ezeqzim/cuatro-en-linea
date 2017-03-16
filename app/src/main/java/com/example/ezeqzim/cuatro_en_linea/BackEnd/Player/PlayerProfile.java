package com.example.ezeqzim.cuatro_en_linea.BackEnd.Player;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerProfile implements Parcelable {
    private String name;

    public static final Parcelable.Creator<PlayerProfile> CREATOR = new Parcelable.Creator<PlayerProfile>() {
        public PlayerProfile createFromParcel(Parcel parcel) {
            return new PlayerProfile(parcel.readString());
        }

        public PlayerProfile[] newArray(int size) {
            return new PlayerProfile[size];
        }
    };

    public PlayerProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
    }
}
