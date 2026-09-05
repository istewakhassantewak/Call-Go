package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocationPoint
import com.example.data.model.Ride
import com.example.data.model.RideStatus
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealAccent
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealPrimary

@Composable
fun InteractiveMap(
    pickupLocation: LocationPoint,
    destinationLocation: LocationPoint?,
    activeRide: Ride?,
    modifier: Modifier = Modifier,
    isDriverMode: Boolean = false,
    onCenterLocation: () -> Unit = {}
) {
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    // Pulsing GPS halo
    val infiniteTransition = rememberInfiniteTransition(label = "map_radar")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 36f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radar"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F6F9))
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    zoomScale = (zoomScale * zoom).coerceIn(0.6f, 2.5f)
                    panOffsetX += pan.x
                    panOffsetY += pan.y
                }
            }
    ) {
        // Map Surface rendering
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val centerX = w / 2f + panOffsetX
            val centerY = h / 2f + panOffsetY

            // 1. Draw Bangladesh Waterways (Buriganga / Turag / Gulshan Lake)
            val lakeBrush = Brush.linearGradient(
                colors = listOf(Color(0xFFD6EAF8), Color(0xFFCCE4F5))
            )
            val lakePath = Path().apply {
                moveTo(w * 0.1f + panOffsetX, 0f)
                cubicTo(
                    w * 0.25f + panOffsetX, h * 0.3f + panOffsetY,
                    w * 0.15f + panOffsetX, h * 0.7f + panOffsetY,
                    w * 0.3f + panOffsetX, h
                )
                lineTo(w * 0.38f + panOffsetX, h)
                cubicTo(
                    w * 0.22f + panOffsetX, h * 0.7f + panOffsetY,
                    w * 0.32f + panOffsetX, h * 0.3f + panOffsetY,
                    w * 0.18f + panOffsetX, 0f
                )
                close()
            }
            drawPath(lakePath, brush = lakeBrush)

            // 2. Draw Major Dhaka Road Network (Mirpur Rd, Airport Rd, Gulshan Ave, Pragati Sarani)
            val roadColor = Color.White
            val roadBorder = Color(0xFFE2E8F0)

            // Grid background roads
            for (i in -4..5) {
                val y = centerY + i * 110f * zoomScale
                drawLine(
                    color = roadBorder,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 14f * zoomScale
                )
                drawLine(
                    color = roadColor,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 10f * zoomScale
                )

                val x = centerX + i * 110f * zoomScale
                drawLine(
                    color = roadBorder,
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 14f * zoomScale
                )
                drawLine(
                    color = roadColor,
                    start = Offset(x, 0f),
                    end = Offset(x, h),
                    strokeWidth = 10f * zoomScale
                )
            }

            // Diagonal Expressways (Dhaka Elevated Expressway style)
            drawLine(
                color = Color(0xFFFFECC8),
                start = Offset(0f, centerY - 180f * zoomScale),
                end = Offset(w, centerY + 240f * zoomScale),
                strokeWidth = 18f * zoomScale,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0xFFFDBA74),
                start = Offset(0f, centerY - 180f * zoomScale),
                end = Offset(w, centerY + 240f * zoomScale),
                strokeWidth = 12f * zoomScale,
                cap = StrokeCap.Round
            )

            // Coordinates mapping to canvas points
            val pickupPt = Offset(centerX - 80f * zoomScale, centerY + 60f * zoomScale)
            val destPt = if (destinationLocation != null) {
                Offset(centerX + 120f * zoomScale, centerY - 140f * zoomScale)
            } else null

            // 3. Draw Route Polyline if destination selected
            if (destPt != null) {
                val routePath = Path().apply {
                    moveTo(pickupPt.x, pickupPt.y)
                    // Road waypoints
                    val midPt1 = Offset(pickupPt.x + 40f * zoomScale, pickupPt.y - 80f * zoomScale)
                    val midPt2 = Offset(destPt.x - 30f * zoomScale, midPt1.y)
                    lineTo(midPt1.x, midPt1.y)
                    lineTo(midPt2.x, midPt2.y)
                    lineTo(destPt.x, destPt.y)
                }

                // Shadow line
                drawPath(
                    path = routePath,
                    color = TealDark.copy(alpha = 0.25f),
                    style = Stroke(
                        width = 14f * zoomScale,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                // Active route gradient
                drawPath(
                    path = routePath,
                    brush = Brush.linearGradient(
                        colors = listOf(TealPrimary, OrangePrimary)
                    ),
                    style = Stroke(
                        width = 8f * zoomScale,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                // Pulsing dotted centerline
                drawPath(
                    path = routePath,
                    color = Color.White.copy(alpha = 0.8f),
                    style = Stroke(
                        width = 2.5f * zoomScale,
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 16f), 0f)
                    )
                )

                // Destination Pin Marker (Orange)
                drawCircle(
                    color = OrangePrimary.copy(alpha = 0.3f),
                    radius = 24f * zoomScale,
                    center = destPt
                )
                drawCircle(
                    color = OrangePrimary,
                    radius = 12f * zoomScale,
                    center = destPt
                )
                drawCircle(
                    color = Color.White,
                    radius = 5f * zoomScale,
                    center = destPt
                )
            }

            // 4. Pickup Location Marker (Navy/Teal with radar pulse)
            drawCircle(
                color = TealPrimary.copy(alpha = pulseAlpha),
                radius = pulseRadius * zoomScale,
                center = pickupPt
            )
            drawCircle(
                color = TealPrimary,
                radius = 11f * zoomScale,
                center = pickupPt
            )
            drawCircle(
                color = Color.White,
                radius = 5f * zoomScale,
                center = pickupPt
            )

            // 5. Active Driver Marker
            val driverPt = if (activeRide != null && activeRide.status != RideStatus.IDLE) {
                // Interpolate driver between positions
                when (activeRide.status) {
                    RideStatus.DRIVER_ASSIGNED, RideStatus.DRIVER_ARRIVING -> {
                        Offset(
                            pickupPt.x - 70f * zoomScale + (panOffsetX % 20),
                            pickupPt.y - 80f * zoomScale + (panOffsetY % 20)
                        )
                    }
                    RideStatus.DRIVER_ARRIVED -> pickupPt
                    RideStatus.TRIP_IN_PROGRESS -> {
                        if (destPt != null) {
                            Offset(
                                (pickupPt.x + destPt.x) / 2f,
                                (pickupPt.y + destPt.y) / 2f
                            )
                        } else pickupPt
                    }
                    else -> pickupPt
                }
            } else {
                // Roaming drivers nearby in Dhaka
                Offset(centerX + 60f * zoomScale, centerY + 30f * zoomScale)
            }

            drawDriverVehicleMarker(
                point = driverPt,
                zoom = zoomScale,
                isAssigned = activeRide?.driver != null
            )

            // Extra roaming drivers on Dhaka streets
            drawDriverVehicleMarker(
                point = Offset(centerX - 120f * zoomScale, centerY - 60f * zoomScale),
                zoom = zoomScale,
                isAssigned = false,
                color = Slate700
            )
            drawDriverVehicleMarker(
                point = Offset(centerX + 140f * zoomScale, centerY + 110f * zoomScale),
                zoom = zoomScale,
                isAssigned = false,
                color = Slate700
            )
        }

        // Top Floating City Label (Bangladesh Hub)
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .shadow(4.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = Navy900.copy(alpha = 0.92f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Dhaka City Live Map • বাংলাদেশ",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Right Floating Map Action Controls (Zoom, Re-center, Navigation)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FloatingActionButton(
                onClick = { zoomScale = (zoomScale + 0.2f).coerceAtMost(2.5f) },
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = Navy800,
                elevation = FloatingActionButtonDefaults.elevation(3.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Zoom In", modifier = Modifier.size(20.dp))
            }

            FloatingActionButton(
                onClick = { zoomScale = (zoomScale - 0.2f).coerceAtLeast(0.6f) },
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = Navy800,
                elevation = FloatingActionButtonDefaults.elevation(3.dp)
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Zoom Out", modifier = Modifier.size(20.dp))
            }

            FloatingActionButton(
                onClick = {
                    panOffsetX = 0f
                    panOffsetY = 0f
                    zoomScale = 1.0f
                    onCenterLocation()
                },
                modifier = Modifier.size(46.dp),
                shape = CircleShape,
                containerColor = if (isDriverMode) OrangePrimary else TealPrimary,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(imageVector = Icons.Default.MyLocation, contentDescription = "My Location", modifier = Modifier.size(22.dp))
            }
        }
    }
}

private fun DrawScope.drawDriverVehicleMarker(
    point: Offset,
    zoom: Float,
    isAssigned: Boolean = false,
    color: Color = Navy800
) {
    val size = 20f * zoom
    // Halo shadow
    drawCircle(
        color = if (isAssigned) OrangePrimary.copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.15f),
        radius = size * 1.5f,
        center = point
    )

    // Vehicle circle badge
    drawCircle(
        color = if (isAssigned) OrangePrimary else color,
        radius = size,
        center = point
    )

    // Inner vehicle arrow/dot
    drawCircle(
        color = Color.White,
        radius = size * 0.45f,
        center = point
    )

    if (isAssigned) {
        // Pulse ring around assigned driver
        drawCircle(
            color = OrangePrimary,
            radius = size * 1.25f,
            center = point,
            style = Stroke(width = 2.5f * zoom)
        )
    }
}
