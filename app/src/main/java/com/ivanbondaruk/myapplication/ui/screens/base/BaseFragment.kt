package com.ivanbondaruk.myapplication.ui.screens.base

import androidx.fragment.app.Fragment
import com.ivanbondaruk.myapplication.utilits.APP_ACTIVITY

/* Базовый фрагмент, от него наследуются все фрагменты приложения, кроме главного */

open class BaseFragment( layout:Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }
}
