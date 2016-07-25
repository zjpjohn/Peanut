package com.dhn.peanut.like;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhn.peanut.R;
import com.dhn.peanut.data.LikedShot;
import com.dhn.peanut.data.Shot;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DHN on 2016/6/8.
 */
public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeHolder> {

    private List<LikedShot> likes;
    private LikeContract.View view;
    private Context context;

    public LikeAdapter(Context context, LikeContract.View view) {
        likes = new ArrayList<>();
        this.view = view;
        this.context = context;
    }

    @Override
    public LikeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shot, parent, false);
        return new LikeHolder(view);
    }

    @Override
    public void onBindViewHolder(LikeHolder holder, int position) {
        final LikedShot shot = likes.get(position);

        //头像
        Uri avatarUri = Uri.parse(shot.getShot().getUser().getAvatar_url());
        GenericDraweeHierarchyBuilder draweeBuilder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy avatrrHierarchy = draweeBuilder
                .setRoundingParams(RoundingParams.asCircle())
                .setPlaceholderImage(context.getResources().getDrawable(R.drawable.avatar), ScalingUtils.ScaleType.FIT_CENTER)
                .build();
        holder.avatarView.setHierarchy(avatrrHierarchy);
        holder.avatarView.setImageURI(avatarUri);

        //作品
        Uri normalPicUri = Uri.parse(shot.getShot().getImages().getNormal());
        Uri teaserPicUri = Uri.parse(shot.getShot().getImages().getTeaser());
        //设置自动播放动画,低像素图片
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(teaserPicUri))
                .setImageRequest(ImageRequest.fromUri(normalPicUri))
                .setAutoPlayAnimations(true)
                .build();
        //设置进度条,占位符
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setProgressBarImage(new ProgressBarDrawable())
                .setPlaceholderImage(context.getResources().getDrawable(R.drawable.holder), ScalingUtils.ScaleType.FIT_CENTER)
                .build();
        holder.draweeView.setController(controller);
        holder.draweeView.setHierarchy(hierarchy);
        holder.draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.showShotDetails(shot);
            }
        });

        holder.name.setText(shot.getShot().getUser().getUsername());
        holder.like.setText("" + shot.getShot().getLikes_count());
        holder.views.setText("" + shot.getShot().getViews_count());
        holder.comments.setText("" + shot.getShot().getComments_count());
    }

    @Override
    public int getItemCount() {
        return likes.size();
    }

    public void replaceData(List<LikedShot> likes) {
        this.likes = likes;
        notifyDataSetChanged();
    }

    class LikeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shot_avatar)
        SimpleDraweeView avatarView;
        @BindView(R.id.shot_draweeview)
        SimpleDraweeView draweeView;
        @BindView(R.id.shot_name)
        TextView name;
        @BindView(R.id.item_shot_like)
        TextView like;
        @BindView(R.id.item_shot_views)
        TextView views;
        @BindView(R.id.item_shot_comments)
        TextView comments;

        public LikeHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
