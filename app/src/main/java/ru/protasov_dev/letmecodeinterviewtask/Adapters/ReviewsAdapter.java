package ru.protasov_dev.letmecodeinterviewtask.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModel;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private List<PostModel> posts;
    private Context context;

    public ReviewsAdapter(List<PostModel> posts, Context context){
        this.posts = posts;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviewes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostModel post = posts.get(position);
        holder.txtTitleReviewes.setText(post.getResults().get(position).getDisplayTitle());
        holder.txtSummaryShortReviewes.setText(post.getResults().get(position).getSummaryShort());
        holder.txtDateReviewes.setText(post.getResults().get(position).getPublicationDate());
        holder.txtByline.setText(post.getResults().get(position).getByline());
        Glide.with(context) //FIXME убрать контекст
                .load(post.getResults().get(position).getMultimedia().getSrc())
                .into(holder.imgReviewes);
    }

    @Override
    public int getItemCount() {
        if(posts == null)
            return 0;
        return posts.size();
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
