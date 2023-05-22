package tht.webgames.watchhelloworld.presentation.network

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import java.io.Serializable
import kotlin.math.roundToLong


open class BaseData : Serializable {
    fun toJson(): JsonElement {
        return Gson().toJsonTree(this)
    }
}

data class ForexRs(
    var Date: String? = null,
    @SerializedName("USD/NTD")var USD_NTD: String,
    @SerializedName("RMB/NTD")var RMB_NTD: String,
    @SerializedName("EUR/USD")var EUR_USD: String,
    @SerializedName("USD/JPY")var USD_JPY: String,
    @SerializedName("GBP/USD")var GBO_USD: String,
    @SerializedName("AUD/USD")var AUD_USD: String,
) : BaseData() {
    fun getUSD(): Float = USD_NTD.toFloat()
    fun getRMB(): Float = RMB_NTD.toFloat()
    fun getEUR(): Float = USD_NTD.toFloat() * EUR_USD.toFloat()
    fun getJPY(): Float = USD_NTD.toFloat() / USD_JPY.toFloat()
    fun getAUD(): Float = USD_NTD.toFloat() * AUD_USD.toFloat()
}