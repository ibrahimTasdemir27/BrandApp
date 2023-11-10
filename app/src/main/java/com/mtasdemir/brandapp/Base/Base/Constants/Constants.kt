package com.mtasdemir.brandapp.Base.Base.Constants

enum class Constants {
    ;

    enum class ADRewarded(val key: String) {
        ALRewardedAdMob("ca-app-pub-3264598769747909/3957222614"),
        ALRewardedInMobi("940c899570e389ac"),
        ALRewardedLiftOff("c55e19ba4b66233f");

        companion object {
            val allCases = arrayOf(ALRewardedAdMob, ALRewardedInMobi, ALRewardedLiftOff)

            fun randomRewardedIdentifier(): String {
                return allCases.random().key
            }
        }
    }


    enum class ADBanner(val key: String) {
        ALBannerAdMob("ca-app-pub-3264598769747909/5358505017"),
        ALBannerInMobi("25fcacf47eaa2e8d"),
        ALBannerLiftOff("66c8c0389cd19332");


        companion object {
            val allCases = arrayOf(ALBannerAdMob, ALBannerInMobi, ALBannerLiftOff)

            fun randomBannerIdentifier(): String {
                return allCases.random().key
            }
        }
    }



}