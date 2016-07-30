package com.dhn.peanut.shots;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.profile.ProfileActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * Created by DHN on 2016/5/31.
 */
class ShotAdapter extends RecyclerView.Adapter<ShotAdapter.Holder> {

    private List<Shot> data;
    private Context context;
    private ShotsContract.View mView;

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOTER = 1;

    private int lastPosition = -1;
    private boolean isShowFooter = true;


    public ShotAdapter(Context context, ShotsContract.View view) {
        data = new ArrayList<>();
        this.context = context;
        this.mView = view;
    }

    //设置是否显示footer
    public void setShowFooter(boolean isShowFooter) {
        this.isShowFooter = isShowFooter;
        notifyDataSetChanged();
    }

    public void relpaceData(List<Shot> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    protected void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                    .anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(Holder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder.cardview != null) {
            holder.cardview.clearAnimation();
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.item_shot, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recyclerview_footer, parent, false);
        }
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            final Shot shot = data.get(position);

            //头像
            Uri avatarUri = Uri.parse(shot.getUser().getAvatar_url());
            GenericDraweeHierarchyBuilder draweeBuilder = new GenericDraweeHierarchyBuilder(context.getResources());
            GenericDraweeHierarchy avatrrHierarchy = draweeBuilder
                    .setRoundingParams(RoundingParams.asCircle())
                    .setPlaceholderImage(context.getResources().getDrawable(R.drawable.avatar), ScalingUtils.ScaleType.FIT_CENTER)
                    .build();
            holder.avatarView.setHierarchy(avatrrHierarchy);
            holder.avatarView.setImageURI(avatarUri);
            holder.avatarView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("user", shot.getUser());
                    context.startActivity(intent);
                }
            });

            holder.llBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mView.showShotDetails(shot);
                }
            });

            //加载图片
            Uri normalPicUri = Uri.parse(shot.getImages().getNormal());
            Uri teaserPicUri = Uri.parse(shot.getImages().getTeaser());
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
                    mView.showShotDetails(shot);
                }
            });

            holder.name.setText(shot.getUser().getUsername());
            holder.like.setText("" + shot.getLikes_count());
            holder.views.setText("" + shot.getViews_count());
            holder.comments.setText("" + shot.getComments_count());

            setAnimation(holder.cardview, position);

        } else if (getItemViewType(position) == TYPE_FOOTER) {
            //do nothing
        }

    }

    @Override
    public int getItemCount() {
        if (isShowFooter) {
            return data.size() + 1;
        } else {
            return data.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowFooter) {
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        } else {
            return TYPE_ITEM;
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.shot_avatar)
        SimpleDraweeView avatarView;
        @Nullable
        @BindView(R.id.shot_draweeview)
        SimpleDraweeView draweeView;
        @Nullable
        @BindView(R.id.shot_name)
        TextView name;
        @Nullable
        @BindView(R.id.item_shot_like)
        TextView like;
        @Nullable
        @BindView(R.id.item_shot_views)
        TextView views;
        @Nullable
        @BindView(R.id.item_shot_comments)
        TextView comments;
        @Nullable
        @BindView(R.id.shot_cardview)
        CardView cardview;
        @Nullable
        @BindView(R.id.ll_bottom)
        LinearLayout llBottom;


        public Holder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
