package com.example.ui.screens.admin

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Driver
import com.example.data.model.FleetVehicle
import com.example.data.model.Language
import com.example.data.model.NotificationItem
import com.example.data.model.Promotion
import com.example.data.model.SupportTicket
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import com.example.data.model.VehicleCategory
import com.example.data.repository.CallAndGoRepository
import com.example.ui.components.BdtPriceText
import com.example.ui.components.CallAndGoLogo
import com.example.ui.components.CallGoButton
import com.example.ui.components.CallGoCard
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

@Composable
fun AdminDashboardScreen(
    repository: CallAndGoRepository,
    onBack: () -> Unit,
    onSwitchRole: (UserRole) -> Unit
) {
    val language by repository.currentLanguage.collectAsState()
    val adminConfig by repository.adminConfig.collectAsState()
    val driverList by repository.adminDriverList.collectAsState()
    val activeRide by repository.activeRide.collectAsState()
    val userAccounts by repository.userAccounts.collectAsState()
    val fleetVehicles by repository.fleetVehicles.collectAsState()
    val transactions by repository.transactions.collectAsState()
    val supportTickets by repository.supportTickets.collectAsState()
    val promotions by repository.availablePromotions.collectAsState()
    val notifications by repository.notifications.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var currentCommission by remember { mutableStateOf(adminConfig.platformCommissionPercent.toFloat()) }
    var currentSurge by remember { mutableStateOf(adminConfig.baseSurgeMultiplier.toFloat()) }
    var adminRoleName by remember { mutableStateOf("Super Admin (সুপার অ্যাডমিন)") }

    val adminTabs = listOf(
        Pair(if (language == Language.BANGLA) "অ্যানালিটিক্স" else "Analytics", Icons.Default.TrendingUp),
        Pair(if (language == Language.BANGLA) "ইউজার তালিকা" else "Users", Icons.Default.Group),
        Pair(if (language == Language.BANGLA) "ড্রাইভার ও KYC" else "Drivers KYC", Icons.Default.Security),
        Pair(if (language == Language.BANGLA) "যানবাহন ফ্লিট" else "Vehicles", Icons.Default.DirectionsCar),
        Pair(if (language == Language.BANGLA) "ভাড়া ও কমিশন" else "Fare & Rates", Icons.Default.Tune),
        Pair(if (language == Language.BANGLA) "পেমেন্টস ও MFS" else "Payments MFS", Icons.Default.Payment),
        Pair(if (language == Language.BANGLA) "প্রোমো ও নোটিশ" else "Promos & Alert", Icons.Default.LocalOffer),
        Pair(if (language == Language.BANGLA) "সাপোর্ট ও রোলস" else "Support & Roles", Icons.Default.SupportAgent)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
    ) {
        // Admin Top Navigation Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900)
        ) {
            Column(modifier = Modifier.padding(top = 12.dp, start = 14.dp, end = 14.dp, bottom = 4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        CallAndGoLogo(iconSize = 28.dp, showText = false)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (language == Language.BANGLA) "কল অ্যান্ড গো অ্যাডমিন" else "Call & Go Admin Hub",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = adminRoleName,
                                color = OrangePrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = OrangePrimary,
                            modifier = Modifier.clickable { onSwitchRole(UserRole.RIDER) }
                        ) {
                            Text(
                                text = if (language == Language.BANGLA) "যাত্রী মোড" else "Rider App",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable Admin Tab Row
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = OrangePrimary,
                    edgePadding = 0.dp,
                    divider = {}
                ) {
                    adminTabs.forEachIndexed { index, pair ->
                        val isSelected = selectedTab == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTab = index },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = pair.second,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (isSelected) OrangePrimary else Color.White.copy(alpha = 0.65f)
                                    )
                                    Text(
                                        text = pair.first,
                                        color = if (isSelected) OrangePrimary else Color.White.copy(alpha = 0.65f),
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        // Tab Content Views
        when (selectedTab) {
            0 -> AdminAnalyticsTab(
                driverList = driverList,
                userAccounts = userAccounts,
                activeRide = activeRide,
                language = language
            )
            1 -> AdminUserManagementTab(
                userAccounts = userAccounts,
                onToggleSuspension = { userId -> repository.toggleUserSuspension(userId) },
                language = language
            )
            2 -> AdminDriverKycTab(
                driverList = driverList,
                onApprove = { driverId -> repository.approveDriverKyc(driverId) },
                onToggleSuspension = { driverId -> repository.toggleDriverSuspension(driverId) },
                language = language
            )
            3 -> AdminVehiclesTab(
                vehicles = fleetVehicles,
                language = language
            )
            4 -> AdminPricingAndCommissionTab(
                commission = currentCommission,
                onCommissionChange = {
                    currentCommission = it
                    repository.updatePlatformCommission(it.toDouble())
                },
                surge = currentSurge,
                onSurgeChange = {
                    currentSurge = it
                    repository.updateSurgeMultiplier(it.toDouble())
                },
                language = language
            )
            5 -> AdminPaymentsTab(
                transactions = transactions,
                onApprovePayout = { txnId -> repository.approvePayoutTransaction(txnId) },
                language = language
            )
            6 -> AdminPromosAndBroadcastTab(
                promotions = promotions,
                notifications = notifications,
                onAddPromo = { code, descEn, descBn, pct, cap ->
                    repository.addNewPromotion(
                        com.example.data.model.Promotion(
                            code = code,
                            discountPercent = pct,
                            maxDiscountBDT = cap,
                            descriptionEn = descEn,
                            descriptionBn = descBn
                        )
                    )
                },
                onSendBroadcast = { title, msg ->
                    repository.sendSystemNotification(title, msg)
                },
                language = language
            )
            7 -> AdminSupportAndRolesTab(
                tickets = supportTickets,
                currentRole = adminRoleName,
                onSelectRole = { adminRoleName = it },
                onResolveTicket = { ticketId -> repository.resolveSupportTicket(ticketId) },
                language = language
            )
        }
    }
}

@Composable
private fun AdminAnalyticsTab(
    driverList: List<Driver>,
    userAccounts: List<UserAccount>,
    activeRide: com.example.data.model.Ride?,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // KPI Grid (2x2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AdminKpiCard(
                title = if (language == Language.BANGLA) "মোট জিএমভি রাজস্ব" else "Total Gross GMV",
                value = "৳ 4,82,500",
                icon = Icons.Default.AccountBalance,
                accentColor = OrangePrimary,
                modifier = Modifier.weight(1f)
            )
            AdminKpiCard(
                title = if (language == Language.BANGLA) "নেট প্ল্যাটফর্ম কমিশন" else "Net Commission (৳)",
                value = "৳ 72,375",
                icon = Icons.Default.TrendingUp,
                accentColor = TealPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AdminKpiCard(
                title = if (language == Language.BANGLA) "নিবন্ধিত যাত্রী" else "Total Riders",
                value = "${userAccounts.size + 1420}",
                icon = Icons.Default.Person,
                accentColor = Navy800,
                modifier = Modifier.weight(1f)
            )
            AdminKpiCard(
                title = if (language == Language.BANGLA) "অনলাইন ড্রাইভার" else "Online Drivers",
                value = "${driverList.count { it.isOnline }} / ${driverList.size}",
                icon = Icons.Default.DirectionsCar,
                accentColor = SuccessGreen,
                modifier = Modifier.weight(1f)
            )
        }

        // Live Active Ride Telemetry Card
        Text(
            text = if (language == Language.BANGLA) "লাইভ রাইড মনিটরিং (Dhaka Feed)" else "Live Ride Telemetry & Map Feed",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Navy900
        )

        if (activeRide != null) {
            CallGoCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Ride #${activeRide.id}", fontWeight = FontWeight.Bold, color = Navy900)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = OrangeLight
                        ) {
                            Text(
                                text = activeRide.status.name,
                                color = OrangePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Rider: ${activeRide.riderName} (${activeRide.riderPhone})", fontSize = 12.sp, color = Slate600)
                    Text(text = "Driver: ${activeRide.driver?.name ?: "Matching"} • Vehicle: ${activeRide.vehicleCategory.nameEn}", fontSize = 12.sp, color = Slate600)
                    Text(text = "Route: ${activeRide.pickup.name} → ${activeRide.destination.name}", fontSize = 12.sp, color = TealDark, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Distance: ${activeRide.estimatedDistanceKm} km", fontSize = 12.sp, color = Slate400)
                        BdtPriceText(amount = activeRide.totalFareBDT, fontSize = 15)
                    }
                }
            }
        } else {
            CallGoCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(SuccessGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == Language.BANGLA)
                            "সিস্টেম স্বাভাবিক। ঢাকা ও চট্টগ্রামে ট্রাফিক রাডার সক্রিয়।"
                        else
                            "System Healthy: Live traffic radar active across Dhaka & Chittagong.",
                        fontSize = 12.sp,
                        color = Slate700
                    )
                }
            }
        }

        // City Heatmap Breakdown Card
        CallGoCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (language == Language.BANGLA) "বিভাগীয় রাইড বণ্টন" else "Bangladesh Region Distribution",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Navy900
                )
                RegionProgressRow(name = "Dhaka Metro (ঢাকা মেট্রো)", count = "72% (1,363 trips)", color = OrangePrimary)
                RegionProgressRow(name = "Chittagong Port City (চট্টগ্রাম)", count = "18% (340 trips)", color = TealPrimary)
                RegionProgressRow(name = "Sylhet & Rajshahi (সিলেট ও রাজশাহী)", count = "10% (191 trips)", color = Navy800)
            }
        }
    }
}

@Composable
private fun RegionProgressRow(name: String, count: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = name, fontSize = 12.sp, color = Slate700)
        }
        Text(text = count, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy900)
    }
}

@Composable
private fun AdminUserManagementTab(
    userAccounts: List<UserAccount>,
    onToggleSuspension: (String) -> Unit,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Text(
            text = if (language == Language.BANGLA) "যাত্রী ও ইউজার অ্যাকাউন্ট ব্যবস্থাপনা" else "Registered Passenger Accounts",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Navy900,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(userAccounts) { user ->
                CallGoCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy900)
                                Text(text = user.phone, fontSize = 12.sp, color = Slate600)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (!user.isSuspended) Color(0xFFD1FAE5) else Color(0xFFFFE4E6)
                            ) {
                                Text(
                                    text = if (!user.isSuspended) "ACTIVE" else "BLOCKED",
                                    color = if (!user.isSuspended) SuccessGreen else ErrorRed,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate100)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Trips: ${user.totalRides} • Spent: ৳${(user.totalRides * 240.0).toInt()}",
                                fontSize = 12.sp,
                                color = Slate600
                            )

                            Button(
                                onClick = { onToggleSuspension(user.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!user.isSuspended) ErrorRed else SuccessGreen
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = if (!user.isSuspended) "Block User" else "Unblock User",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
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
private fun AdminDriverKycTab(
    driverList: List<Driver>,
    onApprove: (String) -> Unit,
    onToggleSuspension: (String) -> Unit,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Text(
            text = if (language == Language.BANGLA) "বিআরটিএ ড্রাইভার কেওয়াইসি ও নথি যাচাই" else "Driver Partner KYC & BRTA Verification",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Navy900,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(driverList) { driver ->
                CallGoCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = driver.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy900)
                                Text(text = driver.phone, fontSize = 12.sp, color = Slate600)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (driver.isKycApproved) Color(0xFFD1FAE5) else OrangeLight
                                ) {
                                    Text(
                                        text = if (driver.isKycApproved) "VERIFIED" else "PENDING",
                                        color = if (driver.isKycApproved) SuccessGreen else OrangePrimary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (driver.isOnline) TealLight else Slate100
                                ) {
                                    Text(
                                        text = if (driver.isOnline) "ONLINE" else "OFFLINE",
                                        color = if (driver.isOnline) TealDark else Slate600,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate100)

                        Text(text = "Vehicle: ${driver.vehicleModel} • Plate: ${driver.vehiclePlate}", fontSize = 12.sp, color = Slate700)
                        Text(text = "BRTA Driving License: ${driver.licenseNumber} • NID: ${driver.nidNumber}", fontSize = 12.sp, color = Slate600)
                        Text(text = "Rating: ★${driver.rating} • Trips: ${driver.totalTrips} • Wallet: ৳${driver.walletBalanceBDT.toInt()}", fontSize = 12.sp, color = OrangePrimary, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!driver.isKycApproved) {
                                Button(
                                    onClick = { onApprove(driver.id) },
                                    modifier = Modifier.weight(1f).height(36.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Approve KYC", fontSize = 11.sp)
                                }
                            }

                            Button(
                                onClick = { onToggleSuspension(driver.id) },
                                modifier = Modifier.weight(1f).height(36.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (driver.isKycApproved) ErrorRed else Navy800
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (driver.isKycApproved) "Suspend Account" else "Reject Application",
                                    fontSize = 11.sp
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
private fun AdminVehiclesTab(
    vehicles: List<FleetVehicle>,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Text(
            text = if (language == Language.BANGLA) "নিবন্ধিত যানবাহন ও ফ্লিট ব্যবস্থাপনা" else "Registered Fleet & Vehicle Inventory",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Navy900,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(vehicles) { v ->
                CallGoCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${v.model} (${v.plateNumber})", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy900)
                                Text(text = "Category: ${v.category.nameEn} • ${v.category.capacity} Seats", fontSize = 12.sp, color = Slate600)
                            }
                            val fitnessStatus = if (v.isCompliant) "VALID" else "PENDING"
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (v.isCompliant) Color(0xFFD1FAE5) else OrangeLight
                            ) {
                                Text(
                                    text = "FITNESS: $fitnessStatus",
                                    color = if (v.isCompliant) SuccessGreen else OrangePrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate100)
                        Text(text = "Assigned Driver: ${v.driverName}", fontSize = 12.sp, color = Slate700, fontWeight = FontWeight.SemiBold)
                        Text(text = "BRTA Fitness Expiry: ${v.fitnessExpiry}", fontSize = 11.sp, color = Slate400)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminPricingAndCommissionTab(
    commission: Float,
    onCommissionChange: (Float) -> Unit,
    surge: Float,
    onSurgeChange: (Float) -> Unit,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Platform Commission Card
        CallGoCard {
            Column {
                Text(
                    text = if (language == Language.BANGLA) "প্ল্যাটফর্ম সার্ভিস কমিশন" else "Platform Commission Rate",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Navy900
                )
                Text(
                    text = "Current: ${commission.toInt()}% per trip",
                    fontSize = 13.sp,
                    color = OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
                )
                Slider(
                    value = commission,
                    onValueChange = onCommissionChange,
                    valueRange = 8f..25f,
                    steps = 17,
                    colors = SliderDefaults.colors(
                        thumbColor = OrangePrimary,
                        activeTrackColor = OrangePrimary
                    )
                )
            }
        }

        // Surge Multiplier Card
        CallGoCard {
            Column {
                Text(
                    text = if (language == Language.BANGLA) "বৃষ্টি ও পিক আওয়ার সার্জ রেট" else "Peak Hour & Rain Surge Multiplier",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Navy900
                )
                Text(
                    text = "Current: ${"%.1f".format(surge)}x Base Rate",
                    fontSize = 13.sp,
                    color = TealPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
                )
                Slider(
                    value = surge,
                    onValueChange = onSurgeChange,
                    valueRange = 1.0f..3.0f,
                    steps = 20,
                    colors = SliderDefaults.colors(
                        thumbColor = TealPrimary,
                        activeTrackColor = TealPrimary
                    )
                )
            }
        }

        // Fare Rate Matrix Table for Bangladesh
        CallGoCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (language == Language.BANGLA) "বিআরটিএ রেট চার্ট (ঢাকা ও জাতীয়)" else "Official Fare Matrix (BDT ৳)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Navy900
                )
                FareRow(vehicle = "Call & Go Bike (বাইক)", base = "৳35", perKm = "৳14/km")
                FareRow(vehicle = "CNG Auto-Rickshaw (সিএনজি)", base = "৳50", perKm = "৳18/km")
                FareRow(vehicle = "Car Economy (কার ইকোনমি)", base = "৳70", perKm = "৳24/km")
                FareRow(vehicle = "Car Premium (কার প্রিমিয়াম)", base = "৳100", perKm = "৳34/km")
                FareRow(vehicle = "Microbus / XL (মাইক্রোবাস)", base = "৳150", perKm = "৳42/km")
            }
        }
    }
}

@Composable
private fun FareRow(vehicle: String, base: String, perKm: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = vehicle, fontSize = 12.sp, color = Slate700)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = "Base: $base", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Navy900)
            Text(text = perKm, fontSize = 12.sp, color = OrangePrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AdminPaymentsTab(
    transactions: List<TransactionRecord>,
    onApprovePayout: (String) -> Unit,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        Text(
            text = if (language == Language.BANGLA) "পেমেন্ট লেনদেন ও ক্যাশ-আউট অডিট" else "MFS Payments & Payout Approvals",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Navy900,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(transactions) { txn ->
                CallGoCard {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${txn.userName} (${txn.rideId})", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Navy900)
                                Text(text = "Gateway: ${txn.method.nameEn} • ID: ${txn.id}", fontSize = 11.sp, color = Slate600)
                            }
                            BdtPriceText(amount = txn.amountBDT, fontSize = 15)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate100)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (txn.status) {
                                    "SETTLED" -> Color(0xFFD1FAE5)
                                    "PENDING" -> OrangeLight
                                    else -> Color(0xFFFFE4E6)
                                }
                            ) {
                                Text(
                                    text = txn.status,
                                    color = when (txn.status) {
                                        "SETTLED" -> SuccessGreen
                                        "PENDING" -> OrangePrimary
                                        else -> ErrorRed
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }

                            if (txn.status == "PENDING") {
                                Button(
                                    onClick = { onApprovePayout(txn.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BkashPink),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Approve Payout", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminPromosAndBroadcastTab(
    promotions: List<Promotion>,
    notifications: List<NotificationItem>,
    onAddPromo: (String, String, String, Int, Double) -> Unit,
    onSendBroadcast: (String, String) -> Unit,
    language: Language
) {
    var showAddPromoDialog by remember { mutableStateOf(false) }
    var showBroadcastDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Promos Header + Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == Language.BANGLA) "সক্রিয় প্রোমো ভাউচার" else "Active Promo Codes",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Navy900
            )

            Button(
                onClick = { showAddPromoDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Code", fontSize = 11.sp)
            }
        }

        promotions.forEach { promo ->
            CallGoCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = promo.code, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Navy900)
                        Text(text = promo.descriptionEn, fontSize = 12.sp, color = Slate600)
                        Text(text = "Max cap: ৳${promo.maxDiscountBDT.toInt()} • Exp: ${promo.expiryText}", fontSize = 10.sp, color = Slate400)
                    }
                    Surface(shape = RoundedCornerShape(8.dp), color = OrangeLight) {
                        Text(
                            text = "${promo.discountPercent}% OFF",
                            color = OrangePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // System Push Notifications Broadcast Header + Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (language == Language.BANGLA) "সিস্টেম পুশ নোটিফিকেশন" else "System Push Broadcasts",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Navy900
            )

            Button(
                onClick = { showBroadcastDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Broadcast", fontSize = 11.sp)
            }
        }

        notifications.forEach { notif ->
            CallGoCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = if (language == Language.BANGLA) notif.titleBn else notif.titleEn, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy900)
                        Text(text = notif.timeAgo, fontSize = 10.sp, color = Slate400)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = if (language == Language.BANGLA) notif.messageBn else notif.messageEn, fontSize = 12.sp, color = Slate600)
                }
            }
        }
    }

    // Add Promo Dialog
    if (showAddPromoDialog) {
        var code by remember { mutableStateOf("") }
        var discountPct by remember { mutableStateOf("20") }
        var maxCap by remember { mutableStateOf("100") }

        AlertDialog(
            onDismissRequest = { showAddPromoDialog = false },
            title = { Text("Create Promo Code", fontWeight = FontWeight.Bold, color = Navy900) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it.uppercase() },
                        label = { Text("Promo Code (e.g. DHAKA25)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = discountPct,
                        onValueChange = { discountPct = it },
                        label = { Text("Discount Percentage (%)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = maxCap,
                        onValueChange = { maxCap = it },
                        label = { Text("Max Discount Cap (BDT ৳)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (code.isNotBlank()) {
                            onAddPromo(
                                code,
                                "$discountPct% off on all Call & Go trips",
                                "$discountPct% বিশেষ ছাড় সব ট্রিপে",
                                discountPct.toIntOrNull() ?: 20,
                                maxCap.toDoubleOrNull() ?: 100.0
                            )
                            showAddPromoDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text("Save Voucher")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPromoDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Broadcast Push Dialog
    if (showBroadcastDialog) {
        var bTitle by remember { mutableStateOf("Call & Go Safety Advisory") }
        var bMessage by remember { mutableStateOf("Dhaka metro traffic is heavy. Drivers & riders please wear helmets and stay safe.") }

        AlertDialog(
            onDismissRequest = { showBroadcastDialog = false },
            title = { Text("Send Push Broadcast", fontWeight = FontWeight.Bold, color = Navy900) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bTitle,
                        onValueChange = { bTitle = it },
                        label = { Text("Notification Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = bMessage,
                        onValueChange = { bMessage = it },
                        label = { Text("Message Body") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (bTitle.isNotBlank()) {
                            onSendBroadcast(bTitle, bMessage)
                            showBroadcastDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("Send Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBroadcastDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun AdminSupportAndRolesTab(
    tickets: List<SupportTicket>,
    currentRole: String,
    onSelectRole: (String) -> Unit,
    onResolveTicket: (String) -> Unit,
    language: Language
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Role Switcher Card
        CallGoCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (language == Language.BANGLA) "অ্যাডমিন রোল ও পারমিশন" else "Admin Permission & Role Switcher",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Navy900
                )

                val roles = listOf(
                    "Super Admin (সুপার অ্যাডমিন)",
                    "Fleet & Operations Manager",
                    "Customer Support Lead",
                    "Finance & MFS Auditor"
                )

                roles.forEach { r ->
                    val isChosen = r == currentRole
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isChosen) Navy900 else Slate100,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectRole(r) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isChosen) "● $r" else "○ $r",
                                color = if (isChosen) Color.White else Slate700,
                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Support Complaints Feed
        Text(
            text = if (language == Language.BANGLA) "অভিযোগ ও সহায়তা টিকেট" else "Complaints & Support Tickets",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Navy900
        )

        tickets.forEach { ticket ->
            CallGoCard {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Ticket #${ticket.id}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy900)
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (ticket.status == "OPEN") OrangeLight else Color(0xFFD1FAE5)
                        ) {
                            Text(
                                text = ticket.status,
                                color = if (ticket.status == "OPEN") OrangePrimary else SuccessGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${ticket.userName} (${ticket.issueType})", fontSize = 12.sp, color = Slate700, fontWeight = FontWeight.SemiBold)
                    Text(text = ticket.description, fontSize = 12.sp, color = Slate600)

                    if (ticket.status == "OPEN") {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onResolveTicket(ticket.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Mark Resolved", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminKpiCard(
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Slate200)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 11.sp, color = Slate600)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Navy900)
        }
    }
}
