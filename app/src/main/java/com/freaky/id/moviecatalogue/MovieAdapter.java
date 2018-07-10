package com.freaky.id.moviecatalogue;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.GsonBuilder;

import java.util.List;

import static android.content.ContentValues.TAG;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    List<Result> movieList;

    public MovieAdapter(List<Result> movieList) {
        this.movieList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        holder.tvTitle.setText(movieList.get(position).getTitle());
        holder.tvDescription.setText(movieList.get(position).getOverview());
        holder.tvDate.setText(movieList.get(position).getReleaseDate());
        Glide.with(holder.itemView.getContext())
                .load(RetrofitInterface.BASE_IMAGE + movieList.get(position).getPosterPath())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setData(List<Result> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvDescription;
        TextView tvDate;
        ImageView ivPoster;
        CardView cvMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            cvMovie = (CardView) itemView.findViewById(R.id.cv_movie);

            cvMovie.setOnClickListener(this);

            tvTitle.setSelected(true);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Result data = movieList.get(position);
            final Context context = v.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("movie",new GsonBuilder().create().toJson(data));
            context.startActivity(intent);
        }
    }
}

