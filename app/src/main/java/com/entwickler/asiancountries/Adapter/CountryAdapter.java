package com.entwickler.asiancountries.Adapter;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.entwickler.asiancountries.DisplayActivity;
import com.entwickler.asiancountries.Model.CountryClass;
import com.entwickler.asiancountries.R;
import com.entwickler.asiancountries.SvgDecoder;
import com.entwickler.asiancountries.SvgDrawableTranscoder;
import com.entwickler.asiancountries.SvgSoftwareLayerSetter;


import org.json.JSONException;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CountryAdapter  extends RecyclerView.Adapter<CountryAdapter.MyHolder> {

    private Context context;
    private List<CountryClass> countryList;

    public CountryAdapter(Context context, List<CountryClass> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CountryAdapter.MyHolder holder, final int position) {

        final CountryClass countryClass = countryList.get(position);

        holder.item_layout_name.setText(countryClass.getName());
        holder.item_layout_population.setText("pop : "+countryClass.getPopulation());
        holder.item_layout_capital.setText(countryClass.getCapital());

        try {
            GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(context)
                    .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                    .from(Uri.class)
                    .as(SVG.class)
                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                    .sourceEncoder(new StreamEncoder())
                    .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                    .decoder(new SvgDecoder())
                    .listener(new SvgSoftwareLayerSetter<Uri>());

            requestBuilder.diskCacheStrategy(DiskCacheStrategy.NONE)
                    .load(Uri.parse(countryClass.getFlag()))
                    .into(holder.item_layout_imageView);
        }
        catch (Exception e){
            e.printStackTrace();

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int adapterPosition = holder.getAdapterPosition();
                CountryClass countryClass1 = countryList.get(adapterPosition);

                Intent intent = new Intent(context, DisplayActivity.class);
                intent.putExtra("Name",countryClass1.getName());
                intent.putExtra("Capital",countryClass1.getCapital());
                intent.putExtra("Region",countryClass1.getRegion());
                intent.putExtra("Subregion",countryClass1.getSubregion());
                intent.putExtra("Population",countryClass1.getPopulation());

                String borders="",languages="";
                try {
                for (int i=0;i<countryClass1.getBorders().length();i++){

                        borders+=countryClass1.getBorders().getString(i);

                        if (i!=countryClass1.getBorders().length()-1){
                            borders+=", ";
                        }

                    }
                intent.putExtra("Borders",borders);


                Map<Integer,List<String>> map =countryClass1.getLanguages();

                for (int i=0;i<map.size();i++){
                    List<String> list = map.get(i);
                    String iso639_1 = list.get(0);
                    String iso639_2 = list.get(1);
                    String name_lang = list.get(2);
                    String nativeName = list.get(3);

                    languages += "iso639_1- "+ iso639_1 +"\niso639_2- "+iso639_2+"\nName- "+name_lang+"\nNative Name- "+nativeName;

                    if (i!=map.size()-1){
                        languages+="\n\n";
                    }
                }

                intent.putExtra("Languages",languages);

                }
                catch (JSONException e) {
                        e.printStackTrace();

                }

                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return countryList.size();
    }


   public static class MyHolder extends RecyclerView.ViewHolder {

        TextView item_layout_name, item_layout_capital, item_layout_population;
        CircleImageView item_layout_imageView;

        public MyHolder(View itemView) {
            super(itemView);

            item_layout_name = itemView.findViewById(R.id.item_layout_name);
            item_layout_capital = itemView.findViewById(R.id.item_layout_capital);
            item_layout_population = itemView.findViewById(R.id.item_layout_population);
            item_layout_imageView = itemView.findViewById(R.id.item_layout_imageview);

        }
    }

}
