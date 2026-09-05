package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShareLocation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate900

@Composable
fun SosEmergencyDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    emergencyContact: String,
    rideId: String? = null,
    currentLocationName: String = "Gulshan, Dhaka",
    language: Language = Language.BANGLA
) {
    if (!isOpen) return
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (language == Language.BANGLA) "বন্ধ করুন" else "Close",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ErrorRed.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (language == Language.BANGLA) "জরুরি এসওএস (SOS)" else "Emergency SOS Alert",
                    fontWeight = FontWeight.ExtraBold,
                    color = ErrorRed,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (language == Language.BANGLA)
                        "আপনার নিরাপত্তাই আমাদের সর্বোচ্চ অগ্রাধিকার। নিচে যেকোনো জরুরি নাম্বারে সরাসরি যোগাযোগ করুন বা তাৎক্ষণিক লাইভ অবস্থান শেয়ার করুন।"
                    else
                        "Your safety is our top priority. Contact national emergency services directly or broadcast your live ride coordinates.",
                    fontSize = 13.sp,
                    color = Slate600,
                    lineHeight = 18.sp
                )

                // 1. National 999 Hotline
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:999"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.LocalPolice, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.BANGLA) "৯৯৯ এ কল করুন (জাতীয় জরুরি সেবা)" else "Call 999 (National Emergency)",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                // 2. Call & Go 24/7 Security Helpline
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+8809612345678"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Navy900),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.BANGLA) "কল অ্যান্ড গো ২৪/৭ হেল্পলাইন" else "Call & Go 24/7 Safety Desk",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                // 3. Share Live Location SMS to emergency contact
                Button(
                    onClick = {
                        val message = "EMERGENCY: I am on Call & Go ride #$rideId at $currentLocationName. Please track my journey immediately."
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$emergencyContact")).apply {
                            putExtra("sms_body", message)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.ShareLocation, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.BANGLA) "পরিবারকে লাইভ ট্র্যাকিং লিংক পাঠান" else "Alert Emergency Contact ($emergencyContact)",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(18.dp),
        containerColor = Color.White
    )
}
