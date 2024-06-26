package com.ivanbondaruk.myapplication.ui.screens.settings

import com.ivanbondaruk.myapplication.R
import com.ivanbondaruk.myapplication.database.USER
import com.ivanbondaruk.myapplication.ui.screens.base.BaseChangeFragment
import com.ivanbondaruk.myapplication.database.setBioToDatabase
import kotlinx.android.synthetic.main.fragment_cnage_bio.*

/* Фрагмент для изменения информации о пользователе */

class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_cnage_bio) {

    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = settings_input_bio.text.toString()
        setBioToDatabase(newBio)
    }
}
