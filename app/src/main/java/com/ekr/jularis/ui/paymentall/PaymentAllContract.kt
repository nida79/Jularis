package com.ekr.jularis.ui.paymentall

import com.ekr.jularis.data.payment.DataGetPayment
import com.ekr.jularis.data.payment.DataPayment
import com.ekr.jularis.data.payment.DatapostPayment
import com.ekr.jularis.data.payment.DatapostPayment2
import com.ekr.jularis.data.response.ResponseGetDataPayment
import java.io.File

interface PaymentAllContract{
    interface Presenter{
        fun getDataPayment(token:String)
        fun uploadFoto(token: String,photo: File)
        fun postDataPayment(token: String,datapostPayment: DatapostPayment2)
    }
    interface View{
        fun initListener()
        fun radioSelected()
        fun onLoading(loading : Boolean)
        fun loadingFoto(loadingFoto:Boolean)
        fun onResultDataPayment(responseGetDataPayment: ResponseGetDataPayment,dataPayment:List<DataGetPayment>)
        fun onResultUploadPhoto(photo_payment:String)
        fun resultPayment(sukses:Boolean)
        fun showMessage(message:String)
    }
}