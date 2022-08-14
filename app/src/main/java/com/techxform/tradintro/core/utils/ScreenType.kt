package com.techxform.tradintro.core.utils

import androidx.annotation.IntDef
import com.techxform.tradintro.core.utils.ScreenType.Companion.MARKET
import com.techxform.tradintro.core.utils.ScreenType.Companion.PORTFOLIO
import com.techxform.tradintro.core.utils.ScreenType.Companion.WATCHLIST




@IntDef(WATCHLIST, PORTFOLIO, MARKET)
@Retention(AnnotationRetention.SOURCE)
annotation class ScreenType {
    companion object
    {
        const val WATCHLIST : Int = 0
        const val PORTFOLIO : Int = 1
        const val MARKET : Int = 2

    }
}