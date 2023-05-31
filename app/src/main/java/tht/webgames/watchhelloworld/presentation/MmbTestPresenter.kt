package tht.webgames.watchhelloworld.presentation

import androidx.lifecycle.LifecycleCoroutineScope
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tht.webgames.watchhelloworld.presentation.network.ForexRs
import tht.webgames.watchhelloworld.presentation.network.RetrofitManager

class MmbTestPresenter : MmbTestContract.Presenter {

    var view: MmbTestContract.View? = null

    override fun getForex(lifecycleCoroutineScope: LifecycleCoroutineScope) {

        val call: Call<Collection<ForexRs>> = RetrofitManager.service.queryForex()

        call.enqueue(object : Callback<Collection<ForexRs>> {
            override fun onResponse(
                call: Call<Collection<ForexRs>>,
                response: Response<Collection<ForexRs>>
            ) {
                // 連線成功
                response.body()?.last()?.let { view?.onForexSuccess(it) }
                Logger.d("連線成功")
            }

            override fun onFailure(call: Call<Collection<ForexRs>>, t: Throwable) {
                // 連線失敗
                Logger.d(t.printStackTrace())
            }
        })
    }

    override fun setViewImpl(view: MmbTestContract.View?) {
        this.view = view
    }
}