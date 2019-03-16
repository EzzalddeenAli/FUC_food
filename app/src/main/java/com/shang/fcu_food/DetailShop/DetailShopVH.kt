package com.shang.fcu_food.DetailShop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.shang.fcu_food.Data.DataConstant
import com.shang.fcu_food.Data.shop.Shop
import com.shang.fcu_food.DetailMenu.DetailMenuActivity
import com.shang.fcu_food.DetailPlace
import com.shang.fcu_food.DetailPlaceAdapter
import com.shang.fcu_food.Dialog.AddMenuDialog
import com.shang.fcu_food.Dialog.ImageViewDialog
import com.shang.fcu_food.GooglePlace
import com.shang.fcu_food.Maps.MapsActivity
import com.shang.fcu_food.R
import com.shang.fcu_food.Unit.FileStorageUnit
import kotlinx.android.synthetic.main.cardview_detailshop.view.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

class DetailShopVH(itemView: View) : RecyclerView.ViewHolder(itemView), GooglePlace.ChangeGooglePlace {

    var shopNameTv = itemView.findViewById<TextView>(R.id.shopNameTv)
    var shopPhoneTv = itemView.findViewById<TextView>(R.id.shopPhoneTv)
    var shopOpenTv = itemView.findViewById<TextView>(R.id.shopOpenTv)
    var shopStarTv = itemView.findViewById<TextView>(R.id.shopStarTv)
    var shopMapIg = itemView.findViewById<ImageView>(R.id.shopMapIg)
    var shopMenuRecyc = itemView.findViewById<RecyclerView>(R.id.shopMenuRecyc)
    var shopPictureImg = itemView.findViewById<ImageView>(R.id.shopPictureImg)
    var shopEditImg = itemView.findViewById<ImageView>(R.id.shopEditImg)
    var shopAddMenuImg = itemView.findViewById<ImageView>(R.id.shopAddMenuImg)
    var shopMenuImg = itemView.findViewById<ImageView>(R.id.shopMenuImg)

    fun bind(model: Shop, position: Int, activity: DetailShopActivity) {

        itemView.shopNameTv.text = model.name
        itemView.shopOpenTv.text = model.time
        itemView.shopStarTv.text = String.format("%.1f", model.star)
        itemView.shopPhoneTv.text = model.phone

        //Google Map
        itemView.shopMapIg.setOnClickListener {
            activity.startActivity(Intent(activity, MapsActivity::class.java).apply {
                this.action = activity.javaClass.simpleName
                this.putExtra(DataConstant.SHOP_TYPE_TAG, model.shop_tag)
                this.putExtra(DataConstant.POSITION, position)
            })
        }

        //店家圖片
        itemView.shopPictureImg.setOnClickListener {
            ImageViewDialog.getInstance(model.shop_tag, model.name, model.name)
                .show(activity.supportFragmentManager, ImageViewDialog.TAG)
        }

        //店家圖片讀取
        FileStorageUnit.ImageLoader(
            itemView.context,
            itemView.shopPictureImg,
            model.getFile(itemView.context),
            model.getStorageRef(),
            model.errorDrawable,
            RequestOptions()
        )

        //下面的Menu
        itemView.shopMenuRecyc.layoutManager = GridLayoutManager(itemView.context, 2)
        itemView.shopMenuRecyc.adapter =
            SimpleMenuAdapter(
                model.menu,
                getItemClick(model.shop_tag, model.id, model.name, itemView.context)
            )

        //修改店家
        itemView.shopEditImg.setOnClickListener {
            //EditShopDialog.getInstance(model).show(activity.supportFragmentManager, EditShopDialog.TAG)
            var googlePlace = GooglePlace.getInstance(this)
            googlePlace.getGooglePlaceData(position.toString())
        }

        //新增菜單
        itemView.shopAddMenuImg.setOnClickListener {
            AddMenuDialog.getInstance(model.name).show(activity.supportFragmentManager, AddMenuDialog.TAG)
        }

        itemView.shopMenuImg.setOnClickListener {
            ImageViewDialog.getInstance(model.shop_tag, model.name, "菜單")
                .show(activity.supportFragmentManager, ImageViewDialog.TAG)
        }


    }

    //傳遞shop的id和type 還有position
    fun getItemClick(tag: String, id: Int, name: String, context: Context): OnItemClickHandler {
        var itemClick = object : OnItemClickHandler {
            override fun onItemClick(bundle: Bundle) {
                bundle.putString(DataConstant.SHOP_NAME, name)
                bundle.putString(DataConstant.SHOP_TYPE_TAG, tag)
                bundle.putInt(DataConstant.SHOP_ID, id)
                var intent = Intent(context, DetailMenuActivity::class.java).apply { this.putExtras(bundle) }
                context.startActivity(intent)
            }
        }
        return itemClick
    }

    //取得異步google place資料
    override fun changeGooglePlace(detailPlace: DetailPlace?) {
        if(detailPlace!=null){
            itemView.context.runOnUiThread {
                itemView.shopMenuRecyc.adapter = DetailPlaceAdapter(detailPlace!!)
                itemView.shopMenuRecyc.layoutManager = LinearLayoutManager(itemView.context)
            }
        }else{
            itemView.context.runOnUiThread {
                this.toast("NULL")
            }
        }
    }

    var a = fun(): Int {
        return 1
    }

    val plus: (Int, Int) -> Int = { firstNumber, secondNumber ->
        firstNumber + secondNumber
    }

    fun aa(plus: Int) {

    }
}