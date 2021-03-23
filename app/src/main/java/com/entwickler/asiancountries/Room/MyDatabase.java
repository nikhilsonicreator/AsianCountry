package com.entwickler.asiancountries.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Country.class}, version = 1, exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class MyDatabase extends RoomDatabase {

    public abstract DAO dao();

}
