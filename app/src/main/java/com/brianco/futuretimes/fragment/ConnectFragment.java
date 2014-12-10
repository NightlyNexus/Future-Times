package com.brianco.futuretimes.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brianco.futuretimes.R;

public class ConnectFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final TextView rootView = (TextView) inflater.inflate(
                R.layout.fragment_text, container, false);
        rootView.setTextSize(getResources().getDimension(R.dimen.text_size_default));
        rootView.setClickable(true);
        rootView.setMovementMethod(LinkMovementMethod.getInstance());
        rootView.setText(Html.fromHtml(getString(R.string.connect_text)));
        return rootView;
    }
}
