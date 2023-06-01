package tht.webgames.watchhelloworld.presentation.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import tht.webgames.watchhelloworld.presentation.MmbTestContract
import tht.webgames.watchhelloworld.presentation.MmbTestPresenter

val appModule = module{
//    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    factoryOf(::MmbTestPresenter) { bind<MmbTestContract.Presenter>() }
//    viewModelOf(::UserViewModel)
}