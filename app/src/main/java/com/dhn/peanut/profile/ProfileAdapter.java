package com.dhn.peanut.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.data.base.ProfileDataSource;
import com.dhn.peanut.shotdetail.ShotDetailActivity;
import com.dhn.peanut.util.AuthoUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DHN on 2016/6/15.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileHolder> {


    private Context context;
    private Shot.User user;
    private List<Shot> data;
    private boolean isShowFooter = true;
    private boolean firstIn = true;

    ProfileAdapter(Context context, Shot.User user) {
        this.context = context;
        this.user = user;
        data = new ArrayList<>();
    }

    public void setShowFooter(boolean isShowFooter) {
        this.isShowFooter = isShowFooter;
        notifyDataSetChanged();
    }

    public void replaceData(List<Shot> shots) {
        if (firstIn) {
            Shot header = new Shot();
            header.setType(Shot.TYPE_HEADER);
            data.add(header);
            firstIn = false;
        }

        data.addAll(shots);
        notifyDataSetChanged();
    }

    @Override
    public ProfileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == Shot.TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.item_shot, parent, false);
        } else if (viewType == Shot.TYPE_HEADER){
            view = LayoutInflater.from(context).inflate(R.layout.item_user_profile, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recyclerview_footer, parent, false);
        }
        return new ProfileHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileHolder holder, int position) {

        if (getItemViewType(position) == Shot.TYPE_ITEM) {

            final Shot shot = data.get(position);
            //作者头像
            Uri avatarUri = Uri.parse(user.getAvatar_url());
            holder.avatarView.setImageURI(avatarUri);

            //作品
            Uri picUri = Uri.parse(shot.getImages().getNormal());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(picUri)
                    .setAutoPlayAnimations(true)
                    .build();
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();
            holder.draweeView.setController(controller);
            holder.draweeView.setHierarchy(hierarchy);

            holder.draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShotDetailActivity.class);
                    shot.setUser(user);
                    intent.putExtra("shot", shot);
                    context.startActivity(intent);
                }
            });

            //作者名称、阅读次数
            holder.name.setText(user.getUsername());
            holder.like.setText("" + shot.getLikes_count());
            holder.views.setText("" + shot.getViews_count());
            holder.comments.setText("" + shot.getComments_count());


        } else if (getItemViewType(position) == Shot.TYPE_HEADER){
            //头像
            Uri avatarUri = Uri.parse(user.getAvatar_url());
            holder.header_avatar.setImageURI(avatarUri);

            //following, follower, location
            holder.tvName.setText(user.getUsername());
            holder.tvFollowingCount.setText("" + user.getFollowings_count());
            holder.tvFollowerCount.setText("" + user.getFollowers_count());
            holder.tvLocation.setText(user.getLocation());

            //
        } else {
            //Footer, do nothing
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
        if (position == data.size()) {
            return Shot.TYPE_ROOTER;
        } {
            return data.get(position).getType();
        }
    }

    class ProfileHolder extends RecyclerView.ViewHolder {
        //item
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

        //header
        @Nullable
        @BindView(R.id.iv_avatar)
        SimpleDraweeView header_avatar;
        @Nullable
        @BindView(R.id.tv_name)
        TextView tvName;
        @Nullable
        @BindView(R.id.tv_following_count)
        TextView tvFollowingCount;
        @Nullable
        @BindView(R.id.tv_follower_count)
        TextView tvFollowerCount;
        @Nullable
        @BindView(R.id.tv_location_content)
        TextView tvLocation;

        public ProfileHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
