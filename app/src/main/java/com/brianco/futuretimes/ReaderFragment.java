package com.brianco.futuretimes;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class ReaderFragment extends Fragment implements TitleSettable {

    public static final String KEY_PAGE = "KEY_PAGE";

    private TextView mDescriptionView;
    private ImageView mPicView;
    private TextView mPdfLinkView;
    private TextView mTranscriptView;

    @Override
    public final void setTitle() {
        final Page page = (Page) getArguments().getSerializable(KEY_PAGE);
        getActivity().setTitle(getString(R.string.action_bar_title_page,
                page.getPage(),
                page.getVolume(),
                page.getDate()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView
                = inflater.inflate(R.layout.fragment_reader, container, false);
        final Page page = (Page) getArguments().getSerializable(KEY_PAGE);
        mDescriptionView = (TextView) rootView.findViewById(R.id.description);
        mDescriptionView.setText(page.getDescription());
        mPicView = (ImageView) rootView.findViewById(R.id.page_pic);
        Picasso.with(getActivity()).load(page.getPicLink()).into(mPicView);
        mTranscriptView = (TextView) rootView.findViewById(R.id.transcript);
        mTranscriptView.setVisibility(View.GONE);
        if (page.getTranscriptLink() != null) {
            setTranscriptText(page.getTranscriptLink());
        }
        mPdfLinkView = (TextView) rootView.findViewById(R.id.click_to_pdf);
        mPdfLinkView.setClickable(true);
        mPdfLinkView.setMovementMethod(LinkMovementMethod.getInstance());
        final String text = "<a href='" + page.getPdfLink() + "'>"
                + getString(R.string.link_to_pdf_text) + "</a>";
        mPdfLinkView.setText(Html.fromHtml(text));
        return rootView;
    }

    private void setTranscriptText(final String url) {
        new TranscriptNetworkTask(mTranscriptView, url).execute();
    }

    private static class TranscriptNetworkTask extends AsyncTask<Void, Void, String> {
        private final OkHttpClient mClient = new OkHttpClient();
        private final WeakReference<TextView> mTextViewReference;
        private final String mUrl;

        private TranscriptNetworkTask(TextView textView, String url) {
            mTextViewReference = new WeakReference<TextView>(textView);
            mUrl = url;
        }

        @Override
        protected String doInBackground(Void... params) {
            final Request request = new Request.Builder()
                    .url(mUrl)
                    .build();

            final Response response;
            try {
                response = mClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (!response.isSuccessful()) try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String str) {
            final TextView textView = mTextViewReference.get();
            if (textView == null) return;
            if (str == null || str.length() <= 0) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(str);
            }
        }
    }
}
