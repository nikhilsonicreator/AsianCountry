package com.entwickler.asiancountries.Model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CountryClass implements Serializable {

    String region ,subregion ,name ,flag ,capital;
    JSONArray borders;
    Map<Integer, List<String>> languages;
    long population;

    public CountryClass(String name, String capital, String flag, String region, String subregion, JSONArray borders, Map<Integer,List<String>> languages, long population) {
        this.region = region;
        this.subregion = subregion;
        this.name = name;
        this.flag = flag;
        this.capital = capital;
        this.borders = borders;
        this.languages = languages;
        this.population = population;
    }

    public CountryClass() {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public JSONArray getBorders() {
        return borders;
    }

    public void setBorders(JSONArray borders) {
        this.borders = borders;
    }

    public Map<Integer,List<String>> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<Integer,List<String>> languages) {
        this.languages = languages;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }
}
