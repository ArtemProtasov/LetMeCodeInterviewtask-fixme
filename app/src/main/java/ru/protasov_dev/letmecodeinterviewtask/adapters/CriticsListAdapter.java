package ru.protasov_dev.letmecodeinterviewtask.adapters;

import android.content.Context;
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

import ru.protasov_dev.letmecodeinterviewtask.parsetaskmanagers.PostModelCritics.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;


public class CriticsListAdapter extends RecyclerView.Adapter<CriticsListAdapter.CriticsViewHolder> {
    private List<Result> items = null;
    private CriticsListener listener;
    private String URL = null;

    public interface CriticsListener{
        void onCriticsItemClick(Result item);
        void onCriticsLongImageClick(ImageView imageView, String title);
    }

    public CriticsListAdapter(){
        super();
    }

    public CriticsListAdapter(CriticsListener criticsListener){
        super();
        listener = criticsListener;
    }


    @Override
    public CriticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_critics, parent, false);
        return new CriticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CriticsViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        final Result item = items.get(position);

        if(item != null){
            String URL = null;

            if(item.getMultimedia() != null) {
                URL = item.getMultimedia().getResource().getSrc();
            }

            RequestOptions requestOptions
                    = new RequestOptions().placeholder(R.drawable.avatar).centerCrop();

            Glide.with(context)
                    .load(URL)
                    .apply(requestOptions)
                    .into(holder.imageViewPhotoOfCritic);

            if(item.getDisplayName() != null){
                holder.textViewDisplayName.setText(item.getDisplayName());
            } else {
                holder.textViewDisplayName.setText(R.string.no_value);
            }

            if(item.getStatus() != null){
                holder.textViewStatus.setText(item.getStatus());
            } else {
                holder.textViewStatus.setText(R.string.no_value);
            }

            if(item.getMultimedia() != null) {
                this.URL = item.getMultimedia().getResource().getSrc();
            } else {
                this.URL = null;
            }
        }

        holder.cardViewCritic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onCriticsItemClick(item);
                }
            }
        });

        holder.imageViewPhotoOfCritic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener != null){
                    listener.onCriticsLongImageClick(holder.imageViewPhotoOfCritic, holder.textViewDisplayName.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    public void clearItems(){
        if(this.items != null){
            this.items.clear();
            notifyDataSetChanged();
        }
    }

    public void addItems(List<Result> items){
        if(this.items != null) {
            this.items.addAll(items);
            notifyDataSetChanged();
        } else {
            setItems(items);
        }
    }

    public void setItems(List<Result> items){
        this.items = items;
        notifyDataSetChanged();
    }

    static class CriticsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhotoOfCritic;
        TextView textViewDisplayName;
        TextView textViewStatus;
        CardView cardViewCritic;

        CriticsViewHolder(View itemView){
            super(itemView);
            imageViewPhotoOfCritic = itemView.findViewById(R.id.critic_photo);
            textViewDisplayName = itemView.findViewById(R.id.critic_name);
            textViewStatus = itemView.findViewById(R.id.status);
            cardViewCritic = itemView.findViewById(R.id.card_view_critics);
        }
    }
}
