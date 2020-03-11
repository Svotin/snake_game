package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.mygdx.game.Enums.GameState

class SoundManager {
    val ingameSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/ingame_loop.mp3"))
    val deathSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/death_sound.mp3"))
    val menuSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/inmenu_loop.mp3"))

    var currentSound: Music

    constructor() {
        currentSound = menuSound
        currentSound.isLooping = true
        currentSound.play()
    }

    fun execute() {
        if (!Global.VOLUME_ENABLED) {
            if (currentSound.isPlaying()) {
                currentSound.stop()
            }
        }

        if (Global.GAME_STATE == GameState.INGAME || Global.GAME_STATE == GameState.PAUSE) {
            if (currentSound != ingameSound || !currentSound.isPlaying) {
                currentSound.stop()
                currentSound = ingameSound
                currentSound.isLooping = true
                currentSound.play()
            }
        } else if (Global.GAME_STATE == GameState.GAMEOVER) {
            if (currentSound != deathSound) {
                currentSound.stop()
                currentSound = deathSound
                currentSound.isLooping = false
                currentSound.play()
            }
        } else if (Global.GAME_STATE == GameState.MENU) {
            if (currentSound != menuSound || !currentSound.isPlaying) {
                currentSound.stop()
                currentSound = menuSound
                currentSound.isLooping = true
                currentSound.play()
            }
        }
    }


    fun dispose() {
        deathSound.dispose()
        ingameSound.dispose()
        menuSound.dispose()
    }
}