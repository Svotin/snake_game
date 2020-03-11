package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.mygdx.game.Enums.GameState
import kotlin.system.exitProcess

class MenuScreen {
    var gameStage: Stage

    constructor() {
        gameStage = Stage(ScreenViewport())
        gameStage.addActor(MenuBackground())
        createPlayButton(gameStage)
        createVolumeButton(1, gameStage, {imageButton ->  imageButton.isChecked = Global.VOLUME_ENABLED;  Global.VOLUME_ENABLED = !Global.VOLUME_ENABLED;},
                "volume_on.png","volume_off.png" )
    }

    fun render() {
        gameStage.draw()

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            exitProcess(0)
        }
    }

    fun show() {
        if (Gdx.input.inputProcessor !== gameStage)
            Gdx.input.setInputProcessor(gameStage)
    }

    fun hide() {
        if (Gdx.input.inputProcessor == gameStage)
            Gdx.input.inputProcessor = null
    }

    fun dispose() {
        gameStage.dispose()
    }
}

fun createPlayButton(stage: Stage) {
    var textureRegion = TextureRegion(Texture("play_icon.png"))
    var textureRegionDrawable = TextureRegionDrawable(textureRegion)
    val imageButton = ImageButton(textureRegionDrawable)


    var x = Gdx.graphics.width.toFloat() * 0.35f
    var w = Gdx.graphics.width.toFloat() * 0.3f

    var y = Gdx.graphics.height.toFloat() * 0.2f
    var h = Gdx.graphics.height.toFloat() * 0.2f

    imageButton.setBounds(x, y, w, h)


    var background = MenuButtonBackground(Gdx.graphics.width.toFloat() * 0.45f, Gdx.graphics.height.toFloat() * 0.2f, Gdx.graphics.width.toFloat() * 0.1f, Gdx.graphics.height.toFloat() * 0.2f)
    imageButton.addListener(object : InputListener() {

        override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
            Global.GAME_STATE = GameState.INGAME
            Global.isBestScore = false
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

fun createVolumeButton(index: Int, stage: Stage, onUp: (imageButton: ImageButton) -> Unit, iconPath: String = "retry_icon.png", checkedImagePath: String  = "retry_icon.png") {
    var textureRegion = TextureRegion(Texture(iconPath))
    var textureRegionDrawable = TextureRegionDrawable(textureRegion)
    val imageButton = ImageButton(textureRegionDrawable, textureRegionDrawable, TextureRegionDrawable(TextureRegion(Texture(checkedImagePath))))
    val w = Gdx.graphics.width / 2f
    val h = Gdx.graphics.height / 4f
    var boxSize = w/10

    imageButton.setBounds(boxSize*0.5f, boxSize*0.5f, boxSize, boxSize)
    imageButton.isChecked = !Global.VOLUME_ENABLED

    var background = MenuButtonBackground(boxSize*0.3f, boxSize*0.35f, boxSize*1.4f, boxSize*1.3f, Color(1f, 1f, 1f, 0.6f),  Color(1f, 1f, 1f, 0.7f))
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


class MenuButtonBackground: Actor {
    var texture = Texture("gameover_background.png")
    var isHovered = false
    var colorUnhovered: Color
    var colorHovered: Color
    constructor(x: Float, y:Float, w:Float, h:Float, color: Color = Color(0f, 0f, 0f, .7f), colorHovered: Color = Color(0.1f, 0.1f, 0.1f, .8f)) {
        setBounds(x, y, w, h)
        this.colorUnhovered = color
        this.colorHovered = colorHovered
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch == null) return

        if (!isHovered)
            batch.setColor(colorUnhovered)
        else
            batch.setColor(colorHovered)

        batch.draw(texture, x, y, width, height)
    }
}


class MenuBackground: Actor {
    var animation: Animation<TextureRegion>? = null
    var elapsed = 0f

    var texture = Texture("menu_background.jpg")
    var logoTexture = Texture("game_logo.png")

    constructor() {

//        animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("menu_background.gif").read());


        var x = Gdx.graphics.width.toFloat() * 0.15f
        var w = Gdx.graphics.width.toFloat() * 0.7f

        var y = Gdx.graphics.height.toFloat() * 0.7f
        var h = Gdx.graphics.height.toFloat() * 0.2f
        setBounds(x, y, w, h)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        elapsed += Gdx.graphics.getDeltaTime();
        if (batch == null) return
        batch.draw(texture, 0f,0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
//        batch.draw(animation?.getKeyFrame(elapsed), 0f,0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        batch.draw(texture, 0f,0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        batch.draw(logoTexture, x, y, width, height)
    }
}