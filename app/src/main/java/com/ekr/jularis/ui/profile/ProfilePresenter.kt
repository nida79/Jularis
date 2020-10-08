package com.ekr.jularis.ui.profile

import com.ekr.jularis.data.profile.DataGet
import com.ekr.jularis.data.response.*
import com.ekr.jularis.networking.ApiService
import com.ekr.jularis.utils.SessionManager
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfilePresenter(val view: ProfileContract.View) : ProfileContract.Presenter {
    init {
        view.initListener()
        view.onLoading(false)
    }

    override fun getProfile(token: String) {
        view.onLoading(true)
        ApiService.endpoint.getProfile(token).enqueue(object : Callback<ResponseGetProfile> {
            override fun onResponse(
                call: Call<ResponseGetProfile>,
                response: Response<ResponseGetProfile>
            ) {
                view.onLoading(false)
                when {
                    response.isSuccessful -> {
                        val dataGet: DataGet = response.body()!!.data
                        view.resultGetProfile(dataGet)
                    }
                    response.code() != 200 -> {
                        val responseGlobal: ResponseGlobal = Gson().fromJson(
                            response.errorBody()!!.charStream(),
                            ResponseGlobal::class.java
                        )
                        view.showMessage(responseGlobal.message)
                    }
                }

            }

            override fun onFailure(call: Call<ResponseGetProfile>, t: Throwable) {
                view.onLoading(false)
                view.showMessage(t.stackTraceToString())
            }
        })
    }

    override fun doUpdateProfile(
        token: String,
        full_name: String,
        no_hp: String,
        alamat: String
    ) {

        ApiService.endpoint.doUpdateProfile(token, full_name, no_hp, alamat)
            .enqueue(object : Callback<ResponseUpdateProfile> {
                override fun onResponse(
                    call: Call<ResponseUpdateProfile>,
                    response: Response<ResponseUpdateProfile>
                ) {
                    when {
                        response.isSuccessful -> {
                            val result: ResponseUpdateProfile? = response.body()
                            view.resultUpdate(result!!.status)
                            view.showMessage(result.message)
                        }
                        response.code() != 200 -> {
                            val responseGlobal: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(responseGlobal.message)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseUpdateProfile>, t: Throwable) {
                    view.showMessage(t.stackTraceToString())
                }
            })
    }

    override fun doUploadFoto(token: String, file: File) {
        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val multipartBody: MultipartBody.Part? = MultipartBody.Part.createFormData(
            "photo",
            file.name, requestBody
        )

        ApiService.endpoint.doChangePhotoProfile(token, multipartBody!!)
            .enqueue(object : Callback<ResponseGlobal> {
                override fun onResponse(
                    call: Call<ResponseGlobal>,
                    response: Response<ResponseGlobal>
                ) {

                    when {
                        response.isSuccessful -> {
                            val result: String = response.body()!!.message
                            view.showMessage(result)
                            view.resultUpload(response.body()!!.status)
                        }
                        response.code() != 200 -> {
                            val result: ResponseGlobal = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ResponseGlobal::class.java
                            )
                            view.showMessage(result.message)

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseGlobal>, t: Throwable) {
                    view.showMessage(t.message.toString())

                }
            })
    }


}