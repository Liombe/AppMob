package com.dubalais.android.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dubalais.android.R;
import com.dubalais.android.models.Advert;
import com.dubalais.android.viewholder.AdvertViewHolder;

import java.util.List;

/**
 * Created by leach on 08/03/2018.
 */

public class AdvertAdapter extends ArrayAdapter<Advert> {
    //tweets est la liste des models à afficher

    public AdvertAdapter(Context context, List<Advert> ads) {
        super(context, 0, ads);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_advert,parent, false);
        }

        AdvertViewHolder viewHolder = (AdvertViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new AdvertViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.txtTitle);
            viewHolder.price = (TextView) convertView.findViewById(R.id.txtPrice);
            viewHolder.date = (TextView) convertView.findViewById(R.id.txtDate);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Advert advert = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.title.setText(advert.getTitle());
        viewHolder.price.setText(advert.getPrice().toString());
        viewHolder.date.setText(advert.getDate());

        return convertView;
    }

}
