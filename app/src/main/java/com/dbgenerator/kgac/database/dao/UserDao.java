package com.dbgenerator.kgac.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dbgenerator.kgac.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE username LIKE :username " + "LIMIT 1")
    User findByUsername(String username);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    public void deleteAll();
}
