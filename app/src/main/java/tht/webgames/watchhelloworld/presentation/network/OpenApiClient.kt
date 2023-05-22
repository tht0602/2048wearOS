package tht.webgames.watchhelloworld.presentation.network

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.lang.reflect.Type


object OpenApiClient {

    private var client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    private var lifecycleScope: CoroutineScope? = null
    private const val WEB_SITE = "openapi.taifex.com.tw"

    fun init(lifecycleScope: CoroutineScope) {
        OpenApiClient.lifecycleScope = lifecycleScope
    }

    fun <T1 : BaseData> sendMsg(pathList: List<String>, type: Type, query: BaseData? = null, successCallback: ((response: Collection<T1>) -> Unit)? = null) {
        lifecycleScope?.launch {
            val entries: Set<Map.Entry<String, JsonElement>> = query?.toJson()?.asJsonObject?.entrySet() ?: setOf()

            val httpUrl = HttpUrl.Builder()
                .scheme("https")
                .host(WEB_SITE)
                .apply {
                    pathList.forEach {
                        addPathSegment(it)
                    }
                    entries.forEach {
                        this.addQueryParameter(it.key, it.value.asString)
                    }
                }
                .build()

            val request = Request.Builder()
                .url(httpUrl)
                .header("accept", "application/json")
                .build()

            val call = client.newCall(request)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Logger.d(e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    lifecycleScope?.launch(Dispatchers.Main) {
                        response.body?.string()?.let { // string() 是 data stream 只能用一次
                            Logger.d(it)
                            successCallback?.invoke(Gson().fromJson(it, type))
                        }
                        response.close()
                    }
                }
            })
        }
    }
}