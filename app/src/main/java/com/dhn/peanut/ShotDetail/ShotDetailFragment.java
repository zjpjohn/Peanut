package com.dhn.peanut.shotdetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dhn.peanut.R;
import com.dhn.peanut.data.Comment;
import com.dhn.peanut.data.Shot;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by DHN on 2016/6/1.
 */
public class ShotDetailFragment extends Fragment implements ShotDetailContract.View{

    private SimpleDraweeView mDraweeView;
    private RecyclerView mRecyclerView;
    private Menu mMenu;

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

        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shot_detail, container, false);
        mDraweeView = (SimpleDraweeView) view.findViewById(R.id.detail_iv);
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
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.checkLiked(mShot.getId());
            }
        }, 1000);
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(getContext(), mShot);
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

    }

    @Override
    public void showLike() {
        mMenu.findItem(R.id.menu_like).setIcon(R.drawable.heart);
    }

    @Override
    public void showUnLike() {
        mMenu.findItem(R.id.menu_like).setIcon(R.drawable.heart_outline);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_shot_detail, menu);
        mMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                //TODO
                Toast.makeText(getActivity(), "in develop", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_like:
                //TODO
                mPresenter.changeLike(mShot.getId());
                break;
        }
        return true;

    }
}
