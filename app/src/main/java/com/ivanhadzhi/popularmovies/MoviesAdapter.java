package com.ivanhadzhi.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanhadzhi.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Movie> movies;
    private Context context;
    private MovieClickListener movieClickListener;

    @FunctionalInterface
    public interface MovieClickListener {
        void execute(Movie movie);
    }

    public MoviesAdapter(@NonNull Context context, @NonNull List<Movie> movies) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (movies != null && movies.size() > 0) {
            Movie movie = movies.get(position);
            holder.movieTitle.setText(movie.getTitle());
            // TODO: move this url to network utils
            Picasso.get().load("https://image.tmdb.org/t/p/w185" + movie.getPosterPath()).into(holder.moviePoster);
        } else {
            holder.moviePoster.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.size();
        } else {
            return 0;
        }
    }

    public void setClickListener(MovieClickListener movieClickListener) {
        this.movieClickListener = movieClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView moviePoster;
        private TextView movieTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
        }

        public void onItemClick(View view, int position) {
            if (movieClickListener != null && movies != null && movies.size() > position) {
                movieClickListener.execute(movies.get(position));
            }
        }
    }

}
