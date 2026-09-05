package com.example.domain

import com.example.data.model.Language
import com.example.data.model.RideStatus
import com.example.data.model.VehicleCategory

object Localization {

    fun get(key: String, language: Language): String {
        val dict = if (language == Language.BANGLA) banglaStrings else englishStrings
        return dict[key] ?: englishStrings[key] ?: key
    }

    fun getRideStatusText(status: RideStatus, language: Language): String {
        return when (status) {
            RideStatus.IDLE -> if (language == Language.BANGLA) "রাইড শুরু করুন" else "Ready to Ride"
            RideStatus.SEARCHING_DRIVER -> if (language == Language.BANGLA) "নিকটস্থ ড্রাইভার খোঁজা হচ্ছে..." else "Finding nearest driver..."
            RideStatus.DRIVER_ASSIGNED -> if (language == Language.BANGLA) "ড্রাইভার বুকিং নিশ্চিত হয়েছে" else "Driver Confirmed"
            RideStatus.DRIVER_ARRIVING -> if (language == Language.BANGLA) "ড্রাইভার পিকআপের পথে আসছে" else "Driver is arriving at pickup"
            RideStatus.DRIVER_ARRIVED -> if (language == Language.BANGLA) "ড্রাইভার পিকআপ পয়েন্টে পৌঁছেছে" else "Driver has arrived at pickup"
            RideStatus.TRIP_IN_PROGRESS -> if (language == Language.BANGLA) "যাত্রা চলমান রয়েছে" else "Trip in progress"
            RideStatus.TRIP_COMPLETED -> if (language == Language.BANGLA) "যাত্রা সমাপ্ত হয়েছে" else "Trip Completed"
            RideStatus.CANCELLED -> if (language == Language.BANGLA) "রাইড বাতিল করা হয়েছে" else "Ride Cancelled"
        }
    }

    private val englishStrings = mapOf(
        "app_title" to "Call & Go",
        "tagline" to "Fast, Safe & Reliable Ride-Sharing in Bangladesh",
        "role_rider" to "Passenger",
        "role_driver" to "Driver Partner",
        "role_admin" to "Admin Console",
        "pickup_hint" to "Enter pickup location...",
        "dropoff_hint" to "Where to in Bangladesh?...",
        "select_vehicle" to "Select Your Ride",
        "book_ride" to "Book Call & Go",
        "finding_driver" to "Contacting nearby drivers in Dhaka...",
        "driver_info" to "Driver Details",
        "otp_label" to "Ride Start OTP",
        "sos_button" to "Emergency SOS",
        "cancel_ride" to "Cancel Ride",
        "fare_estimate" to "Fare Breakdown",
        "payment_method" to "Payment Option",
        "apply_promo" to "Apply Promo",
        "promo_applied" to "Promo discount applied!",
        "trips_history" to "Trip History",
        "profile" to "My Profile",
        "earnings" to "Driver Earnings",
        "go_online" to "Go Online",
        "go_offline" to "Go Offline",
        "online_status" to "You are online and receiving rides",
        "offline_status" to "You are offline. Toggle to receive requests",
        "accept" to "Accept Ride",
        "decline" to "Decline",
        "arrived_at_pickup" to "Arrived at Pickup Point",
        "enter_otp_prompt" to "Ask Passenger for 4-digit Ride OTP",
        "start_trip" to "Start Trip",
        "complete_trip" to "Complete Trip & Collect ৳",
        "kyc_status" to "BRTA Driver Verification (KYC)",
        "kyc_approved" to "Verified Partner (BRTA Approved)",
        "admin_dashboard" to "Call & Go Admin Operations",
        "total_revenue" to "Platform Revenue",
        "live_rides" to "Live Active Rides",
        "drivers_online" to "Active Drivers Online",
        "emergency_title" to "Bangladesh Emergency Alert",
        "call_999" to "Call National Emergency (999)",
        "call_support" to "Call & Go 24/7 Police Hotline",
        "rate_ride" to "How was your driver?"
    )

    private val banglaStrings = mapOf(
        "app_title" to "কল অ্যান্ড গো",
        "tagline" to "বাংলাদেশে দ্রুত, নিরাপদ ও বিশ্বস্ত রাইড সেবা",
        "role_rider" to "যাত্রী",
        "role_driver" to "ড্রাইভার পার্টনার",
        "role_admin" to "অ্যাডমিন প্যানেল",
        "pickup_hint" to "পিকআপের অবস্থান লিখুন...",
        "dropoff_hint" to "কোথায় যেতে চান?...",
        "select_vehicle" to "বাহন নির্বাচন করুন",
        "book_ride" to "কল অ্যান্ড গো বুক করুন",
        "finding_driver" to "কাছের ড্রাইভারদের সাথে যোগাযোগ করা হচ্ছে...",
        "driver_info" to "ড্রাইভারের তথ্য",
        "otp_label" to "যাত্রা শুরুর ওটিপি",
        "sos_button" to "জরুরি এসওএস",
        "cancel_ride" to "রাইড বাতিল করুন",
        "fare_estimate" to "ভাড়ার বিবরণ",
        "payment_method" to "পেমেন্ট মাধ্যম",
        "apply_promo" to "প্রোমো কোড যোগ করুন",
        "promo_applied" to "প্রোমো ছাড় কার্যকর হয়েছে!",
        "trips_history" to "ভ্রমণ ইতিহাস",
        "profile" to "আমার প্রোফাইল",
        "earnings" to "ড্রাইভার আয় বিবরণ",
        "go_online" to "অনলাইন যান",
        "go_offline" to "অফলাইন যান",
        "online_status" to "আপনি অনলাইন আছেন এবং রাইড গ্রহণ করতে পারবেন",
        "offline_status" to "আপনি অফলাইনে আছেন। রাইড পেতে চালু করুন",
        "accept" to "রাইড গ্রহণ করুন",
        "decline" to "প্রত্যাখ্যান",
        "arrived_at_pickup" to "পিকআপে পৌঁছেছি",
        "enter_otp_prompt" to "যাত্রীর নিকট থেকে ৪ সংখ্যার ওটিপি নিন",
        "start_trip" to "যাত্রা শুরু করুন",
        "complete_trip" to "যাত্রা সমাপ্ত ও ৳ গ্রহণ",
        "kyc_status" to "বিআরটিএ ড্রাইভার ভেরিফিকেশন (কেওয়াইসি)",
        "kyc_approved" to "যাচাইকৃত পার্টনার (অনুমোদিত)",
        "admin_dashboard" to "কল অ্যান্ড গো অ্যাডমিন কনসোল",
        "total_revenue" to "প্ল্যাটফর্ম মোট আয়",
        "live_rides" to "চলমান লাইভ রাইড",
        "drivers_online" to "অনলাইনে সক্রিয় ড্রাইভার",
        "emergency_title" to "জরুরি নিরাপত্তা সেবা",
        "call_999" to "জাতীয় জরুরি সেবা (৯৯৯) কল করুন",
        "call_support" to "কল অ্যান্ড গো ২৪/৭ সহায়তা কেন্দ্র",
        "rate_ride" to "আপনার যাত্রা ও ড্রাইভার কেমন ছিল?"
    )
}
