package com.v2cross.googlebilling;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.SkuDetails;
import com.v2cross.googlebillinglib.BuildConfig;
import com.v2cross.googlebillinglib.billing.GoogleBillingUtil;
import com.v2cross.googlebillinglib.billing.OnGoogleBillingListener;

import java.util.List;


public class PurchaseActivity extends AppCompatActivity {

    private GoogleBillingUtil mGoogleBillingUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        GoogleBillingUtil.isDebug(BuildConfig.DEBUG);
        GoogleBillingUtil.setSkus(new String[]{"shopSku"}, new String[]{"subSku"});// 这里需要自行修改sku
        GoogleBillingUtil.setIsAutoAcknowledgePurchase(true);

        mGoogleBillingUtil = GoogleBillingUtil.getInstance().addOnGoogleBillingListener(this, new OnMyGoogleBillingListener()).build(this);
        findViewById(R.id.button1).setOnClickListener(v -> {
            mGoogleBillingUtil.purchaseInApp(this, mGoogleBillingUtil.getInAppSkuByPosition(0));
        });
        findViewById(R.id.button2).setOnClickListener(v -> {
            mGoogleBillingUtil.queryPurchaseHistoryAsyncInApp(this);
        });
        findViewById(R.id.button3).setOnClickListener(v -> {
            mGoogleBillingUtil.purchaseSubs(this, mGoogleBillingUtil.getSubsSkuByPosition(0));
        });
        findViewById(R.id.button4).setOnClickListener(v -> {
            mGoogleBillingUtil.queryPurchaseHistoryAsyncSubs(this);
        });
    }

    public static class OnMyGoogleBillingListener extends OnGoogleBillingListener {

        @Override
        public void onSetupSuccess(boolean isSelf) {
            super.onSetupSuccess(isSelf);
            log("内购服务初始化完成");

        }

        @Override
        public void onQuerySuccess(@NonNull String skuType, @NonNull List<SkuDetails> list, boolean isSelf) {
            super.onQuerySuccess(skuType, list, isSelf);
            if (!isSelf) {
                return;
            }

            for (SkuDetails skuDetails : list) {
                log(skuDetails.getSku() + "---" + skuDetails.getDescription() + "---" + skuDetails.getPrice());
            }

        }

        @Override
        public boolean onRecheck(@NonNull String skuType, @NonNull Purchase purchase, boolean isSelf) {
            log("检测到订单" + skuType + ":" + purchase.getSkus().toString() + "--" + getPurchaseState(purchase.getPurchaseState()));
            return true;
        }

        @Override
        public boolean onPurchaseSuccess(@NonNull Purchase purchase, boolean isSelf) {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                log("支付成功");
            }
            return true;
        }

        @Override
        public void onConsumeSuccess(@NonNull String purchaseToken, boolean isSelf) {
            log("消耗商品成功:" + purchaseToken);
        }

        @Override
        public void onAcknowledgePurchaseSuccess(boolean isSelf) {
            log("确认购买商品成功");
        }

        @Override
        public void onFail(@NonNull GoogleBillingUtil.GoogleBillingListenerTag tag,
                           int responseCode, boolean isSelf) {
            log("操作失败:tag=" + tag.name() + ",responseCode=" + responseCode);
        }

        @Override
        public void onError(@NonNull GoogleBillingUtil.GoogleBillingListenerTag tag,
                            boolean isSelf) {
            log("发生错误:tag= " + tag.name());
        }

        @Override
        public void onQueryHistory(@NonNull List<PurchaseHistoryRecord> purchaseList) {
            super.onQueryHistory(purchaseList);
            log("返回所有内购和订阅的历史订单: " + purchaseList.size());
        }
    }

    public static void log(String msg) {
        Log.e("googlebillinglib", msg);
    }

    public static String getPurchaseState(int purchaseState) {
        switch (purchaseState) {
            case Purchase.PurchaseState.PURCHASED:
                return "PURCHASED";
            case Purchase.PurchaseState.PENDING:
                return "PENDING";
            case Purchase.PurchaseState.UNSPECIFIED_STATE:
                return "UNSPECIFIED_STATE";
            default:
                return "未知状态";
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleBillingUtil != null) {
            mGoogleBillingUtil.onDestroy(this);
        }
        GoogleBillingUtil.endConnection();
    }
}


