package com.ronaker.app.injection.module

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.GsonBuilder
import com.ronaker.app.BuildConfig
import com.ronaker.app.General
import com.ronaker.app.base.ResourcesRepository
import com.ronaker.app.data.*
import com.ronaker.app.data.local.PreferencesProvider
import com.ronaker.app.data.network.*
import com.ronaker.app.utils.BASE_URL
import com.ronaker.app.utils.GOOGLE_URL
import com.ronaker.app.utils.LocaleHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    /**
     * Provides the User service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the User service implementation.
     */
    @Provides
    @Singleton
    internal fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }


    /**
     * Provides the Category service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the User service implementation.
     */
    @Provides
    @Singleton
    internal fun provideCategoryApi(retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }


    /**
     * Provides the order service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Order service implementation.
     */
    @Provides
    @Singleton
    internal fun provideOrderApi(retrofit: Retrofit): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }


    /**
     * Provides the Content service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the User service implementation.
     */
    @Provides
    @Singleton
    internal fun provideContentApi(retrofit: Retrofit): ContentApi {
        return retrofit.create(ContentApi::class.java)
    }


    /**
     * Provides the Product service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Product service implementation.
     */
    @Provides
    @Singleton
    internal fun provideProductApi(retrofit: Retrofit): ProductApi {
        return retrofit.create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    internal fun providePaymentInfoApi(retrofit: Retrofit): PaymentInfoApi {
        return retrofit.create(PaymentInfoApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideGoogleMapApi(): GoogleMapApi {
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .apply {
                if (BuildConfig.DEBUG) addNetworkInterceptor(StethoInterceptor())
            }

            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl(GOOGLE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()



        return retrofit.create(GoogleMapApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideUserRepository(
        userApi: UserApi,
        preferencesProvider: PreferencesProvider
    ): UserRepository {

        return DefaultUserRepository(userApi, preferencesProvider)
    }


    @Provides
    internal fun provideResourceRepository(
        @ApplicationContext context: Context
    ): ResourcesRepository {

        return ResourcesRepository(LocaleHelper.onAttach(context))
    }


    @Provides
    @Singleton
    internal fun provideAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics {

      return  (context.applicationContext as General).analytics

    }

    @Provides
    @Singleton
    internal fun providePaymentInfoRepository(
        api: PaymentInfoApi,
        userRepository: UserRepository
    ): PaymentInfoRepository {

        return DefaultPaymentInfoRepository(api, userRepository)
    }


    @Provides
    @Singleton
    internal fun provideContentRepository(
        api: ContentApi, @ApplicationContext context: Context,
        userRepository: UserRepository
    ): ContentRepository {

        return DefaultContentRepository(
            api, context,
            userRepository
        )
    }

    @Provides
    @Singleton
    internal fun provideProductRepository(
        productApi: ProductApi,
        userRepository: UserRepository
    ): ProductRepository {
        return DefaultProductRepository(
            productApi,
            userRepository
        )
    }

    @Provides
    @Singleton
    internal fun provideCategoryRepository(
        api: CategoryApi,
        preferencesProvider: PreferencesProvider,
        userRepository: UserRepository
    ): CategoryRepository {
        return DefaultCategoryRepository(
            api,
            preferencesProvider,
            userRepository
        )
    }


    @Provides
    @Singleton
    internal fun provideOrderRepository(
        api: OrderApi,
        userRepository: UserRepository
    ): OrderRepository {
        return DefaultOrderRepository(
            api,
            userRepository
        )
    }


    @Provides
    @Singleton
    internal fun provideGoogleMapRepository(
        api: GoogleMapApi, resourcesRepository: ResourcesRepository
    ): GoogleMapRepository {
        return DefaultGoogleMapRepository(api, resourcesRepository)
    }


    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit {
        val jsonFormat = "yyyy-MM-dd'T'HH:mm:ssZ"

        val gson = GsonBuilder()
            .setDateFormat(jsonFormat)
//            .serializeNulls()
            .create()

        val clientBuilder = OkHttpClient.Builder()
//        val clientBuilder = SslUtils.getUnsafeOkHttpClient(context)
        clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        if (BuildConfig.DEBUG)
            clientBuilder.addNetworkInterceptor(StethoInterceptor())


        val client = clientBuilder.build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }


    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    @Singleton
    internal fun providePreferencesInterface(@ApplicationContext context: Context): PreferencesProvider {

        return PreferencesProvider(context)
    }


}