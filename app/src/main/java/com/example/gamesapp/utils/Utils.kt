package com.example.gamesapp.utils

import android.app.Dialog
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.graphics.Rect
import android.media.AudioManager
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.data.model.Games
import com.example.gamesapp.data.model.ShortScreenshot
import com.google.android.material.bottomsheet.BottomSheetDialog

fun View.visible(): View {
    this.visibility = View.VISIBLE
    return this
}

fun View.gone(): View {
    this.visibility = View.GONE
    return this
}

// Check if the view is fully visible in a vertical scrollview
fun View.isFullyVisible(scrollView: ScrollView) : Boolean {
    val scrollBounds = Rect()
    scrollView.getDrawingRect(scrollBounds)
    val top = y
    val bottom = top + height
    return scrollBounds.top < top && scrollBounds.bottom > bottom
}

// Check if the view is fully visible in a horizontal scrollview
fun View.isFullyVisibleHorizontal(horizontalScrollView: HorizontalScrollView) : Boolean {
    @Suppress("CanBeVal") var scrollBounds = Rect()
    horizontalScrollView.getDrawingRect(scrollBounds)
    val left = x
    val right = left + width
    return scrollBounds.left < left && scrollBounds.right > right
}

// Check if the view is partially/fully visible in a vertical scrollview
fun View.isPartiallyOrFullyVisible(scrollView: ScrollView) : Boolean {
    @Suppress("CanBeVal") var scrollBounds = Rect()
    scrollView.getHitRect(scrollBounds)
    return getLocalVisibleRect(scrollBounds)
}

// Check if the view is partially/fully visible in a horizontal scrollview
fun View.isPartiallyOrFullyVisibleHorizontal(horizontalScrollView: HorizontalScrollView) : Boolean {
    @Suppress("CanBeVal") var scrollBounds = Rect()
    horizontalScrollView.getHitRect(scrollBounds)
    return getLocalVisibleRect(scrollBounds)
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
        val navController = parentFragmentManager?.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        navController?.navigate(R.id.GameDetailFragment, bundleOf("game" to games))
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

// Function that turns on the sound of the phone
fun volumeOn(context: Context) {
    val manager = context.getSystemService(AUDIO_SERVICE) as AudioManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        manager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
    }
}

// Function that mutes the phone
fun volumeOff(context: Context) {
    val manager = context.getSystemService(AUDIO_SERVICE) as AudioManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        manager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
    }
}

// Set Color progress bar in the Swipe Layout
fun setColorSwipe (context: Context, swipe: SwipeRefreshLayout) {
    val colorGold = ContextCompat.getColor(context,R.color.gold)
    val colorBlue = ContextCompat.getColor(context,R.color.blue_500)
    val colorPurple = ContextCompat.getColor(context,R.color.purple_200)
    val colorBlack = ContextCompat.getColor(context,R.color.black)
    swipe.setColorSchemeColors(colorGold, colorBlue,colorBlack, colorPurple)
}