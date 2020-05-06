package com.ivanhadzhi.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.ivanhadzhi.popularmovies.data.MovieDao;
import com.ivanhadzhi.popularmovies.data.MoviesDatabase;
import com.ivanhadzhi.popularmovies.databinding.MovieListItemBinding;
import com.ivanhadzhi.popularmovies.model.ImageSize;
import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;
    private MovieClickListener movieClickListener;
    private final MovieDao movieDao;

    @FunctionalInterface
    public interface MovieClickListener {
        void execute(Movie movie);
    }

    public MoviesAdapter(@NonNull Context context) {
        this.context = context;
        movieDao = MoviesDatabase.getInstance(context).movieDao();
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
        boolean favoriteFlag;

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
                itemBinding.favoriteAction.setOnClickListener(view -> markFavorite(view));
                Movie dbMovie = movieDao.fetchById(movie.getMovieId());
                if (dbMovie != null) {
                    favoriteFlag = true;
                } else {
                    favoriteFlag = false;
                }
                setImageActionDrawable(favoriteFlag);
            } else {
                itemBinding.moviePoster.setImageResource(R.drawable.no_image);
            }
        }

        private void markFavorite(View view) {
            final Movie selectedMovie = movies.get(getAdapterPosition());
            if (favoriteFlag) {
                // the movie has been marked as favorite so we will remove it from the db
                movieDao.delete(selectedMovie);
            } else {
                // mark as favorite, i.e. insert to db
                movieDao.insert(selectedMovie);
            }
            favoriteFlag = !favoriteFlag;
            setImageActionDrawable(favoriteFlag);
            // TODO: move the string to resources
            Snackbar.make(view, "Marking " + selectedMovie.getTitle() + " as favorite.", Snackbar.LENGTH_SHORT).show();
        }

        private void setImageActionDrawable(boolean markFavorite) {
            if (markFavorite) {
                itemBinding.favoriteAction.setImageResource(R.drawable.favorite_selected);
            } else {
                itemBinding.favoriteAction.setImageResource(R.drawable.favorite);
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
