package com.example.slotmachine4.game

import android.content.Context
import android.media.MediaPlayer
import com.example.slotmachine4.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Sounds {
    private var mMediaPlayer: MediaPlayer? = null

    fun playSound(musicID: Int, mContext: Context){
        mMediaPlayer = MediaPlayer.create(mContext, musicID)
        mMediaPlayer!!.start()
    }

    fun playSoundInGame(musicID: Int, mContext: Context) {
        mMediaPlayer = MediaPlayer.create(mContext, musicID)
        mMediaPlayer!!.start()

        GlobalScope.launch {
            delay(800)
            launch(Dispatchers.Main) {
                mMediaPlayer!!.stop()
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
        }
    }

    fun insertCoin(mContext: Context) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(mContext, R.raw.coin)
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }
}