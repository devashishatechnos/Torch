package com.atechnos.torch;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by user on 5/26/2017.
 */

public class SlidingAdapter extends ArrayAdapter {
    private List<SlidingModel> modelList;
    private int resourse;
    private LayoutInflater inflater;
    Context context;

    public SlidingAdapter(Context context, int resource, List<SlidingModel> objects) {
        super(context, resource, objects);
        this.modelList =objects;
        this.resourse=resource;
        this.context=context;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(resourse, null);
        }
        SlidingModel detail = modelList.get(position);
        final ImageView ivAppIcon;
        TextView tvappname;
        final ProgressBar progressBar;
        ivAppIcon = (ImageView)convertView.findViewById(R.id.jyoti);
        tvappname = (TextView)convertView.findViewById(R.id.tvMovie);
        //  progressBar= (ProgressBar) convertView.findViewById(R.id.progressBar);
        tvappname.setText(detail.getProduct_name());
        tvappname.setTextColor(ContextCompat.getColor(context,R.color.TextColor));






        Glide.with(context)
                .load("http://173.254.29.44/appmenu/upload/"+ detail.getImage())
                .transform(new RoundImageTransform(context))
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //  progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(ivAppIcon);

        return convertView;


    }

}
