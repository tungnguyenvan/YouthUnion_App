package com.dev.nguyenvantung.fg_app.data.source.remote.response.hoatdong;

import com.dev.nguyenvantung.fg_app.data.model.hoatdong.HoatDong;
import com.dev.nguyenvantung.fg_app.data.source.remote.response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HoatDongResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private HoatDong data;

    public HoatDong getData() {
        return data;
    }

    public void setData(HoatDong data) {
        this.data = data;
    }
}
