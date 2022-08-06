package com.finalyearproject.daretodonate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.finalyearproject.daretodonate.Models.OnBoardingScreenModel;
import com.finalyearproject.daretodonate.R;

import java.util.List;

public class OnBoardingScreenAdapter extends PagerAdapter {

    private Context context;
    private List<OnBoardingScreenModel> onBoardingScreenModelList;

    public OnBoardingScreenAdapter(Context context, List<OnBoardingScreenModel> onBoardingScreenModelList) {
        this.context = context;
        this.onBoardingScreenModelList = onBoardingScreenModelList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View onBoardingScreenLayout = inflater.inflate(R.layout.on_boarding_screen_item, null);

        ImageView boardingScreenImage = onBoardingScreenLayout.findViewById(R.id.boarding_screen_image);
        TextView boardingScreenTitle = onBoardingScreenLayout.findViewById(R.id.boarding_screen_title);
        TextView boardingScreenDescription = onBoardingScreenLayout.findViewById(R.id.boarding_screen_description);

        boardingScreenImage.setImageDrawable(onBoardingScreenModelList.get(position).getOnBoardingImage());
        boardingScreenTitle.setText(onBoardingScreenModelList.get(position).getOnBoardingTitle());
        boardingScreenDescription.setText(onBoardingScreenModelList.get(position).getOnBoardingDescription());

        container.addView(onBoardingScreenLayout);
        return onBoardingScreenLayout;
    }

    @Override
    public int getCount() {
        return onBoardingScreenModelList.size();
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
