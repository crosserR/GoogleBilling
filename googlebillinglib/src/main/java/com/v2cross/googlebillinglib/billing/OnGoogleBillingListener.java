package com.v2cross.googlebillinglib.billing;

import androidx.annotation.NonNull;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.SkuDetails;

import java.util.List;

/**
 * Created by v2cross.com on 2020/5/31.
 */
public class OnGoogleBillingListener {

    @SuppressWarnings("WeakerAccess")
    public String tag = null;

    /**
     * 查询成功
     * @param skuType 内购或者订阅
     * @param list 商品列表
     * @param isSelf 是否是当前页面的结果
     */
    public void onQuerySuccess(@NonNull String skuType, @NonNull List<SkuDetails> list, boolean isSelf){}


    public boolean onPurchaseSuccess(@NonNull Purchase purchase, boolean isSelf){return true;}

    /**
     * 初始化成功
     * @param isSelf 是否是当前页面的结果
     */
    public void onSetupSuccess(boolean isSelf){}


    public boolean onRecheck(@NonNull String skuType, @NonNull Purchase purchase, boolean isSelf) {
        return false;
    }

    /**
     * 链接断开
     */
    @SuppressWarnings("WeakerAccess")
    public void onBillingServiceDisconnected(){ }

    /**
     * 消耗成功
     * @param purchaseToken token
     * @param isSelf 是否是当前页面的结果
     */
    public void onConsumeSuccess(@NonNull String purchaseToken,boolean isSelf){}


    /**
     * 确认购买成功
     * @param isSelf 是否是当前页面的结果
     */
    public void onAcknowledgePurchaseSuccess(boolean isSelf){}

    /**
     * 失败回调
     * @param tag {@link GoogleBillingUtil.GoogleBillingListenerTag}
     * @param responseCode 返回码{https://developer.android.com/google/play/billing/billing_reference}
     * @param isSelf 是否是当前页面的结果
     */
    public void onFail(@NonNull GoogleBillingUtil.GoogleBillingListenerTag tag, int responseCode, boolean isSelf){}

    /**
     * google组件初始化失败等等。
     * @param tag {@link GoogleBillingUtil.GoogleBillingListenerTag}
     * @param isSelf 是否是当前页面的结果
     */
    public void onError(@NonNull GoogleBillingUtil.GoogleBillingListenerTag tag, boolean isSelf){}


    /**
     * 获取内购和订阅所有历史订单-无论是否还有效
     * @param purchaseList 商品历史列表
     */
    public void onQueryHistory(@NonNull List<PurchaseHistoryRecord> purchaseList){

    }
}