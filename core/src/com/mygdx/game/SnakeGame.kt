package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.GameState



class SnakeGame {

    var snakeBody = Stage(FillViewport(Global.WORLD_WIDTH, Global.WORLD_HEIGHT))
    var foodStage = Stage(FillViewport(Global.WORLD_WIDTH, Global.WORLD_HEIGHT))
    var foodEntity: FoodCell
    var boxSize = 0f
    var tailImage = Texture("body.png")
    var headImage = Texture("head.png")
    var foodImage = Texture("banana.png")




    var initSize = 4

    var alreadyStarted = false



    fun dispose(){
        tailImage.dispose()
        snakeBody.dispose()
        foodStage.dispose()
        Global.scoreFont.dispose()
    }

    fun restart() {
        snakeBody.dispose()
        snakeBody = Stage(FillViewport(Global.WORLD_WIDTH, Global.WORLD_HEIGHT))
        initSnakeBody(initSize)

        Global.moveDirection = Direction.RIGHT


        newFood()

        Global.updateScore(0)

        show()
    }

    internal constructor() {

        boxSize = Global.BOX_SIZE
        foodStage.addActor(FoodCell(foodImage))
        var tmpFood = foodStage.actors.first()
        if (tmpFood is FoodCell)
            foodEntity = tmpFood
        else
            foodEntity = FoodCell(foodImage)



    }

    fun hide() {
        if (Gdx.input.inputProcessor !== null)
            Gdx.input.inputProcessor = null
    }

    fun show() {
        Global.gameScreen.show()
    }




    fun handleEvents() {
        if (Global.directionHasBeenChanged)
            return

        Global.directionHasBeenChanged = true
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && Global.moveDirection !== Direction.DOWN)
            Global.moveDirection = Direction.UP
        else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && Global.moveDirection !== Direction.UP)
            Global.moveDirection = Direction.DOWN
        else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && Global.moveDirection !== Direction.RIGHT)
            Global.moveDirection = Direction.LEFT
        else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && Global.moveDirection !== Direction.LEFT)
            Global.moveDirection = Direction.RIGHT
        else Global.directionHasBeenChanged = false
    }

    fun initSnakeBody(size: Int) {
        snakeBody.addActor(BodyCell(headImage, (size-1) * Global.INT_BOX_SIZE, 0, Global.INT_BOX_SIZE, Global.INT_BOX_SIZE, true))

        for (i in size - 1 downTo 0) {
            snakeBody.addActor(BodyCell(tailImage, i*Global.INT_BOX_SIZE, 0, Global.INT_BOX_SIZE, Global.INT_BOX_SIZE, false))
        }
    }

    fun eat() {
        var lastCell = snakeBody.actors.last()
        if (lastCell !is BodyCell) return

        snakeBody.addActor(BodyCell(tailImage, lastCell.getX().toInt(), lastCell.getY().toInt(), Global.INT_BOX_SIZE, Global.INT_BOX_SIZE, false))

        increaseScore()
    }

    fun increaseScore() {
        Global.score++
        Global.scoreLabel.setText("Score: ${Global.score}")
    }


    fun newFood() {
        var markedCells = BoolMatrix(Global.INT_WORLD_WIDTH, Global.INT_WORLD_HEIGHT)

        var actors = snakeBody.actors
        for (i in actors.size-1 downTo  0) {
            var actor = actors[i]
            if (actor !is BodyCell)
                continue
            markedCells[actor.getX().toInt(), actor.getY().toInt()] = true
        }
        foodEntity.spawn(markedCells)
    }

    fun move() {
        var markedCells = BoolMatrix(Global.INT_WORLD_WIDTH, Global.INT_WORLD_HEIGHT)

        var actors = snakeBody.actors
        // нулевой индекс - голова, последний - хвост
        for (i in actors.size-1 downTo  0) {
            var actor = actors[i]
            if (actor !is BodyCell)
                continue
            if (actor.isHead) {
                actor.moveTo(Global.moveDirection)
                actor.keepInBound()
                if (actor.hasCollision(markedCells)) {
                    // game over
                    onDeath()
                }

                markedCells[actor.getX().toInt(), actor.getY().toInt()] = true
                if (!foodEntity.isActive) {
                    foodEntity.spawn(markedCells)
                    foodEntity.isActive = true
                }

                if (foodEntity.eaten(actor.getX(), actor.getY())) {
                    eat()
                    foodEntity.spawn(markedCells)
                }
            }
            else {
                val nextActor = actors[i-1]
                if (nextActor is BodyCell) {
                    actor.setBounds(nextActor.getX(), nextActor.getY(), nextActor.getWidth(), nextActor.getHeight())
                    actor.keepInBound()
                    // запоминание занятых клеток
                    markedCells[nextActor.getX().toInt(), nextActor.getY().toInt()] = true
                }
            }

        }
    }

    fun onDeath() {
        Global.GAME_STATE = GameState.GAMEOVER
        Global.TICK_DELAY = Global.SPEED_DOWN
        if (Global.bestScore < Global.score) {
            Global.isBestScore = true
            Global.bestScore = Global.score
        }
    }


    fun bodyRender() {
        draw()
    }

    fun draw() {
        snakeBody.draw()
        foodStage.draw()

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (Global.GAME_STATE == GameState.INGAME)
                Global.GAME_STATE = GameState.PAUSE
            else if (Global.GAME_STATE == GameState.PAUSE)
                Global.GAME_STATE = GameState.MENU
        }
    }



}







