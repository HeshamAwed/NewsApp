package com.hesham.newsapp.di

import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hesham.newsapp.BuildConfig
import com.hesham.newsapp.application.NewsApplication.Companion.context
import com.hesham.newsapp.domain.datasources.ArticleDataSource
import com.hesham.newsapp.domain.datasources.SearchArticleDataSource
import com.hesham.newsapp.domain.gateway.local.db.AppDatabase
import com.hesham.newsapp.domain.gateway.remote.AppGateway
import com.hesham.newsapp.domain.gateway.remote.HeaderInterceptor
import com.hesham.newsapp.domain.repositories.ArticleRepository
import com.hesham.newsapp.domain.repositories.ArticleRepositoryImpl
import com.hesham.newsapp.ui.articles.details.DetailsViewModel
import com.hesham.newsapp.ui.articles.favorites.FavoritesViewModel
import com.hesham.newsapp.ui.articles.home.HomeViewModel
import com.hesham.newsapp.ui.articles.search.SearchViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import org.koin.androidx.viewmodel.dsl.viewModel

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
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_API_URL)
            .client(get(named(DependencyInjection.APP_CLIENT)))
            .build()
            .create(AppGateway::class.java)
    }
}

val repositoriesModule = module {

    single<ArticleRepository> {
        ArticleRepositoryImpl(
            get(named(DependencyInjection.APP_RETROFIT)),
            get()
        )
    }


}

val dataSources = module {

    single {
        ArticleDataSource(
            get(named(DependencyInjection.APP_RETROFIT))
        )
    }
    single {
        SearchArticleDataSource(
            get(named(DependencyInjection.APP_RETROFIT)),
            ""
        )
    }

}

val useCasesModule = module {
//    single { InitConfigUseCase(get()) }

}

val viewModelsModule = module{
    viewModel { HomeViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { SearchViewModel(get(),get()) }
    viewModel { DetailsViewModel() }

}

val databaseModule = module {
    single {
        Room.databaseBuilder(context, AppDatabase::class.java, "articles_database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    single { get<AppDatabase>().articleDao() }
}

val applicationModules = listOf(
    applicationModule,
    databaseModule,
    appGatewayModule,
    repositoriesModule,
    dataSources,
    useCasesModule,
    viewModelsModule
)
