package com.example.damandroid.presentation.createactivity.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.damandroid.domain.model.ActivityVisibility
import com.example.damandroid.domain.model.SkillLevel
import com.example.damandroid.domain.model.SportCategory
import com.example.damandroid.presentation.createactivity.model.CreateActivityUiState
import com.example.damandroid.presentation.createactivity.viewmodel.CreateActivityViewModel
import com.example.damandroid.ui.theme.AppThemeColors
import com.example.damandroid.ui.theme.LocalThemeController
import com.example.damandroid.ui.theme.rememberAppThemeColors

@Composable
fun CreateActivityRoute(
    viewModel: CreateActivityViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    CreateActivityScreen(
        state = uiState,
        onBack = onBack,
        onSportSelected = viewModel::onSportSelected,
        onTitleChange = viewModel::onTitleChanged,
        onDescriptionChange = viewModel::onDescriptionChanged,
        onLocationChange = viewModel::onLocationChanged,
        onDateChange = viewModel::onDateChanged,
        onTimeChange = viewModel::onTimeChanged,
        onParticipantsChange = viewModel::onParticipantsChanged,
        onSkillLevelChange = viewModel::onLevelSelected,
        onVisibilityChange = viewModel::onVisibilitySelected,
        onSubmit = viewModel::onSubmit,
        onSuccessDismiss = viewModel::onSuccessDialogDismissed,
        modifier = modifier
    )
}

@Composable
fun CreateActivityScreen(
    state: CreateActivityUiState,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onParticipantsChange: (Int) -> Unit,
    onSportSelected: (SportCategory) -> Unit,
    onSkillLevelChange: (SkillLevel) -> Unit,
    onVisibilityChange: (ActivityVisibility) -> Unit,
    onSubmit: () -> Unit,
    onSuccessDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> LoadingState(modifier)
        state.error != null -> ErrorState(state.error, modifier)
        else -> ContentState(state, onBack, onTitleChange, onDescriptionChange, onLocationChange, onDateChange, onTimeChange, onParticipantsChange, onSportSelected, onSkillLevelChange, onVisibilityChange, onSubmit, onSuccessDismiss, modifier)
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ContentState(
    state: CreateActivityUiState,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onParticipantsChange: (Int) -> Unit,
    onSportSelected: (SportCategory) -> Unit,
    onSkillLevelChange: (SkillLevel) -> Unit,
    onVisibilityChange: (ActivityVisibility) -> Unit,
    onSubmit: () -> Unit,
    onSuccessDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberCreatePalette(rememberAppThemeColors(LocalThemeController.current.isDarkMode))
    var sportExpanded by remember { mutableStateOf(false) }
    var levelExpanded by remember { mutableStateOf(false) }
    var visibilityExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(palette.background)
    ) {
        state.success?.let { result ->
            SuccessDialog(message = result.shareLink, palette = palette, onDismiss = onSuccessDismiss)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Header(onBack = onBack, palette = palette)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                SportSelector(
                    sportExpanded,
                    onExpandedChange = { sportExpanded = it },
                    selectedSport = state.selectedSport,
                    sports = state.sportCategories,
                    palette = palette,
                    onSportSelected = {
                        sportExpanded = false
                        onSportSelected(it)
                    }
                )

                ActivityTextField(
                    value = state.title,
                    onValueChange = onTitleChange,
                    label = "Activity Title *",
                    placeholder = "e.g., Morning run at the park",
                    palette = palette
                )

                ActivityTextField(
                    value = state.description,
                    onValueChange = onDescriptionChange,
                    label = "Description",
                    placeholder = "Tell participants what to expect...",
                    palette = palette,
                    minLines = 4
                )

                ActivityTextField(
                    value = state.location,
                    onValueChange = onLocationChange,
                    label = "Location *",
                    placeholder = "Where will this activity take place?",
                    palette = palette
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    ActivityTextField(
                        value = state.date,
                        onValueChange = onDateChange,
                        label = "Date *",
                        placeholder = "Select date",
                        palette = palette,
                        leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = palette.mutedText) },
                        modifier = Modifier.weight(1f)
                    )
                    ActivityTextField(
                        value = state.time,
                        onValueChange = onTimeChange,
                        label = "Time *",
                        placeholder = "Select time",
                        palette = palette,
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = palette.mutedText) },
                        modifier = Modifier.weight(1f)
                    )
                }

                ParticipantsSection(participants = state.participants, onParticipantsChange = onParticipantsChange, palette = palette)

                LevelSelector(
                    expanded = levelExpanded,
                    onExpandedChange = { levelExpanded = !levelExpanded },
                    selected = state.level,
                    onSelected = {
                        levelExpanded = false
                        onSkillLevelChange(it)
                    },
                    palette = palette
                )

                VisibilitySelector(
                    expanded = visibilityExpanded,
                    onExpandedChange = { visibilityExpanded = !visibilityExpanded },
                    selected = state.visibility,
                    onSelected = {
                        visibilityExpanded = false
                        onVisibilityChange(it)
                    },
                    palette = palette
                )
            }

            SubmitBar(onSubmit = onSubmit, palette = palette)
        }
    }
}

@Composable
private fun Header(onBack: () -> Unit, palette: CreatePalette) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = palette.glassSurface),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, palette.glassBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = palette.primaryText)
            }
            Text(text = "Create Activity", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = palette.primaryText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SportSelector(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selectedSport: SportCategory?,
    sports: List<SportCategory>,
    palette: CreatePalette,
    onSportSelected: (SportCategory) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel("Sport Type *", palette)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
            OutlinedTextField(
                value = selectedSport?.name.orEmpty(),
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select a sport", color = palette.mutedText) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(18.dp),
                colors = textFieldColors(palette)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                sports.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(text = category.icon, fontSize = 18.sp)
                                Text(text = category.name, color = palette.primaryText)
                            }
                        },
                        onClick = { onSportSelected(category) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    palette: CreatePalette,
    minLines: Int = 1,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = modifier) {
        FieldLabel(label, palette)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = palette.mutedText) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            minLines = minLines,
            maxLines = minLines * 2,
            leadingIcon = leadingIcon,
            colors = textFieldColors(palette)
        )
    }
}

@Composable
private fun FieldLabel(text: String, palette: CreatePalette) {
    Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = palette.secondaryText)
}

@Composable
private fun ParticipantsSection(participants: Int, onParticipantsChange: (Int) -> Unit, palette: CreatePalette) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel("Participants", palette)
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = palette.primaryText, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = participants.toString(), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = palette.primaryText)
            }
            Text(text = "Max 20", fontSize = 12.sp, color = palette.mutedText)
        }
        Slider(
            value = participants.toFloat(),
            onValueChange = { onParticipantsChange(it.toInt()) },
            valueRange = 2f..20f,
            steps = 18,
            colors = SliderDefaults.colors(
                thumbColor = palette.accentPurple,
                activeTrackColor = palette.accentPurple,
                inactiveTrackColor = palette.sliderTrack
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LevelSelector(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selected: SkillLevel?,
    onSelected: (SkillLevel) -> Unit,
    palette: CreatePalette
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel("Skill Level", palette)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { onExpandedChange(!expanded) }) {
            OutlinedTextField(
                value = selected?.displayName.orEmpty(),
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select level", color = palette.mutedText) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(18.dp),
                colors = textFieldColors(palette)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                SkillLevel.values().forEach { level ->
                    DropdownMenuItem(text = { Text(level.displayName, color = palette.primaryText) }, onClick = { onSelected(level) })
                }
            }
        }
    }
}

private val SkillLevel.displayName: String
    get() = when (this) {
        SkillLevel.BEGINNER -> "Beginner"
        SkillLevel.INTERMEDIATE -> "Intermediate"
        SkillLevel.ADVANCED -> "Advanced"
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VisibilitySelector(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    selected: ActivityVisibility,
    onSelected: (ActivityVisibility) -> Unit,
    palette: CreatePalette
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FieldLabel("Visibility", palette)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { onExpandedChange(!expanded) }) {
            OutlinedTextField(
                value = selected.label,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select visibility", color = palette.mutedText) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                shape = RoundedCornerShape(18.dp),
                colors = textFieldColors(palette)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                ActivityVisibility.values().forEach { visibility ->
                    DropdownMenuItem(text = { Text(visibility.label, color = palette.primaryText) }, onClick = { onSelected(visibility) })
                }
            }
        }
    }
}

private val ActivityVisibility.label: String
    get() = when (this) {
        ActivityVisibility.PUBLIC -> "Public"
        ActivityVisibility.FRIENDS -> "Friends"
    }

@Composable
private fun SubmitBar(onSubmit: () -> Unit, palette: CreatePalette) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        shape = RoundedCornerShape(20.dp),
        color = palette.glassSurface,
        border = BorderStroke(1.dp, palette.glassBorder)
    ) {
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = palette.accentPurple)
        ) {
            Text(text = "Create Activity", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = palette.iconOnAccent)
        }
    }
}

@Composable
private fun SuccessDialog(message: String, palette: CreatePalette, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        color = Color.Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = palette.cardSurface),
                border = BorderStroke(1.dp, palette.glassBorder)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = palette.accentPurple, modifier = Modifier.size(36.dp))
                    Text(text = "Activity Created!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = palette.primaryText)
                    Text(text = message, fontSize = 13.sp, color = palette.secondaryText, textAlign = TextAlign.Center)
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = palette.accentPurple)) {
                        Text(text = "Got it", color = palette.iconOnAccent)
                    }
                }
            }
        }
    }
}

@Composable
private fun textFieldColors(palette: CreatePalette) = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = palette.glassSurface,
    unfocusedContainerColor = palette.glassSurface,
    focusedBorderColor = palette.accentPurple,
    unfocusedBorderColor = palette.glassBorder,
    unfocusedTextColor = palette.primaryText,
    focusedTextColor = palette.primaryText,
    cursorColor = palette.primaryText,
    unfocusedLabelColor = palette.secondaryText,
    focusedLabelColor = palette.primaryText
)

// region Palette

data class CreatePalette(
    val background: Brush,
    val glassSurface: Color,
    val glassBorder: Color,
    val cardSurface: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val mutedText: Color,
    val iconOnAccent: Color,
    val accentPurple: Color,
    val accentBlue: Color,
    val sliderTrack: Color
)

@Composable
private fun rememberCreatePalette(colors: AppThemeColors): CreatePalette {
    return CreatePalette(
        background = colors.backgroundGradient,
        glassSurface = colors.glassSurface,
        glassBorder = colors.glassBorder,
        cardSurface = colors.glassSurface.copy(alpha = 0.95f),
        primaryText = colors.primaryText,
        secondaryText = colors.secondaryText,
        mutedText = colors.mutedText,
        iconOnAccent = colors.iconOnAccent,
        accentPurple = colors.accentPurple,
        accentBlue = colors.accentBlue,
        sliderTrack = colors.subtleSurface
    )
}

// endregion

