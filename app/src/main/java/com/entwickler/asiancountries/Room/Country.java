package com.entwickler.asiancountries.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;


@Entity
public class Country {

    @PrimaryKey (autoGenerate =  true)
    int countryId;
    String name;
    String capital;
    String flag;
    String region;
    String subregion;
    long population;
    @TypeConverters({DataConverter.class})
    Map<Integer,List<String>> languages;
    @TypeConverters(DataConverter.class)
    JSONArray borders;

    public Country(String name, String capital,String flag, String region, String subregion, JSONArray borders, Map<Integer,List<String>> languages, long population) {
        this.name = name;
        this.capital = capital;
        this.flag = flag;
        this.region = region;
        this.subregion = subregion;
        this.population = population;
        this.languages = languages;
        this.borders = borders;
    }


    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public Map<Integer, List<String>> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<Integer, List<String>> languages) {
        this.languages = languages;
    }

    public JSONArray getBorders() {
        return borders;
    }

    public void setBorders(JSONArray borders) {
        this.borders = borders;
    }
}
