package com.example.gamesapp.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.data.model.Games
import com.example.gamesapp.data.model.ShortScreenshot
import com.example.gamesapp.ui.view.GameDetailFragment
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
fun showModalBottomSheetGames(games: Games, context: Context, parentFragmentManager: FragmentManager? = null) {
    val dialog = BottomSheetDialog(context)
    dialog.setContentView(R.layout.bottom_sheet)
    dialog.dismissWithAnimation = true
    val modal = dialog.findViewById<View>(R.id.standard_bottom_sheet)
    modal?.setOnClickListener {
        dialog.dismiss()
        // Navigate to Game Detail Fragment with object games
        parentFragmentManager?.setFragmentResult("game_detail", bundleOf("game" to games))
        parentFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentContainerView, GameDetailFragment())?.addToBackStack(null)
            ?.commit()
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

// Show dialog with screenshot of game
fun showDialogScreenshot (screenshot: ShortScreenshot, context: Context) {
    // Create image view for each screenshot
    val image = ImageView(context)
    image.load(screenshot.image)
    // Resize image
    // saber tamaño de pantalla
    image.layoutParams = ViewGroup.LayoutParams(
        context.resources.displayMetrics.widthPixels,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    image.scaleType = ImageView.ScaleType.CENTER_CROP
    // Add image to ConstraintLayout
    val constraint = ConstraintLayout(context)
    constraint.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    constraint.addView(image)
    // Set view to dialog layout and show dialog
    val dialog = Dialog(context)
    dialog.setContentView(constraint)
    dialog.show()
}