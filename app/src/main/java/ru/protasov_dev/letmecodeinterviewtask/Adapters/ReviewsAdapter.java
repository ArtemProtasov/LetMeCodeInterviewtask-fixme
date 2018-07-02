package ru.protasov_dev.letmecodeinterviewtask.Adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.protasov_dev.letmecodeinterviewtask.Activity.ReviewPage;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<PostModelReviews> postModelReviews;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviewes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Result post = postModelReviews.get(position).getResults().get(position);

        String date = post.getPublicationDate();

        holder.txtTitleReviewes.setText(post.getDisplayTitle());
        holder.txtSummaryShortReviewes.setText(post.getSummaryShort());
        holder.txtDateReviewes.setText(date);
        holder.txtByline.setText(post.getByline());

        String URL;

        if (post.getMultimedia() != null) {
            URL = post.getMultimedia().getSrc();
        } else {
            URL = holder.imgReviewes.getContext().getString(R.string.src_search);
        }

        Glide.with(holder.imgReviewes.getContext())
                .load(URL)
                .into(holder.imgReviewes);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startReviewPage = new Intent(view.getContext(), ReviewPage.class)
                        .putExtra("URL", post.getLink().getUrl())
                        .putExtra("ARTICLE_TITLE", post.getDisplayTitle());
                view.getContext().startActivity(startReviewPage);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (postModelReviews == null)
            return 0;
        return postModelReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgReviewes;
        TextView txtTitleReviewes;
        TextView txtSummaryShortReviewes;
        TextView txtDateReviewes;
        TextView txtByline;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            imgReviewes = itemView.findViewById(R.id.img_reviewes);
            txtTitleReviewes = itemView.findViewById(R.id.txt_title_reviewes);
            txtSummaryShortReviewes = itemView.findViewById(R.id.txt_summary_short_reviewes);
            txtDateReviewes = itemView.findViewById(R.id.txt_date_reviewes);
            txtByline = itemView.findViewById(R.id.txt_byline);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

    public void setPostModelReviews(List<PostModelReviews> postModelReviews) {
        for (int i = 0; i < postModelReviews.size(); i++) {
            this.postModelReviews.add(postModelReviews.get(i));
        }
    }
}
