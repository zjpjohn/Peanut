package com.dhn.peanut.ShotDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DHN on 2016/6/2.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private Context mContext;
    private List<Comment> comments;

    CommentAdapter(Context context) {
        mContext = context;
        comments = new ArrayList<>();
    }

    public void replaceComments(List<Comment> data) {
        Log.e("adapter", "replace");
        comments = data;
        notifyDataSetChanged();
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment comment = comments.get(position);

        Log.e("adapter", comment.toString());

        holder.name.setText(comment.getUser().getUsername());
        holder.content.setText(comment.getBody());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView content;

        public CommentHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_comment_name);
            content = (TextView) itemView.findViewById(R.id.item_comment_content);

        }
    }
}
