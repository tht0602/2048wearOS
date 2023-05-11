package tht.webgames.watchhelloworld.presentation

import kotlin.random.Random

class Game2048 {
    var array = Array(4) { Array(4) { 0 } }

    fun startGame() {
        array = Array(4) { Array(4) { 0 } }
//        array[2][2] = 2
//        array[1][2] = 2
        spawnNewTwo()
        spawnNewTwo()
    }

    data class Movement(
        val fromX: Int,
        val fromY: Int,
        val toX: Int,
        val toY: Int
    )

    fun moveCardsHorizon(reverse: Boolean = false): List<Movement> {
        var moved = false
        for (y in 0..3) {
            if (reverse) array[y].reverse()
            var mergedX = -1
            for (x in 0..3) {
                val temp = array[y][x]
                if (array[y][x] != 0) {
                    var moveToX = x
                    while (moveToX > 0) {
                        if (mergedX != moveToX - 1 && array[y][moveToX - 1] == temp) {
                            array[y][moveToX - 1] = temp * 2
                            array[y][moveToX] = 0
                            moved = true
                            mergedX = moveToX - 1
                            break
                        }
                        if (array[y][moveToX - 1] != 0) {
                            break
                        }
                        array[y][moveToX - 1] = temp
                        array[y][moveToX] = 0
                        moved = true
                        moveToX -= 1
                    }
                }
            }
            if (reverse) array[y].reverse()
        }
        if (moved) {
            spawnNewTwo()
        }
        return listOf()
    }

    fun moveCardsVertical(reverse: Boolean = false): List<Movement> {
        var moved = false
        if (reverse) array.reverse()
        for (x in 0..3) {
            var mergedY = -1
            for (y in 0..3) {
                val temp = array[y][x]
                if (array[y][x] != 0) {
                    var moveToY = y
                    while (moveToY > 0) {
                        if (mergedY != moveToY - 1 && array[moveToY - 1][x] == temp) {
                            array[moveToY - 1][x] = temp * 2
                            array[moveToY][x] = 0
                            moved = true
                            mergedY = moveToY - 1
                            break
                        }
                        if (array[moveToY - 1][x] != 0) {
                            break
                        }
                        array[moveToY - 1][x] = temp
                        array[moveToY][x] = 0
                        moved = true
                        moveToY -= 1
                    }
                }
            }
        }
        if (reverse) array.reverse()
        if (moved) {
            spawnNewTwo()
        }
        return listOf()
    }

    private fun spawnNewTwo() {
        var zeroCount = 0
        array.forEach {
            it.forEach { it2 ->
                if (it2 == 0) {
                    zeroCount++
                }
            }
        }
        if (zeroCount > 0) {
            var nextAddSlot = Random.nextInt(zeroCount)
            array.forEach {
                it.forEachIndexed { i, it2 ->
                    if (it2 == 0) {
                        if (nextAddSlot == 0) {
                            it[i] = 2
                        }
                        nextAddSlot--
                    }
                }
            }
        }
    }
}