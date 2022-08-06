package com.finalyearproject.daretodonate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.finalyearproject.daretodonate.Models.MainScreenBannerModel;
import com.finalyearproject.daretodonate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainScreenBannerAdapter extends PagerAdapter {

    private Context context;
    private List<MainScreenBannerModel> mainScreenBannerModelList;

    public MainScreenBannerAdapter(Context context, List<MainScreenBannerModel> mainScreenBannerModelList) {
        this.context = context;
        this.mainScreenBannerModelList = mainScreenBannerModelList;
    }

    @Override
    public int getCount() {
        return mainScreenBannerModelList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainScreenBannerLayout = inflater.inflate(R.layout.main_screen_banner_item, null);

        ImageView bannerImage = mainScreenBannerLayout.findViewById(R.id.banner_image);
        Picasso.get().load(mainScreenBannerModelList.get(position).getImageUrl()).into(bannerImage);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mainScreenBannerModelList.get(position).getYoutubeUrl()));
        mainScreenBannerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(intent);
            }
        });

        container.addView(mainScreenBannerLayout);
        return mainScreenBannerLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
