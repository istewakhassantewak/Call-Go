package com.example.data.model

import java.util.UUID

enum class UserRole {
    RIDER,
    DRIVER,
    ADMIN
}

enum class Language(val code: String, val label: String) {
    BANGLA("bn", "বাংলা"),
    ENGLISH("en", "English")
}

data class User(
    val id: String = "usr_${UUID.randomUUID().toString().take(8)}",
    val name: String = "Rahim Ahmed",
    val phone: String = "+8801711223344",
    val email: String = "rahim.ahmed@callandgo.com.bd",
    val role: UserRole = UserRole.RIDER,
    val profilePicUrl: String = "",
    val rating: Double = 4.9,
    val emergencyContact: String = "+8801811998877",
    val language: Language = Language.ENGLISH
)

enum class VehicleCategory(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val baseFareBDT: Double,
    val perKmRateBDT: Double,
    val perMinRateBDT: Double,
    val capacity: Int,
    val iconName: String
) {
    BIKE(
        id = "bike",
        nameEn = "Call&Go Bike",
        nameBn = "কল অ্যান্ড গো বাইক",
        descriptionEn = "Fastest through Dhaka traffic",
        descriptionBn = "ঢাকার যানজটে দ্রুততম যাত্রা",
        baseFareBDT = 35.0,
        perKmRateBDT = 14.0,
        perMinRateBDT = 1.0,
        capacity = 1,
        iconName = "two_wheeler"
    ),
    CNG(
        id = "cng",
        nameEn = "CNG Auto-Rickshaw",
        nameBn = "সিএনজি অটো-রিকশা",
        descriptionEn = "Affordable 3-wheeler for up to 3",
        descriptionBn = "সাশ্রয়ী ও সুরক্ষিত ৩ জনের যাত্রা",
        baseFareBDT = 55.0,
        perKmRateBDT = 18.0,
        perMinRateBDT = 1.5,
        capacity = 3,
        iconName = "electric_rickshaw"
    ),
    CAR_ECONOMY(
        id = "car_eco",
        nameEn = "Car Economy",
        nameBn = "কার ইকোনমি",
        descriptionEn = "Comfortable AC hatchback/compact",
        descriptionBn = "আরামদায়ক এসি হ্যাচব্যাক গাড়ি",
        baseFareBDT = 85.0,
        perKmRateBDT = 24.0,
        perMinRateBDT = 2.5,
        capacity = 4,
        iconName = "directions_car"
    ),
    CAR_PREMIUM(
        id = "car_prem",
        nameEn = "Car Premium Sedan",
        nameBn = "কার প্রিমিয়াম সিডান",
        descriptionEn = "Top-tier sedans with high rated drivers",
        descriptionBn = "অভিজাত সিডান ও সেরা ড্রাইভার",
        baseFareBDT = 140.0,
        perKmRateBDT = 34.0,
        perMinRateBDT = 3.5,
        capacity = 4,
        iconName = "airport_shuttle"
    ),
    MICROBUS(
        id = "microbus",
        nameEn = "Call&Go Microbus",
        nameBn = "কল অ্যান্ড গো মাইক্রোবাস",
        descriptionEn = "HiAce 7-11 seats for family & groups",
        descriptionBn = "পরিবার বা দলের জন্য ৭-১১ সিট",
        baseFareBDT = 320.0,
        perKmRateBDT = 45.0,
        perMinRateBDT = 5.0,
        capacity = 8,
        iconName = "local_shipping"
    )
}

data class LocationPoint(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val zoneName: String = "Dhaka Central"
)

enum class RideStatus {
    IDLE,
    SEARCHING_DRIVER,
    DRIVER_ASSIGNED,
    DRIVER_ARRIVING,
    DRIVER_ARRIVED,
    TRIP_IN_PROGRESS,
    TRIP_COMPLETED,
    CANCELLED
}

enum class PaymentMethod(val id: String, val nameEn: String, val nameBn: String) {
    CASH("cash", "Cash", "নগদ টাকা"),
    BKASH("bkash", "bKash (MFS)", "বিকাশ"),
    NAGAD("nagad", "Nagad (MFS)", "নগদ"),
    CARD("card", "Credit / Debit Card", "কার্ড")
}

data class Driver(
    val id: String = "drv_001",
    val name: String = "Kamal Hossain",
    val phone: String = "+8801822334455",
    val rating: Double = 4.88,
    val totalTrips: Int = 1420,
    val vehicleType: VehicleCategory = VehicleCategory.CAR_ECONOMY,
    val vehiclePlate: String = "Dhaka Metro GA-34-8921",
    val vehicleModel: String = "Toyota Corolla Axio (Silver)",
    val licenseNumber: String = "DK-8839210",
    val nidNumber: String = "19922619420000123",
    val isKycApproved: Boolean = true,
    val isOnline: Boolean = true,
    val currentLat: Double = 23.7925,
    val currentLng: Double = 90.4078,
    val currentHeading: Float = 45f,
    val walletBalanceBDT: Double = 3450.0
)

data class Ride(
    val id: String = "CG-${UUID.randomUUID().toString().take(6).uppercase()}",
    val riderId: String = "usr_01",
    val riderName: String = "Rahim Ahmed",
    val riderPhone: String = "+8801711223344",
    val driver: Driver? = null,
    val pickup: LocationPoint,
    val destination: LocationPoint,
    val vehicleCategory: VehicleCategory = VehicleCategory.CAR_ECONOMY,
    val status: RideStatus = RideStatus.IDLE,
    val estimatedDistanceKm: Double = 6.4,
    val estimatedMinutes: Int = 22,
    val baseFareBDT: Double = 85.0,
    val distanceFareBDT: Double = 153.6,
    val timeFareBDT: Double = 55.0,
    val discountBDT: Double = 0.0,
    val totalFareBDT: Double = 293.6,
    val paymentMethod: PaymentMethod = PaymentMethod.BKASH,
    val startOtp: String = "4829",
    val createdAtMillis: Long = System.currentTimeMillis(),
    val completedAtMillis: Long? = null,
    val userRating: Int? = null,
    val userReview: String? = null,
    val cancellationReason: String? = null,
    val driverCurrentLat: Double = 23.7925,
    val driverCurrentLng: Double = 90.4078
)

data class Promotion(
    val code: String,
    val discountPercent: Int,
    val maxDiscountBDT: Double,
    val descriptionEn: String,
    val descriptionBn: String,
    val expiryText: String = "Valid till 30 Sep 2026"
)

data class SupportTicket(
    val id: String = "TKT-${UUID.randomUUID().toString().take(5).uppercase()}",
    val userId: String,
    val userName: String = "Rahim Ahmed",
    val issueType: String,
    val description: String,
    val status: String = "OPEN",
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val senderName: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class SavedPlace(
    val id: String = UUID.randomUUID().toString(),
    val labelEn: String,
    val labelBn: String,
    val location: LocationPoint,
    val iconType: String = "home" // home, work, star
)

data class NotificationItem(
    val id: String = UUID.randomUUID().toString(),
    val titleEn: String,
    val titleBn: String,
    val messageEn: String,
    val messageBn: String,
    val timeAgo: String,
    val type: String = "promo" // promo, trip, safety, system
)

data class UserAccount(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val totalRides: Int,
    val rating: Double,
    val isSuspended: Boolean = false
)

data class FleetVehicle(
    val id: String,
    val plateNumber: String,
    val model: String,
    val category: VehicleCategory,
    val driverName: String,
    val fitnessExpiry: String,
    val isCompliant: Boolean = true
)

data class TransactionRecord(
    val id: String = "TXN-${UUID.randomUUID().toString().take(6).uppercase()}",
    val rideId: String,
    val userName: String,
    val amountBDT: Double,
    val method: PaymentMethod,
    val status: String = "SUCCESS",
    val timestamp: Long = System.currentTimeMillis()
)

data class AdminConfig(
    val platformCommissionPercent: Double = 12.0,
    val baseSurgeMultiplier: Double = 1.0,
    val nightFareMultiplier: Double = 1.15,
    val cancellationFeeBDT: Double = 40.0,
    val isAppMaintenanceMode: Boolean = false
)
