package ru.protasov_dev.letmecodeinterviewtask.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.protasov_dev.letmecodeinterviewtask.Activity.CriticPage;
import ru.protasov_dev.letmecodeinterviewtask.Elements.CriticsElement;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class MyCustomAdapterCritics extends RecyclerView.Adapter<MyCustomAdapterCritics.ViewHolder> {
    private static List<CriticsElement> list;

    public MyCustomAdapterCritics(List<CriticsElement> list){
        MyCustomAdapterCritics.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_critics, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CriticsElement criticsElement = list.get(position);
        Glide.with(criticsElement.getContext())
                .load(criticsElement.getUrlImg())
                .into(holder.imgCritics);
        holder.txtNameCritics.setText(criticsElement.getName());
        holder.txtStatusCritics.setText(criticsElement.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgCritics;
        TextView txtNameCritics;
        TextView txtStatusCritics;

        public ViewHolder(View itemView) {
            super(itemView);

            imgCritics = itemView.findViewById(R.id.critic_photo);
            txtNameCritics = itemView.findViewById(R.id.critic_name);
            txtStatusCritics = itemView.findViewById(R.id.status);

            imgCritics.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String name = txtNameCritics.getText().toString();
            String status = txtStatusCritics.getText().toString();
            Intent startCriticPage = new Intent(view.getContext(), CriticPage.class);
            startCriticPage.putExtra("URL", createURL(name));
            startCriticPage.putExtra("NAME", name);
            startCriticPage.putExtra("STATUS", status);
            CriticsElement ce = list.get(getPosition());
            startCriticPage.putExtra("BIO", ce.getBio());
            startCriticPage.putExtra("IMG_URL", ce.getUrlImg());


            view.getContext().startActivity(startCriticPage);
        }

        private String createURL(String name){
            String url = "https://api.nytimes.com/svc/movies/v2/reviews/search.json";
            url += "?api-key=020eb74eff674e3da8aaa1e8e311edda" + "&reviewer=" + name.replace(" ", "%20");
            return url;
        }
    }
}
