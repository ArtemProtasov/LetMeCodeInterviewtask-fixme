package ru.protasov_dev.letmecodeinterviewtask.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.RecoverySystem;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.protasov_dev.letmecodeinterviewtask.Activity.ReviewPage;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private PostModelReviews posts;

    public ReviewsAdapter(PostModelReviews posts){
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviewes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Result post = posts.getResults().get(position);
        holder.txtTitleReviewes.setText(post.getDisplayTitle());
        holder.txtSummaryShortReviewes.setText(post.getSummaryShort());
        holder.txtDateReviewes.setText(post.getPublicationDate());
        holder.txtByline.setText(post.getByline());

        String URL;

        if(post.getMultimedia().getSrc() != null)
            URL = post.getMultimedia().getSrc();
        else
            URL = holder.imgReviewes.getContext().getString(R.string.src_search);
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
        if(posts == null)
            return 0;
        return posts.getNumResults();
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
}
