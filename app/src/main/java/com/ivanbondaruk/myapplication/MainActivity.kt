package com.ivanbondaruk.myapplication

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.ivanbondaruk.myapplication.database.AUTH
import com.ivanbondaruk.myapplication.database.initFirebase
import com.ivanbondaruk.myapplication.database.initUser
import com.ivanbondaruk.myapplication.databinding.ActivityMainBinding
import com.ivanbondaruk.myapplication.ui.objects.AppDrawer
import com.ivanbondaruk.myapplication.ui.screens.main_list.MainListFragment
import com.ivanbondaruk.myapplication.ui.screens.register.EnterPhoneNumberFragment
import com.ivanbondaruk.myapplication.utilits.APP_ACTIVITY
import com.ivanbondaruk.myapplication.utilits.AppStates
import com.ivanbondaruk.myapplication.utilits.READ_CONTACTS
import com.ivanbondaruk.myapplication.utilits.initContacts
import com.ivanbondaruk.myapplication.utilits.replaceFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    lateinit var mToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        /* Функция запускается один раз, при создании активити */
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunc()
        }

    }



    private fun initFunc() {
        /* Функция инициализирует функциональность приложения */
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser != null) {
            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(),false)
        }
    }

    private fun initFields() {
        /* Функция инициализирует переменные */
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }
}
