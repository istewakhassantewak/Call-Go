package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Language
import com.example.data.model.UserRole
import com.example.data.repository.CallAndGoRepository
import com.example.ui.components.BangladeshPhoneInput
import com.example.ui.components.CallAndGoLogo
import com.example.ui.components.CallGoButton
import com.example.ui.components.CallGoCard
import com.example.ui.theme.BkashPink
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
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealLight
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2200)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF071426), Color(0xFF0B2545), Color(0xFF003840))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CallAndGoLogo(
                iconSize = 110.dp,
                showText = true,
                animated = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "দ্রুত ও নিরাপদ যাত্রা • বাংলাদেশ",
                color = OrangePrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Your Trusted Ride in Bangladesh",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(
                color = OrangePrimary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        }

        Text(
            text = "Version 1.0 • Made for Bangladesh",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun OnboardingScreen(
    language: Language,
    onComplete: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }

    val slides = listOf(
        Triple(
            if (language == Language.BANGLA) "ঢাকার যানজটে দ্রুততম রাইড" else "Fastest Rides in Dhaka",
            if (language == Language.BANGLA) "বাইক, সিএনজি ও কার - এক ক্লিকেই দরজায় হাজির।" else "Bikes, CNG auto-rickshaws, and comfortable cars on demand.",
            Icons.Default.Speed
        ),
        Triple(
            if (language == Language.BANGLA) "সহজ ও স্বচ্ছ ভাড়া (৳)" else "Transparent BDT Pricing",
            if (language == Language.BANGLA) "কোনো গোপন চার্জ নেই। বিকাশ, নগদ ও ক্যাশে সহজে পরিশোধ করুন।" else "No hidden surge surprises. Pay effortlessly with bKash, Nagad, or Cash.",
            Icons.Default.TwoWheeler
        ),
        Triple(
            if (language == Language.BANGLA) "সর্বোচ্চ নিরাপত্তা ও জরুরি এসওএস" else "24/7 Safety & SOS Alert",
            if (language == Language.BANGLA) "বিআরটিএ অনুমোদিত ড্রাইভার এবং ৯৯৯ জাতীয় জরুরি সেবার সার্বক্ষণিক সুরক্ষা।" else "BRTA verified drivers and direct integration with Bangladesh 999.",
            Icons.Default.Security
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CallAndGoLogo(iconSize = 40.dp, showText = false)
            TextButton(onClick = onComplete) {
                Text(
                    text = if (language == Language.BANGLA) "এড়িয়ে যান" else "Skip",
                    color = Slate600,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Slide Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        when (currentStep) {
                            0 -> OrangePrimary.copy(alpha = 0.12f)
                            1 -> TealPrimary.copy(alpha = 0.12f)
                            else -> Navy800.copy(alpha = 0.12f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = slides[currentStep].third,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = when (currentStep) {
                        0 -> OrangePrimary
                        1 -> TealPrimary
                        else -> Navy800
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = slides[currentStep].first,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = slides[currentStep].second,
                fontSize = 14.sp,
                color = Slate600,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Indicators & Bottom Button
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(slides.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .height(8.dp)
                            .width(if (currentStep == index) 24.dp else 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (currentStep == index) OrangePrimary else Slate200)
                    )
                }
            }

            CallGoButton(
                text = if (currentStep == slides.lastIndex) {
                    if (language == Language.BANGLA) "শুরু করুন" else "Get Started"
                } else {
                    if (language == Language.BANGLA) "পরবর্তী" else "Next"
                },
                onClick = {
                    if (currentStep < slides.lastIndex) {
                        currentStep++
                    } else {
                        onComplete()
                    }
                },
                leadingIcon = Icons.AutoMirrored.Filled.ArrowForward
            )
        }
    }
}

@Composable
fun LoginScreen(
    language: Language,
    repository: CallAndGoRepository? = null,
    onLoginSuccess: (UserRole) -> Unit,
    onNavigateToOtp: (String) -> Unit
) {
    var isSignUpMode by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("Istewak Hassan") }
    var phoneInput by remember { mutableStateOf("01712345678") }
    var passwordInput by remember { mutableStateOf("password123") }
    var selectedRole by remember { mutableStateOf(UserRole.RIDER) }
    var isLoading by remember { mutableStateOf(false) }
    var showGoogleChooser by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            CallAndGoLogo(iconSize = 64.dp, showText = true)
            Spacer(modifier = Modifier.height(16.dp))

            // Sign In vs Sign Up Tab Switcher
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    // Sign In Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (!isSignUpMode) Navy800 else Color.Transparent)
                            .clickable { isSignUpMode = false }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == Language.BANGLA) "লগইন (Sign In)" else "Sign In",
                            fontSize = 13.sp,
                            fontWeight = if (!isSignUpMode) FontWeight.Bold else FontWeight.Medium,
                            color = if (!isSignUpMode) Color.White else Slate700
                        )
                    }

                    // Sign Up Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSignUpMode) OrangePrimary else Color.Transparent)
                            .clickable { isSignUpMode = true }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == Language.BANGLA) "নতুন একাউন্ট (Sign Up)" else "Sign Up",
                            fontSize = 13.sp,
                            fontWeight = if (isSignUpMode) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSignUpMode) Color.White else Slate700
                        )
                    }
                }
            }

            Text(
                text = if (isSignUpMode) {
                    if (language == Language.BANGLA) "কল অ্যান্ড গো-তে নতুন একাউন্ট খুলুন" else "Create Call & Go Account"
                } else {
                    if (language == Language.BANGLA) "স্বাগতম! আপনার একাউন্টে প্রবেশ করুন" else "Welcome Back! Sign In"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy900,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (language == Language.BANGLA) "বাংলাদেশি মোবাইল নাম্বার অথবা গুগল দিয়ে শুরু করুন" else "Fast & secure ride hailing in Bangladesh",
                fontSize = 12.sp,
                color = Slate600,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
            )

            // Prominent Google Sign-In / Sign-Up Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            isLoading = true
                            if (repository != null) {
                                val result = repository.signInWithGoogleCredential(context, selectedRole)
                                isLoading = false
                                if (result.isSuccess) {
                                    onLoginSuccess(selectedRole)
                                } else {
                                    showGoogleChooser = true
                                }
                            } else {
                                isLoading = false
                                showGoogleChooser = true
                            }
                        }
                    },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, Color(0xFF4285F4).copy(alpha = 0.4f)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Google 4-color 'G' icon
                    Surface(
                        shape = CircleShape,
                        color = Slate100,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "G",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF4285F4)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isSignUpMode) {
                            if (language == Language.BANGLA) "গুগল দিয়ে সাইন আপ করুন" else "Sign up with Google"
                        } else {
                            if (language == Language.BANGLA) "গুগল অ্যাকাউন্ট দিয়ে লগইন" else "Continue with Google"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }
            }

            // Divider: OR with Mobile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Slate200)
                Text(
                    text = if (language == Language.BANGLA) " অথবা মোবাইল দিয়ে " else " or with mobile number ",
                    fontSize = 12.sp,
                    color = Slate400,
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = Slate200)
            }

            // Role Selector Tab
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    val roles = listOf(
                        Triple(UserRole.RIDER, if (language == Language.BANGLA) "যাত্রী" else "Passenger", OrangePrimary),
                        Triple(UserRole.DRIVER, if (language == Language.BANGLA) "ড্রাইভার" else "Driver", TealPrimary),
                        Triple(UserRole.ADMIN, if (language == Language.BANGLA) "অ্যাডমিন" else "Admin", Navy800)
                    )

                    roles.forEach { (role, label, color) ->
                        val isSelected = selectedRole == role
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) color else Color.Transparent)
                                .clickable { selectedRole = role }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Slate700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // If Sign Up mode, ask for Full Name
            if (isSignUpMode) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text(if (language == Language.BANGLA) "আপনার পূর্ণ নাম" else "Full Name", color = Slate700) },
                    placeholder = { Text("e.g. Istewak Hassan", color = Slate400) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = OrangePrimary) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = Slate900,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
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
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Bangladesh Phone Input with high contrast visible text
            BangladeshPhoneInput(
                value = phoneInput,
                onValueChange = { phoneInput = it },
                label = if (language == Language.BANGLA) "মোবাইল নাম্বার (বাংলাদেশ)" else "Mobile Number (Bangladesh)"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Password Field with high contrast visible text
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text(if (language == Language.BANGLA) "পাসওয়ার্ড (ঐচ্ছিক)" else "Password (Optional)", color = Slate700) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(14.dp),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Slate600) },
                textStyle = TextStyle(
                    color = Slate900,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
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
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Forgot Password Link
            if (!isSignUpMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = if (language == Language.BANGLA) "পাসওয়ার্ড ভুলে গেছেন?" else "Forgot Password / PIN?",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangePrimary,
                        modifier = Modifier.clickable { showForgotPasswordDialog = true }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions Bottom
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CallGoButton(
                text = if (isLoading) {
                    if (language == Language.BANGLA) "যাচাই করা হচ্ছে..." else "Verifying..."
                } else {
                    if (language == Language.BANGLA) "ওটিপি (OTP) যাচাই করুন" else "Continue with OTP"
                },
                onClick = {
                    repository?.loginOrSignUpWithPhone(nameInput, phoneInput, selectedRole)
                    onNavigateToOtp(phoneInput)
                },
                enabled = phoneInput.isNotBlank() && !isLoading,
                isSecondary = selectedRole == UserRole.RIDER
            )

            // Direct role login for fast testing & seamless role jump
            CallGoButton(
                text = if (language == Language.BANGLA) "সরাসরি প্রবেশ করুন (${selectedRole.name})" else "Instant Demo Login (${selectedRole.name})",
                onClick = {
                    repository?.setActiveRole(selectedRole)
                    onLoginSuccess(selectedRole)
                },
                isSecondary = false
            )

            Text(
                text = if (language == Language.BANGLA)
                    "চালিয়ে যাওয়ার মাধ্যমে আপনি কল অ্যান্ড গো-এর শর্তাবলী ও নীতিমালা মেনে নিচ্ছেন।"
                else
                    "By continuing, you agree to Call & Go Terms of Service and Privacy Policy.",
                fontSize = 11.sp,
                color = Slate400,
                textAlign = TextAlign.Center,
                lineHeight = 15.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }

    // Interactive Google Account Chooser Dialog
    if (showGoogleChooser) {
        GoogleAccountChooserDialog(
            language = language,
            onDismiss = { showGoogleChooser = false },
            onSelectAccount = { name, email ->
                showGoogleChooser = false
                repository?.loginOrSignUpWithGoogle(name, email, selectedRole)
                onLoginSuccess(selectedRole)
            }
        )
    }

    // Forgot Password & Reset PIN Dialog
    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(
            language = language,
            defaultPhone = phoneInput,
            onDismiss = { showForgotPasswordDialog = false },
            onSendOtp = { resetPhone ->
                showForgotPasswordDialog = false
                repository?.loginOrSignUpWithPhone(nameInput, resetPhone, selectedRole)
                onNavigateToOtp(resetPhone)
            }
        )
    }
}

@Composable
private fun GoogleAccountChooserDialog(
    language: Language,
    onDismiss: () -> Unit,
    onSelectAccount: (String, String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "G",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF4285F4)
                )
                Text(
                    text = if (language == Language.BANGLA) "গুগল অ্যাকাউন্ট নির্বাচন করুন" else "Sign in with Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy900
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (language == Language.BANGLA)
                        "Call & Go বাংলাদেশ-এ চালিয়ে যাওয়ার জন্য একটি অ্যাকাউন্ট নির্বাচন করুন:"
                    else
                        "Choose an account to continue to Call & Go Bangladesh:",
                    fontSize = 12.sp,
                    color = Slate600,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Primary Google Account (Istewak Hassan)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectAccount("Istewak Hassan", "Istewakhassantewak121@gmail.com") },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    border = BorderStroke(1.dp, TealPrimary.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(OrangePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "I",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Istewak Hassan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Navy900
                            )
                            Text(
                                text = "Istewakhassantewak121@gmail.com",
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Secondary Account
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectAccount("Call & Go User", "rider.dhaka@callandgo.com.bd") },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Slate200)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(TealPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "C",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Call & Go Bangladesh",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Navy900
                            )
                            Text(
                                text = "rider.dhaka@callandgo.com.bd",
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Add Another Account Option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectAccount("New Google User", "user@gmail.com") }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Slate600, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == Language.BANGLA) "অন্য অ্যাকাউন্ট ব্যবহার করুন" else "Use another account",
                        fontSize = 13.sp,
                        color = Slate700,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == Language.BANGLA) "বাতিল" else "Cancel", color = Slate600)
            }
        }
    )
}

@Composable
fun ForgotPasswordDialog(
    language: Language,
    defaultPhone: String,
    onDismiss: () -> Unit,
    onSendOtp: (String) -> Unit
) {
    var phoneToReset by remember { mutableStateOf(defaultPhone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = OrangePrimary
                )
                Text(
                    text = if (language == Language.BANGLA) "পাসওয়ার্ড / পিন রিসেট" else "Reset Password / PIN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Navy900
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (language == Language.BANGLA)
                        "আপনার নিবন্ধিত বাংলাদেশি মোবাইল নাম্বার লিখুন। আমরা তাৎক্ষণিক ৪-সংখ্যার ওটিপি (OTP) ভেরিফিকেশন কোড পাঠাব।"
                    else
                        "Enter your registered Bangladeshi mobile number. We will instantly dispatch a 4-digit verification OTP to reset your access PIN.",
                    fontSize = 13.sp,
                    color = Slate600,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                BangladeshPhoneInput(
                    value = phoneToReset,
                    onValueChange = { phoneToReset = it },
                    label = if (language == Language.BANGLA) "মোবাইল নাম্বার" else "Mobile Number"
                )
            }
        },
        confirmButton = {
            CallGoButton(
                text = if (language == Language.BANGLA) "রিসেট কোড পাঠান" else "Send Reset OTP",
                onClick = { onSendOtp(phoneToReset) },
                enabled = phoneToReset.isNotBlank(),
                isSecondary = false
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = if (language == Language.BANGLA) "বাতিল" else "Cancel",
                    color = Slate600,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun OtpVerificationScreen(
    phoneNumber: String,
    language: Language,
    repository: CallAndGoRepository? = null,
    onVerified: () -> Unit,
    onBack: () -> Unit
) {
    val liveOtp = repository?.currentOtp?.collectAsState()?.value ?: "4829"
    var otpCode by remember { mutableStateOf("") }
    var secondsLeft by remember { mutableIntStateOf(58) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var isVerifiedSuccess by remember { mutableStateOf(false) }
    var resendSuccessNotice by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Countdown Timer
    LaunchedEffect(secondsLeft) {
        if (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    // Auto-verify when 4 digits are completed
    LaunchedEffect(otpCode) {
        if (otpCode.length == 4) {
            val role = repository?.activeRole?.value ?: UserRole.RIDER
            val isOk = repository?.verifyOtpViaAuthService(otpCode, role) ?: (otpCode == liveOtp || otpCode == "4829" || otpCode == "1234" || otpCode.length == 4)
            if (isOk) {
                validationError = null
                isVerifiedSuccess = true
                delay(400)
                onVerified()
            } else {
                validationError = if (language == Language.BANGLA)
                    "ভুল ওটিপি কোড। সঠিক কোডটি লিখুন: $liveOtp"
                else
                    "Invalid OTP. Enter the correct code: $liveOtp"
            }
        } else {
            validationError = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header with Back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Navy900
                    )
                }
                Text(
                    text = if (language == Language.BANGLA) "ওটিপি (OTP) যাচাইকরণ" else "Verify Mobile OTP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Navy900
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            CallAndGoLogo(iconSize = 56.dp, showText = true)
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == Language.BANGLA)
                    "+880 $phoneNumber নাম্বারে ৪ সংখ্যার ভেরিফিকেশন কোড পাঠানো হয়েছে।"
                else
                    "Enter the 4-digit verification code sent to +880 $phoneNumber",
                fontSize = 13.sp,
                color = Slate600,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Highlighted Live SMS OTP Card with 1-Tap Auto Fill
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = BorderStroke(1.dp, Color(0xFF93C5FD))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == Language.BANGLA) "💬 প্রাপ্ত এসএমএস ওটিপি:" else "💬 Received SMS Code:",
                            fontSize = 11.sp,
                            color = Slate600
                        )
                        Text(
                            text = liveOtp,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Navy900,
                            letterSpacing = 4.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = TealPrimary,
                        modifier = Modifier.clickable {
                            otpCode = liveOtp
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (language == Language.BANGLA) "⚡ অটোফিল" else "⚡ Auto-fill",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            if (resendSuccessNotice != null) {
                Text(
                    text = resendSuccessNotice!!,
                    color = SuccessGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4 OTP Digit Boxes
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..3) {
                    val char = otpCode.getOrNull(i)?.toString() ?: ""
                    val isCurrent = otpCode.length == i
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White)
                            .border(
                                BorderStroke(
                                    width = if (isCurrent || char.isNotEmpty()) 2.dp else 1.dp,
                                    color = when {
                                        isVerifiedSuccess -> SuccessGreen
                                        validationError != null -> ErrorRed
                                        isCurrent -> OrangePrimary
                                        char.isNotEmpty() -> TealPrimary
                                        else -> Slate200
                                    }
                                ),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Navy900
                        )
                    }
                }
            }

            // Success or Error Feedback
            if (isVerifiedSuccess) {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == Language.BANGLA) "ওটিপি কোড সফলভাবে যাচাই হয়েছে!" else "OTP code verified successfully!",
                        color = SuccessGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (validationError != null) {
                Text(
                    text = validationError!!,
                    color = ErrorRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // On-Screen Keypad (for 100% reliable digit entry on emulator & web)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Slate200)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val keyRows = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("C", "0", "⌫")
                    )

                    keyRows.forEach { rowKeys ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowKeys.forEach { key ->
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clickable {
                                            when (key) {
                                                "C" -> otpCode = ""
                                                "⌫" -> {
                                                    if (otpCode.isNotEmpty()) {
                                                        otpCode = otpCode.dropLast(1)
                                                    }
                                                }
                                                else -> {
                                                    if (otpCode.length < 4) {
                                                        otpCode += key
                                                    }
                                                }
                                            }
                                        },
                                    shape = RoundedCornerShape(10.dp),
                                    color = when (key) {
                                        "C" -> Color(0xFFFEE2E2)
                                        "⌫" -> Slate100
                                        else -> Color(0xFFF1F5F9)
                                    }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = key,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (key) {
                                                "C" -> ErrorRed
                                                "⌫" -> Slate700
                                                else -> Navy900
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Resend Code
            Text(
                text = if (secondsLeft > 0) {
                    if (language == Language.BANGLA) "পুনরায় ওটিপি পাঠান: ${secondsLeft}s" else "Resend code in ${secondsLeft}s"
                } else {
                    if (language == Language.BANGLA) "🔄 কোড পুনরায় পাঠান (নতুন কোড)" else "🔄 Resend New OTP Code"
                },
                fontSize = 13.sp,
                color = if (secondsLeft > 0) Slate400 else OrangePrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = secondsLeft == 0) {
                    val newCode = repository?.generateNewOtp() ?: "4829"
                    resendSuccessNotice = if (language == Language.BANGLA) "নতুন ওটিপি কোড পাঠানো হয়েছে: $newCode" else "New OTP code sent: $newCode"
                    secondsLeft = 60
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CallGoButton(
                text = if (language == Language.BANGLA) "যাচাই সম্পন্ন করুন" else "Verify & Continue",
                onClick = {
                    val role = repository?.activeRole?.value ?: UserRole.RIDER
                    coroutineScope.launch {
                        val isOk = repository?.verifyOtpViaAuthService(otpCode, role) ?: (otpCode == liveOtp || otpCode == "4829" || otpCode.length == 4)
                        if (isOk) {
                            onVerified()
                        } else {
                            validationError = if (language == Language.BANGLA) "সঠিক কোড: $liveOtp লিখুন" else "Please enter $liveOtp"
                        }
                    }
                },
                isSecondary = true,
                enabled = otpCode.length == 4 || otpCode.isNotEmpty()
            )

            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (language == Language.BANGLA) "মোবাইল নাম্বার পরিবর্তন করুন" else "Change Mobile Number",
                    color = Slate600,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
