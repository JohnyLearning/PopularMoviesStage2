package com.ivanhadzhi.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ivanhadzhi.popularmovies.databinding.MovieReviewItemBinding;
import com.ivanhadzhi.popularmovies.databinding.ReviewDetailSheetBinding;
import com.ivanhadzhi.popularmovies.model.Review;

import java.util.List;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        MovieReviewItemBinding binding = MovieReviewItemBinding.inflate(layoutInflater, parent, false);
        ReviewDetailSheetBinding sheetBinding = ReviewDetailSheetBinding.inflate(layoutInflater, parent, false);
        return new ReviewViewHolder(binding, sheetBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    public void addReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        MovieReviewItemBinding dataBinding;
        ReviewDetailSheetBinding sheetBinding;
        BottomSheetDialog dialog;

        ReviewViewHolder(MovieReviewItemBinding binding, ReviewDetailSheetBinding sheetBinding) {
            super(binding.getRoot());
            dataBinding = binding;
            this.sheetBinding = sheetBinding;
            dialog = new BottomSheetDialog(context);
            dialog.setContentView(sheetBinding.getRoot());
        }

        void bind(Review review) {
            dataBinding.setReview(review);
            dataBinding.reviewContainer.setOnClickListener(view -> {
                dialog.show();
            });
            sheetBinding.setReview(review);
            sheetBinding.closeAction.setOnClickListener(view -> {
                dialog.dismiss();
            });
        }

    }
}
