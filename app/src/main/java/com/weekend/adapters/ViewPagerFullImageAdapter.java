package com.weekend.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.weekend.R;
import com.weekend.models.PropertyDetailModel;
import com.weekend.models.PropertyListModel;
import com.weekend.utils.ImageUtil;
import com.weekend.views.TouchImageView;

import java.util.List;

public class ViewPagerFullImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PropertyDetailModel.PropertyImage> propertyImages;
    private List<PropertyDetailModel.ChaletImage> chaletImages;
    private List<PropertyListModel.PropertyImage> listPropertyImages;

    public ViewPagerFullImageAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setPropertyDetailListImages(List<PropertyDetailModel.PropertyImage> propertyImages) {
        this.propertyImages = propertyImages;
    }

    public void setPropertyListImages(List<PropertyListModel.PropertyImage> listPropertyImages) {
        this.listPropertyImages = listPropertyImages;
    }

    public void setChaletImages(List<PropertyDetailModel.ChaletImage> chaletImages) {
        this.chaletImages = chaletImages;
    }

    @Override
    public int getCount() {
        if (propertyImages != null) {
            return propertyImages.size();
        } else if (chaletImages != null) {
            return chaletImages.size();
        } else if (listPropertyImages != null) {
            return listPropertyImages.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if(view!=null) {
            TouchImageView ivImage = (TouchImageView) view.findViewById(R.id.ivFullImage);
            if(ivImage!=null) {
                ivImage.resetZoom();
            }
        }
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.inflate_full_image, container, false);
        TouchImageView ivImage = (TouchImageView) itemView.findViewById(R.id.ivFullImage);

        String image = null;
        if (propertyImages != null && propertyImages.size() > 0) {
            image = propertyImages.get(position).getPropertyImages();
        } else if (chaletImages != null && chaletImages.size() > 0) {
            image = chaletImages.get(position).getImageUrl();
        } else if (listPropertyImages != null && listPropertyImages.size() > 0) {
            image = listPropertyImages.get(position).getPropertyImage();
        }
        ImageUtil.loadPropertyImage(context, image, (ImageView) ivImage);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}