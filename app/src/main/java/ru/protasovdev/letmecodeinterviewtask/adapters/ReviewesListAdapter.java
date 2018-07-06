package ru.protasovdev.letmecodeinterviewtask.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ru.protasovdev.letmecodeinterviewtask.parsetaskmanagers.PostModelReviews.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class ReviewesListAdapter extends RecyclerView.Adapter<ReviewesListAdapter.ReviewesViewHolder> {

    private List<Result> items = null;
    private Bitmap bitmap;

    public interface ReviewesListener {
        void onReviewesItemClick(Result item);
        void onReviewesLongImageClick(ImageView imageView, String title);
    }

    private ReviewesListener listener;

    public ReviewesListAdapter(){
        super();
    }

    public ReviewesListAdapter(ReviewesListener reviewesListener) {
        super();
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
        Context context = holder.itemView.getContext();
        final Result item = items.get(position);

        if (item != null) {
            String URL = null;

            if (item.getMultimedia() != null) {
                URL = item.getMultimedia().getSrc();
            }

            RequestOptions requestOptions
                    = new RequestOptions().placeholder(R.drawable.image).centerCrop();

            Glide.with(context)
                    .load(URL)
                    .apply(requestOptions)
                    .into(holder.imageViewPresentation);

            BitmapDrawable bitmapDrawable = (BitmapDrawable) holder.imageViewPresentation.getDrawable();
            bitmap = bitmapDrawable.getBitmap();

            if (item.getDisplayTitle() != null) {
                holder.textViewTitle.setText(item.getDisplayTitle());
            } else {
                holder.textViewTitle.setText(R.string.no_value);
            }

            if (item.getSummaryShort() != null) {
                holder.textViewSummaryShort.setText(item.getSummaryShort());
            } else {
                holder.textViewSummaryShort.setText(R.string.no_value);
            }

            if (item.getByline() != null) {
                holder.textViewByline.setText(item.getByline());
            } else {
                holder.textViewByline.setText(R.string.no_value);
            }

            if (item.getPublicationDate() != null) {
                holder.textViewDate.setText(item.getPublicationDate());
            } else {
                holder.textViewDate.setText(R.string.no_value);
            }
        }

        holder.cardViewReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onReviewesItemClick(item);
                }
            }
        });

        holder.imageViewPresentation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener != null){
                    listener.onReviewesLongImageClick(holder.imageViewPresentation,
                            holder.textViewTitle.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void clearItems() {
        if(this.items != null) {
            this.items.clear();
            notifyDataSetChanged();
        }
    }

    public void updateItems(List<Result> items) {
        if (this.items != null) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        } else {
            setItems(items);
        }
    }

    public void addItems(List<Result> items) {
        if(this.items != null) {
            this.items.addAll(items);
            notifyDataSetChanged();
        } else {
            setItems(items);
        }
    }

    public void setItems(List<Result> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    static class ReviewesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPresentation;
        TextView textViewTitle;
        TextView textViewSummaryShort;
        TextView textViewByline;
        TextView textViewDate;
        CardView cardViewReview;

        ReviewesViewHolder(View itemView) {
            super(itemView);
            imageViewPresentation = itemView.findViewById(R.id.img_reviewes);
            textViewTitle = itemView.findViewById(R.id.txt_title_reviewes);
            textViewSummaryShort = itemView.findViewById(R.id.txt_summary_short_reviewes);
            textViewByline = itemView.findViewById(R.id.txt_byline);
            textViewDate = itemView.findViewById(R.id.txt_date_reviewes);
            cardViewReview = itemView.findViewById(R.id.cardView);
        }
    }
}
