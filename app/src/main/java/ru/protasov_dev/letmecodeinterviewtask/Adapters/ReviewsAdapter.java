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
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.protasov_dev.letmecodeinterviewtask.Activity.ReviewPage;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private PostModelReviews postModelReviews;
    private List<PostModelReviews> postModelReviewsList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviewes, parent, false);
        return new ViewHolder(v);
    }

    public void addItem(PostModelReviews postModelReviews) {
        postModelReviewsList.add(postModelReviews);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Result post = postModelReviews.getResults().get(position);

        String sDate = post.getPublicationDate();

        SimpleDateFormat commonForman = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
        Date date = null;
        try {
            date = commonForman.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat reformatted = new SimpleDateFormat("yyyy/mm/dd", Locale.US);
        String formatDate = reformatted.format(date);

        holder.txtTitleReviewes.setText(post.getDisplayTitle());
        holder.txtSummaryShortReviewes.setText(post.getSummaryShort());
        holder.txtDateReviewes.setText(formatDate);
        holder.txtByline.setText(post.getByline());

        String URL = null;

        if (post.getMultimedia() != null) {
            URL = post.getMultimedia().getSrc();
        }

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.image).centerCrop();

        Glide.with(holder.imgReviewes.getContext())
                .load(URL)
                .apply(requestOptions)
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
        return postModelReviews.getNumResults();
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

    public void setPostModelReviews(PostModelReviews postModelReviews) {
        this.postModelReviews = postModelReviews;
    }
}
