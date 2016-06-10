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
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
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
        //添加图片
        Comment headerPic = new Comment();
        headerPic.setType(Comment.HEADER_PIC);
        comments.add(headerPic);
        //添加图片介绍
        Comment header = new Comment();
        header.setType(Comment.HEADER);
        comments.add(header);
        //评论
        comments.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == Comment.NORNAL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
            view.setTag("normal");
        } else if (viewType == Comment.HEADER){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_head, parent, false);
            view.setTag("header");
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_pic, parent, false);
            view.setTag("pic");
        }

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment comment = comments.get(position);
        if (comment.getType() == Comment.NORNAL) {
            holder.name.setText(comment.getUser().getUsername());
            holder.content.setText(Html.fromHtml(comment.getBody()));
            //设置头像
            Uri uri = Uri.parse(comment.getUser().getAvatar_url());
            holder.commenterPic.setImageURI(uri);
        } else if (comment.getType() == comment.HEADER){
            holder.commentTitle.setText(shot.getTitle());
            String desc = shot.getDescription() != null ? shot.getDescription() : "";
            holder.commentDesc.setText(Html.fromHtml(desc));
            Uri uri = Uri.parse(shot.getUser().getAvatar_url());
            holder.authorPic.setImageURI(uri);
            holder.authorName.setText(shot.getUser().getUsername());
        } else {
            Uri uri = Uri.parse(shot.getImages().getNormal());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(mContext.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();
            holder.draweeView.setController(controller);
            holder.draweeView.setHierarchy(hierarchy);
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

        SimpleDraweeView draweeView;

        //comments
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
            } else if (itemView.getTag().equals("header")){
                commentTitle = (TextView) itemView.findViewById(R.id.comment_title);
                commentDesc  = (TextView) itemView.findViewById(R.id.commnet_desc);
                authorPic = (SimpleDraweeView) itemView.findViewById(R.id.author_pic);
                authorName = (TextView) itemView.findViewById(R.id.author_name);
            } else {
                draweeView = (SimpleDraweeView) itemView.findViewById(R.id.detail_iv);
            }

        }
    }


}
