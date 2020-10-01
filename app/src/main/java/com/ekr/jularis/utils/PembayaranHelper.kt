package com.ekr.jularis.utils

import com.ekr.jularis.data.cart.postcheckout.Data
import com.ekr.jularis.data.cart.postcheckout.DataPOST

class PembayaranHelper {
    companion object{
        fun addCart(checked: String, productId: String,qty: Int): DataPOST {
            var hasil = qty.plus(1)
            val data = Data(checked.toInt(),productId,hasil)
            val dataPOST = DataPOST(listOf(data))
            return dataPOST
        }

        fun minusCart(checked: String, productId: String,qty: Int): DataPOST {
            var hasil = qty.minus(1)
            val data = Data(checked.toInt(),productId,hasil)
            val dataPOST = DataPOST(listOf(data))
            return dataPOST
        }
        fun statisCart(checked: String, productId: String,qty: Int): DataPOST {
           var ceklis = checked
            if (ceklis =="1"){
                ceklis = "0"
            }else{
                ceklis="1"
            }
            val data = Data(ceklis.toInt(),productId,qty)
            val dataPOST = DataPOST(listOf(data))
            return dataPOST
        }
    }
}