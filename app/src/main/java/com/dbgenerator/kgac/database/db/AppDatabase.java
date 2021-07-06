package com.dbgenerator.kgac.database.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dbgenerator.kgac.database.dao.EANMasterDao;
import com.dbgenerator.kgac.database.dao.UserDao;
import com.dbgenerator.kgac.database.entity.EANMaster;
import com.dbgenerator.kgac.database.entity.User;

@Database(entities = {User.class, EANMaster.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EANMasterDao eanMasterDao();
}