package com.mygdx.game

import com.badlogic.gdx.*
import com.badlogic.gdx.math.Vector2




class GameScreen: Screen, InputProcessor {
    val HALF_SCREEN_WIDTH = Gdx.graphics.width / 2

    var leftStart = Vector2()
    var leftEnd = Vector2()

    var rightButton = -1

    var leftStarted = false

    var MIN_SWIPE_DISTANCE = Gdx.graphics.width*0.05f

    override fun show() {
        val plex = InputMultiplexer(this)
        Gdx.input.inputProcessor = plex
    }

    override fun render(delta: Float) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {

        for (i in 0..pointer) {

            if (screenX > HALF_SCREEN_WIDTH && rightButton == -1)
            // right touched
            {
                rightButton = pointer
                Global.TICK_DELAY = Global.SPEED_UP
            }

            if (screenX < HALF_SCREEN_WIDTH)
            // left touched
            {
                leftStart.set(screenX.toFloat(), screenY.toFloat())
                leftStarted = true
            }

        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        for (i in 0..pointer) {

            if (rightButton == pointer)
            // right released
            {

                Global.TICK_DELAY = Global.SPEED_DOWN
                rightButton = -1
            }

            if (screenX < HALF_SCREEN_WIDTH && leftStarted)
            // left released
            {
                leftStarted = false
                leftEnd.set(screenX.toFloat(), screenY.toFloat())

                var delta = leftStart.cpy().sub(leftEnd)


                if (Math.abs(delta.x) > Math.abs(delta.y)) {
                    if (delta.x < -MIN_SWIPE_DISTANCE)
                        Global.onRight()
                    else if (delta.x > MIN_SWIPE_DISTANCE)
                        Global.onLeft()
                    else {
                        if (delta.y < -MIN_SWIPE_DISTANCE)
                            Global.onDown()
                        else if (delta.y > MIN_SWIPE_DISTANCE)
                            Global.onUp()
                    }
                } else {
                    if (delta.y < -MIN_SWIPE_DISTANCE)
                        Global.onDown()
                    else if (delta.y > MIN_SWIPE_DISTANCE)
                        Global.onUp()
                }
            }



        }
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun resize(width: Int, height: Int) {}

    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {}

    override fun keyDown(keycode: Int): Boolean {
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        return true
    }




}