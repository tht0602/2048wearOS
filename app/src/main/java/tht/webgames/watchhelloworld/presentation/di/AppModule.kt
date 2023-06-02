package tht.webgames.watchhelloworld.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import tht.webgames.watchhelloworld.presentation.MmbTestViewModel

val appModule = module {
//    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
//    factoryOf(::MmbTestPresenter) { bind<MmbTestContract.Presenter>() }
    viewModelOf(::MmbTestViewModel)
}