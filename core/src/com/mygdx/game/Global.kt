package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.GameState

class Global {
    companion object {
        var moveDirection = Direction.RIGHT
        var directionHasBeenChanged = false

        fun onUp() {
            if (!directionHasBeenChanged && moveDirection !== Direction.DOWN)
                moveDirection = Direction.UP
        }

        fun onRight() {
            if (!directionHasBeenChanged && moveDirection !== Direction.LEFT)
                moveDirection = Direction.RIGHT
        }

        fun onLeft() {
            if (!directionHasBeenChanged && moveDirection !== Direction.RIGHT)
                moveDirection = Direction.LEFT
        }

        fun onDown() {
            if (!directionHasBeenChanged && moveDirection !== Direction.UP)
                moveDirection = Direction.DOWN
        }

        var score = 0
        var isBestScore = false




        var scoreFile = Gdx.files.local("best_score.txt")
        var bestScore: Int
            get() = readBestScore()
            set(value: Int) = writeBestScore(value)

        fun readBestScore(): Int {
            if (!scoreFile.exists())
                scoreFile.writeString("0", false)
            var response = scoreFile.readString().toInt()
            if (response <= 0)
                response = 0

            return response
        }


        fun writeBestScore(value: Int) {
            scoreFile.writeString(value.toString(), false)
        }



        var VOLUME_ENABLED = true

        var gameScreen = GameScreen()


        var scoreFont = BitmapFont()
        var scoreColor = Color(.1f, .1f, .1f, 1f)
        var scoreLabelStyle = Label.LabelStyle(scoreFont, scoreColor)
        var scoreLabel: Label = Label("Score: $score", scoreLabelStyle)

        fun updateScore(newScore: Int) {
            score = newScore
            scoreLabel.setText("Score: $score")
        }

        lateinit var batch: SpriteBatch

        val BOX_SIZE = 1f
        val INT_BOX_SIZE = BOX_SIZE.toInt()
        // эти поля должны быть делимы на BOX_SIZE
        val WORLD_WIDTH = 16f
        val WORLD_HEIGHT = 9f

        val INT_WORLD_WIDTH = WORLD_WIDTH.toInt()
        val INT_WORLD_HEIGHT = WORLD_HEIGHT.toInt()

        // скорость при ускореении и без
        val SPEED_UP = 0.3f
        val SPEED_DOWN = 0.6f

        // время в секундах для следующего обновления
        var TICK_DELAY = SPEED_DOWN

        var GAME_STATE = GameState.MENU

        lateinit var font26: BitmapFont

        fun drawText(str: String, x: Float, y: Float) {
            batch.begin()
            font26.setColor(1.0f, 1.0f, 1.0f, 1.0f)
            font26.draw(batch, str, x, y)
            batch.end()

        }



    }
}