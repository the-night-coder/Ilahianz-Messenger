package com.nightcoder.ilahianz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.nightcoder.ilahianz.R;

public class SlideAdapter extends PagerAdapter {

    private Context context;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    private int[] slideImages = {
            R.mipmap.welcome,
            R.mipmap.chat_onboard,
            R.mipmap.location,
            R.mipmap.helpline
    };
    private String[] SlideHeading = {
            "Welcome", "Chat", "Location", "Help"
    };
    private String[] slide_contents = {
            "To make better Community. and Friendship",
            "Chat with your Ilahianz , loved once " +
                    "And Teachers",
            "Locate your friends.",
            "Use Help if you need Check it out our Help Center"
    };

    @Override
    public int getCount() {
        return SlideHeading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        View view = layoutInflater.inflate(R.layout.slide, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView heading = view.findViewById(R.id.heading);
        TextView contents = view.findViewById(R.id.contents);
        heading.setText(SlideHeading[position]);
        imageView.setImageResource(slideImages[position]);
        contents.setText(slide_contents[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
