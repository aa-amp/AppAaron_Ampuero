// remote/RetrofitInstance.kt
package remote

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    @Volatile
    private var baseUrl: String = "http://10.0.2.2:3001/"

    @Volatile
    private var bearerToken: String? = null

    fun setBaseUrl(url: String) {
        baseUrl = if (url.endsWith("/")) url else "$url/"
    }

    fun setToken(token: String?) {
        bearerToken = token
    }

    private val authInterceptor = Interceptor { chain ->
        val reqBuilder = chain.request().newBuilder()
        bearerToken?.let { reqBuilder.addHeader("Authorization", "Bearer $it") }
        chain.proceed(reqBuilder.build())
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun buildClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    private fun buildRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(buildClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: ApiService
        get() = buildRetrofit().create(ApiService::class.java)
}