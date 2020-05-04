package com.ivanhadzhi.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ivanhadzhi.popularmovies.databinding.MovieListItemBinding;
import com.ivanhadzhi.popularmovies.model.ImageSize;
import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Movie> movies;
    private Context context;
    private MovieClickListener movieClickListener;

    @FunctionalInterface
    public interface MovieClickListener {
        void execute(Movie movie);
    }

    public MoviesAdapter(@NonNull Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void addMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        MovieListItemBinding itemBinding = MovieListItemBinding.inflate(layoutInflater, parent, false);
        return new MovieViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (movies != null && movies.size() > position && movies.get(position).getPosterPath() != null) {
            Movie movie = movies.get(position);
            holder.bindItem(movie);
        } else {
            holder.bindItem(null);
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

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final MovieListItemBinding itemBinding;

        public MovieViewHolder(MovieListItemBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        public void bindItem(@Nullable Movie movie) {
            if (movie != null) {
                itemBinding.setMovie(movie);
                Picasso.get()
                        .load(NetworkUtils.getImageURL(ImageSize.w300, movie.getPosterPath()).toString())
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                        .into(itemBinding.moviePoster);
                itemView.setOnClickListener(this);
            } else {
                itemBinding.moviePoster.setImageResource(R.drawable.no_image);
            }
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
