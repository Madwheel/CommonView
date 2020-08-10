package com.iwangzhe.commonview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.iwangzhe.commonlibs.mod.tool.ToolCommonLibsMain;
import com.iwangzhe.commonlibs.mod.tool.img.CornerTransform;
import com.iwangzhe.commonview.adv.AdvCommonViewMain;
import com.iwangzhe.commonview.adv.model.IAdListener;
import com.iwangzhe.commonview.adv.model.OnSlideClickListener;
import com.iwangzhe.commonview.adv.model.OnSlideShowListener;
import com.iwangzhe.commonview.adv.view.SlideShowView;

import java.util.ArrayList;
import java.util.List;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/59:18
 * desc   :
 */
public class BannerAdManager {
    private static BannerAdManager mBannerAdManager = null;
    private String mPageKey;
    private String mPosKey;
    private float mCorner;

    public static BannerAdManager getInstance(String pageKey, String posKey) {
        synchronized (BannerAdManager.class) {
            if (mBannerAdManager == null) {
                mBannerAdManager = new BannerAdManager(pageKey, posKey);
            }
        }
        return mBannerAdManager;
    }

    private BannerAdManager(String pageKey, String posKey) {
        this.mPageKey = pageKey;
        this.mPosKey = posKey;
    }

    public void setCorner(float corner) {
        this.mCorner = corner;
    }

    public void initBannerAd() {
        AdvCommonViewMain.getInstance().getControl().initAdverts(mPageKey, mPosKey);
    }

    public void loadAd(final SlideShowView view, final OnSlideClickListener listener) {
        if (AdvCommonViewMain.getInstance().getControl().isExistAdv(mPageKey, mPosKey)) {
            showAd(mPageKey, mPosKey, mCorner, view, listener);
        } else {
            view.setVisibility(View.GONE);
            AdvCommonViewMain.getInstance().getControl().setAdListener(new IAdListener() {
                @Override
                public void onSuccess(final String pageKey, final String posKey) {
                    if (pageKey.equals(pageKey) && posKey.equals(posKey)) {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                showAd(pageKey, posKey, mCorner, view, listener);
                            }
                        });
                    }
                }
            });
        }
    }

    private void showAd(String pageKey, String posKey, final float corner, SlideShowView view, final OnSlideClickListener listener) {
        view.setVisibility(View.VISIBLE);
        final List<Integer> positionList = new ArrayList<>();
        AdvCommonViewMain.getInstance().getControl().showAdvView(view, pageKey, posKey, new OnSlideShowListener() {

            @Override
            public void onItemClick(int position, String imgUrl, String url, String resourEntryName) {
                if (listener != null) {
                    listener.onItemClick(position, imgUrl, url, resourEntryName);
                }
            }

            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                if (corner == 0) {
                    Glide.with(context)
                            .asBitmap()
                            .load(path)
                            .into(imageView);
                } else {
                    CornerTransform transformation = new CornerTransform(context, ToolCommonLibsMain.getInstance().getControl().dip2px(context, 5));
                    transformation.setExceptCorner(false, false, false, false);
                    Glide.with(context)
                            .asBitmap()
                            .load(path)
                            .apply(RequestOptions.bitmapTransform(transformation).diskCacheStrategy(DiskCacheStrategy.ALL))
                            .into(imageView);
                }
            }

            @Override
            public void onItemSelected(int mapId, int position, int total) {
                if (positionList != null && positionList.size() > 0 && positionList.contains(position)) {
                    return;
                }
                positionList.add(position);
                AdvCommonViewMain.getInstance().getControl().reportAdShow(mapId, position, total);
            }
        });
    }

}
