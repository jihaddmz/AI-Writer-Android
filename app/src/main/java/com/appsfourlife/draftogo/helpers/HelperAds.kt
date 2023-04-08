package com.appsfourlife.draftogo.helpers

import android.app.Activity
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object HelperAds {

    fun loadAds(onAdLoad: () -> Unit) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            App.context,
            Constants.AD_UNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    SettingsNotifier.mRewardedAds = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    SettingsNotifier.mRewardedAds = ad

                    SettingsNotifier.mRewardedAds?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                // Called when a click is recorded for an ad.
                                HelperAnalytics.sendEvent("ad_clicked")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                SettingsNotifier.mRewardedAds = null
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when ad fails to show.
                                SettingsNotifier.mRewardedAds = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                SettingsNotifier.showLoadingDialog.value = false
                            }
                        }

                    onAdLoad()
                }
            })
    }

    fun showAds(activity: Activity, onUserEarned: (Int) -> Unit) {
        SettingsNotifier.mRewardedAds?.let { ad ->
            ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
                // Handle the reward.
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type
                onUserEarned(rewardAmount)
            })
        } ?: run {
            // The rewarded ad wasn't ready yet.
        }
    }
}