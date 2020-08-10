package com.iwangzhe.commonview;

import android.app.Activity;

import com.iwangzhe.commonview.adv.AdvCommonViewMain;
import com.iwangzhe.commonview.adv.model.OnSplashAdListener;

/**
 * author : 亚辉
 * e-mail : 2372680617@qq.com
 * date   : 2020/8/611:13
 * desc   :
 */
public class SplashAdManager {
    private static SplashAdManager mSplashAdManager = null;
    private final String mPosKey;
    private String mPageKey;

    public static SplashAdManager getInstance(String pageKey, String posKey) {
        synchronized (SplashAdManager.class) {
            if (mSplashAdManager == null) {
                mSplashAdManager = new SplashAdManager(pageKey, posKey);
            }
        }
        return mSplashAdManager;
    }

    private SplashAdManager(String pageKey, String posKey) {
        this.mPageKey = pageKey;
        this.mPosKey = posKey;
    }

    public void initSplashAd() {
        AdvCommonViewMain.getInstance().getControl().initAdverts(mPageKey, mPosKey);
    }

    public void loadAd(Activity activity, OnSplashAdListener listener) {
        if (AdvCommonViewMain.getInstance().getControl().isExistAdv(mPageKey, mPosKey)) {
            AdvCommonViewMain.getInstance().getControl().showSplashAd(mPageKey, mPosKey, activity, listener);
        } else {
            if (listener != null) {
                listener.closeAd();
            }
        }
    }
}
