package com.kursach.neuropsy;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GlossaryAdapter extends ArrayAdapter<Glossary> {

    private final int resourceId;

    private List<Glossary> mObjects; // Добавьте поле для хранения данных

    public GlossaryAdapter(Context context, int resourceId, List<Glossary> articles) {
        super(context, resourceId, articles);
        this.resourceId = resourceId;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView :
                LayoutInflater.from(getContext()).inflate(resourceId, null);

        Glossary article = getItem(position);
        TextView titleTextView = view.findViewById(R.id.textViewTitle);
        TextView contentTextView = view.findViewById(R.id.textViewContent);
        if (titleTextView != null) {
            titleTextView.setText(article.getTerm_name());
        }
        titleTextView.setText(article.getTerm_name());
        if (contentTextView != null) {
            contentTextView.setText(article.getDescription());
        }
        contentTextView.setText(article.getDescription());
        return view;
    }
}
