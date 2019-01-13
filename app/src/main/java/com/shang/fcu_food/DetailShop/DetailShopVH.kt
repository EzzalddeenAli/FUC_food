package com.shang.fcu_food.DetailShop

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.shang.fcu_food.Data.*
import com.shang.fcu_food.R
import kotlinx.android.synthetic.main.cardview_detailshop.view.*

class DetailShopVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var shopNameTv=itemView.findViewById<TextView>(R.id.shopNameTv)
    var shopPhoneTv=itemView.findViewById<TextView>(R.id.shopPhoneTv)
    var shopOpenTv=itemView.findViewById<TextView>(R.id.shopOpenTv)
    var shopStarTv=itemView.findViewById<TextView>(R.id.shopStarTv)
    var shopMapImg=itemView.findViewById<ImageView>(R.id.shopMapIg)

    fun bind(tag:String,itemView: DetailShopVH,model: Shop){
        when (tag) {
            BreakfastShop.tag -> model as BreakfastShop
            DinnerShop.tag -> model as DinnerShop
            SnackShop.tag -> model as SnackShop
            DrinkShop.tag -> model as DrinkShop
        }
        itemView.shopNameTv.setText(model.name)
        itemView.shopOpenTv.setText(model.time)
        itemView.shopStarTv.setText(model.getStars().toString())
        itemView.shopPhoneTv.setText(model.phone)


    }


}