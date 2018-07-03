package ru.protasov_dev.letmecodeinterviewtask;

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
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.Result;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Result> reviewesItemModel = new ArrayList<>();
    private int positionn;

    public void addItem(Result postModelReviews) {
        reviewesItemModel.add(postModelReviews);
    }

    public void clearAllItem(){
        reviewesItemModel.clear();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviewes, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        positionn = position;
        holder.tvTitle.setText(reviewesItemModel.get(position).getDisplayTitle());
        holder.tvSummaryShort.setText(reviewesItemModel.get(position).getSummaryShort());
        holder.tvByline.setText(reviewesItemModel.get(position).getByline());
        holder.tvDate.setText(dateFormat(reviewesItemModel.get(position).getPublicationDate()));
        holder.cvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startReviewPage = new Intent(view.getContext(), ReviewPage.class)
                        .putExtra("URL", reviewesItemModel.get(positionn).getLink().getUrl())
                        .putExtra("ARTICLE_TITLE", reviewesItemModel.get(positionn).getDisplayTitle());
                view.getContext().startActivity(startReviewPage);
            }
        });

        String URL = null;
        if (reviewesItemModel.get(position).getMultimedia() != null) {
            URL = reviewesItemModel.get(position).getMultimedia().getSrc();
        }
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.image).centerCrop();
        Glide.with(holder.iwImage.getContext())
                .load(URL)
                .apply(requestOptions)
                .into(holder.iwImage);


    }

    @Override
    public int getItemCount() {
        if (reviewesItemModel == null)
            return 0;
        return reviewesItemModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvSummaryShort;
        private TextView tvByline;
        private TextView tvDate;
        private ImageView iwImage;
        private CardView cvPost;


        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.txt_title_reviewes);
            tvSummaryShort = itemView.findViewById(R.id.txt_summary_short_reviewes);
            tvByline = itemView.findViewById(R.id.txt_byline);
            tvDate = itemView.findViewById(R.id.txt_date_reviewes);
            iwImage = itemView.findViewById(R.id.img_reviewes);
            cvPost = itemView.findViewById(R.id.cardView);
        }
    }

    private String dateFormat(String oldDate) {
        SimpleDateFormat commonFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
        Date date = null;
        try {
            date = commonFormat.parse(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat reformatted = new SimpleDateFormat("yyyy/mm/dd", Locale.US);
        return reformatted.format(date);
    }
}
