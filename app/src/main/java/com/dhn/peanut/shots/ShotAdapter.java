package com.dhn.peanut.shots;

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
import com.dhn.peanut.data.Shot;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

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


    public ShotAdapter(Context context, ShotsContract.View view) {
        data = new ArrayList<>();
        this.context = context;
        this.mView = view;
    }

    public void relpaceData(List<Shot> data) {
        this.data = data;
        notifyDataSetChanged();

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
            Uri avatarUri = Uri.parse(shot.getUser().getAvatar_url());
            holder.avatarView.setImageURI(avatarUri);

            Uri picUri = Uri.parse(shot.getImages().getNormal());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(picUri)
                    .setAutoPlayAnimations(true)
                    .build();
            holder.draweeView.setController(controller);

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

        } else if (getItemViewType(position) == TYPE_FOOTER) {
            //do nothing
        }

    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
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


        public Holder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
