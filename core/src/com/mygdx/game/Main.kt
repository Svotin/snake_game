package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mygdx.game.Enums.GameState

class Main : ApplicationAdapter() {
    lateinit var grassLight: Texture
    lateinit var grassDark: Texture


    lateinit var backgroundStage: Stage

    lateinit var snakeGame: SnakeGame

    lateinit var pauseWindow: GameOverWindow

    lateinit var menuScreen: MenuScreen

    lateinit var soundManager: SoundManager

    var lastTick: Long = 0L

    override fun create() {
        Gdx.input.setCatchBackKey(true);
        Global.batch = SpriteBatch()

        pauseWindow = GameOverWindow()
        // загрузка двух разных текстур для травы, которые будут чередоваться в шахматном порядке
        grassLight = Texture("grass01.png")
        grassDark = Texture("grass02.png")

        backgroundStage = Stage(FitViewport(Global.WORLD_WIDTH, Global.WORLD_HEIGHT))
        fillBackgroundActors(backgroundStage)


        snakeGame = SnakeGame()
        menuScreen = MenuScreen()

        loadFont()

        soundManager = SoundManager()

    }

    fun loadFont() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = Gdx.graphics.width / 48
        parameter.color = Color.WHITE
        parameter.borderColor = Color.BLACK
        parameter.borderWidth = 2f
        Global.font26 = generator.generateFont(parameter)
        generator.dispose() // avoid memory leaks, important
    }



    fun drawScore() {
        Global.drawText("Score: ${Global.score}", Gdx.graphics.width*0.01f, Gdx.graphics.height*0.99f)
    }

    override fun render() {
        soundManager.execute()

        if (Global.GAME_STATE == GameState.RETRY) {
            Global.isBestScore = false
            restartGame()
            pauseWindow.hide()
            Global.GAME_STATE = GameState.INGAME
        }

        if (Global.GAME_STATE != GameState.MENU) {
            if (!snakeGame.alreadyStarted){
                snakeGame.alreadyStarted = true
                snakeGame.restart()
            }
            updateSnakePosition(snakeGame)
            drawBackground(backgroundStage)

            snakeGame.bodyRender()
            drawScore()

            pauseWindow.hide()
        } else {
            if (snakeGame.alreadyStarted)
                snakeGame.alreadyStarted = false

            menuScreen.show()
            menuScreen.render()
        }



        // Должно стоять позже всего
        if (Global.GAME_STATE == GameState.GAMEOVER || Global.GAME_STATE == GameState.PAUSE) {
            pauseWindow.show()
            pauseWindow.render()
        }
    }

    fun restartGame() {
        snakeGame.restart()
    }

    fun fillBackgroundActors(stage: Stage) {
        // количество иттераций по вертикали и горизонтали
        var widthIterations: Int = Global.INT_WORLD_WIDTH / Global.INT_BOX_SIZE -1
        var heightIterations: Int = Global.INT_WORLD_HEIGHT / Global.INT_BOX_SIZE -1
        for (w in 0..widthIterations){
            for (h in 0..heightIterations) {
                var currentTexture = when ((h+w) % 2){
                    0 -> grassLight
                    1 -> grassDark
                    else -> grassLight
                }
                stage.addActor(BackgroundActor(currentTexture, (w*Global.INT_BOX_SIZE), (h*Global.INT_BOX_SIZE), Global.INT_BOX_SIZE, Global.INT_BOX_SIZE))
            }
        }
    }

    fun drawBackground(stage: Stage) {
        stage.draw()
    }

    fun updateSnakePosition(snakeGame: SnakeGame) {
        snakeGame.handleEvents()
        if (Global.GAME_STATE == GameState.GAMEOVER || Global.GAME_STATE == GameState.PAUSE) {
            return
        }

        if (!sleepReady(Global.TICK_DELAY, lastTick)) return
        Global.directionHasBeenChanged = false

        lastTick = System.currentTimeMillis()
        snakeGame.move()
    }

    fun sleepReady(delay:Float, lastTick:Long) : Boolean {
        return (System.currentTimeMillis() - lastTick).toFloat() / 1000 > delay
    }

    override fun dispose() {
        Global.batch.dispose()

        soundManager.dispose()

        grassLight.dispose()
        grassDark.dispose()
        menuScreen.dispose()

        snakeGame.dispose()

    }
}
