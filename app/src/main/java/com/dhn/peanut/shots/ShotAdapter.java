package com.dhn.peanut.shots;

import android.content.Context;
import android.net.Uri;
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

/**
 * Created by DHN on 2016/5/31.
 */
class ShotAdapter extends RecyclerView.Adapter<ShotAdapter.Holder> {

    //TODO
    private List<Shot> data;
    private Context context;
    private ShotsContract.View mView;

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
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_shot, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Shot shot = data.get(position);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        hierarchy.setPlaceholderImage(context.getResources().getDrawable(R.drawable.placeholder), ScalingUtils.ScaleType.FIT_XY);



        Uri uri = Uri.parse(shot.getImages().getNormal());
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
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


        public Holder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
