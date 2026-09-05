package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TealPrimary

@Composable
fun CallAndGoLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 64.dp,
    showText: Boolean = true,
    animated: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_anim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (animated) 8f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Dynamic Call & Go Logo Mark
        Box(
            modifier = Modifier
                .size(iconSize)
                .offset(y = (-pulse).dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(iconSize)) {
                val w = size.width
                val h = size.height
                val center = Offset(w * 0.52f, h * 0.48f)
                val radius = w * 0.36f

                // 1. Left Speed Trail Lines
                val lineStroke = w * 0.035f
                drawLine(
                    color = OrangePrimary,
                    start = Offset(w * 0.05f, h * 0.42f),
                    end = Offset(w * 0.28f, h * 0.42f),
                    strokeWidth = lineStroke,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = OrangePrimary,
                    start = Offset(w * 0.02f, h * 0.50f),
                    end = Offset(w * 0.32f, h * 0.50f),
                    strokeWidth = lineStroke * 1.2f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = TealPrimary,
                    start = Offset(w * 0.08f, h * 0.58f),
                    end = Offset(w * 0.30f, h * 0.58f),
                    strokeWidth = lineStroke,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = TealPrimary,
                    start = Offset(w * 0.12f, h * 0.66f),
                    end = Offset(w * 0.28f, h * 0.66f),
                    strokeWidth = lineStroke,
                    cap = StrokeCap.Round
                )

                // 2. Main Outer Circular Arc (Navy Blue & Teal)
                val arcStroke = w * 0.14f
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(Navy800, TealPrimary, Navy900, TealPrimary)
                    ),
                    startAngle = 135f,
                    sweepAngle = 260f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = arcStroke, cap = StrokeCap.Round)
                )

                // 3. Sweeping Roadway Surface inside the lower curve
                val roadPath = Path().apply {
                    moveTo(center.x - radius * 0.6f, center.y + radius * 0.9f)
                    cubicTo(
                        center.x - radius * 0.2f, center.y + radius * 0.3f,
                        center.x + radius * 0.2f, center.y + radius * 0.1f,
                        center.x + radius * 0.9f, center.y + radius * 0.05f
                    )
                    lineTo(center.x + radius * 0.9f, center.y + radius * 0.45f)
                    cubicTo(
                        center.x + radius * 0.4f, center.y + radius * 0.4f,
                        center.x + radius * 0.1f, center.y + radius * 0.6f,
                        center.x - radius * 0.3f, center.y + radius * 1.0f
                    )
                    close()
                }
                drawPath(
                    path = roadPath,
                    brush = Brush.linearGradient(
                        colors = listOf(Navy900, TealPrimary)
                    )
                )

                // 4. Road Dashed Markings
                val roadLinePath = Path().apply {
                    moveTo(center.x - radius * 0.4f, center.y + radius * 0.85f)
                    cubicTo(
                        center.x - radius * 0.1f, center.y + radius * 0.4f,
                        center.x + radius * 0.2f, center.y + radius * 0.25f,
                        center.x + radius * 0.85f, center.y + radius * 0.2f
                    )
                }
                drawPath(
                    path = roadLinePath,
                    color = Color.White.copy(alpha = 0.95f),
                    style = Stroke(
                        width = w * 0.03f,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(w * 0.06f, w * 0.05f), 0f)
                    )
                )

                // 5. Orange Speed Wing (top right)
                val wingPath = Path().apply {
                    moveTo(center.x + radius * 0.2f, center.y - radius * 0.65f)
                    lineTo(center.x + radius * 0.85f, center.y - radius * 0.65f)
                    cubicTo(
                        center.x + radius * 0.95f, center.y - radius * 0.65f,
                        center.x + radius * 0.95f, center.y - radius * 0.25f,
                        center.x + radius * 0.8f, center.y - radius * 0.25f
                    )
                    lineTo(center.x + radius * 0.25f, center.y - radius * 0.25f)
                    close()
                }
                drawPath(wingPath, color = OrangePrimary)

                // 6. Orange Center Location Pin
                val pinCenter = Offset(center.x - radius * 0.08f, center.y - radius * 0.32f)
                val pinRadius = radius * 0.32f
                val pinPath = Path().apply {
                    moveTo(pinCenter.x, pinCenter.y + pinRadius * 1.5f)
                    cubicTo(
                        pinCenter.x - pinRadius * 1.1f, pinCenter.y + pinRadius * 0.6f,
                        pinCenter.x - pinRadius, pinCenter.y - pinRadius,
                        pinCenter.x, pinCenter.y - pinRadius
                    )
                    cubicTo(
                        pinCenter.x + pinRadius, pinCenter.y - pinRadius,
                        pinCenter.x + pinRadius * 1.1f, pinCenter.y + pinRadius * 0.6f,
                        pinCenter.x, pinCenter.y + pinRadius * 1.5f
                    )
                    close()
                }
                drawPath(pinPath, color = OrangePrimary)

                // Inner white dot in pin
                drawCircle(
                    color = Color.White,
                    radius = pinRadius * 0.36f,
                    center = Offset(pinCenter.x, pinCenter.y - pinRadius * 0.1f)
                )
            }
        }

        if (showText) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Call",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Navy900,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "and",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy700Color,
                    letterSpacing = (-0.2).sp,
                    modifier = Modifier.padding(horizontal = 1.dp)
                )
                Text(
                    text = "Go",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OrangePrimary,
                    letterSpacing = (-0.5).sp
                )
                // Small pin accent next to Go
                Box(
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(10.dp)
                        .background(OrangePrimary, CircleShape)
                )
            }
        }
    }
}

private val Navy700Color = Color(0xFF133E68)
