package com.ivanbondaruk.myapplication.ui.screens.register

import androidx.fragment.app.Fragment
import com.google.firebase.auth.PhoneAuthProvider
import com.ivanbondaruk.myapplication.R
import com.ivanbondaruk.myapplication.database.AUTH
import com.ivanbondaruk.myapplication.database.CHILD_ID
import com.ivanbondaruk.myapplication.database.CHILD_PHONE
import com.ivanbondaruk.myapplication.database.CHILD_USERNAME
import com.ivanbondaruk.myapplication.database.NODE_PHONES
import com.ivanbondaruk.myapplication.database.NODE_USERS
import com.ivanbondaruk.myapplication.database.REF_DATABASE_ROOT
import com.ivanbondaruk.myapplication.utilits.APP_ACTIVITY
import com.ivanbondaruk.myapplication.utilits.AppTextWatcher
import com.ivanbondaruk.myapplication.utilits.AppValueEventListener
import com.ivanbondaruk.myapplication.utilits.restartActivity
import com.ivanbondaruk.myapplication.utilits.showToast
import kotlinx.android.synthetic.main.fragment_enter_code.*

/* Фрагмент для ввода кода подтверждения при регистрации */

class EnterCodeFragment(val phoneNumber: String, val id: String) :
    Fragment(R.layout.fragment_enter_code) {


    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        /* Функция проверяет код, если все нормально, производит создания информации о пользователе в базе данных.*/
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber


                REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
                    .addListenerForSingleValueEvent(AppValueEventListener{

                        if (!it.hasChild(CHILD_USERNAME)){
                            dateMap[CHILD_USERNAME] = uid
                        }

                        REF_DATABASE_ROOT.child(
                            NODE_PHONES
                        ).child(phoneNumber).setValue(uid)
                            .addOnFailureListener { showToast(it.message.toString()) }
                            .addOnSuccessListener {
                                REF_DATABASE_ROOT.child(
                                    NODE_USERS
                                ).child(uid).updateChildren(dateMap)
                                    .addOnSuccessListener {
                                        showToast("Добро пожаловать")
                                        restartActivity()
                                    }
                                    .addOnFailureListener { showToast(it.message.toString()) }
                            }
                    })



            } else showToast(task.exception?.message.toString())
        }
    }
}
