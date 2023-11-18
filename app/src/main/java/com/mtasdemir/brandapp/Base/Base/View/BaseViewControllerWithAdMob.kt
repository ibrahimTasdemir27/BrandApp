package com.mtasdemir.brandapp.Base.Base.View

import android.media.tv.AdRequest
import android.os.Bundle
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.mtasdemir.brandapp.Base.Base.Constants.Constants
import com.mtasdemir.brandapp.Base.Base.Protocols.ConnectivityMiddleware
import com.mtasdemir.brandapp.Manager.SPrefencesManager

interface BaseViewControllerWithAdMobDelegate {
    fun rewardedSuccess(task: ADSRewardedTask)
    fun bannerLoaded(banner: AdView)
}


open abstract class BaseViewControllerWithAdMob:
    BaseViewController(),
    ConnectivityMiddleware {

    private var rewardedAd: RewardedAd? = null
    private var bannerAd: AdView? = null

    private var showAd = false

    private var lastRewardedRequestTask: ADSRewardedTask? = null

    open fun provideAdsDelegate(): BaseViewControllerWithAdMobDelegate? {
        return null
    }

    var adsDelegate: BaseViewControllerWithAdMobDelegate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (provideAdsDelegate() != null) {
            adsDelegate = provideAdsDelegate()
        }

        createRewardedAd()

    }





    /** Create ADS **/
    private fun createRewardedAd() {
        var adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        RewardedAd.load(this,"ca-app-pub-3264598769747909/3957222614", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
            }
        })
    }


    open fun createBannerAd() {
        /*
        bannerAd = MaxAdView(Constants.ADBanner.randomBannerIdentifier(), this)
        bannerAd?.setListener(this)
        bannerAd?.loadAd()

        bannerAd?.startAutoRefresh()

        adsDelegate?.bannerLoaded(bannerAd!!)

         */
    }









    //MARK: -> Request ADS
    fun requestRewardedADS(task: ADSRewardedTask): Boolean {
        if (!isConnectedToInternet()) {
            //showAlert(with: BaseError.networkError.localizedDescription)
            return false
        }



        lastRewardedRequestTask = task

        when(task) {
            ADSRewardedTask.searchBrand -> return SPrefencesManager.isSearchable
        }
    }






    //MARK: -> Provide ADS
    fun provideRewardedADS(task: ADSRewardedTask) {
        showRewardedADS()
    }






    private fun showRewardedADS() {
        println("show reward ads")
        if (rewardedAd == null) {
            createRewardedAd()
        } else {
            rewardedAd?.show(this, OnUserEarnedRewardListener {
                if (lastRewardedRequestTask != null) {
                    adsDelegate?.rewardedSuccess(lastRewardedRequestTask!!)
                    createRewardedAd()
                }
            })
        }
    }



    /*


   /** AD Delegate **/
   override fun onAdLoaded(p0: MaxAd?) {
       if (showAd) {
           if (p0 != null) {
               if (p0.format == MaxAdFormat.REWARDED) {
                   rewardedAd?.showAd()
               } else if (p0.format == MaxAdFormat.BANNER) {
                   print("Banner Load Success")
               } else {
                   //interstitialAd?.show()
               }
           }
           showAd = false
       }
   }

   override fun onAdDisplayed(p0: MaxAd?) {

   }

   override fun onAdHidden(p0: MaxAd?) {

   }

   override fun onAdClicked(p0: MaxAd?) {

   }

   override fun onAdLoadFailed(p0: String?, p1: MaxError?) {

   }

   override fun onAdDisplayFailed(p0: MaxAd?, p1: MaxError?) {

   }


   override fun onUserRewarded(p0: MaxAd?, p1: MaxReward?) {
       if (lastRewardedRequestTask != null) {
           adsDelegate?.rewardedSuccess(lastRewardedRequestTask!!)
       }
   }

   override fun onRewardedVideoStarted(p0: MaxAd?) {

   }

   override fun onRewardedVideoCompleted(p0: MaxAd?) {

   }




   /** Specific Banner **/
   override fun onAdExpanded(p0: MaxAd?) {

   }

   override fun onAdCollapsed(p0: MaxAd?) {

   }
    */

}

