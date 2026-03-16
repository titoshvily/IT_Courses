package com.titoshvily.it_courses

import android.app.Application
import com.titoshvily.it_courses.feature.account.accountModule
import com.titoshvily.it_courses.feature.courses.di.coursesModule
import com.titoshvily.it_courses.feature.login.di.loginModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(loginModule, coursesModule, accountModule)
        }
    }
}
