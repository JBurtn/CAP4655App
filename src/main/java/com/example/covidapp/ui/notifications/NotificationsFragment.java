package com.example.covidapp.ui.notifications;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.covidapp.GPStracking;
import com.example.covidapp.R;
import com.example.covidapp.VolleyBE;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        VolleyBE volleyBE = VolleyBE.getInstance(getContext());
        GPStracking gpStracking = new GPStracking(getContext());
        LinearLayout scrollView = root.findViewById(R.id.NewsList);


        notificationsViewModel.getNews(volleyBE);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), news -> {
            ArrayList<NotificationsViewModel.news> piece = notificationsViewModel.getText().getValue();
            for(int i = 0; i < piece.size(); i++){
                TextView title = new TextView(getContext());
                TextView Link = new TextView(getContext());
                TextView excerpt = new TextView(getContext());

                Link.setAutoLinkMask(Linkify.WEB_URLS);
                Link.setLinksClickable(true);

                title.setText(piece.get(i).getTitle());
                Link.setText(piece.get(i).getURL());
                excerpt.setText(piece.get(i).getExcerpt());

                title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                title.setLines(2);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);

                Link.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Link.setLines(2);
                excerpt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                excerpt.setLines(3);

                title.setPaddingRelative(0, 5, 0 ,5);
                Link.setPaddingRelative(0, 5, 0 ,5);
                excerpt.setPaddingRelative(0, 5, 0 ,5);

                scrollView.addView(title);
                scrollView.addView(Link);
                scrollView.addView(excerpt);

                Log.d("timestamp", notificationsViewModel.getGdata());
            }
        });

        return root;
    }
}
