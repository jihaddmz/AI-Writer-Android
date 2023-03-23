package com.appsfourlife.draftogo.helpers

import android.app.Activity
import com.appsfourlife.draftogo.App
import com.appsfourlife.draftogo.R
import com.appsfourlife.draftogo.util.SettingsNotifier
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
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
                    Helpers.logD("error loading")
                    SettingsNotifier.mRewardedAds = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Helpers.logD("success loading")
                    SettingsNotifier.mRewardedAds = ad

                    SettingsNotifier.mRewardedAds?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Helpers.logD("Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Helpers.logD("Ad dismissed fullscreen content.")
                                SettingsNotifier.mRewardedAds = null
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when ad fails to show.
                                Helpers.logD("Ad failed to show fullscreen content.")
                                SettingsNotifier.mRewardedAds = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Helpers.logD("Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Helpers.logD("Ad showed fullscreen content.")
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
            Helpers.logD("The rewarded ad wasn't ready yet.")
        }
    }
}