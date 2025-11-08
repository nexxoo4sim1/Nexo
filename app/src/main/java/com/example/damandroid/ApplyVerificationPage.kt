package com.example.damandroid

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors
import com.example.damandroid.ui.theme.AppThemeColors

enum class UserType {
    COACH_TRAINER, CLUB_OWNER
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyVerificationPage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var userType by remember { mutableStateOf(UserType.COACH_TRAINER) }
    var fullName by remember { mutableStateOf("John Smith") }
    var about by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("Running, Fitness") }
    var yearsOfExperience by remember { mutableStateOf("") }
    var certifications by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var clubName by remember { mutableStateOf("SportHub LA") }
    var sportFocus by remember { mutableStateOf("Tennis, Swimming") }

    var expandedYears by remember { mutableStateOf(false) }
    val yearsOptions = (1..50).map { "$it years" }

    val themeController = LocalThemeController.current
    val theme = rememberAppThemeColors(themeController.isDarkMode)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(theme.backgroundGradient)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = theme.glassSurface,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = theme.primaryText,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "Apply for Verification",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.primaryText
                        )

                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // User Type Selection
                    Text(
                        text = "I am a *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        UserTypeButton(
                            type = UserType.COACH_TRAINER,
                            isSelected = userType == UserType.COACH_TRAINER,
                            onClick = { userType = UserType.COACH_TRAINER },
                            modifier = Modifier.weight(1f),
                            theme = theme
                        )
                        UserTypeButton(
                            type = UserType.CLUB_OWNER,
                            isSelected = userType == UserType.CLUB_OWNER,
                            onClick = { userType = UserType.CLUB_OWNER },
                            modifier = Modifier.weight(1f),
                            theme = theme
                        )
                    }

                    // Form Fields
                    if (userType == UserType.COACH_TRAINER) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name *", color = theme.secondaryText) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = theme.glassSurface,
                                unfocusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedLabelColor = theme.secondaryText,
                                focusedLabelColor = theme.primaryText,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )

                        OutlinedTextField(
                            value = about,
                            onValueChange = { about = it },
                            label = { Text("About *") },
                            placeholder = { Text("Tell us about your coaching experience and philosophy...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = theme.glassSurface,
                                unfocusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedLabelColor = theme.secondaryText,
                                focusedLabelColor = theme.primaryText,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )

                        OutlinedTextField(
                            value = specialization,
                            onValueChange = { specialization = it },
                            label = { Text("Specialization *") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = theme.glassSurface,
                                unfocusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedLabelColor = theme.secondaryText,
                                focusedLabelColor = theme.primaryText,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )
                    } else {
                        OutlinedTextField(
                            value = clubName,
                            onValueChange = { clubName = it },
                            label = { Text("Club Name *") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = theme.glassSurface,
                                unfocusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedLabelColor = theme.secondaryText,
                                focusedLabelColor = theme.primaryText,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )

                        OutlinedTextField(
                            value = about,
                            onValueChange = { about = it },
                            label = { Text("About *") },
                            placeholder = { Text("Describe your club, facilities, and what makes you special...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = theme.glassSurface,
                                unfocusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedLabelColor = theme.secondaryText,
                                focusedLabelColor = theme.primaryText,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )

                        OutlinedTextField(
                            value = sportFocus,
                            onValueChange = { sportFocus = it },
                            label = { Text("Sport Focus *") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = theme.glassSurface,
                                unfocusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedLabelColor = theme.secondaryText,
                                focusedLabelColor = theme.primaryText,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )
                    }

                    // Years of Experience Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedYears,
                        onExpandedChange = { expandedYears = !expandedYears }
                    ) {
                        OutlinedTextField(
                            value = yearsOfExperience,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Years of Experience *", color = theme.secondaryText) },
                            placeholder = { Text("Select years", color = theme.mutedText) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYears)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = theme.glassSurface,
                                unfocusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedYears,
                            onDismissRequest = { expandedYears = false }
                        ) {
                            yearsOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        yearsOfExperience = option
                                        expandedYears = false
                                    }
                                )
                            }
                        }
                    }

                    // Certifications
                    OutlinedTextField(
                        value = certifications,
                        onValueChange = { certifications = it },
                        label = { Text("Certifications / License *") },
                        placeholder = { Text(if (userType == UserType.COACH_TRAINER) "NASM CPT, ACE, etc." else "Business License Number") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = theme.glassSurface,
                            unfocusedContainerColor = theme.glassSurface,
                            unfocusedBorderColor = theme.glassBorder,
                            focusedBorderColor = theme.accentPurple,
                            unfocusedLabelColor = theme.secondaryText,
                            focusedLabelColor = theme.primaryText,
                            unfocusedTextColor = theme.primaryText,
                            focusedTextColor = theme.primaryText,
                            cursorColor = theme.primaryText
                        )
                    )

                    // Location
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location *") },
                        placeholder = { Text("City, State") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = theme.glassSurface,
                            unfocusedContainerColor = theme.glassSurface,
                            unfocusedBorderColor = theme.glassBorder,
                            focusedBorderColor = theme.accentPurple,
                            unfocusedLabelColor = theme.secondaryText,
                            focusedLabelColor = theme.primaryText,
                            unfocusedTextColor = theme.primaryText,
                            focusedTextColor = theme.primaryText,
                            cursorColor = theme.primaryText
                        )
                    )

                    // Website / Social Media
                    OutlinedTextField(
                        value = website,
                        onValueChange = { website = it },
                        label = { Text("Website / Social Media") },
                        placeholder = { Text("https://...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = theme.glassSurface,
                            unfocusedContainerColor = theme.glassSurface,
                            unfocusedBorderColor = theme.glassBorder,
                            focusedBorderColor = theme.accentPurple,
                            unfocusedLabelColor = theme.secondaryText,
                            focusedLabelColor = theme.primaryText,
                            unfocusedTextColor = theme.primaryText,
                            focusedTextColor = theme.primaryText,
                            cursorColor = theme.primaryText
                        )
                    )

                    // Upload Documents
                    Text(
                        text = "Upload Verification Documents *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clickable { /* Handle upload */ },
                        shape = RoundedCornerShape(16.dp),
                        color = theme.subtleSurface,
                        border = BorderStroke(2.dp, theme.glassBorder)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                contentDescription = null,
                                tint = theme.accentPink,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Upload ID, Certifications, or Business License",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = theme.primaryText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "PDF, JPG, or PNG (max 5MB)",
                                fontSize = 11.sp,
                                color = theme.mutedText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = { /* Handle submit */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = theme.accentPurple
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = theme.iconOnAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Submit Application",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.iconOnAccent
                        )
                    }

                    // Disclaimer
                    Text(
                        text = "By submitting, you agree to our verification process and terms of service.",
                        fontSize = 11.sp,
                        color = theme.mutedText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserTypeButton(
    type: UserType,
    isSelected: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    theme: AppThemeColors
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) theme.subtleSurface else theme.glassSurface,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) theme.accentBlue else theme.glassBorder
        ),
        shadowElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (type == UserType.COACH_TRAINER) "🏆" else "🏢",
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (type == UserType.COACH_TRAINER) "Coach / Trainer" else "Club Owner",
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = theme.primaryText
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ApplyVerificationPagePreview() {
    CompositionLocalProvider(
        LocalThemeController provides ThemeController(
            isDarkMode = false,
            setDarkMode = {}
        )
    ) {
        DamAndroidTheme(darkTheme = false) {
            ApplyVerificationPage(
                onBack = { }
            )
        }
    }
}

