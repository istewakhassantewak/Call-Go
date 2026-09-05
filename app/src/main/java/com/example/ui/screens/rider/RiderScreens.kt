package com.example.ui.screens.rider

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.LocationPoint
import com.example.data.model.PaymentMethod
import com.example.data.model.Ride
import com.example.data.model.RideStatus
import com.example.data.model.UserRole
import com.example.data.model.VehicleCategory
import com.example.data.repository.CallAndGoRepository
import com.example.domain.Localization
import com.example.ui.components.BdtPriceText
import com.example.ui.components.CallAndGoLogo
import com.example.ui.components.CallGoButton
import com.example.ui.components.CallGoCard
import com.example.ui.components.CallGoOutlinedButton
import com.example.ui.components.InteractiveMap
import com.example.ui.components.RatingStarsRow
import com.example.ui.components.SosEmergencyDialog
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BkashPink
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.Navy100
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
import com.example.ui.theme.WarningAmber

@Composable
fun RiderHomeScreen(
    repository: CallAndGoRepository,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onSwitchRole: (UserRole) -> Unit
) {
    val language by repository.currentLanguage.collectAsState()
    val pickup by repository.pickupLocation.collectAsState()
    val destination by repository.destinationLocation.collectAsState()
    val selectedVehicle by repository.selectedVehicle.collectAsState()
    val selectedPayment by repository.selectedPayment.collectAsState()
    val activeRide by repository.activeRide.collectAsState()
    val appliedPromo by repository.appliedPromo.collectAsState()
    val user by repository.currentUser.collectAsState()

    var showBookingSheet by remember { mutableStateOf(false) }
    var showSosDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var promoInput by remember { mutableStateOf("") }
    var promoMessage by remember { mutableStateOf<String?>(null) }

    // If ride is completed, show the Digital Receipt / Rating overlay
    if (activeRide != null && activeRide!!.status == RideStatus.TRIP_COMPLETED) {
        TripReceiptScreen(
            ride = activeRide!!,
            language = language,
            onClose = { repository.resetRide() },
            onRate = { stars, review -> repository.rateRide(stars, review) }
        )
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fullscreen Live Interactive Map
        InteractiveMap(
            pickupLocation = pickup,
            destinationLocation = destination,
            activeRide = activeRide,
            modifier = Modifier.fillMaxSize()
        )

        // Top Navigation Bar
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo
                CallAndGoLogo(iconSize = 36.dp, showText = true)

                // Top Controls: Language Toggle, Role Selector, Profile
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Bangla/English Toggle Button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Navy100,
                        modifier = Modifier.clickable {
                            val next = if (language == Language.BANGLA) Language.ENGLISH else Language.BANGLA
                            repository.setLanguage(next)
                        }
                    ) {
                        Text(
                            text = if (language == Language.BANGLA) "বাংলা" else "EN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Navy900,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Role Switch Button (Rider / Driver / Admin)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = OrangeLight,
                        modifier = Modifier.clickable {
                            onSwitchRole(UserRole.DRIVER)
                        }
                    ) {
                        Text(
                            text = if (language == Language.BANGLA) "ড্রাইভার মোড" else "Driver Mode",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangePrimary,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "History", tint = Navy900)
                    }

                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Navy900)
                    }
                }
            }
        }

        // Active Live Ride Tracker Overlay (when ride is booked or in progress)
        if (activeRide != null && activeRide!!.status != RideStatus.IDLE && activeRide!!.status != RideStatus.TRIP_COMPLETED) {
            LiveRideTrackingCard(
                ride = activeRide!!,
                language = language,
                onCancelClick = { showCancelDialog = true },
                onSosClick = { showSosDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        } else {
            // Idle State: Location Search & Vehicle Selector Bottom Sheet
            RiderBookingPanel(
                repository = repository,
                pickup = pickup,
                destination = destination,
                selectedVehicle = selectedVehicle,
                selectedPayment = selectedPayment,
                appliedPromo = appliedPromo,
                language = language,
                onBookRide = { repository.bookRide() },
                onSosClick = { showSosDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }

        // SOS Dialog
        SosEmergencyDialog(
            isOpen = showSosDialog,
            onDismiss = { showSosDialog = false },
            emergencyContact = user.emergencyContact,
            rideId = activeRide?.id,
            currentLocationName = pickup.name,
            language = language
        )

        // Cancel Ride Confirmation Dialog
        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                title = {
                    Text(
                        text = if (language == Language.BANGLA) "রাইড বাতিল নিশ্চিতকরণ" else "Confirm Ride Cancellation",
                        fontWeight = FontWeight.Bold,
                        color = ErrorRed
                    )
                },
                text = {
                    Text(
                        text = if (language == Language.BANGLA)
                            "ড্রাইভার ইতিমধ্যে আপনার পথে রওয়ানা হয়েছে। বাতিল করলে বিআরটিএ গাইডলাইন অনুযায়ী ৪০ ৳ ফি প্রযোজ্য হতে পারে। আপনি কি নিশ্চিত?"
                        else
                            "The driver is already navigating to your location. A cancellation fee of ৳40 may apply. Are you sure you want to cancel?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            repository.cancelRide("Passenger cancelled")
                            showCancelDialog = false
                        }
                    ) {
                        Text(
                            text = if (language == Language.BANGLA) "হ্যাঁ, বাতিল করুন" else "Yes, Cancel",
                            color = ErrorRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCancelDialog = false }) {
                        Text(if (language == Language.BANGLA) "না, অপেক্ষা করব" else "Keep Ride")
                    }
                }
            )
        }
    }
}

@Composable
fun RiderBookingPanel(
    repository: CallAndGoRepository,
    pickup: LocationPoint,
    destination: LocationPoint,
    selectedVehicle: VehicleCategory,
    selectedPayment: PaymentMethod,
    appliedPromo: com.example.data.model.Promotion?,
    language: Language,
    onBookRide: () -> Unit,
    onSosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var promoCodeInput by remember { mutableStateOf("") }
    var promoMsg by remember { mutableStateOf<String?>(null) }

    val distanceKm = repository.calculateDistanceKm(pickup, destination)
    val estimatedFare = repository.estimateFare(selectedVehicle, distanceKm)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Pick-up & Destination Route Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Slate100)
                    .padding(12.dp)
            ) {
                // Pickup Point
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(TealPrimary)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == Language.BANGLA) "পিকআপ পয়েন্ট" else "Pickup Location",
                            fontSize = 11.sp,
                            color = Slate600
                        )
                        Text(
                            text = pickup.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Navy900
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                    color = Slate200
                )

                // Dropoff Point
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(OrangePrimary)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == Language.BANGLA) "গন্তব্য স্থল" else "Destination (Where to?)",
                            fontSize = 11.sp,
                            color = Slate600
                        )
                        Text(
                            text = destination.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangePrimary
                        )
                    }
                }
            }

            // Quick Bangladesh Hotspot Selector Chips
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repository.bangladeshLocations.forEach { loc ->
                    val isDest = loc.id == destination.id
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDest) OrangePrimary else Slate100,
                        border = BorderStroke(1.dp, if (isDest) OrangePrimary else Slate200),
                        modifier = Modifier.clickable {
                            repository.setDestination(loc)
                        }
                    ) {
                        Text(
                            text = loc.name.take(16),
                            fontSize = 12.sp,
                            fontWeight = if (isDest) FontWeight.Bold else FontWeight.Medium,
                            color = if (isDest) Color.White else Slate700,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Vehicle Category Cards Carousel (Bike, CNG, Car Economy, Car Premium, Microbus)
            Text(
                text = if (language == Language.BANGLA) "বাহন নির্বাচন করুন" else "Available Call & Go Services",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Slate700
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VehicleCategory.entries.forEach { vehicle ->
                    val isSelected = selectedVehicle == vehicle
                    val fare = repository.estimateFare(vehicle, distanceKm)

                    Card(
                        modifier = Modifier
                            .width(135.dp)
                            .clickable { repository.selectVehicle(vehicle) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Navy800 else Color.White
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) OrangePrimary else Slate200
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) OrangePrimary else Slate100),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getVehicleIcon(vehicle),
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else Navy900,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (language == Language.BANGLA) vehicle.nameBn else vehicle.nameEn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Navy900,
                                maxLines = 1
                            )
                            Text(
                                text = if (language == Language.BANGLA) "${vehicle.capacity} সিট" else "${vehicle.capacity} seats",
                                fontSize = 10.sp,
                                color = if (isSelected) Slate400 else Slate600
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            BdtPriceText(
                                amount = fare,
                                fontSize = 15,
                                color = if (isSelected) Color.White else Navy900
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Payment Options Row (bKash / Nagad / Cash / Card)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (language == Language.BANGLA) "পেমেন্ট:" else "Pay via:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate600
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    PaymentMethod.entries.forEach { method ->
                        val isChosen = selectedPayment == method
                        val pillColor = when (method) {
                            PaymentMethod.BKASH -> BkashPink
                            PaymentMethod.NAGAD -> NagadOrange
                            else -> Navy800
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isChosen) pillColor else Slate100,
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable { repository.selectPayment(method) }
                        ) {
                            Text(
                                text = if (language == Language.BANGLA) method.nameBn.take(6) else method.nameEn.take(6),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isChosen) Color.White else Slate700,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Promo code apply toggle
                Text(
                    text = if (appliedPromo != null) "✓ ${appliedPromo.code}" else (if (language == Language.BANGLA) "+ প্রোমো" else "+ Promo"),
                    color = if (appliedPromo != null) SuccessGreen else OrangePrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        isExpanded = !isExpanded
                    }
                )
            }

            // Expandable Promo Code Box
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = promoCodeInput,
                            onValueChange = { promoCodeInput = it.uppercase() },
                            placeholder = { Text("CALLGO50 / DHAKA20", fontSize = 12.sp, color = Slate400) },
                            textStyle = TextStyle(
                                color = Slate900,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Slate900,
                                unfocusedTextColor = Slate900,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = OrangePrimary,
                                focusedBorderColor = OrangePrimary,
                                unfocusedBorderColor = Slate400
                            ),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        CallGoButton(
                            text = if (language == Language.BANGLA) "প্রয়োগ" else "Apply",
                            onClick = {
                                val success = repository.applyPromotion(promoCodeInput)
                                promoMsg = if (success) {
                                    if (language == Language.BANGLA) "প্রোমো কোড যুক্ত হয়েছে!" else "Promo applied!"
                                } else {
                                    if (language == Language.BANGLA) "ভুল প্রোমো কোড" else "Invalid Promo Code"
                                }
                            },
                            modifier = Modifier.width(90.dp),
                            isSecondary = true
                        )
                    }
                    if (promoMsg != null) {
                        Text(
                            text = promoMsg!!,
                            fontSize = 11.sp,
                            color = if (appliedPromo != null) SuccessGreen else ErrorRed,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Fare Summary & Booking Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (language == Language.BANGLA) "আনুমানিক ভাড়া ($distanceKm কিমি)" else "Est. Total ($distanceKm km)",
                        fontSize = 11.sp,
                        color = Slate600
                    )
                    BdtPriceText(amount = estimatedFare, fontSize = 22)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Quick SOS Button
                    IconButton(
                        onClick = onSosClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(ErrorRed.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "SOS",
                            tint = ErrorRed
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    CallGoButton(
                        text = if (language == Language.BANGLA) "কল অ্যান্ড গো বুক করুন" else "Book Call & Go",
                        onClick = onBookRide,
                        modifier = Modifier.width(180.dp),
                        isSecondary = false
                    )
                }
            }
        }
    }
}

@Composable
fun LiveRideTrackingCard(
    ride: Ride,
    language: Language,
    onCancelClick: () -> Unit,
    onSosClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Status & Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatusBadge(status = ride.status)

                // 4-digit Ride Start OTP
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = OrangeLight,
                    border = BorderStroke(1.dp, OrangePrimary)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == Language.BANGLA) "ওটিপি: " else "PIN: ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate700
                        )
                        Text(
                            text = ride.startOtp,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OrangePrimary,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Driver Information Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Driver Avatar
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Navy800),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ride.driver?.name ?: "Searching nearest driver...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = ride.driver?.vehicleModel ?: "Call & Go Partner",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                    Text(
                        text = ride.driver?.vehiclePlate ?: "Dhaka Metro",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangePrimary
                    )
                }

                // Driver Action Buttons: Phone Call & SMS
                if (ride.driver != null) {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${ride.driver.phone}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(TealLight)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call Driver", tint = TealPrimary)
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${ride.driver.phone}"))
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Slate100)
                    ) {
                        Icon(Icons.Default.Message, contentDescription = "Message", tint = Navy800)
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = OrangePrimary
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Slate100)

            // Live status description
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = Localization.getRideStatusText(ride.status, language),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate700
                )
                BdtPriceText(amount = ride.totalFareBDT, fontSize = 18)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: SOS & Cancel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CallGoButton(
                    text = if (language == Language.BANGLA) "জরুরি এসওএস (SOS)" else "Emergency SOS",
                    onClick = onSosClick,
                    isDanger = true,
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.Warning
                )

                CallGoOutlinedButton(
                    text = if (language == Language.BANGLA) "বাতিল" else "Cancel Ride",
                    onClick = onCancelClick,
                    borderColor = Slate400,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TripReceiptScreen(
    ride: Ride,
    language: Language,
    onClose: () -> Unit,
    onRate: (Int, String) -> Unit
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    var reviewText by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A).copy(alpha = 0.7f))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Badge
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD1FAE5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (language == Language.BANGLA) "যাত্রা সমাপ্ত হয়েছে!" else "Trip Completed!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Navy900
                )
                Text(
                    text = "Ride ID: ${ride.id} • Call & Go Bangladesh",
                    fontSize = 12.sp,
                    color = Slate400
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fare Breakdown Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate50Color)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ReceiptRow(
                            label = if (language == Language.BANGLA) "মূল ভাড়া (Base Fare)" else "Base Fare",
                            value = "৳ ${Math.round(ride.baseFareBDT)}"
                        )
                        ReceiptRow(
                            label = if (language == Language.BANGLA) "দূরত্ব ভাড়া (${ride.estimatedDistanceKm} km)" else "Distance Fare (${ride.estimatedDistanceKm} km)",
                            value = "৳ ${Math.round(ride.distanceFareBDT)}"
                        )
                        ReceiptRow(
                            label = if (language == Language.BANGLA) "সময় চার্জ" else "Time Charge",
                            value = "৳ ${Math.round(ride.timeFareBDT)}"
                        )
                        if (ride.discountBDT > 0) {
                            ReceiptRow(
                                label = if (language == Language.BANGLA) "প্রোমো ছাড়" else "Promo Discount",
                                value = "- ৳ ${Math.round(ride.discountBDT)}",
                                isDiscount = true
                            )
                        }
                        HorizontalDivider(color = Slate200)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == Language.BANGLA) "মোট প্রদেয় বিল" else "Total Paid",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = Navy900
                            )
                            BdtPriceText(amount = ride.totalFareBDT, fontSize = 22)
                        }
                        Text(
                            text = if (language == Language.BANGLA) "পরিশোধ পদ্ধতি: ${ride.paymentMethod.nameBn}" else "Paid via: ${ride.paymentMethod.nameEn}",
                            fontSize = 11.sp,
                            color = TealPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rating & Review Section
                if (!isSubmitted) {
                    Text(
                        text = if (language == Language.BANGLA) "ড্রাইভারকে রেটিং দিন" else "Rate your Driver",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (star in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (star <= selectedStars) WarningAmber else Slate200,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable { selectedStars = star }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        placeholder = {
                            Text(
                                if (language == Language.BANGLA) "যাত্রার অভিজ্ঞতা লিখুন (ঐচ্ছিক)..." else "Leave feedback for driver...",
                                fontSize = 12.sp,
                                color = Slate400
                            )
                        },
                        textStyle = TextStyle(
                            color = Slate900,
                            fontSize = 13.sp
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
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CallGoButton(
                        text = if (language == Language.BANGLA) "রেটিং জমা দিন ও সম্পন্ন করুন" else "Submit Rating & Done",
                        onClick = {
                            onRate(selectedStars, reviewText)
                            isSubmitted = true
                            onClose()
                        },
                        isSecondary = true
                    )
                } else {
                    Text(
                        text = if (language == Language.BANGLA) "ধন্যবাদ! আপনার রিভিউ জমা হয়েছে।" else "Thank you! Your feedback has been recorded.",
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CallGoButton(
                        text = if (language == Language.BANGLA) "বন্ধ করুন" else "Done",
                        onClick = onClose
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = Slate600)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDiscount) SuccessGreen else Navy900
        )
    }
}

@Composable
fun TripHistoryScreen(
    repository: CallAndGoRepository,
    onBack: () -> Unit
) {
    val completedRides by repository.completedRidesFromDb.collectAsState(initial = emptyList())
    val language by repository.currentLanguage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
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
                text = if (language == Language.BANGLA) "ভ্রমণ ইতিহাস" else "My Ride History",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy900
            )
        }

        if (completedRides.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Slate400
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == Language.BANGLA) "এখনও কোনো রাইড সম্পন্ন হয়নি" else "No rides completed yet",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700
                    )
                    Text(
                        text = if (language == Language.BANGLA) "আপনার সম্পন্ন সকল রাইডের ডিজিটাল রসিদ এখানে দেখতে পাবেন।" else "Your trip records and receipts will appear here.",
                        fontSize = 12.sp,
                        color = Slate400,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(completedRides) { ride ->
                    CallGoCard {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = ride.vehicleName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Navy900
                                )
                                BdtPriceText(amount = ride.totalFareBDT, fontSize = 16)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "From: ${ride.pickupAddress}",
                                fontSize = 12.sp,
                                color = Slate600
                            )
                            Text(
                                text = "To: ${ride.dropoffAddress}",
                                fontSize = 12.sp,
                                color = Slate600
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Driver: ${ride.driverName}",
                                    fontSize = 11.sp,
                                    color = Slate400
                                )
                                Text(
                                    text = ride.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (ride.status == "COMPLETED") SuccessGreen else ErrorRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RiderProfileScreen(
    repository: CallAndGoRepository,
    onBack: () -> Unit,
    onSwitchRole: (UserRole) -> Unit
) {
    val user by repository.currentUser.collectAsState()
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
                text = if (language == Language.BANGLA) "প্রোফাইল ও সেটিংস" else "Profile & Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy900
            )
        }

        // User Avatar Card
        CallGoCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Navy800),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = user.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Navy900
                    )
                    Text(
                        text = user.phone,
                        fontSize = 13.sp,
                        color = Slate600
                    )
                    Text(
                        text = user.email,
                        fontSize = 12.sp,
                        color = Slate400
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Role Switcher Cards
        Text(
            text = if (language == Language.BANGLA) "মোড পরিবর্তন করুন" else "Switch Experience Mode",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Slate700
        )
        Spacer(modifier = Modifier.height(8.dp))

        CallGoCard(
            onClick = { onSwitchRole(UserRole.DRIVER) },
            backgroundColor = TealLight
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (language == Language.BANGLA) "ড্রাইভার মোডে যান" else "Switch to Driver Partner Mode",
                        fontWeight = FontWeight.Bold,
                        color = TealDark,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (language == Language.BANGLA) "রাইড রিকুয়েস্ট গ্রহণ করুন এবং আয় করুন" else "Accept ride requests & manage earnings",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                }
                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = TealDark)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        CallGoCard(
            onClick = { onSwitchRole(UserRole.ADMIN) },
            backgroundColor = OrangeLight
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = if (language == Language.BANGLA) "অ্যাডমিন প্যানেল কনসোল" else "Open Admin Operations Console",
                        fontWeight = FontWeight.Bold,
                        color = OrangePrimary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (language == Language.BANGLA) "লাইভ রাইড, ড্রাইভার কেওয়াইসি ও ভাড়া কনফিগারেশন" else "Monitor live rides, verify drivers & platform stats",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                }
                Icon(Icons.Default.Security, contentDescription = null, tint = OrangePrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language Selector Card
        Text(
            text = if (language == Language.BANGLA) "অ্যাপ ভাষা (Language)" else "App Language",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Slate700
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CallGoOutlinedButton(
                text = "বাংলা (Bangla)",
                onClick = { repository.setLanguage(Language.BANGLA) },
                borderColor = if (language == Language.BANGLA) OrangePrimary else Slate200,
                modifier = Modifier.weight(1f)
            )
            CallGoOutlinedButton(
                text = "English",
                onClick = { repository.setLanguage(Language.ENGLISH) },
                borderColor = if (language == Language.ENGLISH) Navy800 else Slate200,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Safety Information
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Slate200)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = if (language == Language.BANGLA) "নিরাপত্তা ও জরুরি যোগাযোগ" else "Emergency & Trust Info",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Navy900
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Emergency Contact: ${user.emergencyContact}",
                    fontSize = 12.sp,
                    color = Slate600
                )
                Text(
                    text = "• Bangladesh National Emergency Hotline: 999",
                    fontSize = 12.sp,
                    color = Slate600
                )
                Text(
                    text = "• Call & Go 24/7 Security Center: +880 9612-345678",
                    fontSize = 12.sp,
                    color = Slate600
                )
            }
        }
    }
}

private fun getVehicleIcon(category: VehicleCategory): ImageVector {
    return when (category) {
        VehicleCategory.BIKE -> Icons.Default.TwoWheeler
        VehicleCategory.CNG -> Icons.Default.ElectricRickshaw
        VehicleCategory.CAR_ECONOMY -> Icons.Default.DirectionsCar
        VehicleCategory.CAR_PREMIUM -> Icons.Default.AirportShuttle
        VehicleCategory.MICROBUS -> Icons.Default.LocalShipping
    }
}

private val Slate50Color = Color(0xFFF8FAFC)
