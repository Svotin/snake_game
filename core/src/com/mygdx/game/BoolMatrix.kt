package com.mygdx.game


class BoolMatrix(xSize: Int, ySize: Int) {
    val matrix = Array(xSize, {BooleanArray(ySize)})

    operator fun get(x: Int, y: Int): Boolean {
        return matrix[x][y]
    }

    operator fun set(x: Int, y: Int, t: Boolean) {
        matrix[x][y] = t
    }
}