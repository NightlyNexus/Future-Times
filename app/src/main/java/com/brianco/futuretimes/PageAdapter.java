package com.brianco.futuretimes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.ViewHolder> {

    private final List<Page> mPageList;
    private final FutureActivity mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View v;
        public TextView titleView;
        public ImageView imageView;
        public TextView descriptionView;
        public ViewHolder(View v) {
            super(v);
            this.v = v;
            titleView = (TextView) v.findViewById(R.id.title);
            imageView = (ImageView) v.findViewById(R.id.image);
            descriptionView = (TextView) v.findViewById(R.id.description);
        }
    }

    public PageAdapter(FutureActivity context, List<Page> pageList) {
        mContext = context;
        mPageList = pageList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Page page = mPageList.get(position);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.launchReaderFragment(page);
            }
        });
        if (page.getPicLink() == null) {
            holder.imageView.setVisibility(View.GONE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(page.getPicLink()).into(holder.imageView);
        }
        holder.titleView.setText(page.getTitle());
        holder.descriptionView.setText(page.getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPageList.size();
    }
}
