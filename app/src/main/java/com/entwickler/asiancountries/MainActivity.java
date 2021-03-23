package com.entwickler.asiancountries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.entwickler.asiancountries.Adapter.CountryAdapter;
import com.entwickler.asiancountries.Model.CountryClass;
import com.entwickler.asiancountries.Room.Country;
import com.entwickler.asiancountries.Room.MyDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String api;
    private static Context context;
    private static List<CountryClass> country_list;
    private RecyclerView recyclerView;
    private static ProgressDialog progressDialog;
    private static CountryAdapter countryAdapter;
    private Button delete_btn;
    private static boolean del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        country_list = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        delete_btn = findViewById(R.id.delete_btn);
        del =false;

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyDatabase myDatabase = Room.databaseBuilder(context, MyDatabase.class, "CountryDB").allowMainThreadQueries().build();
                List<Country> countries_data = myDatabase.dao().getCountry();
                if (countries_data==null || countries_data.isEmpty()){
                    Toast.makeText(MainActivity.this, "Database Already Empty", Toast.LENGTH_SHORT).show();
                }

                else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("You want to delete local database");
                    builder.setTitle("Are you sure ?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            myDatabase.dao().deleteAll();
                            Toast.makeText(MainActivity.this, "Database Deleted Successfully", Toast.LENGTH_SHORT).show();
                            if (del){
                                country_list.clear();
                                countryAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {

                        }
                    });

                  builder.show();
                }

            }
        });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        countryAdapter = new CountryAdapter(this, country_list);
        recyclerView.setAdapter(countryAdapter);
        country_list.clear();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);


        DownloadTask dt = new DownloadTask();
        try {
            progressDialog.show();
            api = "https://restcountries.eu/rest/v2/region/Asia";
            dt.execute(api);
        } catch (Exception e) {
            progressDialog.dismiss();
            Log.i("error", e.getMessage() + "");
            Toast.makeText(this, "Unable to Load Results", Toast.LENGTH_SHORT).show();
        }

    }

    public static class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... Params) {
            URL url;
            HttpURLConnection urlConnection;
            String result = "";
            try {
                url = new URL(Params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while (data != -1) {
                    char c = (char) data;
                    result += c;
                    data = reader.read();
                }


                JSONArray jsonArray = new JSONArray(result);

                if (jsonArray.length() == 0) {
                    return "no result";
                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = jsonArray.getJSONObject(i);

                    String name = obj.getString("name");
                    String capital = obj.getString("capital");
                    String flag = obj.getString("flag");
                    String region = obj.getString("region");
                    String subregion = obj.getString("subregion");
                    long population = obj.getLong("population");
                    JSONArray borders = obj.getJSONArray("borders");
                    JSONArray lang = obj.getJSONArray("languages");

                    Map<Integer, List<String>> languages = new HashMap<>();

                    for (int j = 0; j < lang.length(); j++) {

                        JSONObject obj1 = lang.getJSONObject(j);
                        String iso639_1 = obj1.getString("iso639_1");
                        String iso639_2 = obj1.getString("iso639_2");
                        String name_lang = obj1.getString("name");
                        String nameNative = obj1.getString("nativeName");

                        List<String> list = new ArrayList<>();
                        list.add(iso639_1);
                        list.add(iso639_2);
                        list.add(name_lang);
                        list.add(nameNative);

                        languages.put(j, list);
                    }


                    MyDatabase myDatabase = Room.databaseBuilder(context, MyDatabase.class, "CountryDB").build();

                    List<Country> countries_data = myDatabase.dao().getCountry();
                    if (countries_data.size() != 50) {
                        Country country = new Country(name, capital, flag, region, subregion, borders, languages, population);
                        myDatabase.dao().countryInsertion(country);
                    }

                    CountryClass countryClass = new CountryClass(name, capital, flag, region, subregion, borders, languages, population);

                    country_list.add(countryClass);

                    myDatabase.close();
                }


                return " ";

            } catch (IOException e) {
                Log.i("error", e.getMessage() + "");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            if (s != null) {
                if (s.equals("no result")) {
                    Toast.makeText(context, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
                countryAdapter.notifyDataSetChanged();
            }
            if (country_list.isEmpty()) {

                MyDatabase myDatabase = Room.databaseBuilder(context, MyDatabase.class, "CountryDB").allowMainThreadQueries().build();
                List<Country> countries_data = myDatabase.dao().getCountry();
                if (countries_data.size() == 50) {

                    for (int k = 0; k < countries_data.size(); k++) {

                        CountryClass countryClass = new CountryClass(countries_data.get(k).getName(),
                                countries_data.get(k).getCapital(), countries_data.get(k).getFlag(),
                                countries_data.get(k).getRegion(), countries_data.get(k).getSubregion(), countries_data.get(k).getBorders(),
                                countries_data.get(k).getLanguages(), countries_data.get(k).getPopulation());

                        country_list.add(countryClass);

                    }
                    del = true;
                    countryAdapter.notifyDataSetChanged();

                    myDatabase.close();

                } else {
                    Toast.makeText(context, "No Data In Database", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}