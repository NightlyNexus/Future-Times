package com.brianco.futuretimes;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class FutureFragment extends Fragment implements TitleSettable {

    private static final String ENDPOINT
            = "https://www.googleapis.com/storage/v1/b/future_times/o";

    private RecyclerView mRecyclerView;
    private PageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PageService mRestService;
    private List<Page> mPageList;

    private final Callback<List<PageRetro>> mCallback = new Callback<List<PageRetro>>() {

        @Override
        public void success(List<PageRetro> pageRetros, Response response) {
            if (isDetached()) return;
            mPageList.clear();
            mPageList.addAll(mergeDuplicates(pageRetros));
            mAdapter.notifyDataSetChanged();
            setTitle();
        }

        @Override
        public void failure(RetrofitError error) {
            if (isDetached()) return;
        }
    };

    @Override
    public final void setTitle() {
        if (mPageList.size() >= 1) {
            final Page page0 = mPageList.get(0);
            getActivity().setTitle(getString(R.string.action_bar_title_page_list,
                    page0.getVolume(),
                    page0.getDate()));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageList = new ArrayList<Page>();
        final Gson gson = new GsonBuilder()
                        .registerTypeAdapter(List.class, new PageDeserializer())
                        .create();
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .build();
        mRestService = restAdapter.create(PageService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mRecyclerView
                = (RecyclerView) inflater.inflate(R.layout.fragment_future, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PageAdapter((FutureActivity) getActivity(), mPageList);
        mRecyclerView.setAdapter(mAdapter);

        mRestService.getPageList(mCallback);
    }

    private static Page mergePageRetros(final PageRetro pageRetro, final PageRetro pageRetro1) {
        final PageRetro pageWithMetadata;
        final String picLink;
        final String pdfLink;
        if (pageRetro1 == null) {
            if (pageRetro.isPic()) {
                throw new RuntimeException("We always need to have the PDF for a page!");
                /*picLink = pageRetro.getMediaLink();
                pdfLink = null;*/
            } else {
                picLink = null;
                pdfLink = pageRetro.getMediaLink();
                pageWithMetadata = pageRetro;
            }
        } else {
            if (pageRetro.isPic()) {
                picLink = pageRetro.getMediaLink();
                pdfLink = pageRetro1.getMediaLink();
                pageWithMetadata = pageRetro1;
            } else {
                picLink = pageRetro1.getMediaLink();
                pdfLink = pageRetro.getMediaLink();
                pageWithMetadata = pageRetro;
            }
        }
        return new Page(pdfLink, picLink,
                pageWithMetadata.getTranscriptLink(), pageWithMetadata.getContributor(),
                pageWithMetadata.getDate(), pageWithMetadata.getPublisher(),
                pageWithMetadata.getSource(), pageWithMetadata.getCreator(),
                pageWithMetadata.getDescription(), pageWithMetadata.getTitle(),
                pageWithMetadata.getVolume(), pageWithMetadata.getPage());
    }

    private static List<Page> mergeDuplicates(List<PageRetro> listContainingDuplicates) {
        final List<PageRetro> listDuplicates = new ArrayList<PageRetro>();
        final List<PageRetro> listFull = new ArrayList<PageRetro>();
        for (PageRetro pageRetro : listContainingDuplicates) {
            if (listFull.contains(pageRetro)) {
                listDuplicates.add(pageRetro);
            } else {
                listFull.add(pageRetro);
            }
        }
        final List<Page> pageList = new ArrayList<Page>(listFull.size());
        for (PageRetro pageRetro : listFull) {
            PageRetro duplicate = null;
            for (PageRetro pageRetro1 : listDuplicates) {
                if (pageRetro.equals(pageRetro1)) {
                    duplicate = pageRetro1;
                }
            }
            pageList.add(mergePageRetros(pageRetro, duplicate));
        }
        Collections.sort(pageList);
        return pageList;
    }
}
