package tht.webgames.watchhelloworld.presentation

import androidx.lifecycle.LifecycleCoroutineScope
import com.google.gson.reflect.TypeToken
import tht.webgames.watchhelloworld.presentation.network.ForexRs
import tht.webgames.watchhelloworld.presentation.network.OpenApiClient

class MmbTestPresenter : MmbTestContract.Presenter {
    var view: MmbTestContract.View? = null

    companion object{
        val FOREX_PATH = listOf("v1", "DailyForeignExchangeRates")
    }

    override fun getForex(lifecycleCoroutineScope: LifecycleCoroutineScope) {
        OpenApiClient.init(lifecycleCoroutineScope)
        OpenApiClient.sendMsg<ForexRs>(FOREX_PATH, object : TypeToken<Collection<ForexRs>>() {}.type) {
            view?.onForexSuccess(it.last())
        }
    }

    override fun setViewImpl(view: MmbTestContract.View?) {
        this.view = view
    }
}