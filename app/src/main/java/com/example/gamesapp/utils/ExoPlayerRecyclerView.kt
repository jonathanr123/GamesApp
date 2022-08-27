package com.example.gamesapp.utils

import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.gamesapp.R
import com.example.gamesapp.domain.model.MediaObject
import com.example.gamesapp.ui.view.adapters.MediaRecyclerAdapter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ExoPlayerRecyclerView : RecyclerView {
    /**
     * PlayerViewHolder UI component
     * Watch PlayerViewHolder class
     */
    private var mediaCoverImage: ImageView? = null
    private var volumeControl: ImageView? = null
    private var ivPlayLoad: CircleImageView? = null
    private var progressBar: ProgressBar? = null
    private var viewHolderParent: View? = null
    private var mediaContainer: FrameLayout? = null
    private var videoSurfaceView: StyledPlayerView? = null
    private var videoPlayer: ExoPlayer? = null

    /**
     * Variable declaration
     */
    // Media List
    private var mediaObjects = ArrayList<MediaObject>()
    private var videoSurfaceDefaultHeight = 0
    private var screenDefaultHeight = 0
    private var myContext: Context? = null
    private var playPosition = -1
    private var isVideoViewAdded = false

    // Controlling volume state
    private lateinit var volumeState: VolumeState
    private val videoViewClickListener = OnClickListener { toggleVolume() }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        this.myContext = context.applicationContext
        val point = Point()
        videoSurfaceDefaultHeight = point.x
        screenDefaultHeight = point.y
        videoSurfaceView = StyledPlayerView(this.context)
        videoSurfaceView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

        //Create the player using ExoPlayerFactory
        videoPlayer = ExoPlayer.Builder(this.context).build()
        // Disable Player Control
        videoSurfaceView?.useController = false
        // Bind the player to the view.
        videoSurfaceView?.player = videoPlayer
        // Turn on Volume
        setVolumeControl(VolumeState.ON)
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    // Show the old thumbnail
                    mediaCoverImage?.visibility = VISIBLE
                    ivPlayLoad?.visibility = VISIBLE

                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    if (!recyclerView.canScrollVertically(1)) {
                        playVideo(true)
                    } else {
                        playVideo(false)
                    }
                }
            }

        })
        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}
            override fun onChildViewDetachedFromWindow(view: View) {
                if (viewHolderParent != null && viewHolderParent == view) {
                    resetVideoView()
                }
            }
        })
        videoPlayer?.addListener(object : Player.Listener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {}
            override fun onTracksChanged(tracks: Tracks) {
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.")
                        progressBar?.visibility = VISIBLE
                        ivPlayLoad?.visibility = VISIBLE
                    }
                    Player.STATE_ENDED -> {
                        Log.d(TAG, "onPlayerStateChanged: Video ended.")
                        videoPlayer?.seekTo(0)
                    }
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_READY -> {
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.")
                        progressBar?.visibility = GONE
                        ivPlayLoad?.visibility = GONE
                        if (!isVideoViewAdded) {
                            addVideoView()
                        }
                    }
                    else -> {
                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPlayerError(error: PlaybackException) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            @Deprecated("Deprecated in Java")
            override fun onSeekProcessed() {}
        })
    }

    fun playVideo(isEndOfList: Boolean) {
        val targetPosition: Int
        if (!isEndOfList) {
            val startPosition =
                (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
            var endPosition =
                (layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()

            // If there is more than 2 list-items on the screen, set the difference to be 1
            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1
            }

            // Something is wrong. return.
            if (startPosition < 0 || endPosition < 0) {
                return
            }

            // If there is more than 1 list-item on the screen
            targetPosition = if (startPosition != endPosition) {
                val startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition)
                val endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition)
                if (startPositionVideoHeight > endPositionVideoHeight) startPosition else endPosition
            } else {
                startPosition
            }
        } else {
            targetPosition = mediaObjects.size - 1
        }
        Log.d(TAG, "playVideo: target position: $targetPosition")

        // Video is already playing so return
        if (targetPosition == playPosition) {
            return
        }

        // Set the position of the list-item that is to be played
        playPosition = targetPosition
        if (videoSurfaceView == null) {
            return
        }

        // Remove any old surface views from previously playing videos
        videoSurfaceView?.visibility = INVISIBLE
        removeVideoView(videoSurfaceView)
        val currentPosition = targetPosition - (Objects.requireNonNull(
            layoutManager
        ) as LinearLayoutManager).findFirstVisibleItemPosition()
        val child = getChildAt(currentPosition) ?: return
        val holder = child.tag as MediaRecyclerAdapter.PlayerViewHolder
        mediaCoverImage = holder.itemView.findViewById(R.id.ivMediaCoverImage)
        ivPlayLoad = holder.itemView.findViewById(R.id.ivPlayLoad)
        progressBar = holder.itemView.findViewById(R.id.progressBar)
        volumeControl = holder.itemView.findViewById(R.id.ivVolumeControl)
        viewHolderParent = holder.itemView
        mediaContainer = holder.itemView.findViewById(R.id.mediaContainer)
        videoSurfaceView?.player = videoPlayer
        viewHolderParent?.setOnClickListener(videoViewClickListener)
        val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(this.context)
        val mediaUrl = mediaObjects[targetPosition].url
        if (mediaUrl != null) {
            val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(mediaUrl)))
            videoPlayer?.setMediaSource(videoSource)
            videoPlayer?.prepare()
            videoPlayer?.playWhenReady = true
        }
    }

    /**
     * Returns the visible region of the video surface on the screen.
     * If some is cut off, it will return less than the @videoSurfaceDefaultHeight
     */
    private fun getVisibleVideoSurfaceHeight(playPosition: Int): Int {
        val at = playPosition - (Objects.requireNonNull(
            layoutManager
        ) as LinearLayoutManager).findFirstVisibleItemPosition()
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: $at")
        val child = getChildAt(at) ?: return 0
        val location = IntArray(2)
        child.getLocationInWindow(location)
        return if (location[1] < 0) {
            location[1] + videoSurfaceDefaultHeight
        } else {
            screenDefaultHeight - location[1]
        }
    }

    // Remove the old player
    private fun removeVideoView(videoView: StyledPlayerView?) {
        val parent = videoView!!.parent as? ViewGroup ?: return
        val index = parent.indexOfChild(videoView)
        if (index >= 0) {
            parent.removeViewAt(index)
            isVideoViewAdded = false
            viewHolderParent?.setOnClickListener(null)
        }
    }

    private fun addVideoView() {
        mediaContainer?.addView(videoSurfaceView)
        isVideoViewAdded = true
        videoSurfaceView?.requestFocus()
        videoSurfaceView?.visibility = VISIBLE
        videoSurfaceView?.alpha = 1f
        mediaCoverImage?.visibility = GONE
    }

    private fun resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView(videoSurfaceView)
            playPosition = -1
            videoSurfaceView?.visibility = INVISIBLE
            mediaCoverImage?.visibility = VISIBLE
        }
    }

    fun releasePlayer() {
        videoPlayer?.release()
        videoPlayer = null
        viewHolderParent = null
    }

    fun onPausePlayer() {
        videoPlayer?.stop()
    }

    private fun toggleVolume() {
        if (volumeState == VolumeState.OFF) {
            Log.d(TAG, "togglePlaybackState: enabling volume.")
            setVolumeControl(VolumeState.ON)
        } else if (volumeState == VolumeState.ON) {
            Log.d(TAG, "togglePlaybackState: disabling volume.")
            setVolumeControl(VolumeState.OFF)
        }
    }

    private fun setVolumeControl(state: VolumeState) {
        volumeState = state
        if (state == VolumeState.OFF) {
            videoPlayer?.volume = 0f
            animateVolumeControl()
        } else if (state == VolumeState.ON) {
            videoPlayer?.volume = 1f
            animateVolumeControl()
        }
    }

    private fun animateVolumeControl() {
        volumeControl?.bringToFront()
        if (volumeState == VolumeState.OFF) {
            volumeControl?.load(R.drawable.ic_volume_off)
        } else if (volumeState == VolumeState.ON) {
            volumeControl?.load(R.drawable.ic_volume_on)
        }
        volumeControl?.animate()?.cancel()
        volumeControl?.alpha = 1f
        volumeControl?.animate()
            ?.alpha(0f)
            ?.setDuration(600)?.startDelay = 1000
    }

    fun setMediaObjects(mediaObjects: ArrayList<MediaObject>) {
        this.mediaObjects = mediaObjects
    }

    /**
     * Volume ENUM
     */
    private enum class VolumeState {
        ON, OFF
    }

    companion object {
        private const val TAG = "ExoPlayerRecyclerView"
    }
}