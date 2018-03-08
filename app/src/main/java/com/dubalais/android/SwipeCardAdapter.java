package com.dubalais.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dubalais.android.R;

import java.util.ArrayList;

/**
 * Created by nikola on 01/03/18.
 */

    public class SwipeCardAdapter extends ArrayAdapter<annonce> {

        ArrayList<annonce> card_list;

        public SwipeCardAdapter(Context context, int resource, ArrayList<annonce> card_list) {
            super(context, resource);
            this.card_list = card_list;
        }

        @Override
        public View getView(int position, final View contentView, ViewGroup parent){

            TextView tv_card_number = (TextView)(contentView.findViewById(R.id.tv_card_number));
            tv_card_number.setText(card_list.get(position).getTitre());
            TextView t_prix = (TextView)(contentView.findViewById(R.id.textprix));
            //t_prix.setText(card_list.get(position).getPrix());
            TextView t_descri = (TextView)(contentView.findViewById(R.id.textdesc));
            t_descri.setText(card_list.get(position).getDescription());
            return contentView;
        }


        @Override
        public int getCount() {
            return this.card_list.size();
        }

    }

