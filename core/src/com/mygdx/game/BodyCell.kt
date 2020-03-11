package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.mygdx.game.Enums.Direction


class BodyCell: Actor {

    var texture: Texture
    var isHead = false


    internal constructor(image: Texture, x: Int, y: Int, w: Int, h: Int, head:Boolean = false) {
        isHead = head
        texture = image
        setBounds(x.toFloat(), y.toFloat(),
                w.toFloat(), h.toFloat())
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch == null) return

        if (isHead)
            batch.setColor(1f, 1f, 1f, 1f)
        else
            batch.setColor(0f, 1f, 0f, 1f)

        batch.draw(texture, getX(), getY(), getWidth(), getHeight())
    }

    fun hasCollision(matrix: BoolMatrix): Boolean {
        return matrix[x.toInt(), y.toInt()]
    }

    // после выхода за границу появляется с другой стороны
    fun keepInBound() {
        if (x >= Global.WORLD_WIDTH)
            setX(0f)
        else if (x < 0)
            setX(Global.WORLD_WIDTH - Global.BOX_SIZE)

        if (y >= Global.WORLD_HEIGHT)
            setY(0f)
        else if (y < 0)
            setY(Global.WORLD_HEIGHT - Global.BOX_SIZE)
    }

    fun moveTo(direction: Direction) {
        when (direction) {
            Direction.UP -> setY(y+height)
            Direction.DOWN -> setY(y-height)
            Direction.RIGHT -> setX(x + width)
            Direction.LEFT -> setX(x - width)
            else -> return
        }
    }



}