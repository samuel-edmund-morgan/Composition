package com.example.composition.domain.repository

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question

public interface GameRepository {
    public fun generateQuestion(
        maxSumValue: Int,
        countOfOptions: Int
    ): Question
    public fun getGameSettings(level: Level) : GameSettings
}