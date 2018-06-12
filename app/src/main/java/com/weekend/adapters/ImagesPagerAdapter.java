package com.weekend.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.weekend.R;
import com.weekend.interfaces.IPagerItemClicked;
import com.weekend.models.PropertyDetailModel;
import com.weekend.models.PropertyListModel;
import com.weekend.utils.ImageUtil;

import java.util.List;


public class ImagesPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater infalter;
    private boolean isFromList;
    private List<PropertyDetailModel.PropertyImage> propertyImages;
    private List<PropertyDetailModel.ChaletImage> chaletImages;
    private List<PropertyListModel.PropertyImage> listPropertyImages;
    private boolean isDetail;
    private IPagerItemClicked iPagerItemClicked;

    public ImagesPagerAdapter(Context context) {
        this.context = context;
        infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPropertyDetailListImages(List<PropertyDetailModel.PropertyImage> propertyImages, IPagerItemClicked iPagerItemClicked) {
        this.propertyImages = propertyImages;
        this.iPagerItemClicked = iPagerItemClicked;
    }

    public void setPropertyListImages(List<PropertyListModel.PropertyImage> listPropertyImages, IPagerItemClicked iPagerItemClicked) {
        this.listPropertyImages = listPropertyImages;
        this.iPagerItemClicked = iPagerItemClicked;
    }

    public void setChaletImages(List<PropertyDetailModel.ChaletImage> chaletImages, boolean isDetail, IPagerItemClicked iPagerItemClicked) {
        this.chaletImages = chaletImages;
        this.isDetail = isDetail;
        this.iPagerItemClicked = iPagerItemClicked;
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
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((RelativeLayout) arg1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView picture;
        View itemView = infalter.inflate(R.layout.item_images, null, false);
        picture = (ImageView) itemView.findViewById(R.id.image);
        itemView.setTag("myview" + position);
        String image = null;
        if (propertyImages != null && propertyImages.size() > 0) {
            image = propertyImages.get(position).getPropertyImages();
        } else if (chaletImages != null && chaletImages.size() > 0 && isDetail) {
            image = chaletImages.get(position).getImageUrl();
        } else if (listPropertyImages != null && listPropertyImages.size() > 0) {
            image = listPropertyImages.get(position).getPropertyImage();
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iPagerItemClicked != null) {
                    iPagerItemClicked.onPagerItemClicked(position);
                }
            }
        });

        ImageUtil.loadPropertyImage(context, image, picture);
        ((ViewPager) container).addView(itemView);
        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
