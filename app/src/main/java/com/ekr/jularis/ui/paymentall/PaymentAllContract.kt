package com.ekr.jularis.ui.paymentall

import com.ekr.jularis.data.payment.*
import com.ekr.jularis.data.response.ResponseGetDataPayment
import java.io.File

interface PaymentAllContract{
    interface Presenter{
        fun getDataPayment(token:String)
        fun changeAddressPayment(token:String,alamat:DataAlamat)
        fun uploadFoto(token: String,photo: File)
        fun postDataPayment(token: String,datapostPayment: DatapostPayment2)
    }
    interface View{
        fun initListener()
        fun radioSelected()
        fun onLoading(loading : Boolean)
        fun loadingFoto(loadingFoto:Boolean)
        fun loadingChangeAddress(loadingCA:Boolean)
        fun resultChangeAddress(hasil:Boolean)
        fun onResultDataPayment(responseGetDataPayment: ResponseGetDataPayment,dataPayment:List<DataGetPayment>)
        fun onResultUploadPhoto(photo_payment:String)
        fun resultPayment(sukses:Boolean)
        fun showMessage(message:String)
    }
}