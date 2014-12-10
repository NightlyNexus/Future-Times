package com.brianco.futuretimes;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ReaderFragment extends Fragment implements TitleSettable {

    public static final String KEY_PAGE = "KEY_PAGE";

    private TextView mTranscriptView;
    private ImageView mPicView;
    private TextView mPdfLinkView;

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
        mTranscriptView = (TextView) rootView.findViewById(R.id.transcript);
        mTranscriptView.setText(page.getDescription()); // TODO: get transcript
        mPicView = (ImageView) rootView.findViewById(R.id.page_pic);
        Picasso.with(getActivity()).load(page.getPicLink()).into(mPicView);
        mPdfLinkView = (TextView) rootView.findViewById(R.id.click_to_pdf);
        mPdfLinkView.setClickable(true);
        mPdfLinkView.setMovementMethod(LinkMovementMethod.getInstance());
        final String text = "<a href='" + page.getPdfLink() + "'>"
                + getString(R.string.link_to_pdf_text) + "</a>";
        mPdfLinkView.setText(Html.fromHtml(text));
        return rootView;
    }
}
