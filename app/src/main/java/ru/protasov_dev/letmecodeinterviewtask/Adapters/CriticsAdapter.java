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

import ru.protasov_dev.letmecodeinterviewtask.Activity.CriticPage;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.PostModelCritics;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class CriticsAdapter extends RecyclerView.Adapter<CriticsAdapter.ViewHolder>{
    private PostModelCritics posts;

    public CriticsAdapter(PostModelCritics posts) {
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_critics, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Result post = posts.getResults().get(position);
        holder.criticName.setText(post.getDisplayName());
        holder.status.setText(post.getStatus());

        final String URL;

        if(post.getMultimedia() == null)
            URL = holder.criticPhoto.getContext().getString(R.string.src_user_avatar);
        else
            URL = post.getMultimedia().getResource().getSrc();

        Glide.with(holder.criticPhoto.getContext())
                .load(URL)
                .into(holder.criticPhoto);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startCriticPage = new Intent(view.getContext(), CriticPage.class)
                        .putExtra("NAME", post.getDisplayName())
                        .putExtra("STATUS", post.getStatus())
                        .putExtra("BIO", post.getBio())
                        .putExtra("URL_IMG", URL);
                view.getContext().startActivity(startCriticPage);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (posts == null)
            return 0;
        return posts.getNumResults();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView criticPhoto;
        TextView criticName;
        TextView status;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            criticPhoto = itemView.findViewById(R.id.critic_photo);
            criticName = itemView.findViewById(R.id.critic_name);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.card_view_critics);
        }
    }
}
