package com.dubalais.android.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dubalais.android.R;
import com.dubalais.android.models.Advert;

import java.util.ArrayList;

/**
 * Created by nikola on 01/03/18.
 */

public class SwipeCardAdapter extends ArrayAdapter<Advert> {

    ArrayList<Advert> card_list;

    public SwipeCardAdapter(Context context, int resource, ArrayList<Advert> card_list) {
        super(context, resource);
        this.card_list = card_list;
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){

        TextView t_titre=(TextView)(contentView.findViewById(R.id.texttitre));
        String titre = card_list.get(position).getTitle();
        titre = titre.substring(0,1).toUpperCase() + titre.substring(1);
        t_titre.setText(titre);

        TextView t_prix = (TextView)(contentView.findViewById(R.id.textprix));
        t_prix.setText(card_list.get(position).getstringprice()+"€");

        TextView t_annonceur = (TextView)(contentView.findViewById(R.id.textanonceur ));
        t_annonceur .setText(card_list.get(position).getadvertiser());

        TextView t_ville=(TextView)(contentView.findViewById(R.id.textville));
        t_ville.setText(card_list.get(position).getville());

        TextView t_desc=(TextView)(contentView.findViewById(R.id.textdesc));
        t_desc.setText(card_list.get(position).getchore());

        return contentView;
    }


    @Override
    public int getCount() {
        return this.card_list.size();
    }

}

