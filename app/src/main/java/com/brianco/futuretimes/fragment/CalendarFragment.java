package com.brianco.futuretimes.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.brianco.futuretimes.R;

public class CalendarFragment extends Fragment {

    private static final String WEB_PAGE_URL
            = "http://www.meetup.com/Atlanta-Science-Fiction-Society/events/";

    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mWebView = (WebView) inflater.inflate(
                R.layout.fragment_calendar, container, false);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.loadUrl(WEB_PAGE_URL);
        return mWebView;
    }
}
