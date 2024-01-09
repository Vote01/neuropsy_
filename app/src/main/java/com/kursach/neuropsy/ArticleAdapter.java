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

import java.util.ArrayList;
import java.util.List;
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

public class ArticleAdapter extends ArrayAdapter<Article> {

    private final int resourceId;

    private List<Article> mObjects;

    public ArticleAdapter(Context context, int resourceId, List<Article> articles) {
        super(context, resourceId, articles);
        this.resourceId = resourceId;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView :
                LayoutInflater.from(getContext()).inflate(resourceId, null);

        Article article = getItem(position);
        TextView titleTextView = view.findViewById(R.id.textViewTitle);
        TextView contentTextView = view.findViewById(R.id.textViewContent);
        TextView authorTextView = view.findViewById(R.id.textViewAuthor);
        ImageView imageView = view.findViewById(R.id.imageViewCover);

        if (titleTextView != null) {
            titleTextView.setText(article.getTitle());
        }
        titleTextView.setText(article.getTitle());
        if (contentTextView != null) {
            contentTextView.setText(article.getAbout());
        }
        contentTextView.setText(article.getAbout());
        if (authorTextView != null) {
            authorTextView.setText("Автор: " + article.getAuthor());
        }
        authorTextView.setText("Автор: " + article.getAuthor());
        byte[] imageData = article.getCover();
        if (imageData != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            imageView.setImageBitmap(bitmap);
        } else {
        }

        return view;
    }
}
