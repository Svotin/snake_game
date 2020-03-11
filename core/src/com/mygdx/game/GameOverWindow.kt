package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.*
import com.mygdx.game.Enums.GameState
//import libcore.icu.NativeCollation.setText
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport


class GameOverWindow {
    var gameOverStage: Stage

    var gamePauseStage: Stage


    internal constructor() {
        gameOverStage = createGameOverWindow()

        gamePauseStage = createPauseWindow()

    }

    fun render() {
        if (Global.GAME_STATE == GameState.GAMEOVER) {
            gameOverStage.draw()
            if (!Global.isBestScore)
                Global.drawText("Your score: ${Global.score}", Gdx.graphics.width / 2.35f, Gdx.graphics.height / 2.1f)
            else
                Global.drawText("New best score: ${Global.score}", Gdx.graphics.width / 2.45f, Gdx.graphics.height / 2.1f)
        } else {
            gamePauseStage.draw()
            Global.drawText("Best score: ${Global.bestScore}", Gdx.graphics.width / 2.35f, Gdx.graphics.height / 2.1f)
        }

    }

    fun show() {
        if (Global.GAME_STATE == GameState.GAMEOVER) {
            if (Gdx.input.inputProcessor !== gameOverStage)
                Gdx.input.setInputProcessor(gameOverStage)
        } else {
            if (Gdx.input.inputProcessor !== gamePauseStage)
                Gdx.input.setInputProcessor(gamePauseStage)
        }

    }

    fun hide() {
        if (Gdx.input.inputProcessor == gameOverStage)
            Gdx.input.inputProcessor = null
    }

    fun dispose() {
        gameOverStage.dispose()
        gamePauseStage.dispose()
    }

}

fun createGameOverWindow(): Stage {
    var stage = Stage(ScreenViewport())
    // background layout
    stage.addActor(PauseMenuBackground())
    createButton(0, stage, GameState.RETRY, "retry_icon.png")
    createButton(1, stage, {imageButton ->  imageButton.isChecked = Global.VOLUME_ENABLED;  Global.VOLUME_ENABLED = !Global.VOLUME_ENABLED;},
            "volume_on.png","volume_off.png" )
    createButton(2, stage, GameState.MENU, "exit_icon.png")

    return stage
}

fun createPauseWindow(): Stage {
    var stage = Stage(ScreenViewport())
    // background layout
    stage.addActor(PauseMenuBackground())
    createButton(0, stage, {Global.GAME_STATE = GameState.INGAME; Global.gameScreen.show()}, "play.png", "play.png")
    createButton(1, stage, {imageButton ->  imageButton.isChecked = Global.VOLUME_ENABLED;  Global.VOLUME_ENABLED = !Global.VOLUME_ENABLED;},
            "volume_on.png","volume_off.png" )
    createButton(2, stage, GameState.MENU, "exit_icon.png")

    return stage
}



fun createButton(index: Int, stage: Stage, changeTo: GameState, iconPath: String = "retry_icon.png") {
    var textureRegion = TextureRegion(Texture(iconPath))
    var textureRegionDrawable = TextureRegionDrawable(textureRegion)
    val imageButton = ImageButton(textureRegionDrawable)
    val w = Gdx.graphics.width / 2f
    val h = Gdx.graphics.height / 4f
    var boxSize = w/6

    imageButton.setBounds(w / 2 + boxSize*1.2f + boxSize*1.5f*index, h/2 + boxSize*1.2f, boxSize*0.6f, boxSize*0.6f)


    var background = PauseMenuBackgroundButton(index)
    imageButton.addListener(object : InputListener() {

        override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
            Global.GAME_STATE = changeTo
            background.isHovered = false
        }

        override  fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            background.isHovered = true
            return true
        }
    })

    stage.addActor(background)
    stage.addActor(imageButton)
}

fun createButton(index: Int, stage: Stage, onUp: (imageButton: ImageButton) -> Unit, iconPath: String = "retry_icon.png", checkedImagePath: String  = "retry_icon.png") {
    var textureRegion = TextureRegion(Texture(iconPath))
    var textureRegionDrawable = TextureRegionDrawable(textureRegion)
    val imageButton = ImageButton(textureRegionDrawable, textureRegionDrawable, TextureRegionDrawable(TextureRegion(Texture(checkedImagePath))))
    val w = Gdx.graphics.width / 2f
    val h = Gdx.graphics.height / 4f
    var boxSize = w/6

    imageButton.setBounds(w / 2 + boxSize*1.2f + boxSize*1.5f*index, h/2 + boxSize*1.2f, boxSize*0.6f, boxSize*0.6f)
    imageButton.isChecked = !Global.VOLUME_ENABLED

    var background = PauseMenuBackgroundButton(index)
    imageButton.addListener(object : InputListener() {

        override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
            onUp(imageButton)
            background.isHovered = false
        }

        override  fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            background.isHovered = true
            return true
        }
    })

    stage.addActor(background)
    stage.addActor(imageButton)
}


class PauseMenuBackground : Actor {
    var texture = Texture("gameover_background.png")
    var gameTexture = Texture("game_label.png")
    var overTexture = Texture("over_label.png")
    var pauseTexture = Texture("pause_label.png")
    var labelX = 0f
    var labelY = 0f
    var labelW = 0f
    var labelH = 0f

    constructor() {
        val w = Gdx.graphics.width / 2f
        val h = Gdx.graphics.height / 4f
        setBounds(w/2, h/2, w, Gdx.graphics.height - h)
        labelX = getX() + getWidth() * .2f
        labelY = getY() + getHeight()  * .5f
        labelW = getWidth() * .6f
        labelH = getHeight() * .2f
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch == null) return

        batch.setColor(1f, 1f, 1f, .75f)
        batch.draw(texture, getX(), getY(), getWidth(), getHeight())
        if (Global.GAME_STATE == GameState.GAMEOVER) {
            batch.draw(gameTexture, labelX, labelY, labelW, labelH)
            batch.draw(overTexture, labelX, labelY+labelH*0.8f, labelW, labelH)
        }
        else
            batch.draw(pauseTexture, labelX, labelY+labelH*0.5f, labelW, labelH)

    }
}

class PauseMenuBackgroundButton: Actor {
    var texture = Texture("gameover_background.png")
    var margin: Float
    var isHovered = false
    constructor(index: Int) {

        val w = Gdx.graphics.width / 2f
        val h = Gdx.graphics.height / 4f
        var boxSize = w/6
        margin = boxSize / 10

        setBounds(w / 2 + boxSize + boxSize*1.5f*index - margin, h/2 + boxSize - margin, boxSize + margin*2, boxSize+ margin*2)

    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch == null) return

        if (!isHovered)
            batch.setColor(0f, 0f, 0f, .1f)
        else
            batch.setColor(0.4f, 0.4f, 0.4f, .1f)

        batch.draw(texture, getX(), getY(), getWidth(), getHeight())
    }
}