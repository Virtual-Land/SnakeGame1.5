package com.example.snakegame

import android.health.connect.datatypes.HeightRecord
import android.icu.text.ListFormatter.Width
import android.inputmethodservice.Keyboard.Row
import android.media.Image
import androidx.collection.intFloatMapOf
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.snakegame.ui.theme.Citrine
import com.example.snakegame.ui.theme.Custard
import com.example.snakegame.ui.theme.RoyalBlue

@Composable
fun SnakeGameScreen(
    state: SnakeGameState,
    onEvent: (SnakeGameEvent) -> Unit

) {

    val foodImageBitmap = ImageBitmap.imageResource(id= R.drawable.img_apple)
    val snakeHeadImageBitmap = when(state.direction) {
        Direction.Right -> ImageBitmap.imageResource(id= R.drawable.img_snake_head)
        Direction.Left -> ImageBitmap.imageResource(id= R.drawable.img_snake_head2)
        Direction.Up -> ImageBitmap.imageResource(id= R.drawable.img_snake_head3)
        Direction.Down -> ImageBitmap.imageResource(id= R.drawable.img_snake_head4)
    }
        ImageBitmap.imageResource(id= R.drawable.img_apple)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ){
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ){
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Score: ${state.snake.size - 1}",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 2 / 3f)
        ) {
            val cellSize = size.width / 20
            drawGameBoard(
                cellSize = cellSize,
                cellColor = Custard,
                borderCellColor = RoyalBlue,
                gridWidth = state.xAxisGridSize,
                gridHeight = state.yAxisGridSize

            )
            drawFood(
                foodImage = foodImageBitmap,
                cellSize = cellSize.toInt(),
                coordinate = state.food
            )
            drawSnake(
                snakeHeadImage = snakeHeadImageBitmap,
                cellSize = cellSize,
                snake = state.snake
            )
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {/*TODO*/})
            {
                Text(text = "replay")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
                modifier = Modifier.weight(1f),
                onClick = {/*TODO*/})
            {
                Text(text = "Start")
            }
        }
    }
}

private fun DrawScope.drawGameBoard(
    cellSize: Float,
    cellColor: Color,
    borderCellColor: Color,
    gridWidth: Int,
    gridHeight: Int
){
    for (i in 0 until gridWidth ) {
        for (j in 0 until gridHeight) {
            val isBorderCell = i == 0 || j == 0 || i == gridWidth - 1 || j == gridHeight - 1
            drawRect(
                color = if (isBorderCell) borderCellColor
                else if ((i + j) % 2 == 0) cellColor
                else cellColor.copy(alpha = 0.5f),
                topLeft = Offset(x = i * cellSize, y = j * cellSize),
                size = Size(cellSize, cellSize)

            )
        }
    }
}

private fun DrawScope.drawFood(
    foodImage: ImageBitmap,
    cellSize: Int,
    coordinate: Coordinate
){
    drawImage(
        image = foodImage,
        dstOffset = IntOffset(
            x = (coordinate.x * cellSize),
            y = (coordinate.y * cellSize),
        ),
        dstSize = IntSize(cellSize, cellSize)
    )
}

private fun DrawScope.drawSnake(
    snakeHeadImage: ImageBitmap,
    cellSize: Float,
    snake: List<Coordinate>
){
    val cellSizeInt = cellSize.toInt()
    snake.forEachIndexed { index, coordinate ->
        val radius = if (index == snake.lastIndex) cellSize / 2.5f else cellSize / 2
        if (index == 0) {
            drawImage(
                image = snakeHeadImage,
                dstOffset = IntOffset(
                    x = (coordinate.x * cellSizeInt),
                    y = (coordinate.y * cellSizeInt),
                ),
                dstSize = IntSize(cellSizeInt, cellSizeInt)
            )
        } else {
            drawCircle(
                color = Citrine,
                center = Offset(
                    x = (coordinate.x * cellSize) + radius,
                    y = (coordinate.y * cellSize) + radius,
                ),
                radius = radius
            )
        }
    }
}



