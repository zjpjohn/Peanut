package com.dhn.peanut.ShotDetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.Shot;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DHN on 2016/6/1.
 */
public class ShotDetailFragment extends Fragment implements ShotDetailContract.View{

    private SimpleDraweeView mDraweeView;
    private TextView mTextLike;
    private TextView mTextView;
    private TextView mTextTitle;
    private TextView mTextTime;
    private RecyclerView mRecyclerView;

    private Shot mShot;
    private ShotDetailContract.Presenter mPresenter;
    private CommentAdapter commentAdapter;

    public ShotDetailFragment() {};

    public static ShotDetailFragment getInstance(Shot shot) {
        ShotDetailFragment fragment = new ShotDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("shot", shot);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mShot = (Shot) getArguments().get("shot");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shot_detail, container, false);
        mDraweeView = (SimpleDraweeView) view.findViewById(R.id.detail_iv);
        mTextLike = (TextView) view.findViewById(R.id.like);
        mTextView = (TextView) view.findViewById(R.id.view);
        mTextTitle = (TextView) view.findViewById(R.id.title);
        mTextTime = (TextView) view.findViewById(R.id.time);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.detail_recyclerview);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        //show data in view
        mPresenter.loadComment(mShot.getId());
        showPicture();
        showPicInfor();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(getContext());
        mRecyclerView.setAdapter(commentAdapter);
    }

    @Override
    public void setPresenter(ShotDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showComments(List<Comment> comments) {
        commentAdapter.replaceComments(comments);
    }

    @Override
    public void showPicture() {
        Uri uri = Uri.parse(mShot.getImages().getNormal());
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        mDraweeView.setController(controller);
    }

    @Override
    public void showPicInfor() {

        mTextTitle.setText(mShot.getTitle());
        mTextLike.setText("" + mShot.getLikes_count());
        mTextView.setText("" + mShot.getViews_count());
        mTextTime.setText("" + mShot.getCreated_at());
    }
}
