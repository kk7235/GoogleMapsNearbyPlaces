package com.androidtutorialpoint.googlemapsnearbyplaces;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder>{
    ArrayList<Markerpoints> kr=new ArrayList<>();

Context context;   public static final String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&key=%s&maxheight=100";

    public RecyclerViewAdapter(ArrayList<Markerpoints> ar) {
        this.kr =ar;
    }

    public RecyclerViewAdapter(ArrayList<Markerpoints> ar, Context applicationContext) {this.kr =ar;context=applicationContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.neww,parent,false);
        ViewHolder viewholder=new ViewHolder(contactView);


        return (viewholder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView name1=holder.name;        TextView rating=holder.rating;
        TextView message=holder.message; ImageView i=holder.im;TextView open=holder.open;
        name1.setText(kr.get(position).getPlace()) ;
       rating.setText(kr.get(position).getRating()) ;
       message.setText(kr.get(position).getViccnity()) ;
       // String url = String.format(PHOTO_URL,kr.get(position).getPhoto(),"AIzaSyBMo2RCRnfNKTJxRwUUKG98NZ2QLgPgZIg");
         String url="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+kr.get(position).getPhoto()+"&key=AIzaSyCZ-hLWodR9sjIbf4RXwhDeHCWdZ6Eg7HE";
        /*String url = "https://maps.googleapis.com/maps/api/place/photo?";
        String key = "key=YOUR_BROWSER_KEY";*/
        String sensor = "sensor=true";
        String maxWidth="maxwidth=" + "400";
        String maxHeight = "maxheight=" + "400";
       // url = url + "&" + key + "&" + sensor + "&" + maxWidth + "&" + maxHeight;

String urlbar="https://maps.gstatic.com/mapfiles/place_api/icons/bar-71.png";
if(kr.get(position).getOpen().equals("true"))
{open.setText("Open now");
open.setTextColor(Color.GREEN);}
else
{open.setText("Closed");
    open.setTextColor(Color.parseColor("#a0ed121d"));}

        Glide.with(context).load(url).into(holder.im);

       // Glide.with(context).load(urlbar).into(holder.bar);

    }



    @Override
    public int getItemCount() { return (kr.size());

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView rating,name,message,open;public ImageView im,bar;

        public ViewHolder(View itemView) {
            super(itemView);
            rating=(TextView)itemView.findViewById(R.id.rating);
            name=(TextView)itemView.findViewById(R.id.name);
            message=(TextView)itemView.findViewById(R.id.viccinity);
            im=(ImageView)itemView.findViewById(R.id.imageView);
       //    bar=(ImageView)itemView.findViewById(R.id.bar);
           open=(TextView)itemView.findViewById(R.id.open);

        }
    }

}
