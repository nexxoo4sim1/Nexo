package com.example.damandroid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.damandroid.ui.theme.DamAndroidTheme
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.ThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateActivity(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSuccess by remember { mutableStateOf(false) }
    
    var sportType by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var participants by remember { mutableStateOf(5f) }
    var level by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf("public") }
    
    var expandedSport by remember { mutableStateOf(false) }
    var expandedLevel by remember { mutableStateOf(false) }
    var expandedVisibility by remember { mutableStateOf(false) }

    val themeController = LocalThemeController.current
    val theme = rememberAppThemeColors(themeController.isDarkMode)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(theme.backgroundGradient)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = theme.glassSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Create Activity",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = theme.primaryText
                    )
                }
            }

            // Form
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Sport Type
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Sport Type *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Box {
                        ExposedDropdownMenuBox(
                            expanded = expandedSport,
                            onExpandedChange = { expandedSport = !expandedSport }
                        ) {
                            OutlinedTextField(
                                value = sportType,
                                onValueChange = { },
                                readOnly = true,
                                placeholder = {
                                    Text(
                                        "Select a sport",
                                        color = theme.mutedText
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSport)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = theme.glassSurface,
                                    focusedContainerColor = theme.glassSurface,
                                    unfocusedBorderColor = theme.glassBorder,
                                    focusedBorderColor = theme.accentPurple,
                                    focusedTextColor = theme.primaryText,
                                    unfocusedTextColor = theme.primaryText,
                                    cursorColor = theme.primaryText
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expandedSport,
                                onDismissRequest = { expandedSport = false }
                            ) {
                                sportCategoriesData.forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = category.icon, fontSize = 18.sp)
                                                Text(text = category.name, color = theme.primaryText)
                                            }
                                        },
                                        onClick = {
                                            sportType = category.name
                                            expandedSport = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Title
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Activity Title *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = {
                            Text(
                                "e.g., Morning run at the park",
                                color = theme.mutedText
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = theme.glassSurface,
                            focusedContainerColor = theme.glassSurface,
                            unfocusedBorderColor = theme.glassBorder,
                            focusedBorderColor = theme.accentPurple,
                            unfocusedTextColor = theme.primaryText,
                            focusedTextColor = theme.primaryText,
                            cursorColor = theme.primaryText
                        )
                    )
                }

                // Description
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Description",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = {
                            Text(
                                "Tell participants what to expect...",
                                color = theme.mutedText
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = theme.glassSurface,
                            focusedContainerColor = theme.glassSurface,
                            unfocusedBorderColor = theme.glassBorder,
                            focusedBorderColor = theme.accentPurple,
                            unfocusedTextColor = theme.primaryText,
                            focusedTextColor = theme.primaryText,
                            cursorColor = theme.primaryText
                        )
                    )
                }

                // Location
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Location *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        placeholder = {
                            Text("Where will this activity take place?", color = theme.mutedText)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = theme.glassSurface,
                            focusedContainerColor = theme.glassSurface,
                            unfocusedBorderColor = theme.glassBorder,
                            focusedBorderColor = theme.accentPurple,
                            unfocusedTextColor = theme.primaryText,
                            focusedTextColor = theme.primaryText,
                            cursorColor = theme.primaryText
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Date *",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = theme.secondaryText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            placeholder = {
                                Text("Select date", color = theme.mutedText)
                            },
                            trailingIcon = {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = theme.mutedText)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = theme.glassSurface,
                                focusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Time *",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = theme.secondaryText,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = time,
                            onValueChange = { time = it },
                            placeholder = {
                                Text("Select time", color = theme.mutedText)
                            },
                            trailingIcon = {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = theme.mutedText)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = theme.glassSurface,
                                focusedContainerColor = theme.glassSurface,
                                unfocusedBorderColor = theme.glassBorder,
                                focusedBorderColor = theme.accentPurple,
                                unfocusedTextColor = theme.primaryText,
                                focusedTextColor = theme.primaryText,
                                cursorColor = theme.primaryText
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Participants",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = theme.primaryText, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = participants.toInt().toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = theme.primaryText
                            )
                        }
                        Text(
                            text = "Max 20",
                            fontSize = 12.sp,
                            color = theme.mutedText
                        )
                    }
                    Slider(
                        value = participants,
                        onValueChange = { participants = it },
                        valueRange = 2f..20f,
                        steps = 18,
                        colors = SliderDefaults.colors(
                            thumbColor = theme.accentPurple,
                            activeTrackColor = theme.accentPurple,
                            inactiveTrackColor = theme.subtleSurface
                        )
                    )
                }

                // Skill Level
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Skill Level *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Box {
                        ExposedDropdownMenuBox(
                            expanded = expandedLevel,
                            onExpandedChange = { expandedLevel = !expandedLevel }
                        ) {
                            OutlinedTextField(
                                value = level,
                                onValueChange = { },
                                readOnly = true,
                                placeholder = {
                                    Text(
                                        "Select skill level",
                                        color = theme.mutedText
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLevel)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = theme.glassSurface,
                                    focusedContainerColor = theme.glassSurface,
                                    unfocusedBorderColor = theme.glassBorder,
                                    focusedBorderColor = theme.accentPurple,
                                    unfocusedTextColor = theme.primaryText,
                                    focusedTextColor = theme.primaryText,
                                    cursorColor = theme.primaryText
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expandedLevel,
                                onDismissRequest = { expandedLevel = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Beginner", color = theme.primaryText) },
                                    onClick = {
                                        level = "Beginner"
                                        expandedLevel = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Intermediate", color = theme.primaryText) },
                                    onClick = {
                                        level = "Intermediate"
                                        expandedLevel = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Advanced", color = theme.primaryText) },
                                    onClick = {
                                        level = "Advanced"
                                        expandedLevel = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Visibility
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Visibility",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = theme.secondaryText,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Box {
                        ExposedDropdownMenuBox(
                            expanded = expandedVisibility,
                            onExpandedChange = { expandedVisibility = !expandedVisibility }
                        ) {
                            OutlinedTextField(
                                value = if (visibility == "public") "Public - Anyone can join" else "Friends Only",
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVisibility)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = theme.glassSurface,
                                    focusedContainerColor = theme.glassSurface,
                                    unfocusedBorderColor = theme.glassBorder,
                                    focusedBorderColor = theme.accentPurple,
                                    unfocusedTextColor = theme.primaryText,
                                    focusedTextColor = theme.primaryText,
                                    cursorColor = theme.primaryText
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expandedVisibility,
                                onDismissRequest = { expandedVisibility = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Public - Anyone can join", color = theme.primaryText) },
                                    onClick = {
                                        visibility = "public"
                                        expandedVisibility = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Friends Only", color = theme.primaryText) },
                                    onClick = {
                                        visibility = "friends"
                                        expandedVisibility = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Submit Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = theme.secondaryText
                        )
                    ) {
                        Text("Cancel", fontSize = 15.sp, color = theme.secondaryText)
                    }

                    Button(
                        onClick = { showSuccess = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = theme.accentPurple
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text("Create Room", fontSize = 15.sp, color = Color.White)
                    }
                }
            }
        }

        // Success Dialog
        if (showSuccess) {
            SuccessDialog(
                onClose = { showSuccess = false },
                onShare = {
                    showSuccess = false
                    onBack()
                }
            )
        }
    }
}

@Composable
private fun SuccessDialog(
    onClose: () -> Unit,
    onShare: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Emoji Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            Color(0xFF2ECC71).copy(alpha = 0.1f),
                            RoundedCornerShape(40.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎉",
                        fontSize = 48.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your session is live!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your activity has been created. Share the link with friends or wait for others to join.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onClose,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF374151)
                        )
                    ) {
                        Text("Close", fontSize = 15.sp)
                    }

                    Button(
                        onClick = onShare,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2ECC71)
                        )
                    ) {
                        Text("Share Link", fontSize = 15.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateActivityPreview() {
    DamAndroidTheme {
        CreateActivity(
            onBack = { }
        )
    }
}

