package com.ivanhadzhi.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanhadzhi.popularmovies.model.ImageSize;
import com.ivanhadzhi.popularmovies.network.data.Movie;
import com.ivanhadzhi.popularmovies.utilities.NetworkUtils;
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (movies != null && movies.size() > position && movies.get(position).getPosterPath() != null) {
            Movie movie = movies.get(position);
            holder.movieTitle.setText(movie.getTitle());
            Picasso.get()
                    .load(NetworkUtils.getImageURL(ImageSize.w300, movie.getPosterPath()).toString())
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.moviePoster);
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
