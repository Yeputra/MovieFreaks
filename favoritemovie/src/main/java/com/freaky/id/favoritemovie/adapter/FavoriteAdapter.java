package com.freaky.id.favoritemovie.adapter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.freaky.id.favoritemovie.R;
import com.freaky.id.favoritemovie.activity.DetailActivity;
import com.freaky.id.favoritemovie.model.Result;
import com.google.gson.Gson;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Cursor list;

    public FavoriteAdapter(Cursor items) {
        replaceAll(items);
    }

    public void replaceAll(Cursor items) {
        list = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.movie_list, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.getCount();
    }

    private Result getItem(int position) {
        if (!list.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }
        return new Result(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        TextView tvDate;
        ImageView ivPoster;
        CardView cvMovie;
        Button btnDetail;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            cvMovie = itemView.findViewById(R.id.cv_movie);
            btnDetail = itemView.findViewById(R.id.btn_detail);

        }

        public void bind(final Result item) {
            tvTitle.setText(item.getTitle());
            tvOverview.setText(item.getOverview());
            tvDate.setText("Release Date : " + item.getReleaseDate());
            Glide.with(itemView.getContext())
                    .load("http://image.tmdb.org/t/p/w92" + item.getPosterPath())
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(ivPoster);

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra("movie", new Gson().toJson(item));
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }
}