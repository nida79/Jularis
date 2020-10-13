package com.ekr.jularis.ui.admin.product.mange

import java.io.File

interface ProductManageContract {
    interface Presenter {
        fun doUpdateProduct(
            token: String,
            product_id: String,
            name: String,
            price: String,
            description: String,
            category: String,
            quantity: String,
            ongkir:String,
            photo_product: List<File>?
        )

        fun doDeleteProduct(token: String, product_id: String)

        fun doAddProduct(
            token: String,
            name: String,
            price: String,
            description: String,
            category: String,
            quantity: String,
            ongkir:String,
            photo_product: List<File>
        )
    }

    interface View {
        fun initListener()
        fun resultUpdateProduct(hasil: Boolean)
        fun onLoading(loading: Boolean)
        fun showMessage(message: String)
    }
}