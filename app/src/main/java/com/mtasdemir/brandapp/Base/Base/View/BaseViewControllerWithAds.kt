package com.mtasdemir.brandapp.Base.Base.View

import android.os.Bundle
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxRewardedAd
import com.mtasdemir.brandapp.Base.Base.Constants.Constants
import com.mtasdemir.brandapp.Base.Base.Protocols.ConnectivityMiddleware
import com.mtasdemir.brandapp.Manager.SPrefencesManager

enum class ADSRewardedTask {
    searchBrand
}

interface BaseViewControllerWithAdsDelegate {
    fun rewardedSuccess(task: ADSRewardedTask)
    fun bannerLoaded(banner: MaxAdView)
}


 open abstract class BaseViewControllerWithAds:
    BaseViewController(),
    MaxRewardedAdListener,
    MaxAdViewAdListener,
    ConnectivityMiddleware {

    private var rewardedAd: MaxRewardedAd? = null
    private var bannerAd: MaxAdView? = null

    private var showAd = false

    private var lastRewardedRequestTask: ADSRewardedTask? = null

    open fun provideAdsDelegate(): BaseViewControllerWithAdsDelegate? {
        return null
    }

    var adsDelegate: BaseViewControllerWithAdsDelegate? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (provideAdsDelegate() != null) {
            adsDelegate = provideAdsDelegate()
        }

        createRewardedAd()

    }





    /** Create ADS **/
    private fun createRewardedAd() {
        rewardedAd = MaxRewardedAd.getInstance(Constants.ADRewarded.randomRewardedIdentifier(), this)
        rewardedAd?.setListener(this)
        rewardedAd?.loadAd()
    }


    open fun createBannerAd() {
        bannerAd = MaxAdView(Constants.ADBanner.randomBannerIdentifier(), this)
        bannerAd?.setListener(this)
        bannerAd?.loadAd()

        bannerAd?.startAutoRefresh()

        adsDelegate?.bannerLoaded(bannerAd!!)
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
        if (rewardedAd?.isReady == true) {
            println("Rewarded Ready")
            rewardedAd?.setListener(this)
            rewardedAd?.showAd()
        } else {
            showAd = true
            println("Rewarded Not Ready")
            createRewardedAd()
        }
    }

     override fun onAdLoaded(p0: MaxAd) {
         if (showAd) {

             if (p0.format == MaxAdFormat.REWARDED) {
                 rewardedAd?.showAd()
             } else if (p0.format == MaxAdFormat.BANNER) {
                 print("Banner Load Success")
             } else {
             //interstitialAd?.show()
             }
             showAd = false
         }
     }

     override fun onAdDisplayed(p0: MaxAd) {

     }

     override fun onAdHidden(p0: MaxAd) {

     }

     override fun onAdClicked(p0: MaxAd) {

     }

     override fun onAdLoadFailed(p0: String, p1: MaxError) {

     }

     override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {

     }

     override fun onAdExpanded(p0: MaxAd) {

     }

     override fun onAdCollapsed(p0: MaxAd) {

     }

     override fun onUserRewarded(p0: MaxAd, p1: MaxReward) {
         if (lastRewardedRequestTask != null) {
             adsDelegate?.rewardedSuccess(lastRewardedRequestTask!!)
         }
     }

     override fun onRewardedVideoStarted(p0: MaxAd) {

     }

     override fun onRewardedVideoCompleted(p0: MaxAd) {

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

