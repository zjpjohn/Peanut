package com.dhn.peanut.following;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Following;
import com.dhn.peanut.data.Shot;
import com.dhn.peanut.profile.ProfileActivity;
import com.dhn.peanut.profile.ProfileAdapter;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DHN on 2016/6/17.
 */
public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingHolder> {

    private Context mContext;
    private List<Following> mFollowings;

    FollowingAdapter(Context context) {
        mContext = context;
    }

    public void replaceData(List<Following> followings) {
        mFollowings = followings;
        notifyDataSetChanged();
    }

    @Override
    public FollowingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_following, parent, false);
        return new FollowingHolder(view);

    }

    @Override
    public void onBindViewHolder(FollowingHolder holder, int position) {
        //TODO
        final Shot.User user = mFollowings.get(position).getFollowee();

        holder.sdvFollowing.setImageURI(Uri.parse(user.getAvatar_url()));

        holder.tvFollowerName.setText(user.getUsername());

        holder.tvFollowerBio.setText(Html.fromHtml(user.getBio()));

        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("user", user);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFollowings.size();
    }

    class FollowingHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sdv_following)
        SimpleDraweeView sdvFollowing;
        @BindView(R.id.tv_follower_name)
        TextView tvFollowerName;
        @BindView(R.id.tv_follower_bio)
        TextView tvFollowerBio;
        @BindView(R.id.tv_detail)
        TextView tvDetail;

        public FollowingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
