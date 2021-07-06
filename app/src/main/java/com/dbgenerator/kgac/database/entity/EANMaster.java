package com.dbgenerator.kgac.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EANMaster {

    @PrimaryKey
    @ColumnInfo(name = "code")
    @NonNull
    public String code;

    public EANMaster(@NonNull String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "EANMaster{" +
                "code='" + code + '\'' +
                '}';
    }
}
