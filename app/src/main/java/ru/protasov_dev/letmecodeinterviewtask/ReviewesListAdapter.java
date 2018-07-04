package ru.protasov_dev.letmecodeinterviewtask;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.Result;

public class ReviewesListAdapter extends BaseListAdapter<Result,
        ReviewesListAdapter.ReviewesViewHolder> {

    interface ReviewesListener {
        void onReviewesItemClick(int position);
    }

    private ReviewesListener listener;

    public ReviewesListAdapter(List<Result> items, ReviewesListener reviewesListener) {
        super(items);
        listener = reviewesListener;
    }

    @Override
    public ReviewesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_reviewes, parent, false);
        return new ReviewesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewesViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Context context = holder.itemView.getContext();
        Result item = items.get(position);

        if (item != null) {
            String URL = null;

            if (item.getMultimedia() != null) {
                URL = item.getMultimedia().getSrc();
            }

            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.image).centerCrop();

            Glide.with(context)
                    .load(URL)
                    .apply(requestOptions)
                    .into(holder.imageViewPresentation);

            holder.textViewTitle.setText(item.getDisplayTitle());
            holder.textViewSummaryShort.setText(item.getSummaryShort());
            holder.textViewByline.setText(item.getByline());
            holder.textViewDate.setText(item.getPublicationDate());
        }

        holder.cardViewReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onReviewesItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    static class ReviewesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_reviewes)
        ImageView imageViewPresentation;

        @BindView(R.id.txt_title_reviewes)
        TextView textViewTitle;

        @BindView(R.id.txt_summary_short_reviewes)
        TextView textViewSummaryShort;

        @BindView(R.id.txt_byline)
        TextView textViewByline;

        @BindView(R.id.txt_date_reviewes)
        TextView textViewDate;

        @BindView(R.id.cardView)
        CardView cardViewReview;

        ReviewesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
