package tht.webgames.watchhelloworld.presentation.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    val service: BaseRetrofitService

    private const val WEB_SITE = "https://openapi.taifex.com.tw"

    init {

        // 設置baseUrl即要連的網站，addConverterFactory用Gson作為資料處理Converter
        val retrofit = Retrofit.Builder()
            .baseUrl(WEB_SITE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(BaseRetrofitService::class.java)
    }

}