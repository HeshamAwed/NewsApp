package com.hesham.newsapp.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hesham.newsapp.BuildConfig
import com.hesham.newsapp.domain.gateway.local.db.AppDatabase
import com.hesham.newsapp.domain.gateway.remote.AppGateway
import com.hesham.newsapp.domain.gateway.remote.HeaderInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

enum class DependencyInjection {
    APP_CLIENT,
    APP_RETROFIT,
    APP_INTERCEPTOR
}

val applicationModule = module {
    single<Gson> {
        GsonBuilder()
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .create()
    }
}

val appGatewayModule = module {

    single(named(DependencyInjection.APP_INTERCEPTOR)) { HeaderInterceptor() }
    single(named(DependencyInjection.APP_CLIENT)) {
        OkHttpClient.Builder()
            .addInterceptor(get<HeaderInterceptor>(named(DependencyInjection.APP_INTERCEPTOR)))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(15, TimeUnit.MINUTES)
            .writeTimeout(15, TimeUnit.MINUTES)
            .readTimeout(15, TimeUnit.MINUTES)
            .callTimeout(15, TimeUnit.MINUTES)
            .build()
    }

    single<AppGateway>(named(DependencyInjection.APP_RETROFIT)) {
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_API_URL)
            .client(get(named(DependencyInjection.APP_CLIENT)))
            .build()
            .create(AppGateway::class.java)
    }
}

val repositoriesModule = module {

//    single<StoresRepository> {
//        StoresRepositoryImplementation(
//            get()
//        )
//    }


}

val dataSources = module {

//    single<CategoriesDataSource> {
//        CategoriesDataSourceImplementation(
//            get(),
//            get(named(ON_MARKET_RETROFIT))
//        )
//    }

}

val useCasesModule = module {
//    single { InitConfigUseCase(get()) }

}

val viewModelsModule = module{

}

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "articles_database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    single { get<AppDatabase>().articleDao() }
}

val applicationModules = listOf(
    applicationModule,
    appGatewayModule,
    repositoriesModule,
    dataSources,
    useCasesModule,
    databaseModule,
    viewModelsModule
)
