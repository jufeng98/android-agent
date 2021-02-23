package org.javamaster.agent.common

import android.app.Application
import android.content.Context

/**
 * @author yudong
 * @date 2019/8/18
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        lateinit var context: Context
    }

}