package com.ivanbondaruk.myapplication.ui.screens.groups

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.ivanbondaruk.myapplication.models.CommonModel
import com.ivanbondaruk.myapplication.ui.screens.base.BaseFragment
import com.ivanbondaruk.myapplication.ui.screens.main_list.MainListFragment
import com.ivanbondaruk.myapplication.R
import com.ivanbondaruk.myapplication.database.createGroupToDatabase
import com.ivanbondaruk.myapplication.utilits.APP_ACTIVITY
import com.ivanbondaruk.myapplication.utilits.getPlurals
import com.ivanbondaruk.myapplication.utilits.hideKeyboard
import com.ivanbondaruk.myapplication.utilits.replaceFragment
import com.ivanbondaruk.myapplication.utilits.showToast
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.fragment_settings.*

class CreateGroupFragment(private var listContacts:List<CommonModel>)
    : BaseFragment(R.layout.fragment_create_group) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private var mUri = Uri.EMPTY

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initRecyclerView()
        create_group_photo.setOnClickListener { addPhoto()  }
        create_group_btn_complete.setOnClickListener {
            val nameGroup = create_group_input_name.text.toString()
            if (nameGroup.isEmpty()){
                showToast("Введите имя")
            } else {
                createGroupToDatabase(nameGroup,mUri,listContacts){
                    replaceFragment(MainListFragment())
                }
            }
        }
        create_group_input_name.requestFocus()
        create_group_counts.text = getPlurals(listContacts.size)
    }


    private fun addPhoto() {

    }

    private fun initRecyclerView() {
        mRecyclerView = create_group_recycle_view
        mAdapter = AddContactsAdapter()
        mRecyclerView.adapter = mAdapter
        listContacts.forEach {  mAdapter.updateListItems(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        /* Активность которая запускается для получения картинки для фото пользователя */
        super.onActivityResult(requestCode, resultCode, data)
    }
}