package ru.protasov_dev.letmecodeinterviewtask.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.PostModelCritics;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class CriticsAdapter extends RecyclerView.Adapter<CriticsAdapter.ViewHolder>{
    private List<PostModelCritics> posts;
    private Context context;

    public CriticsAdapter(List<PostModelCritics> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_critics, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostModelCritics post = posts.get(position);
        holder.criticName.setText(post.getResults().get(position).getDisplayName());
        holder.status.setText(post.getResults().get(position).getStatus());
        Glide.with(context) //FIXME убрать контекст
                .load(post.getResults().get(position).getMultimedia().getSrc())
                .into(holder.criticPhoto);
    }

    @Override
    public int getItemCount() {
        if (posts == null)
            return 0;
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView criticPhoto;
        TextView criticName;
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            criticPhoto = itemView.findViewById(R.id.critic_photo);
            criticName = itemView.findViewById(R.id.critic_name);
            status = itemView.findViewById(R.id.status);
        }
    }
}
