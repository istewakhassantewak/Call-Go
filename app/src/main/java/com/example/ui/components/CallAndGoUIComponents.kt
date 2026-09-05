package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RideStatus
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.OrangeLight
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import com.example.ui.theme.WarningAmber

@Composable
fun CallGoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSecondary: Boolean = false,
    isDanger: Boolean = false,
    leadingIcon: ImageVector? = null
) {
    val bgColor = when {
        isDanger -> ErrorRed
        isSecondary -> OrangePrimary
        else -> Navy800
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = Color.White,
            disabledContainerColor = Slate200,
            disabledContentColor = Slate400
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun CallGoOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = TealPrimary,
    leadingIcon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.5.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = borderColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = borderColor
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = borderColor
            )
        }
    }
}

@Composable
fun BangladeshPhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Mobile Number (বাংলাদেশ)",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val filtered = input.filter { it.isDigit() || it == '+' }.take(14)
            onValueChange(filtered)
        },
        modifier = modifier.fillMaxWidth(),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = Slate900,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        ),
        label = { Text(label, fontSize = 13.sp, color = Slate700) },
        placeholder = { Text("017XXXXXXXX", fontSize = 14.sp, color = Slate400) },
        leadingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 12.dp, end = 6.dp)
            ) {
                // Bangladesh Flag Green and Red circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF006A4E)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF42A41))
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "+880",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Slate900
                )
            }
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Slate100)
                        .clickable { onValueChange("") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Slate900,
            unfocusedTextColor = Slate900,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = OrangePrimary,
            focusedBorderColor = Navy800,
            unfocusedBorderColor = Slate400,
            focusedLabelColor = Navy800,
            unfocusedLabelColor = Slate600
        ),
        singleLine = true
    )
}

@Composable
fun CallGoCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = Color.White,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, Slate100),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun StatusBadge(status: RideStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (status) {
        RideStatus.IDLE -> Triple(Slate100, Slate700, "Ready")
        RideStatus.SEARCHING_DRIVER -> Triple(OrangeLight, OrangePrimary, "Searching Driver")
        RideStatus.DRIVER_ASSIGNED -> Triple(TealLight, TealPrimary, "Assigned")
        RideStatus.DRIVER_ARRIVING -> Triple(TealLight, TealPrimary, "Arriving")
        RideStatus.DRIVER_ARRIVED -> Triple(Color(0xFFD1FAE5), SuccessGreen, "Arrived at Pickup")
        RideStatus.TRIP_IN_PROGRESS -> Triple(Color(0xFFDBEAFE), Navy800, "On the Way")
        RideStatus.TRIP_COMPLETED -> Triple(Color(0xFFD1FAE5), SuccessGreen, "Completed")
        RideStatus.CANCELLED -> Triple(Color(0xFFFEE2E2), ErrorRed, "Cancelled")
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun BdtPriceText(
    amount: Double,
    fontSize: Int = 18,
    color: Color = Navy900,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "৳",
            fontSize = (fontSize - 2).sp,
            fontWeight = FontWeight.Bold,
            color = OrangePrimary,
            modifier = Modifier.padding(end = 2.dp)
        )
        Text(
            text = "%,d".format(Math.round(amount)),
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
            color = color
        )
    }
}

@Composable
fun RatingStarsRow(rating: Double, reviewCount: Int = 0) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = WarningAmber,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "%.1f".format(rating),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Slate900
        )
        if (reviewCount > 0) {
            Text(
                text = " ($reviewCount)",
                fontSize = 12.sp,
                color = Slate400
            )
        }
    }
}
