package tht.webgames.watchhelloworld.presentation.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface BaseRetrofitService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json;charset=UTF-8",
        "CONNECT_TIMEOUT:5000",
        "READ_TIMEOUT:5000",
        "WRITE_TIMEOUT:5000"
    )
    @GET("v1/DailyForeignExchangeRates")
    fun queryForex(): Call<Collection<ForexRs>>

}