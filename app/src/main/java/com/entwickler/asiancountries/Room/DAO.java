package com.entwickler.asiancountries.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {

    @Insert
    public void countryInsertion(Country country);

    @Query("Select * From Country")
    List<Country> getCountry();

    @Query("Delete From Country")
    void deleteAll();
}