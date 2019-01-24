package com.shang.fcu_food.Unit

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.shang.fcu_food.Data.TempMenu
import com.shang.fcu_food.Data.TempShop
import com.shang.fcu_food.Data.User
import com.shang.fcu_food.Data.UserComment
import com.shang.fcu_food.DataBind
import com.shang.fcu_food.FirebaseCallback
import com.shang.fcu_food.Main.GlideApp
import com.shang.fcu_food.R
import org.jetbrains.anko.toast

class FirebaseUnits {


    companion object {
        val AUTH_RQEUESTCODE = 100

        fun checkHasAuth(): Boolean {  //檢查是否已經登入
            return FirebaseAuth.getInstance().currentUser != null
        }

        fun auth_Login(activity: Activity) {   //註冊與登入
            val providers =
                arrayListOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.EmailBuilder().build())

            activity.startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), AUTH_RQEUESTCODE
            )
        }

        //取得user
        fun auth_getUser(): FirebaseUser? {
            return FirebaseAuth.getInstance().currentUser
        }

        fun auth_uidToUser():User?{
            return DataBind.allUser.get(FirebaseUnits.auth_getUser()?.uid)
        }

        //綁定使用者資料 即時更新
        fun database_BindAllUser() {
            val userRef = FirebaseDatabase.getInstance()
                .getReference().child("user").addValueEventListener(
                    object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.d("database_BindAllUser", p0.toString())
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var userList = mutableMapOf<String, User>()
                            for (data in snapshot.children) {
                                var user = data.getValue(User::class.java)
                                user?.uid = data.key!!
                                userList.put(user!!.uid, user)
                            }
                            DataBind.allUser = userList
                        }
                    }
                )
        }


        fun storage_loadImg(
            context: Context,
            img: ImageView,
            tag: String,
            shop_name: String,
            name: String,
            option: RequestOptions
        ) {
            //option.transform(RoundedCornersTransformation(20,0))
            var ref = FirebaseStorage.getInstance()
                .getReference(tag).child(shop_name).child("$name.jpg")
            ref.downloadUrl.addOnSuccessListener {
                GlideApp.with(context).load(it).apply(option).into(img)
            }.addOnFailureListener {
            }
        }

        //database更新評論
        //database.getReference("breakfast") 一開始就是fucfood了
        //上傳資料用update 就算沒有那個節點他也會自己幫你生出來 ex:breakfast/3
        //setValue會把你整個節點都改掉
        //push他會自己生出一個hashID這個不是我要用的
        fun database_addCommemt(
            ref_path: String,
            rating: String,
            comment: String,
            uid: String,
            activity: FragmentActivity
        ) {
            val ref = FirebaseDatabase.getInstance().getReference().child(ref_path)
            var map = UserComment(uid, comment, rating).toMap()
            ref.updateChildren(map).addOnSuccessListener {
                activity.toast(R.string.CommentDialog_Success)
            }.addOnFailureListener {
                activity.toast(R.string.CommentDialog_Fail)
            }
        }

        fun addTempData(ref_path: String, value: Any, imageByte: ByteArray, callback: FirebaseCallback) {
            var fileName:String=if(ref_path.equals("tempMenu"))
                (value as TempMenu).menuname else (value as TempShop).shopname

            var database = FirebaseDatabase.getInstance().getReference().child(ref_path)
            var storage = FirebaseStorage
                .getInstance().getReference(ref_path).child(fileName+fileName.hashCode()+".jpeg")
            var database_status = false
            var storage_status = false

            database.push().setValue(value).addOnSuccessListener {
                database_status = true
                callback.statusCallBack(database_status, storage_status)
            }.addOnFailureListener {
                database_status = false
                callback.statusCallBack(database_status, storage_status)
            }

            if (imageByte.size != 0) {  //代表有上傳照片
                var metadata=StorageMetadata.Builder().setContentDisposition("TEST").build()
                storage.putBytes(imageByte,metadata).addOnSuccessListener {
                    storage_status = true
                    callback.statusCallBack(database_status, storage_status)
                }.addOnFailureListener {
                    storage_status = false
                    callback.statusCallBack(database_status, storage_status)
                }
            }else{
                storage_status=true
                callback.statusCallBack(database_status, storage_status)
            }
        }

        //新增和修改使用者資料
        fun database_updateUser(user:User){
            var ref_user=FirebaseDatabase.getInstance().getReference("user").child(user.uid)
            ref_user.updateChildren(user.toMap()).addOnSuccessListener {

            }.addOnFailureListener {

            }
        }

    }
}