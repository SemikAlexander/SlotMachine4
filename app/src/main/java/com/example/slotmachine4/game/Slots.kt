package com.example.slotmachine4.game

class Slots(
        private val spinListener: SpinListener?,
        private val imageList: List<Int>,
        private var start: Long
) : Thread() {
    interface SpinListener {
        fun newImage(img: Int)
    }

    var index = 0
    var idImage = 0
    var isGameStarted = true

    override fun run() {
        sleep(start)

        while (isGameStarted) {
            sleep(100)

            index++
            if (index > imageList.size - 1) {
                index = 0
            }
            idImage = imageList[index]

            spinListener?.newImage(imageList[index])
        }
    }

    fun stopSpin() {
        isGameStarted = false
        start = 50
        run()
    }
}