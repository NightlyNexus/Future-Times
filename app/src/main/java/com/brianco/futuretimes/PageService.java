package com.brianco.futuretimes;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

public interface PageService {

    @GET("/")
    void getPageList(Callback<List<PageRetro>> cb);
}
