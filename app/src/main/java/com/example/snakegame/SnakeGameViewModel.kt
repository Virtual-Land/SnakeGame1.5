package com.example.snakegame

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.whileSelect

class SnakeGameViewModel :ViewModel()  {

    private val _state = MutableStateFlow(SnakeGameState())
    val state = _state.asStateFlow()


    fun onEvent(event: SnakeGameEvent) {
        when (event) {
            SnakeGameEvent.StartGame -> {
                    _state.update { it.copy(gameState = GameState.STARTED) }
                    viewModelScope.launch {
                        while (state.value.gameState == GameState.STARTED) {
                            val delayMillis = when(state.value.snake.size) {
                              in 1 .. 5 -> 120L
                              in 6 .. 10 -> 110L
                              else -> 100L
                            }
                            delay(delayMillis)
                            _state.value = updateGame(state.value)
                        }
                    }
                }
                SnakeGameEvent.PauseGame -> {
                    _state.update { it.copy(gameState = GameState.PAUSED) }
                }
                SnakeGameEvent.ResetGame -> {
                    _state.value = SnakeGameState()
                }
                is SnakeGameEvent.UpdateDirection -> {}
            }
        }

        private fun updateGame(currentGame: SnakeGameState): SnakeGameState {
            if (currentGame.isGameOver) {
                return currentGame
            }

            val head = currentGame.snake.first()
            val xAxisGridSize = currentGame.xAxisGridSize
            val yAxisGridSize = currentGame.yAxisGridSize

            // Update movement of snake
            val newHead = when (currentGame.direction) {
                Direction.Up -> {
                    Coordinate(x = head.x, y = (head.y - 1))
                }

                Direction.Down -> {
                    Coordinate(x = head.x, y = (head.y - 1))
                }

                Direction.Left -> {
                    Coordinate(x = head.x - 1, y = (head.y - 1))
                }

                Direction.Right -> {
                    Coordinate(x = head.x + 1, y = (head.y - 1))
                }
            }

            //Check if the snake collides with itself or goes out of bounds
            if (
                currentGame.snake.contains(newHead) ||
                !isWithinBounds(newHead, xAxisGridSize, yAxisGridSize)
            ) {
                return currentGame.copy(isGameOver = true)
            }

            //Check if the snake eats the food
            var newSnake = mutableListOf(newHead) + currentGame.snake
            val newFood =
                if (newHead == currentGame.food) SnakeGameState.generateRandomFoodCoordinate()
                else currentGame.food

            //Update snake length
            if (newHead != currentGame.food) {
                newSnake = newSnake.toMutableList()
                newSnake.removeAt(newSnake.size - 1)
            }

            return currentGame.copy(snake = newSnake, food = newFood)
        }

        private fun isWithinBounds(
            coordinate: Coordinate,
            xAxisGridSize: Int,
            yAxisGridSize: Int
        ): Boolean {
            return coordinate.x in 1 until xAxisGridSize - 1
                    && coordinate.y in 1 until yAxisGridSize - 1
        }
    }