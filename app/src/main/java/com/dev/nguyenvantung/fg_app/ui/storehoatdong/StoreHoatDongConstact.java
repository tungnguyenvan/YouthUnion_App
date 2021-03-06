package com.dev.nguyenvantung.fg_app.ui.storehoatdong;

import com.dev.nguyenvantung.fg_app.data.model.hoatdong.HoatDongRequest;
import com.dev.nguyenvantung.fg_app.data.model.hoatdongtype.HoatDongType;
import com.dev.nguyenvantung.fg_app.data.source.remote.response.hoatdong.HoatDongsResponse;

import java.util.List;

public interface StoreHoatDongConstact {
    interface View{
        void showProgressbar();
        void dimissProgressbar();
        void setListHoatDongType(List<HoatDongType> hoatDongTypes);
        void createHoatDongSuccess(HoatDongsResponse hoatDong);
    }
    interface Presenter{
        void listHoatDongType(String token);
        void storeHoatDong(String token, HoatDongRequest hoatDongRequest);
        void setView(View mView);
    }
}
