package com.ivanbondaruk.myapplication.ui.screens.settings

import com.ivanbondaruk.myapplication.ui.screens.base.BaseChangeFragment
import com.example.telegram.utilits.*
import com.ivanbondaruk.myapplication.R
import com.ivanbondaruk.myapplication.database.CURRENT_UID
import com.ivanbondaruk.myapplication.database.NODE_USERNAMES
import com.ivanbondaruk.myapplication.database.REF_DATABASE_ROOT
import com.ivanbondaruk.myapplication.database.USER
import com.ivanbondaruk.myapplication.database.updateCurrentUsername
import com.ivanbondaruk.myapplication.utilits.AppValueEventListener
import com.ivanbondaruk.myapplication.utilits.showToast
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

/* Фрагмент для изменения username пользователя */

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)
    }

    override fun change() {
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isEmpty()){
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(
                NODE_USERNAMES
            ).addListenerForSingleValueEvent(AppValueEventListener{
                    if (it.hasChild(mNewUsername)){
                        showToast("Такой пользователь уже существует")
                    } else{
                        changeUsername()
                    }
                })

        }
    }

    private fun changeUsername() {
        /* Изменение username в базе данных */
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mNewUsername).setValue(
            CURRENT_UID
        )
            .addOnCompleteListener {
                if (it.isSuccessful){
                    updateCurrentUsername(mNewUsername)
                }
            }
    }




}
