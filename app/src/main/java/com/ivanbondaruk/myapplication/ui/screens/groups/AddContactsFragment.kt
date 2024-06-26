package com.ivanbondaruk.myapplication.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import com.ivanbondaruk.myapplication.models.CommonModel
import com.ivanbondaruk.myapplication.ui.screens.base.BaseFragment
import com.ivanbondaruk.myapplication.R
import com.ivanbondaruk.myapplication.database.CURRENT_UID
import com.ivanbondaruk.myapplication.database.NODE_MESSAGES
import com.ivanbondaruk.myapplication.database.NODE_PHONES_CONTACTS
import com.ivanbondaruk.myapplication.database.NODE_USERS
import com.ivanbondaruk.myapplication.database.REF_DATABASE_ROOT
import com.ivanbondaruk.myapplication.database.getCommonModel
import com.ivanbondaruk.myapplication.utilits.APP_ACTIVITY
import com.ivanbondaruk.myapplication.utilits.AppValueEventListener
import com.ivanbondaruk.myapplication.utilits.hideKeyboard
import com.ivanbondaruk.myapplication.utilits.replaceFragment
import com.ivanbondaruk.myapplication.utilits.showToast
import kotlinx.android.synthetic.main.fragment_add_contacts.*

/* Главный фрагмент, содержит все чаты, группы и каналы с которыми взаимодействует пользователь*/

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefContactsList = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = "Добавить участника"
        hideKeyboard()
        initRecyclerView()
        add_contacts_btn_next.setOnClickListener {
            if (listContacts.isEmpty()) showToast("Добавьте участника")
            else replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = add_contacts_recycle_view
        mAdapter = AddContactsAdapter()

        // 1 запрос
        mRefContactsList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->
                // 2 запрос
                mRefUsers.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                        val newModel = dataSnapshot1.getCommonModel()

                        // 3 запрос
                        mRefMessages.child(model.id).limitToLast(1)
                            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                                val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                                if (tempList.isEmpty()){
                                    newModel.lastMessage = "Чат очищен"
                                } else {
                                    newModel.lastMessage = tempList[0].text
                                }


                                if (newModel.fullname.isEmpty()) {
                                    newModel.fullname = newModel.phone
                                }
                                mAdapter.updateListItems(newModel)
                            })
                    })
            }
        })

        mRecyclerView.adapter = mAdapter
    }

    companion object{
        val listContacts = mutableListOf<CommonModel>()
    }
}
