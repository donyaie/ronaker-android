package com.ronaker.app.injection.module

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.ronaker.app.BuildConfig
import com.ronaker.app.General
import com.ronaker.app.base.PreferencesProvider
import com.ronaker.app.data.*
import com.ronaker.app.data.network.*
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.GOOGLE_URL
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Module which provides all required dependencies about Repository
 */
@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
object RepositoryModule {


    /**
     * Provides the User service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the User service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }


    /**
     * Provides the category service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the User service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }


    /**
     * Provides the order service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Order service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOrderApi(retrofit: Retrofit): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }


    /**
     * Provides the Content service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the User service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideContentApi(retrofit: Retrofit): ContentApi {
        return retrofit.create(ContentApi::class.java)
    }


    /**
     * Provides the Product service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Product service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideProductApi(retrofit: Retrofit): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideGoogleMapApi(): GoogleMapApi {
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build()


        var retrofit = Retrofit.Builder()
            .baseUrl(GOOGLE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()



        return retrofit.create(GoogleMapApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideUserRepository(
        userApi: UserApi,
        preferencesProvider: PreferencesProvider
    ): UserRepository {
        return UserRepository(userApi, preferencesProvider)
    }


    @Provides
    @Reusable
    @JvmStatic
    internal fun provideContentRepository(api: ContentApi): ContentRepository {

        return ContentRepository(api)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideProductRepository(
        productApi: ProductApi,
        preferencesProvider: PreferencesProvider
    ): ProductRepository {
        return ProductRepository(productApi, preferencesProvider)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideCategoryRepository(
        api: CategoryApi,
        preferencesProvider: PreferencesProvider
    ): CategoryRepository {
        return CategoryRepository(api, preferencesProvider)
    }


    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOrderRepository(
        api: OrderApi,
        preferencesProvider: PreferencesProvider
    ): OrderRepository {
        return OrderRepository(api, preferencesProvider)
    }


    @Provides
    @Reusable
    @JvmStatic
    internal fun provideGoogleMapRepository(api: GoogleMapApi): GoogleMapRepository {
        return GoogleMapRepository(api)
    }


    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {
        val JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"

        val GSON = GsonBuilder()
            .setDateFormat(JSON_DATE_FORMAT)
//            .serializeNulls()
            .create()


        val clientBuilder = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })

        if (BuildConfig.DEBUG)
            clientBuilder.addNetworkInterceptor(StethoInterceptor())

        val client = clientBuilder.build();


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }


    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun providePreferencesInterface(context: Context): PreferencesProvider {

        return PreferencesProvider(context)
    }


    /**
     * Provides the Context object.
     * @return the Context object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideContext(): Context {

        return General.context
    }


}
