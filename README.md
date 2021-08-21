# GoogleBilling
Android, Google billing libs,with billingclient version 4.0.0


# Android Google Billing Library   [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.v2cross.googlebilling/googlebillinglib/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.v2cross.googlebilling/googlebillinglib)

This is a simple, straight-forward implementation of the Android Google billingclient v4.0.0 API.

It supports: In-App Product Purchases (both non-consumable and consumable) and Subscriptions.

## Getting Started

* You project should build against Android 5.1 SDK at least.

* Add this *Android GoogleBilling Library* to your project:
  - If you guys are using Android Studio and Gradle, add this to you build.gradle file:
  
```groovy
repositories {
  mavenCentral()
}

dependencies {
  implementation 'com.v2cross.googlebilling:googlebillinglib:1.0.0'
}
```


* Create instance of GoogleBillingUtil class and implement your OnGoogleBillingListener in your Activity source code. 
 
```java

public class PurchaseActivity extends AppCompatActivity {

    private GoogleBillingUtil mGoogleBillingUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        GoogleBillingUtil.isDebug(BuildConfig.DEBUG);
        GoogleBillingUtil.setSkus(new String[]{"shopSku"}, new String[]{"subSku"});// 这里需要自行修改sku
        GoogleBillingUtil.setIsAutoAcknowledgePurchase(true);//设置是否自动确认购买

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
```

### Remeber create your skus in your google play console and set up here
```java
GoogleBillingUtil.setSkus(new String[]{"shopSku"}, new String[]{"subSku"});// 这里需要自行修改sku
GoogleBillingUtil.setIsAutoAcknowledgePurchase(true);//设置是否自动确认购买
```
### all the callback in the `OnGoogleBillingListener` ,implements your own OnGoogleBillingListener
```java
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
```
 
## Proguard

```
-keep class com.android.vending.billing.**
```

## License

Copyright 2021 v2cross.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

