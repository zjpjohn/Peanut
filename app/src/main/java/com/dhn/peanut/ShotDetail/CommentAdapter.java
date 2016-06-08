package com.dhn.peanut.shotdetail;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.Shot;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DHN on 2016/6/2.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private Context mContext;
    private List<Comment> comments;
    private Shot shot;

    CommentAdapter(Context context, Shot shot) {
        mContext = context;
        comments = new ArrayList<>();
        this.shot = shot;
    }

    public void replaceComments(List<Comment> data) {
        Comment header = new Comment();
        header.setType(Comment.HEADER);
        comments.add(header);
        comments.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == Comment.NORNAL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            view.setTag("normal");
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_head, parent, false);
            view.setTag("header");
        }

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment comment = comments.get(position);
        if (comment.getType() == Comment.NORNAL) {
            holder.name.setText(comment.getUser().getUsername());
            holder.content.setText(Html.fromHtml(comment.getBody()));
            //TODO 设置头像
            Uri uri = Uri.parse(comment.getUser().getAvatar_url());
            holder.commenterPic.setImageURI(uri);
        } else {
            holder.commentTitle.setText(shot.getTitle());
            holder.commentDesc.setText(Html.fromHtml(shot.getDescription()));
            Uri uri = Uri.parse(shot.getUser().getAvatar_url());
            holder.authorPic.setImageURI(uri);
            holder.authorName.setText(shot.getUser().getUsername());
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public int getItemViewType(int position) {
        return comments.get(position).getType();

    }

    class CommentHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView content;
        SimpleDraweeView commenterPic;

        //Header
        TextView commentTitle;
        TextView commentDesc;
        SimpleDraweeView authorPic;
        TextView authorName;

        public CommentHolder(View itemView) {
            super(itemView);
            if (itemView.getTag().equals("normal")) {
                name = (TextView) itemView.findViewById(R.id.item_commenter_name);
                content = (TextView) itemView.findViewById(R.id.item_comment_content);
                commenterPic = (SimpleDraweeView) itemView.findViewById(R.id.item_commenter_pic);
            } else {
                commentTitle = (TextView) itemView.findViewById(R.id.comment_title);
                commentDesc  = (TextView) itemView.findViewById(R.id.commnet_desc);
                authorPic = (SimpleDraweeView) itemView.findViewById(R.id.author_pic);
                authorName = (TextView) itemView.findViewById(R.id.author_name);
            }

        }
    }


}
