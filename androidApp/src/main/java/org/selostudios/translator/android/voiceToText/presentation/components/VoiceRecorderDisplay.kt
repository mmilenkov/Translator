package org.selostudios.translator.android.voiceToText.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.selostudios.translator.android.TranslatorTheme
import org.selostudios.translator.android.translation.presentation.componenets.gradientSurface
import kotlin.random.Random

@Composable
fun VoiceRecorderDisplay(
    powerRatios: List<Float>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colors.primary
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Just stop talking to get the result",
            color = MaterialTheme.colors.primary
        )
        Box(
            modifier = modifier
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
                .gradientSurface()
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .drawBehind {
                    val powerRatioWidth = 3.dp.toPx()
                    val powerRatioCount = (size.width / (2 * powerRatioWidth)).toInt()

                    clipRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height
                    ) {
                        powerRatios
                            .takeLast(powerRatioCount)
                            .reversed()
                            .forEachIndexed { index, ratio ->
                                val yTopStart = center.y - (size.height / 2f) * ratio
                                drawRoundRect(
                                    color = primaryColor,
                                    topLeft = Offset(
                                        x = size.width - index * 2 * powerRatioWidth,
                                        y = yTopStart
                                    ),
                                    size = Size(
                                        width = powerRatioWidth,
                                        height = (center.y - yTopStart) * 2f
                                    ),
                                    cornerRadius = CornerRadius(100f)
                                )
                            }
                    }
                }
        )
    }
}

@Composable
@Preview
fun VoiceRecorderPreview() {
    TranslatorTheme {
        VoiceRecorderDisplay(
            powerRatios = (0..100).map {
            Random.nextFloat()
        }.toList(),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp))
    }
}