package com.androidtutorialpoint.googlemapsnearbyplaces;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.ViewHolder>{
    ArrayList<String> kr=new ArrayList<String>();



    public RecyclerViewAdapter(ArrayList<String> ar) {
        this.kr =ar;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.neww,parent,false);
        ViewHolder viewholder=new ViewHolder(contactView);


        return (viewholder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView name1=holder.name;
        name1.setText(kr.get(position));


    }



    @Override
    public int getItemCount() { return (kr.size());

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name,message;public ImageView im;

        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            im=(ImageView)itemView.findViewById(R.id.image);

        }
    }

}
