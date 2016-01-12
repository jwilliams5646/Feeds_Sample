package com.theplatform.feeds_sample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.theplatform.feeds_sample.FeedModels.Entry;
import com.theplatform.feeds_sample.FeedModels.Feed;

import java.util.ArrayList;

/**
 * Created by john.williams on 1/3/2016.
 */
public class FeedsAdapter extends BaseAdapter {

    private ArrayList<Entry> responses;
    Context context;

    public FeedsAdapter(ArrayList<Entry> responses, Activity context){
        this.responses = responses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return responses.size();
    }

    @Override
    public Object getItem(int position) {
        return responses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Response response;
        if (convertView == null){
            LayoutInflater vi = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.feed_item, null);
            response = new Response();

            response.thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);
            response.title = (TextView)convertView.findViewById(R.id.title);
            response.description = (TextView)convertView.findViewById(R.id.description);
            convertView.setTag(response);
        }else{
            response = (Response)convertView.getTag();
        }

        final Entry content = responses.get(position);
        response.title.setText(content.getTitle());
        //response.description.setText(content.getDescription());
        if(!content.getDefaultThumbnailUrl().isEmpty()) {
            Picasso.with(context)
                    .load(content.getDefaultThumbnailUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.default_background)
                    .into(response.thumbnail);
        }else{
            Picasso.with(context)
                    .load(R.drawable.happy)
                    .into(response.thumbnail);
        }

        response.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.getContent().get(0).getUrl();
                Intent intent = new Intent(context,FeedPlayerActivity.class);
                intent.putExtra("link", content.getContent().get(0).getUrl());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private class Response{
       ImageView thumbnail;
        TextView title;
        TextView description;
    }
}
