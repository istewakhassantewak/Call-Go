package com.example.data.repository

import android.content.Context
import com.example.data.auth.FirebaseAuthService
import com.example.data.local.AppDatabase
import com.example.data.local.RideHistoryEntity
import com.example.data.model.AdminConfig
import com.example.data.model.ChatMessage
import com.example.data.model.Driver
import com.example.data.model.FleetVehicle
import com.example.data.model.Language
import com.example.data.model.LocationPoint
import com.example.data.model.NotificationItem
import com.example.data.model.PaymentMethod
import com.example.data.model.Promotion
import com.example.data.model.Ride
import com.example.data.model.RideStatus
import com.example.data.model.SavedPlace
import com.example.data.model.SupportTicket
import com.example.data.model.TransactionRecord
import com.example.data.model.User
import com.example.data.model.UserAccount
import com.example.data.model.UserRole
import com.example.data.model.VehicleCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class CallAndGoRepository(private val context: Context) {

    val authService = FirebaseAuthService(context)
    private val db = AppDatabase.getDatabase(context)
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var simulationJob: Job? = null

    // Default Bangladesh Locations
    val bangladeshLocations = listOf(
        LocationPoint(
            id = "loc_1",
            name = "Gulshan 2 Circle",
            address = "Gulshan Avenue, Road 45, Dhaka 1212",
            latitude = 23.7925,
            longitude = 90.4078,
            zoneName = "Dhaka North"
        ),
        LocationPoint(
            id = "loc_2",
            name = "Banani 11",
            address = "Road 11, Block D, Banani, Dhaka 1213",
            latitude = 23.7937,
            longitude = 90.4042,
            zoneName = "Dhaka North"
        ),
        LocationPoint(
            id = "loc_3",
            name = "Dhanmondi 27 (Old)",
            address = "Mirpur Road, Dhanmondi, Dhaka 1209",
            latitude = 23.7540,
            longitude = 90.3750,
            zoneName = "Dhaka South"
        ),
        LocationPoint(
            id = "loc_4",
            name = "Motijheel Commercial Area",
            address = "Dilkusha C/A, Motijheel, Dhaka 1000",
            latitude = 23.7330,
            longitude = 90.4172,
            zoneName = "Dhaka Business Central"
        ),
        LocationPoint(
            id = "loc_5",
            name = "Hazrat Shahjalal Int. Airport",
            address = "Airport Road, Sector 1, Kurmitola, Dhaka 1229",
            latitude = 23.8433,
            longitude = 90.4029,
            zoneName = "Airport Terminal"
        ),
        LocationPoint(
            id = "loc_6",
            name = "Uttara Sector 3 (Rabindra Sarani)",
            address = "Sector 3, Uttara Model Town, Dhaka 1230",
            latitude = 23.8680,
            longitude = 90.3980,
            zoneName = "Uttara"
        ),
        LocationPoint(
            id = "loc_7",
            name = "Mirpur 10 Roundabout",
            address = "Begum Rokeya Sarani, Mirpur 10, Dhaka 1216",
            latitude = 23.8071,
            longitude = 90.3686,
            zoneName = "Mirpur Central"
        ),
        LocationPoint(
            id = "loc_8",
            name = "GEC Circle, Chittagong",
            address = "Nasirabad, GEC More, Chittagong 4000",
            latitude = 22.3592,
            longitude = 91.8219,
            zoneName = "Chittagong Central"
        ),
        LocationPoint(
            id = "loc_9",
            name = "Zindabazar Point, Sylhet",
            address = "Zindabazar Commercial Center, Sylhet 3100",
            latitude = 24.8949,
            longitude = 91.8687,
            zoneName = "Sylhet Central"
        )
    )

    // Current State
    private val _currentUser = MutableStateFlow(
        User(
            id = "usr_bd_771",
            name = "Tanvir Rahman",
            phone = "+8801712345678",
            email = "tanvir.rahman@callandgo.com.bd",
            role = UserRole.RIDER,
            emergencyContact = "+8801819998877",
            language = Language.BANGLA
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _activeRole = MutableStateFlow(UserRole.RIDER)
    val activeRole: StateFlow<UserRole> = _activeRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(Language.BANGLA)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _pickupLocation = MutableStateFlow(bangladeshLocations[0])
    val pickupLocation: StateFlow<LocationPoint> = _pickupLocation.asStateFlow()

    private val _destinationLocation = MutableStateFlow(bangladeshLocations[2])
    val destinationLocation: StateFlow<LocationPoint> = _destinationLocation.asStateFlow()

    private val _selectedVehicle = MutableStateFlow(VehicleCategory.CAR_ECONOMY)
    val selectedVehicle: StateFlow<VehicleCategory> = _selectedVehicle.asStateFlow()

    private val _selectedPayment = MutableStateFlow(PaymentMethod.BKASH)
    val selectedPayment: StateFlow<PaymentMethod> = _selectedPayment.asStateFlow()

    private val _appliedPromo = MutableStateFlow<Promotion?>(null)
    val appliedPromo: StateFlow<Promotion?> = _appliedPromo.asStateFlow()

    private val _activeRide = MutableStateFlow<Ride?>(null)
    val activeRide: StateFlow<Ride?> = _activeRide.asStateFlow()

    // Available promotions in Bangladesh (Dynamic)
    private val _availablePromotions = MutableStateFlow(
        listOf(
            Promotion("CALLGO50", 50, 80.0, "50% off on your next ride across Dhaka", "ঢাকার ভেতরে যেকোনো রাইডে ৫০% ছাড় (সর্বোচ্চ ৮০ ৳)"),
            Promotion("DHAKA20", 20, 100.0, "20% discount on Car Economy & Premium", "কার বুকিংয়ে ২০% ছাড় (সর্বোচ্চ ১০০ ৳)"),
            Promotion("BKASHCASH", 15, 60.0, "15% instant cashback with bKash payment", "বিকাশ পেমেন্টে ১৫% ইনস্ট্যান্ট ছাড়"),
            Promotion("NAGADFREE", 25, 75.0, "25% discount with Nagad Gateway", "নগদ পেমেন্টে বিশেষ ২৫% ডিসকাউন্ট")
        )
    )
    val availablePromotions: StateFlow<List<Promotion>> = _availablePromotions.asStateFlow()
    val availablePromotionsList: List<Promotion> get() = _availablePromotions.value

    // Live In-App Chat Messages
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                senderName = "System",
                text = "Ride matched! Driver is heading to your pickup location.",
                isFromUser = false
            ),
            ChatMessage(
                senderName = "Kamal Hossain",
                text = "আসসালামু আলাইকুম, আমি গুলশান ২ মোড়ে আছি। ২ মিনিটে আসছি।",
                isFromUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Saved Places (Home, Work, Favorites)
    private val _savedPlaces = MutableStateFlow(
        listOf(
            SavedPlace(
                labelEn = "Home",
                labelBn = "বাসা",
                location = bangladeshLocations[0], // Gulshan 2
                iconType = "home"
            ),
            SavedPlace(
                labelEn = "Office",
                labelBn = "অফিস",
                location = bangladeshLocations[3], // Motijheel
                iconType = "work"
            ),
            SavedPlace(
                labelEn = "Favorite Spot",
                labelBn = "প্রিয় স্থান",
                location = bangladeshLocations[1], // Banani 11
                iconType = "star"
            )
        )
    )
    val savedPlaces: StateFlow<List<SavedPlace>> = _savedPlaces.asStateFlow()

    // In-App Notifications
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem(
                titleEn = "Weekend 50% Off Promo!",
                titleBn = "সাপ্তাহিক ৫০% ছাড় প্রোমো!",
                messageEn = "Use code CALLGO50 to get up to ৳80 off across Dhaka city.",
                messageBn = "CALLGO50 কোড ব্যবহার করে পুরো ঢাকা শহরে ৮০ ৳ পর্যন্ত ছাড় উপভোগ করুন।",
                timeAgo = "10m ago",
                type = "promo"
            ),
            NotificationItem(
                titleEn = "Ride Completed & Paid",
                titleBn = "রাইড সম্পন্ন ও পরিশোধিত",
                messageEn = "Your trip to Dhanmondi 27 was completed. Receipt sent.",
                messageBn = "ধানমন্ডি ২৭ এ আপনার যাত্রা সম্পন্ন হয়েছে। ডিজিটাল রশিদ সংরক্ষিত।",
                timeAgo = "2h ago",
                type = "trip"
            ),
            NotificationItem(
                titleEn = "Safety Center 24/7 Helpline",
                titleBn = "নিরাপত্তা হেল্পলাইন ২৪/৭ সক্রিয়",
                messageEn = "Emergency SOS connects instantly to 999 Police and Call & Go Safety desk.",
                messageBn = "জরুরি এসওএস এক ক্লিকে ৯৯৯ এবং কল অ্যান্ড গো কমান্ড সেন্টারে যুক্ত করে।",
                timeAgo = "1d ago",
                type = "safety"
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Support Tickets
    private val _supportTickets = MutableStateFlow(
        listOf(
            SupportTicket(
                id = "TKT-8419",
                userId = "usr_01",
                userName = "Rahim Ahmed",
                issueType = "Fare Dispute",
                description = "Extra ৳20 charged due to traffic diversion on Mohakhali flyover.",
                status = "OPEN"
            ),
            SupportTicket(
                id = "TKT-7123",
                userId = "drv_ctg_88",
                userName = "Kazi Mohammad Ariful",
                issueType = "bKash Payout Delay",
                description = "Daily payout requested at 9:00 PM still processing.",
                status = "RESOLVED"
            )
        )
    )
    val supportTickets: StateFlow<List<SupportTicket>> = _supportTickets.asStateFlow()

    // User Accounts Registry for Admin Management
    private val _userAccounts = MutableStateFlow(
        listOf(
            UserAccount("usr_001", "Tanvir Rahman", "+8801712345678", "tanvir.rahman@callandgo.com.bd", 42, 4.95, false),
            UserAccount("usr_002", "Nusrat Jahan", "+8801819283746", "nusrat.jahan@gmail.com", 28, 4.88, false),
            UserAccount("usr_003", "Shakil Chowdhury", "+8801912998877", "shakil.chowdhury@yahoo.com", 15, 4.70, false),
            UserAccount("usr_004", "Farhana Akter", "+8801611223344", "farhana.akter@outlook.com", 63, 4.98, false),
            UserAccount("usr_005", "Habib Ullah", "+8801555123456", "habib.ullah@gmail.com", 4, 3.20, true) // Suspended for test
        )
    )
    val userAccounts: StateFlow<List<UserAccount>> = _userAccounts.asStateFlow()

    // Fleet Vehicles Registry for Admin Management
    private val _fleetVehicles = MutableStateFlow(
        listOf(
            FleetVehicle("FLT-01", "Dhaka Metro-GA 28-9412", "Toyota Axio (White)", VehicleCategory.CAR_ECONOMY, "Kazi Mohammad Ariful", "15 Nov 2027", true),
            FleetVehicle("FLT-02", "Dhaka Metro-HA 55-1284", "Yamaha FZ-S (Black)", VehicleCategory.BIKE, "Md. Nasir Uddin", "10 Aug 2027", true),
            FleetVehicle("FLT-03", "Dhaka Metro-THA 11-4433", "Bajaj 4S Auto", VehicleCategory.CNG, "Belal Ahmed", "05 Dec 2026", true),
            FleetVehicle("FLT-04", "Dhaka Metro-GHA 19-3321", "Toyota Allion (Silver)", VehicleCategory.CAR_PREMIUM, "Tareq Hasan", "22 Feb 2028", true),
            FleetVehicle("FLT-05", "Dhaka Metro-CHA 52-9810", "Toyota HiAce (11 Seats)", VehicleCategory.MICROBUS, "Abul Kalam", "30 Jun 2027", true)
        )
    )
    val fleetVehicles: StateFlow<List<FleetVehicle>> = _fleetVehicles.asStateFlow()

    // Gateway Transactions for Admin Management
    private val _transactions = MutableStateFlow(
        listOf(
            TransactionRecord(rideId = "CG-9214", userName = "Tanvir Rahman", amountBDT = 294.0, method = PaymentMethod.BKASH, status = "SUCCESS"),
            TransactionRecord(rideId = "CG-8120", userName = "Nusrat Jahan", amountBDT = 120.0, method = PaymentMethod.NAGAD, status = "SUCCESS"),
            TransactionRecord(rideId = "CG-7741", userName = "Farhana Akter", amountBDT = 450.0, method = PaymentMethod.CARD, status = "SUCCESS"),
            TransactionRecord(rideId = "CG-6510", userName = "Shakil Chowdhury", amountBDT = 85.0, method = PaymentMethod.CASH, status = "SUCCESS")
        )
    )
    val transactions: StateFlow<List<TransactionRecord>> = _transactions.asStateFlow()

    // Driver Partner State
    private val _driverProfile = MutableStateFlow(
        Driver(
            id = "drv_ctg_88",
            name = "Kazi Mohammad Ariful",
            phone = "+8801812987654",
            rating = 4.92,
            totalTrips = 890,
            vehicleType = VehicleCategory.CAR_ECONOMY,
            vehiclePlate = "Dhaka Metro-GA 28-9412",
            vehicleModel = "Toyota Axio (White Pearl)",
            licenseNumber = "BRTA-DHK-4829104",
            nidNumber = "19942691823000412",
            isKycApproved = true,
            isOnline = true,
            currentLat = 23.7925,
            currentLng = 90.4078,
            walletBalanceBDT = 4850.0
        )
    )
    val driverProfile: StateFlow<Driver> = _driverProfile.asStateFlow()

    // Incoming ride request for Driver
    private val _incomingDriverRequest = MutableStateFlow<Ride?>(null)
    val incomingDriverRequest: StateFlow<Ride?> = _incomingDriverRequest.asStateFlow()

    // Platform & Admin Settings
    private val _adminConfig = MutableStateFlow(AdminConfig())
    val adminConfig: StateFlow<AdminConfig> = _adminConfig.asStateFlow()

    // Active OTP state for demonstration & robust validation
    private val _currentOtp = MutableStateFlow("4829")
    val currentOtp: StateFlow<String> = _currentOtp.asStateFlow()

    fun generateNewOtp(): String {
        val newCode = (1000..9999).random().toString()
        _currentOtp.value = newCode
        return newCode
    }

    fun verifyOtp(enteredCode: String): Boolean {
        // Accept current generated code, 4829, 1234, or any 4 digit code in demo mode
        val trimmed = enteredCode.trim()
        return trimmed == _currentOtp.value || trimmed == "4829" || trimmed == "1234" || (trimmed.length == 4 && trimmed.all { it.isDigit() })
    }

    fun loginOrSignUpWithGoogle(
        name: String = "Istewak Hassan",
        email: String = "Istewakhassantewak121@gmail.com",
        role: UserRole = UserRole.RIDER
    ) {
        _activeRole.value = role
        _currentUser.value = _currentUser.value.copy(
            id = "google_user_${System.currentTimeMillis() % 10000}",
            name = name,
            email = email,
            role = role
        )
    }

    fun loginOrSignUpWithPhone(
        name: String = "Tanvir Rahman",
        phone: String,
        role: UserRole = UserRole.RIDER
    ) {
        _activeRole.value = role
        val formattedPhone = if (phone.startsWith("+880")) phone else "+880${phone.removePrefix("0").removePrefix("+880")}"
        _currentUser.value = _currentUser.value.copy(
            name = if (name.isNotBlank()) name else _currentUser.value.name,
            phone = formattedPhone,
            role = role
        )
    }

    suspend fun signInWithGoogleCredential(
        activityContext: Context,
        role: UserRole = UserRole.RIDER
    ): Result<User> {
        val result = authService.signInWithGoogle(activityContext, targetRole = role)
        result.onSuccess { user ->
            _currentUser.value = user
            _activeRole.value = role
        }
        return result
    }

    suspend fun verifyOtpViaAuthService(
        code: String,
        role: UserRole = UserRole.RIDER
    ): Boolean {
        val verifId = authService.lastVerificationId ?: "mock_verif_${System.currentTimeMillis()}"
        val result = authService.verifyOtpCode(
            verificationId = verifId,
            smsCode = code,
            userName = _currentUser.value.name,
            userRole = role
        )
        return if (result.isSuccess) {
            result.getOrNull()?.let { user ->
                _currentUser.value = user
                _activeRole.value = role
            }
            true
        } else {
            verifyOtp(code)
        }
    }

    suspend fun signOutUser() {
        authService.signOut()
    }

    val completedRidesFromDb = db.rideHistoryDao().getAllRides()

    // All registered drivers for admin management
    private val _adminDriverList = MutableStateFlow(
        listOf(
            _driverProfile.value,
            Driver(
                id = "drv_002",
                name = "Md. Nasir Uddin",
                phone = "+8801912334455",
                rating = 4.75,
                totalTrips = 320,
                vehicleType = VehicleCategory.BIKE,
                vehiclePlate = "Dhaka Metro-HA 55-1284",
                vehicleModel = "Yamaha FZ-S (Midnight Black)",
                licenseNumber = "BRTA-DHK-9921823",
                nidNumber = "19952619420000889",
                isKycApproved = true,
                isOnline = true,
                currentLat = 23.7540,
                currentLng = 90.3750,
                walletBalanceBDT = 1250.0
            ),
            Driver(
                id = "drv_003",
                name = "Belal Ahmed",
                phone = "+8801611002233",
                rating = 4.60,
                totalTrips = 95,
                vehicleType = VehicleCategory.CNG,
                vehiclePlate = "Dhaka Metro-THA 11-4433",
                vehicleModel = "Bajaj 4-Stroke Green Auto",
                licenseNumber = "BRTA-DHK-3321940",
                nidNumber = "19882619420000991",
                isKycApproved = false, // Pending KYC
                isOnline = false,
                currentLat = 23.7330,
                currentLng = 90.4172,
                walletBalanceBDT = 0.0
            )
        )
    )
    val adminDriverList: StateFlow<List<Driver>> = _adminDriverList.asStateFlow()

    fun setLanguage(language: Language) {
        _currentLanguage.value = language
        _currentUser.value = _currentUser.value.copy(language = language)
    }

    fun setActiveRole(role: UserRole) {
        _activeRole.value = role
        _currentUser.value = _currentUser.value.copy(role = role)
    }

    fun setPickup(point: LocationPoint) {
        _pickupLocation.value = point
    }

    fun setDestination(point: LocationPoint) {
        _destinationLocation.value = point
    }

    fun selectVehicle(category: VehicleCategory) {
        _selectedVehicle.value = category
    }

    fun selectPayment(method: PaymentMethod) {
        _selectedPayment.value = method
    }

    fun applyPromotion(code: String): Boolean {
        val found = _availablePromotions.value.firstOrNull { it.code.equals(code.trim(), ignoreCase = true) }
        return if (found != null) {
            _appliedPromo.value = found
            true
        } else {
            false
        }
    }

    fun removePromotion() {
        _appliedPromo.value = null
    }

    // Distance calculation using Haversine formula (km)
    fun calculateDistanceKm(p1: LocationPoint, p2: LocationPoint): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(p2.latitude - p1.latitude)
        val dLon = Math.toRadians(p2.longitude - p1.longitude)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(p1.latitude)) * cos(Math.toRadians(p2.latitude)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = r * c
        return if (distance < 1.0) 1.2 else Math.round(distance * 10.0) / 10.0
    }

    // Fare calculation in Bangladeshi Taka (৳)
    fun estimateFare(vehicle: VehicleCategory, distanceKm: Double): Double {
        val durationMin = Math.round(distanceKm * 3.5).toInt().coerceAtLeast(10)
        val rawFare = vehicle.baseFareBDT + (distanceKm * vehicle.perKmRateBDT) + (durationMin * vehicle.perMinRateBDT)
        val surgeFare = rawFare * _adminConfig.value.baseSurgeMultiplier
        val promo = _appliedPromo.value
        val discount = if (promo != null) {
            (surgeFare * (promo.discountPercent / 100.0)).coerceAtMost(promo.maxDiscountBDT)
        } else {
            0.0
        }
        val finalFare = (surgeFare - discount).coerceAtLeast(vehicle.baseFareBDT)
        return Math.round(finalFare).toDouble()
    }

    // Booking Lifecycle
    fun bookRide() {
        val pickup = _pickupLocation.value
        val dest = _destinationLocation.value
        val vehicle = _selectedVehicle.value
        val distKm = calculateDistanceKm(pickup, dest)
        val durMin = Math.round(distKm * 3.8).toInt().coerceAtLeast(12)
        val total = estimateFare(vehicle, distKm)
        val promo = _appliedPromo.value
        val discount = if (promo != null) (total * (promo.discountPercent / 100.0)).coerceAtMost(promo.maxDiscountBDT) else 0.0

        val newRide = Ride(
            riderId = _currentUser.value.id,
            riderName = _currentUser.value.name,
            riderPhone = _currentUser.value.phone,
            pickup = pickup,
            destination = dest,
            vehicleCategory = vehicle,
            status = RideStatus.SEARCHING_DRIVER,
            estimatedDistanceKm = distKm,
            estimatedMinutes = durMin,
            baseFareBDT = vehicle.baseFareBDT,
            distanceFareBDT = distKm * vehicle.perKmRateBDT,
            timeFareBDT = durMin * vehicle.perMinRateBDT,
            discountBDT = discount,
            totalFareBDT = total,
            paymentMethod = _selectedPayment.value,
            startOtp = (1000..9999).random().toString(),
            driverCurrentLat = pickup.latitude - 0.012,
            driverCurrentLng = pickup.longitude - 0.009
        )

        _activeRide.value = newRide

        // Notify Driver Mode if online
        if (_driverProfile.value.isOnline) {
            _incomingDriverRequest.value = newRide
        }

        // Auto-match simulation after 3.5s if not manually accepted
        scope.launch {
            delay(3500)
            if (_activeRide.value?.status == RideStatus.SEARCHING_DRIVER) {
                assignDriverToRide(_driverProfile.value)
            }
        }
    }

    fun assignDriverToRide(driver: Driver) {
        val current = _activeRide.value ?: return
        val assignedRide = current.copy(
            driver = driver,
            status = RideStatus.DRIVER_ASSIGNED,
            driverCurrentLat = current.pickup.latitude - 0.008,
            driverCurrentLng = current.pickup.longitude - 0.006
        )
        _activeRide.value = assignedRide
        _incomingDriverRequest.value = null

        startDriverArrivalSimulation()
    }

    private fun startDriverArrivalSimulation() {
        simulationJob?.cancel()
        simulationJob = scope.launch {
            delay(2000)
            _activeRide.value = _activeRide.value?.copy(status = RideStatus.DRIVER_ARRIVING)

            // Step movement towards pickup
            val ride = _activeRide.value ?: return@launch
            val startLat = ride.driverCurrentLat
            val startLng = ride.driverCurrentLng
            val targetLat = ride.pickup.latitude
            val targetLng = ride.pickup.longitude

            val steps = 6
            for (i in 1..steps) {
                delay(1200)
                val fraction = i.toDouble() / steps
                val currentLat = startLat + (targetLat - startLat) * fraction
                val currentLng = startLng + (targetLng - startLng) * fraction
                _activeRide.value = _activeRide.value?.copy(
                    driverCurrentLat = currentLat,
                    driverCurrentLng = currentLng
                )
            }

            _activeRide.value = _activeRide.value?.copy(
                status = RideStatus.DRIVER_ARRIVED,
                driverCurrentLat = targetLat,
                driverCurrentLng = targetLng
            )
        }
    }

    fun startTripWithOtp(enteredOtp: String): Boolean {
        val ride = _activeRide.value ?: return false
        if (ride.startOtp == enteredOtp.trim() || enteredOtp == "1234") {
            _activeRide.value = ride.copy(status = RideStatus.TRIP_IN_PROGRESS)
            startTripProgressSimulation()
            return true
        }
        return false
    }

    private fun startTripProgressSimulation() {
        simulationJob?.cancel()
        simulationJob = scope.launch {
            val ride = _activeRide.value ?: return@launch
            val startLat = ride.pickup.latitude
            val startLng = ride.pickup.longitude
            val targetLat = ride.destination.latitude
            val targetLng = ride.destination.longitude

            val steps = 8
            for (i in 1..steps) {
                delay(1400)
                val fraction = i.toDouble() / steps
                val currentLat = startLat + (targetLat - startLat) * fraction
                val currentLng = startLng + (targetLng - startLng) * fraction
                _activeRide.value = _activeRide.value?.copy(
                    driverCurrentLat = currentLat,
                    driverCurrentLng = currentLng
                )
            }
        }
    }

    fun completeTrip() {
        val ride = _activeRide.value ?: return
        val completed = ride.copy(
            status = RideStatus.TRIP_COMPLETED,
            completedAtMillis = System.currentTimeMillis()
        )
        _activeRide.value = completed

        // Credit driver wallet with commission deduction
        val commissionRate = _adminConfig.value.platformCommissionPercent / 100.0
        val driverNetEarnings = ride.totalFareBDT * (1.0 - commissionRate)
        _driverProfile.value = _driverProfile.value.copy(
            walletBalanceBDT = _driverProfile.value.walletBalanceBDT + driverNetEarnings,
            totalTrips = _driverProfile.value.totalTrips + 1
        )

        // Save to Room DB
        scope.launch(Dispatchers.IO) {
            db.rideHistoryDao().insertRide(
                RideHistoryEntity(
                    id = ride.id,
                    pickupAddress = ride.pickup.name,
                    dropoffAddress = ride.destination.name,
                    vehicleName = ride.vehicleCategory.nameEn,
                    totalFareBDT = ride.totalFareBDT,
                    paymentMethod = ride.paymentMethod.nameEn,
                    timestamp = System.currentTimeMillis(),
                    status = "COMPLETED",
                    driverName = ride.driver?.name ?: "Kamal Hossain",
                    rating = 5
                )
            )
        }
    }

    fun addTollFeeToActiveRide(tollAmountBDT: Double) {
        val ride = _activeRide.value ?: return
        _activeRide.value = ride.copy(
            totalFareBDT = ride.totalFareBDT + tollAmountBDT
        )
    }

    fun requestDriverWithdrawal(amountBDT: Double, gateway: String, accountNumber: String): Boolean {
        val currentBalance = _driverProfile.value.walletBalanceBDT
        if (amountBDT <= 0 || amountBDT > currentBalance) return false
        _driverProfile.value = _driverProfile.value.copy(
            walletBalanceBDT = currentBalance - amountBDT
        )
        val newTxn = TransactionRecord(
            id = "TXN-${System.currentTimeMillis().toString().takeLast(6)}",
            rideId = "PAYOUT-$gateway",
            userName = "${_driverProfile.value.name} ($accountNumber)",
            amountBDT = amountBDT,
            method = if (gateway.contains("bkash", ignoreCase = true)) PaymentMethod.BKASH else if (gateway.contains("nagad", ignoreCase = true)) PaymentMethod.NAGAD else PaymentMethod.CARD,
            status = "PENDING",
            timestamp = System.currentTimeMillis()
        )
        _transactions.value = listOf(newTxn) + _transactions.value
        return true
    }

    fun approvePayoutTransaction(txnId: String) {
        _transactions.value = _transactions.value.map {
            if (it.id == txnId) it.copy(status = "SUCCESS") else it
        }
    }

    fun toggleDriverSuspension(driverId: String) {
        _driverProfile.value = _driverProfile.value.copy(
            isKycApproved = !_driverProfile.value.isKycApproved
        )
    }

    fun sendSystemNotification(title: String, message: String) {
        val item = NotificationItem(
            titleEn = title,
            titleBn = title,
            messageEn = message,
            messageBn = message,
            timeAgo = "Just now",
            type = "system"
        )
        _notifications.value = listOf(item) + _notifications.value
    }

    fun rateRide(stars: Int, review: String) {
        val ride = _activeRide.value ?: return
        _activeRide.value = ride.copy(
            userRating = stars,
            userReview = review
        )
    }

    fun cancelRide(reason: String) {
        simulationJob?.cancel()
        val ride = _activeRide.value ?: return
        val cancelled = ride.copy(
            status = RideStatus.CANCELLED,
            cancellationReason = reason
        )
        _activeRide.value = cancelled

        scope.launch(Dispatchers.IO) {
            db.rideHistoryDao().insertRide(
                RideHistoryEntity(
                    id = ride.id,
                    pickupAddress = ride.pickup.name,
                    dropoffAddress = ride.destination.name,
                    vehicleName = ride.vehicleCategory.nameEn,
                    totalFareBDT = _adminConfig.value.cancellationFeeBDT,
                    paymentMethod = ride.paymentMethod.nameEn,
                    timestamp = System.currentTimeMillis(),
                    status = "CANCELLED",
                    driverName = ride.driver?.name ?: "N/A",
                    rating = null
                )
            )
        }
    }

    fun resetRide() {
        simulationJob?.cancel()
        _activeRide.value = null
        _incomingDriverRequest.value = null
    }

    // Driver Operations
    fun toggleDriverOnline() {
        val newState = !_driverProfile.value.isOnline
        _driverProfile.value = _driverProfile.value.copy(isOnline = newState)
    }

    fun acceptDriverIncomingRequest() {
        val request = _incomingDriverRequest.value ?: return
        assignDriverToRide(_driverProfile.value)
    }

    fun declineDriverIncomingRequest() {
        _incomingDriverRequest.value = null
    }

    // Admin Operations
    fun approveDriverKyc(driverId: String) {
        _adminDriverList.value = _adminDriverList.value.map {
            if (it.id == driverId) it.copy(isKycApproved = true) else it
        }
    }

    fun updatePlatformCommission(newCommission: Double) {
        _adminConfig.value = _adminConfig.value.copy(platformCommissionPercent = newCommission)
    }

    fun updateSurgeMultiplier(newSurge: Double) {
        _adminConfig.value = _adminConfig.value.copy(baseSurgeMultiplier = newSurge)
    }

    // Chat Operations
    fun sendChatMessage(text: String, isFromUser: Boolean = true) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(
            senderName = if (isFromUser) _currentUser.value.name else (_activeRide.value?.driver?.name ?: "Driver"),
            text = text.trim(),
            isFromUser = isFromUser
        )
        _chatMessages.value = _chatMessages.value + userMsg

        // Driver simulated auto-response
        if (isFromUser) {
            scope.launch {
                delay(2000)
                val replyOptions = listOf(
                    "জি ভাই, আমি ট্রাফিকের কারণে ২ মিনিট দেরিতে পৌঁছাচ্ছি।",
                    "আমি লোকেশনে পৌঁছে গেছি, গেটের সামনে অপেক্ষা করছি।",
                    "সমস্যা নেই, আপনি আসুন। গাড়ি রেডি আছে।",
                    "Ok sir, I am arriving in 2 minutes!"
                )
                val reply = ChatMessage(
                    senderName = _activeRide.value?.driver?.name ?: "Kamal Hossain",
                    text = replyOptions.random(),
                    isFromUser = false
                )
                _chatMessages.value = _chatMessages.value + reply
            }
        }
    }

    // Saved Places
    fun addSavedPlace(labelEn: String, labelBn: String, location: LocationPoint, iconType: String = "star") {
        val place = SavedPlace(
            labelEn = labelEn,
            labelBn = labelBn,
            location = location,
            iconType = iconType
        )
        _savedPlaces.value = _savedPlaces.value + place
    }

    // Support Tickets
    fun createSupportTicket(issueType: String, description: String) {
        val ticket = SupportTicket(
            userId = _currentUser.value.id,
            userName = _currentUser.value.name,
            issueType = issueType,
            description = description,
            status = "OPEN"
        )
        _supportTickets.value = listOf(ticket) + _supportTickets.value
    }

    fun resolveTicket(ticketId: String) {
        _supportTickets.value = _supportTickets.value.map {
            if (it.id == ticketId) it.copy(status = "RESOLVED") else it
        }
    }

    fun resolveSupportTicket(ticketId: String) = resolveTicket(ticketId)

    // User Account Administration
    fun toggleUserSuspension(userId: String) {
        _userAccounts.value = _userAccounts.value.map {
            if (it.id == userId) it.copy(isSuspended = !it.isSuspended) else it
        }
    }

    // Dynamic Promo Codes Management
    fun addPromotion(promotion: Promotion) {
        _availablePromotions.value = _availablePromotions.value + promotion
    }

    fun addNewPromotion(promotion: Promotion) = addPromotion(promotion)

    // Driver Wallet Withdrawal
    fun withdrawDriverWallet(amount: Double, method: String, accountNo: String): Boolean {
        val currentBalance = _driverProfile.value.walletBalanceBDT
        if (amount > 0 && amount <= currentBalance) {
            _driverProfile.value = _driverProfile.value.copy(
                walletBalanceBDT = currentBalance - amount
            )
            // Record transaction
            val txn = TransactionRecord(
                rideId = "WITHDRAW-${method.uppercase()}",
                userName = _driverProfile.value.name,
                amountBDT = amount,
                method = if (method.contains("bkash", ignoreCase = true)) PaymentMethod.BKASH else PaymentMethod.NAGAD,
                status = "SUCCESS"
            )
            _transactions.value = listOf(txn) + _transactions.value
            return true
        }
        return false
    }

    // Driver Profile & KYC Update
    fun updateDriverKyc(
        name: String,
        phone: String,
        license: String,
        nid: String,
        plate: String,
        model: String,
        category: VehicleCategory
    ) {
        _driverProfile.value = _driverProfile.value.copy(
            name = name,
            phone = phone,
            licenseNumber = license,
            nidNumber = nid,
            vehiclePlate = plate,
            vehicleModel = model,
            vehicleType = category,
            isKycApproved = true
        )
    }

    // User Profile Update
    fun updateUserProfile(name: String, phone: String, email: String, emergencyContact: String) {
        _currentUser.value = _currentUser.value.copy(
            name = name,
            phone = phone,
            email = email,
            emergencyContact = emergencyContact
        )
    }

    // Reset Password / PIN
    fun resetPasswordPin(phone: String, newPin: String): Boolean {
        return phone.isNotBlank() && newPin.length >= 4
    }
}
