package com.ekr.jularis.ui.profile

import com.ekr.jularis.data.profile.DataGet
import com.ekr.jularis.utils.SessionManager
import java.io.File

interface ProfileContract {
    interface Presenter {
        fun getProfile(token:String)
        fun doUpdateProfile(
            token: String,
            full_name: String,
            no_hp: String,
            alamat: String
        )
        fun doUploadFoto(token: String, file: File)
    }

    interface View {
        fun initListener()
        fun resultUpdate(status:Boolean)
        fun resultUpload(status:Boolean)
        fun resultGetProfile(dataGet: DataGet)
        fun showMessage(message:String)
        fun onLoading(loading:Boolean)
    }
}