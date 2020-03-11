package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class FoodCell: Actor {
    var texture: Texture
    var isActive = false

    internal constructor(image: Texture) {
        texture = image
    }

    fun eaten(x: Float, y: Float): Boolean {
        return (this.x == x && this.y == y)
    }

    fun spawn(matrix: BoolMatrix) {
        var x = 0
        var y = 0


        do {
            x = (0..(Global.INT_WORLD_WIDTH - Global.INT_BOX_SIZE) / Global.INT_BOX_SIZE).random() * Global.INT_BOX_SIZE
            y = (0..(Global.INT_WORLD_HEIGHT - Global.INT_BOX_SIZE) / Global.INT_BOX_SIZE).random() * Global.INT_BOX_SIZE
        } while (matrix[x, y] == true)

        setBounds(x.toFloat(), y.toFloat(),
                Global.BOX_SIZE, Global.BOX_SIZE)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch == null) return

        batch.draw(texture, getX(), getY(), getWidth(), getHeight())
    }
}