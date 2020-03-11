package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.StretchViewport


class BackgroundActor : Actor {

    var img : Texture
    internal constructor(image: Texture, x: Int, y: Int, w: Int, h:Int) {
        img = image
        setBounds(x.toFloat(), y.toFloat(),
                w.toFloat(), h.toFloat())
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch == null) return

        batch.draw(img, getX(), getY(), getWidth(), getHeight())
    }
}