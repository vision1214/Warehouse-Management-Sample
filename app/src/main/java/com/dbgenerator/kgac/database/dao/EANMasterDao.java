package com.dbgenerator.kgac.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dbgenerator.kgac.database.entity.EANMaster;

import java.util.List;

@Dao
public interface EANMasterDao {
    @Query("SELECT * FROM eanmaster")
    List<EANMaster> getAll();

    @Query("SELECT * FROM eanmaster WHERE code IN (:codes)")
    List<EANMaster> loadAllByIds(int[] codes);

    @Query("SELECT * FROM eanmaster WHERE code IN (:code)")
    EANMaster loadId(String code);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EANMaster> codes);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(EANMaster code);

    @Delete
    void delete(EANMaster code);

    @Query("DELETE FROM eanmaster")
    public void deleteAll();
}
