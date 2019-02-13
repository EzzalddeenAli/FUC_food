package com.shang.fcu_food.Data

import android.util.Log
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.shang.fcu_food.DetailMenu.DetailMenuActivity
import com.shang.fcu_food.R

class BreakfastMenu: Menu() {
    override var errorDrawable:Int= R.drawable.ic_boy

    override fun getQuery(shop_tag: String, shop_id: String): Query? {
        var query=FirebaseDatabase.getInstance()
        .getReference().child("${BreakfastShop.tag}/1/menu")

        return query
    }

    override fun getOption(shop_tag: String, shop_id: String): FirebaseRecyclerOptions<Menu>? {
        var options = FirebaseRecyclerOptions.Builder<BreakfastMenu>()
            .setQuery(getQuery(shop_tag,shop_id)!!, BreakfastMenu::class.java).build()
        return options as FirebaseRecyclerOptions<Menu>
    }
}