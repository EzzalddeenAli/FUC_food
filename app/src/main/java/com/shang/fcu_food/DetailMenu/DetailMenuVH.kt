package com.shang.fcu_food.DetailMenu

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.shang.fcu_food.Data.menu.Menu
import com.shang.fcu_food.Dialog.AddCommentDialog
import com.shang.fcu_food.Dialog.ImageViewDialog
import com.shang.fcu_food.R
import com.shang.fcu_food.Unit.FileStorageUnit
import kotlinx.android.synthetic.main.cardview_detailmenu.view.*

class DetailMenuVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var menuNameTv = itemView.findViewById<TextView>(R.id.menuNameTv)
    var menuPriceTv = itemView.findViewById<TextView>(R.id.menuPriceTv)
    var menuStarTv = itemView.findViewById<TextView>(R.id.menuStarTv)
    var menuCommentTv = itemView.findViewById<TextView>(R.id.menuCommentTv)
    var menuCommentRecyc = itemView.findViewById<RecyclerView>(R.id.menuCommentRecyc)
    var menuPictureImg = itemView.findViewById<ImageView>(R.id.menuPictureImg)
    var menuAddCommentBt = itemView.findViewById<ImageButton>(R.id.menuAddCommentBt)


    fun bind(position: Int, model: Menu, activity: DetailMenuActivity) {
        itemView.menuNameTv.setText(model.name)
        itemView.menuPriceTv.setText("${model.price}元")
        itemView.menuStarTv.setText(String.format("%.1f", model.star))
        itemView.menuCommentTv.setText("${model.usercomment.size}人")
        itemView.menuAddCommentBt.setOnClickListener {
            AddCommentDialog.getInstace(
                model.shop_tag,
                model.shop_id,
                model.id,
                model.usercomment.size
            ).show(activity.supportFragmentManager, AddCommentDialog.TAG)
        }

        itemView.menuCommentRecyc.layoutManager = LinearLayoutManager(itemView.context)
        itemView.menuCommentRecyc.adapter = CommentAdapter(model.usercomment)

        //Menu圖片讀取
        FileStorageUnit.ImageLoader(
            itemView.context,
            itemView.menuPictureImg,
            model.getFile(itemView.context),
            model.getStorageRef(),
            model.errorDrawable,
            RequestOptions().fitCenter()
        )

        itemView.menuPictureImg.setOnClickListener {
            ImageViewDialog.getInstance(
                model.shop_tag,
                model.shop_name,
                model.name
            ).show(activity.supportFragmentManager, ImageViewDialog.TAG)
        }

    }
}