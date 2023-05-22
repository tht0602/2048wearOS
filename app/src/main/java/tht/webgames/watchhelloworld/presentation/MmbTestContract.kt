package tht.webgames.watchhelloworld.presentation

import androidx.lifecycle.LifecycleCoroutineScope
import tht.webgames.watchhelloworld.presentation.network.ForexRs

interface MmbTestContract {

    interface View {
        fun onForexSuccess(response: ForexRs)
    }

    interface Presenter {
        fun getForex(lifecycleCoroutineScope: LifecycleCoroutineScope)
        fun setViewImpl(view: View?)
    }
}