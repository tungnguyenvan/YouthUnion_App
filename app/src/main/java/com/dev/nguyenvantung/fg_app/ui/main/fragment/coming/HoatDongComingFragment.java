package com.dev.nguyenvantung.fg_app.ui.main.fragment.coming;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dev.nguyenvantung.fg_app.R;
import com.dev.nguyenvantung.fg_app.data.model.hoatdong.HoatDong;
import com.dev.nguyenvantung.fg_app.data.repository.HoatDongRepository;
import com.dev.nguyenvantung.fg_app.data.source.local.HoatDongLocalDataSource;
import com.dev.nguyenvantung.fg_app.data.source.remote.HoatDongRemoteDataSource;
import com.dev.nguyenvantung.fg_app.ui.hoatdongdetail.HoatDongDetailActivity;
import com.dev.nguyenvantung.fg_app.ui.main.fragment.adapter.HoatDongAdapter;
import com.dev.nguyenvantung.fg_app.ui.storehoatdong.StoreHoatDongActivity;
import com.dev.nguyenvantung.fg_app.utils.AppConstants;
import com.dev.nguyenvantung.fg_app.utils.AppPref;
import com.dev.nguyenvantung.fg_app.utils.navigator.Navigator;
import com.dev.nguyenvantung.fg_app.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HoatDongComingFragment extends Fragment implements HoatDongComingConstract.View{
    private static HoatDongComingFragment instance;
    private static final String TAG = HoatDongComingFragment.class.getName();
    private HoatDongComingConstract.Presenter mPresenter;

    @BindView(R.id.rv_hoatdong_coming)
    protected RecyclerView rv_hoatdong_coming;

    @BindView(R.id.coming_progress)
    protected ProgressBar progressBar;

    @BindView(R.id.btn_store_hoatdong)
    protected Button btnStore;

    private HoatDongAdapter hoatDongAdapter;
    private List<HoatDong> listHoatDongComing;
    public HoatDongComingFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(){
        if (instance == null){
            instance = new HoatDongComingFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coming, container, false);
        ButterKnife.bind(this,view);

        initView();
        initPresenter();

        return view;
    }

    private void initView() {
        listHoatDongComing = new ArrayList<>();
        hoatDongAdapter = new HoatDongAdapter(listHoatDongComing, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        rv_hoatdong_coming.setHasFixedSize(true);
        rv_hoatdong_coming.setLayoutManager(linearLayoutManager);
        rv_hoatdong_coming.setAdapter(hoatDongAdapter);

        btnStore.setOnClickListener(appView -> storeHoatDong());
    }

    private void initPresenter(){
        HoatDongRepository hoatDongRepository = new HoatDongRepository(HoatDongLocalDataSource.getInstance(), HoatDongRemoteDataSource.getInstance(getContext()));
        mPresenter = new HoatDongComingPresenter(hoatDongRepository, SchedulerProvider.getInstance());
        mPresenter.setView(this);

        mPresenter.listHoatDongComing(AppConstants.BEARER + AppPref.getInstance(getContext()).getApiToken());
    }

    private void storeHoatDong(){
        Intent intent = new Intent(getActivity(), StoreHoatDongActivity.class);
        new Navigator(getActivity()).startActivityForResult(intent, AppConstants.REQUEST_CODE_STORE_HOATDONG);
    }

    @Override
    public void setListHoatDongComing(List<HoatDong> listHoatDongComing) {
        this.listHoatDongComing.addAll(listHoatDongComing);
        hoatDongAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hoatDongDetail(View view, int id) {
        Navigator navigator = new Navigator(getActivity());
        Intent intent = new Intent(getContext(), HoatDongDetailActivity.class);
        intent.putExtra(AppConstants.ID, id);
        navigator.startActivity(intent, view, getString(R.string.share_hoatdong));
    }
}
