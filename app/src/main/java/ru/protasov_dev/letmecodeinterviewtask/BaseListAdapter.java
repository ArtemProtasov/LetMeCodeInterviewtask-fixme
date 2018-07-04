package ru.protasov_dev.letmecodeinterviewtask;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public abstract class BaseListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    protected List<T> items;
    private OnItemClickListener onItemClickListener;

    public BaseListAdapter(List<T> items) {
        this.items = items;
    }

    public BaseListAdapter(List<T> items, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        if (holder.itemView != null) {
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public T getItem(int position) {
        return this.items.get(position);
    }

    public List<T> getItems() {
        return this.items;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(T item, int position) {
        this.items.add(position, item);
        notifyDataSetChanged();
    }

    public void updateItems(List<T> items) {
        if (this.items != null) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        } else {
            setItems(items);
        }
    }

    public void clearItems() {
        this.items.clear();
        notifyDataSetChanged();
    }
}
