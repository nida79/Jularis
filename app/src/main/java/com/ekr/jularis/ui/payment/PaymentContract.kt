package com.ekr.jularis.ui.payment

import com.ekr.jularis.data.payment.DatapostPayment
import com.ekr.jularis.data.response.ResponseGetDataPayment
import java.io.File

interface PaymentContract {
    interface Presenter {
        fun getDataPayment(token:String,product_id:String)
        fun uploadFoto(token: String,photo: File)
        fun postDataPayment(datapostPayment: DatapostPayment)
    }

    interface View {
        fun initListener()
        fun radioSelected()
        fun onLoading(loading : Boolean)
        fun loadingFoto(loadingFoto:Boolean)
        fun onResultDataPayment(responseGetDataPayment: ResponseGetDataPayment)
        fun onResultUploadPhoto(photo_payment:String)
        fun showMessage(message:String)

    }
}