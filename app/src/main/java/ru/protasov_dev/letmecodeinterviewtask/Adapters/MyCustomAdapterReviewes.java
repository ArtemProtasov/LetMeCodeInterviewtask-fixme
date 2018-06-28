package ru.protasov_dev.letmecodeinterviewtask.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.protasov_dev.letmecodeinterviewtask.R;
import ru.protasov_dev.letmecodeinterviewtask.Activity.ReviewPage;
import ru.protasov_dev.letmecodeinterviewtask.Elements.ReviewesElement;

public class MyCustomAdapterReviewes extends RecyclerView.Adapter<MyCustomAdapterReviewes.ViewHolder>{
    private List<ReviewesElement> list;
    private Context context;
    public MyCustomAdapterReviewes(List<ReviewesElement> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_reviewes, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewesElement reviewesElement = list.get(position);
        Glide.with(reviewesElement.getContext())
                .load(reviewesElement.getUrlImg())
                .into(holder.imgReviewes);
        holder.txtTitleReviewes.setText(reviewesElement.getTitle());
        holder.txtSummaryShortReviewes.setText(reviewesElement.getSummaryShort());
        holder.txtDateReviewes.setText(reviewesElement.getDate());
        holder.txtByline.setText(reviewesElement.getByline());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String articleTitle = list.get(getAdapterPosition()).getUrlPage();
            String url = list.get(getAdapterPosition()).getSuggestedLinkText();

            Intent startReviewPage = new Intent(context, ReviewPage.class);
            startReviewPage.putExtra("URL", url);
            startReviewPage.putExtra("ARTICLE_TITLE", articleTitle);
            context.startActivity(startReviewPage);
        }
    }
}
