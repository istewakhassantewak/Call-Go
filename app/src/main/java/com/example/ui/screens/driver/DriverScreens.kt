package com.example.ui.screens.driver

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.Ride
import com.example.data.model.RideStatus
import com.example.data.model.UserRole
import com.example.data.repository.CallAndGoRepository
import com.example.ui.components.BdtPriceText
import com.example.ui.components.CallAndGoLogo
import com.example.ui.components.CallGoButton
import com.example.ui.components.CallGoCard
import com.example.ui.components.CallGoOutlinedButton
import com.example.ui.components.InteractiveMap
import com.example.ui.components.RatingStarsRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BkashPink
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.NagadOrange
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
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay

@Composable
fun DriverHomeScreen(
    repository: CallAndGoRepository,
    onNavigateToEarnings: () -> Unit,
    onNavigateToKyc: () -> Unit,
    onSwitchRole: (UserRole) -> Unit
) {
    val language by repository.currentLanguage.collectAsState()
    val driver by repository.driverProfile.collectAsState()
    val incomingRequest by repository.incomingDriverRequest.collectAsState()
    val activeRide by repository.activeRide.collectAsState()
    val pickup by repository.pickupLocation.collectAsState()
    val destination by repository.destinationLocation.collectAsState()

    var showOtpEntryDialog by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }
    var showTollDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Driver Live Dhaka Route Map
        InteractiveMap(
            pickupLocation = pickup,
            destinationLocation = destination,
            activeRide = activeRide,
            isDriverMode = true,
            modifier = Modifier.fillMaxSize()
        )

        // Top Driver Status Bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f)),
            border = BorderStroke(1.dp, Slate200),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CallAndGoLogo(iconSize = 32.dp, showText = false)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = driver.name.take(14),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Navy900
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "BRTA Verified",
                                    tint = TealPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = driver.vehiclePlate,
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }
                    }

                    // Online / Offline Switch
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (driver.isOnline) {
                                if (language == Language.BANGLA) "অনলাইন" else "ONLINE"
                            } else {
                                if (language == Language.BANGLA) "অফলাইন" else "OFFLINE"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (driver.isOnline) SuccessGreen else Slate400,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Switch(
                            checked = driver.isOnline,
                            onCheckedChange = { repository.toggleDriverOnline() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SuccessGreen
                            )
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate100)

                // Quick Navigation items: Earnings, KYC, Passenger Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onNavigateToEarnings() }
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        BdtPriceText(amount = driver.walletBalanceBDT, fontSize = 14)
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = TealLight,
                        modifier = Modifier.clickable { onNavigateToKyc() }
                    ) {
                        Text(
                            text = if (language == Language.BANGLA) "বিআরটিএ কেওয়াইসি" else "BRTA KYC",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Navy100Color,
                        modifier = Modifier.clickable { onSwitchRole(UserRole.RIDER) }
                    ) {
                        Text(
                            text = if (language == Language.BANGLA) "যাত্রী মোড" else "Rider Mode",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Active Driver Ride Management Panel
        if (activeRide != null && activeRide!!.status != RideStatus.IDLE && activeRide!!.status != RideStatus.TRIP_COMPLETED) {
            DriverActiveTripCard(
                ride = activeRide!!,
                language = language,
                onArrivedAtPickup = {
                    showOtpEntryDialog = true
                },
                onCompleteTrip = {
                    repository.completeTrip()
                },
                onOpenTollDialog = {
                    showTollDialog = true
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else {
            // Idle Driver Waiting Widget
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (driver.isOnline) {
                            if (language == Language.BANGLA) "নতুন রাইডের জন্য অপেক্ষা করা হচ্ছে..." else "Radar Active: Searching for Passengers in Dhaka"
                        } else {
                            if (language == Language.BANGLA) "আপনি অফলাইনে আছেন। রাইড পেতে চালু করুন।" else "You are offline. Toggle Online above to accept rides."
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (driver.isOnline) TealPrimary else Slate600,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Incoming Ride Request Modal Dialog (15 seconds countdown)
        if (incomingRequest != null) {
            IncomingRideRequestDialog(
                request = incomingRequest!!,
                language = language,
                onAccept = { repository.acceptDriverIncomingRequest() },
                onDecline = { repository.declineDriverIncomingRequest() }
            )
        }

        // Passenger OTP Verification Dialog (Security Check)
        if (showOtpEntryDialog) {
            AlertDialog(
                onDismissRequest = { showOtpEntryDialog = false },
                title = {
                    Text(
                        text = if (language == Language.BANGLA) "যাত্রী ওটিপি (OTP) যাচাই" else "Verify Passenger Ride OTP",
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                },
                text = {
                    Column {
                        Text(
                            text = if (language == Language.BANGLA)
                                "যাত্রীর মোবাইল স্ক্রিনে প্রদর্শিত ৪ সংখ্যার ওটিপি কোড প্রবেশ করান:"
                            else
                                "Enter the 4-digit Ride Start OTP shown on passenger's screen:",
                            fontSize = 13.sp,
                            color = Slate600
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { otpInput = it },
                            placeholder = { Text("4-digit OTP (e.g. ${activeRide?.startOtp})", color = Slate400) },
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Slate900,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Slate900,
                                unfocusedTextColor = Slate900,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = OrangePrimary,
                                focusedBorderColor = Navy800,
                                unfocusedBorderColor = Slate400
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (otpError != null) {
                            Text(
                                text = otpError!!,
                                color = ErrorRed,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val success = repository.startTripWithOtp(otpInput)
                            if (success) {
                                showOtpEntryDialog = false
                                otpError = null
                            } else {
                                otpError = if (language == Language.BANGLA) "ভুল ওটিপি কোড! আবার চেষ্টা করুন।" else "Invalid OTP! Ask passenger for code."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                    ) {
                        Text(if (language == Language.BANGLA) "যাচাই ও ট্রিপ শুরু" else "Verify & Start Trip")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showOtpEntryDialog = false }) {
                        Text(if (language == Language.BANGLA) "বাতিল" else "Cancel")
                    }
                }
            )
        }

        // Toll & Extra Charge Dialog
        if (showTollDialog) {
            DriverTollFeeDialog(
                language = language,
                onDismiss = { showTollDialog = false },
                onAddToll = { fee ->
                    repository.addTollFeeToActiveRide(fee)
                    showTollDialog = false
                }
            )
        }
    }
}

@Composable
fun DriverActiveTripCard(
    ride: Ride,
    language: Language,
    onArrivedAtPickup: () -> Unit,
    onCompleteTrip: () -> Unit,
    onOpenTollDialog: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = ride.status)
                BdtPriceText(amount = ride.totalFareBDT, fontSize = 18)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Passenger Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Navy800),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ride.riderName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Navy900
                    )
                    Text(
                        text = "Payment: ${ride.paymentMethod.nameEn} • Distance: ${ride.estimatedDistanceKm} km",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Destination target
            Text(
                text = if (ride.status == RideStatus.TRIP_IN_PROGRESS) "Heading to: ${ride.destination.name}" else "Pickup at: ${ride.pickup.name}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = OrangePrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Stage button
            when (ride.status) {
                RideStatus.DRIVER_ASSIGNED, RideStatus.DRIVER_ARRIVING -> {
                    CallGoButton(
                        text = if (language == Language.BANGLA) "পিকআপে পৌঁছেছি (Arrived)" else "Mark Arrived at Pickup",
                        onClick = onArrivedAtPickup,
                        isSecondary = false
                    )
                }
                RideStatus.DRIVER_ARRIVED -> {
                    CallGoButton(
                        text = if (language == Language.BANGLA) "ওটিপি যাচাই করে ট্রিপ শুরু করুন" else "Enter Passenger OTP to Start Trip",
                        onClick = onArrivedAtPickup,
                        isSecondary = true
                    )
                }
                RideStatus.TRIP_IN_PROGRESS -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CallGoOutlinedButton(
                            text = if (language == Language.BANGLA) "+ টোল/ফি" else "+ Toll / Fee",
                            onClick = onOpenTollDialog,
                            borderColor = OrangePrimary,
                            modifier = Modifier.weight(1f)
                        )
                        CallGoButton(
                            text = if (language == Language.BANGLA) "ট্রিপ সমাপ্ত" else "Complete Trip",
                            onClick = onCompleteTrip,
                            isSecondary = false,
                            modifier = Modifier.weight(1.3f)
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun DriverTollFeeDialog(
    language: Language,
    onDismiss: () -> Unit,
    onAddToll: (Double) -> Unit
) {
    var customFee by remember { mutableStateOf("") }
    val tollPresets = listOf(
        Pair("Mayor Hanif Flyover (মেয়র হানিফ)", 50.0),
        Pair("Dhaka Elevated Expressway (এক্সপ্রেসওয়ে)", 80.0),
        Pair("Padma Bridge Toll (পদ্মা সেতু)", 100.0),
        Pair("Airport Parking / Toll (বিমানবন্দর)", 80.0)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (language == Language.BANGLA) "টোল অথবা অতিরিক্ত ফি যোগ করুন" else "Add Toll / Parking Fee",
                fontWeight = FontWeight.Bold,
                color = Navy900,
                fontSize = 17.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == Language.BANGLA)
                        "ফ্লাইওভার টোল বা পার্কিং ফি সরাসরি ট্রিপ ভাড়ার সাথে যুক্ত হবে:"
                    else
                        "Select official toll or enter custom extra charge to add to passenger's fare:",
                    fontSize = 12.sp,
                    color = Slate600
                )

                tollPresets.forEach { preset ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate100,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAddToll(preset.second) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = preset.first, fontSize = 12.sp, color = Navy900, fontWeight = FontWeight.Medium)
                            Text(text = "+৳${preset.second.toInt()}", fontSize = 13.sp, color = OrangePrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                OutlinedTextField(
                    value = customFee,
                    onValueChange = { customFee = it },
                    label = { Text("Custom Amount (অন্যান্য ৳)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = customFee.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        onAddToll(amount)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
            ) {
                Text(if (language == Language.BANGLA) "যোগ করুন" else "Apply Toll")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == Language.BANGLA) "বাতিল" else "Cancel", color = Slate600)
            }
        }
    )
}

@Composable
fun IncomingRideRequestDialog(
    request: Ride,
    language: Language,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    var secondsLeft by remember { mutableIntStateOf(15) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
        if (secondsLeft == 0) {
            onDecline()
        }
    }

    AlertDialog(
        onDismissRequest = onDecline,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == Language.BANGLA) "নতুন রাইড রিকুয়েস্ট!" else "Incoming Ride Request!",
                    fontWeight = FontWeight.ExtraBold,
                    color = OrangePrimary,
                    fontSize = 18.sp
                )
                Surface(
                    shape = CircleShape,
                    color = OrangeLight
                ) {
                    Text(
                        text = "${secondsLeft}s",
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangePrimary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = request.vehicleCategory.nameEn, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                    BdtPriceText(amount = request.totalFareBDT, fontSize = 20)
                }
                HorizontalDivider(color = Slate200)
                Text(
                    text = "Pickup: ${request.pickup.name}",
                    fontSize = 13.sp,
                    color = Slate700,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Drop-off: ${request.destination.name}",
                    fontSize = 13.sp,
                    color = OrangePrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Distance: ${request.estimatedDistanceKm} km • Est. Time: ${request.estimatedMinutes} mins",
                    fontSize = 12.sp,
                    color = Slate400
                )
                Text(
                    text = "Passenger: ${request.riderName}",
                    fontSize = 12.sp,
                    color = Slate600
                )
            }
        },
        confirmButton = {
            CallGoButton(
                text = if (language == Language.BANGLA) "রাইড গ্রহণ করুন (${secondsLeft}s)" else "Accept Ride (${secondsLeft}s)",
                onClick = onAccept,
                isSecondary = true
            )
        },
        dismissButton = {
            TextButton(onClick = onDecline) {
                Text(if (language == Language.BANGLA) "প্রত্যাখ্যান" else "Decline", color = Slate600)
            }
        }
    )
}

@Composable
fun DriverEarningsScreen(
    repository: CallAndGoRepository,
    onBack: () -> Unit
) {
    val driver by repository.driverProfile.collectAsState()
    val language by repository.currentLanguage.collectAsState()
    val adminConfig by repository.adminConfig.collectAsState()

    var selectedPeriod by remember { mutableIntStateOf(0) }
    var showWithdrawModal by remember { mutableStateOf(false) }
    var withdrawSuccessMessage by remember { mutableStateOf<String?>(null) }

    val periods = listOf(
        if (language == Language.BANGLA) "আজ (Today)" else "Today",
        if (language == Language.BANGLA) "এই সপ্তাহ" else "This Week",
        if (language == Language.BANGLA) "এই মাস" else "This Month"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Navy900)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.BANGLA) "ড্রাইভার আয় ও ওয়ালেট" else "Driver Wallet & Earnings",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy900
            )
        }

        // Success notification banner if withdrawal was requested
        AnimatedVisibility(visible = withdrawSuccessMessage != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFD1FAE5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = withdrawSuccessMessage ?: "",
                        color = SuccessGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Period Selection Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEachIndexed { index, period ->
                val isSelected = selectedPeriod == index
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) Navy900 else Slate100,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedPeriod = index }
                ) {
                    Text(
                        text = period,
                        color = if (isSelected) Color.White else Slate700,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Wallet Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = if (language == Language.BANGLA) "উত্তোলনযোগ্য ব্যালেন্স" else "Withdrawable Balance",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                BdtPriceText(
                    amount = driver.walletBalanceBDT,
                    fontSize = 32,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                CallGoButton(
                    text = if (language == Language.BANGLA) "বিকাশ / নগদ / ব্যাংকে টাকা তুলুন" else "Withdraw to bKash / Nagad / Bank",
                    onClick = { showWithdrawModal = true },
                    isSecondary = true
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cash vs Digital Payment Breakdown Card
        CallGoCard {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (language == Language.BANGLA) "পেমেন্ট মাধ্যম অনুযায়ী আয়" else "Cash vs Digital Payment Breakdown",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Navy900
                )

                val cashAmount = when (selectedPeriod) {
                    0 -> 650.0
                    1 -> 4200.0
                    else -> 16800.0
                }
                val digitalAmount = when (selectedPeriod) {
                    0 -> 1200.0
                    1 -> 7800.0
                    else -> 28500.0
                }
                val commissionDeduction = (cashAmount + digitalAmount) * (adminConfig.platformCommissionPercent / 100.0)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "• Cash In Hand (ক্যাশ আদায়)", fontSize = 13.sp, color = Slate700)
                    Text(text = "৳ ${cashAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "• Digital (bKash / Nagad / Card)", fontSize = 13.sp, color = Slate700)
                    Text(text = "৳ ${digitalAmount.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TealDark)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "• Platform Commission (${adminConfig.platformCommissionPercent}%)", fontSize = 13.sp, color = Slate600)
                    Text(text = "-৳ ${commissionDeduction.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
                }

                HorizontalDivider(color = Slate100)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Net Driver Income (মোট নিট আয়)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Navy900)
                    Text(text = "৳ ${(cashAmount + digitalAmount - commissionDeduction).toInt()}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = OrangePrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Performance Statistics
        Text(
            text = if (language == Language.BANGLA) "পারফরম্যান্স সারাংশ" else "Performance Stats",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Slate700
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                title = if (language == Language.BANGLA) "মোট ট্রিপ" else "Total Trips",
                value = "${driver.totalTrips}",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = if (language == Language.BANGLA) "রেটিং" else "Rating",
                value = "★ ${driver.rating}",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = if (language == Language.BANGLA) "কমিশন" else "Commission",
                value = "${adminConfig.platformCommissionPercent}%",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (language == Language.BANGLA) "বাংলাদেশ পেমেন্ট পার্টনার" else "Disbursement Partners in Bangladesh",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Slate700
        )
        Spacer(modifier = Modifier.height(8.dp))

        CallGoCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "• Instant MFS Payout: bKash & Nagad (0% transfer charge)", fontSize = 12.sp, color = Slate600)
                Text(text = "• Commercial Banks: City Bank, Brac Bank, Dutch-Bangla BEFTN", fontSize = 12.sp, color = Slate600)
                Text(text = "• BRTA Safety Insurance Coverage included on every trip", fontSize = 12.sp, color = Slate600)
            }
        }

        // Comprehensive Driver Withdrawal Modal Dialog
        if (showWithdrawModal) {
            var selectedGateway by remember { mutableStateOf("bKash") }
            var accountNumber by remember { mutableStateOf(driver.phone) }
            var withdrawAmount by remember { mutableStateOf("${driver.walletBalanceBDT.toInt().coerceAtLeast(100)}") }
            var withdrawError by remember { mutableStateOf<String?>(null) }

            AlertDialog(
                onDismissRequest = { showWithdrawModal = false },
                title = {
                    Text(
                        text = if (language == Language.BANGLA) "টাকা উত্তোলন অনুরোধ (Payout)" else "Request Driver Payout",
                        fontWeight = FontWeight.Bold,
                        color = Navy900,
                        fontSize = 17.sp
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Withdrawable: ৳${driver.walletBalanceBDT.toInt()}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangePrimary
                        )

                        // Gateway selector pills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("bKash", "Nagad", "Bank").forEach { gw ->
                                val isSelected = selectedGateway == gw
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) {
                                        when (gw) {
                                            "bKash" -> BkashPink
                                            "Nagad" -> NagadOrange
                                            else -> Navy900
                                        }
                                    } else Slate100,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedGateway = gw }
                                ) {
                                    Text(
                                        text = gw,
                                        color = if (isSelected) Color.White else Slate700,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        // Account Input
                        OutlinedTextField(
                            value = accountNumber,
                            onValueChange = { accountNumber = it },
                            label = { Text("MFS Mobile / Bank A/C") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Amount Input
                        OutlinedTextField(
                            value = withdrawAmount,
                            onValueChange = { withdrawAmount = it },
                            label = { Text("Amount (BDT ৳)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Quick amount chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("500", "1000", "${driver.walletBalanceBDT.toInt()}").forEach { chip ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Slate100,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { withdrawAmount = chip }
                                ) {
                                    Text(
                                        text = "৳$chip",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate900,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        if (withdrawError != null) {
                            Text(text = withdrawError!!, color = ErrorRed, fontSize = 11.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amt = withdrawAmount.toDoubleOrNull() ?: 0.0
                            if (amt <= 0 || amt > driver.walletBalanceBDT) {
                                withdrawError = if (language == Language.BANGLA) "অপর্যাপ্ত ব্যালেন্স অথবা সঠিক পরিমাণ লিখুন" else "Invalid amount or exceeds balance"
                            } else {
                                val success = repository.requestDriverWithdrawal(amt, selectedGateway, accountNumber)
                                if (success) {
                                    showWithdrawModal = false
                                    withdrawSuccessMessage = if (language == Language.BANGLA)
                                        "৳${amt.toInt()} উত্তোলনের অনুরোধ সফল হয়েছে ($selectedGateway)"
                                    else
                                        "Payout request of ৳${amt.toInt()} to $selectedGateway submitted!"
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (selectedGateway) {
                                "bKash" -> BkashPink
                                "Nagad" -> NagadOrange
                                else -> Navy900
                            }
                        )
                    ) {
                        Text(if (language == Language.BANGLA) "অনুরোধ নিশ্চিত করুন" else "Confirm Payout")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showWithdrawModal = false }) {
                        Text(if (language == Language.BANGLA) "বাতিল" else "Cancel", color = Slate600)
                    }
                }
            )
        }
    }
}

@Composable
fun DriverKycScreen(
    repository: CallAndGoRepository,
    onBack: () -> Unit
) {
    val driver by repository.driverProfile.collectAsState()
    val language by repository.currentLanguage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Navy900)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == Language.BANGLA) "বিআরটিএ ড্রাইভার যাচাইকরণ" else "BRTA Driver Verification (KYC)",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy900
            )
        }

        CallGoCard {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == Language.BANGLA) "কেওয়াইসি স্ট্যাটাস" else "KYC Status",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (driver.isKycApproved) Color(0xFFD1FAE5) else OrangeLight
                    ) {
                        Text(
                            text = if (driver.isKycApproved) "APPROVED (যাচাইকৃত)" else "PENDING REVIEW",
                            color = if (driver.isKycApproved) SuccessGreen else OrangePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Slate200)

                KycItemRow(title = "Driving License", value = driver.licenseNumber, isVerified = true)
                KycItemRow(title = "National ID (NID)", value = driver.nidNumber, isVerified = true)
                KycItemRow(title = "Vehicle Registration Plate", value = driver.vehiclePlate, isVerified = true)
                KycItemRow(title = "Vehicle Model & Fitness", value = driver.vehicleModel, isVerified = true)
            }
        }
    }
}

@Composable
private fun KycItemRow(title: String, value: String, isVerified: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, fontSize = 11.sp, color = Slate400)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Navy900)
        }
        Icon(
            imageVector = if (isVerified) Icons.Default.CheckCircle else Icons.Default.Security,
            contentDescription = null,
            tint = if (isVerified) SuccessGreen else Slate400,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 11.sp, color = Slate600)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Navy900)
        }
    }
}

private val Navy100Color = Color(0xFFE2EBF5)
