package com.mtasdemir.brandapp.Base.Base.Constants

enum class Constants {
    ;

    enum class ADRewarded(val key: String) {
        ALRewardedAdMob("908f6e77e397f8d2"),
        ALRewardedInMobi("1bdf347047b6c7be"),
        ALRewardedLiftOff("3e627d2c832b420e");

        companion object {
            private val allCases = arrayOf(ALRewardedAdMob, ALRewardedInMobi, ALRewardedLiftOff)

            fun randomRewardedIdentifier(): String {
                return allCases.random().key
            }
        }
    }


    enum class ADBanner(val key: String) {
        ALBannerAdMob("725da0ced5e141ae"),
        ALBannerInMobi("900ac08dff9dbeb2"),
        ALBannerLiftOff("ef029c693c19db32");


        companion object {
            private val allCases = arrayOf(ALBannerAdMob, ALBannerInMobi, ALBannerLiftOff)

            fun randomBannerIdentifier(): String {
                return allCases.random().key
            }
        }
    }



}