package com.example.gamesapp.utils

import android.content.Context
import android.view.View
import android.widget.*
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.model.Games
import com.google.android.material.bottomsheet.BottomSheetDialog

fun View.visible(): View {
    this.visibility = View.VISIBLE
    return this
}

fun View.gone(): View {
    this.visibility = View.GONE
    return this
}

// Function for convert date with format yyyy-MM-dd to dd-MM-yyyy
fun String.convertDate(): String {
    val splitDate = this.split("-")
    return "${splitDate[2]}-${splitDate[1]}-${splitDate[0]}"
}

// Function that show bottom sheet dialog with game details
fun showModalBottomSheetGames(games: Games, context: Context) {
    val dialog = BottomSheetDialog(context)
    dialog.setContentView(R.layout.bottom_sheet)
    dialog.dismissWithAnimation = true
    val modal = dialog.findViewById<View>(R.id.standard_bottom_sheet)
    modal?.setOnClickListener {
        // Navigate to detail activity
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
    }
    val image = dialog.findViewById<ImageView>(R.id.iv_image_modal)
    image?.load(games.imageURL)
    val title = dialog.findViewById<TextView>(R.id.tv_title_modal)
    title?.text = games.name
    val date = dialog.findViewById<TextView>(R.id.tv_date_modal)
    date?.text = games.released?.convertDate()
    val rating = dialog.findViewById<RatingBar>(R.id.rbar_rating_modal)
    rating?.rating = games.rating!!
    val btnClose = dialog.findViewById<ImageView>(R.id.close_modal)
    btnClose?.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}