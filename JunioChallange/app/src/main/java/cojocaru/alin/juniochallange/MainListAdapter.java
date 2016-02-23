package cojocaru.alin.juniochallange;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alin on 022 22 02 2016.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainHolder> {

    private List<Field> items;
    private Context context;
    private static RecyclerViewClickListener itemListener;

    public MainListAdapter(Context context, List<Field> items, RecyclerViewClickListener itemListener) {
        this.items = items;
        this.context = context;
        this.itemListener = itemListener;
    }



    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.single_row, null);
        MainHolder holder = new MainHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {
        holder.mName.setText(items.get(position).getName());
        holder.mProducer.setText(items.get(position).getBrand());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface RecyclerViewClickListener
    {

        public void recyclerViewListClicked(View v, int position);
    }

    public class MainHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mName;
        private TextView mProducer;
        private View mLine;


        public MainHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name_text);
            mProducer = (TextView) itemView.findViewById(R.id.producer_text);
            mLine = itemView.findViewById(R.id.lineView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }
}
