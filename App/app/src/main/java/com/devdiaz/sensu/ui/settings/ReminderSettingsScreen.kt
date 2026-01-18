package com.devdiaz.sensu.ui.settings

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devdiaz.sensu.ui.theme.SensuGreen

@Composable
fun ReminderSettingsScreen(
        modifier: Modifier,
        onBack: () -> Unit, viewModel: ReminderViewModel = hiltViewModel()
){
        val uiState by viewModel.uiState.collectAsState()
        val context = LocalContext.current

        val permissionLauncher =
                rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                        if (isGranted) {
                                viewModel.saveSettings()
                                Toast.makeText(context, "Recordatorio guardado", Toast.LENGTH_SHORT)
                                        .show()
                                onBack()
                        } else {
                                Toast.makeText(
                                                context,
                                                "Permiso necesario para notificaciones",
                                                Toast.LENGTH_LONG
                                        )
                                        .show()
                        }
                }

        Scaffold(
                containerColor = Color(0xFFF6F8F7), // Background Light
                topBar = {
                        Row(
                                modifier =
                                        modifier.fillMaxWidth()
                                                .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Surface(
                                        shape = CircleShape,
                                        color = Color.White,
                                        shadowElevation = 2.dp,
                                        modifier = Modifier.size(48.dp)
                                ) {
                                        IconButton(onClick = onBack) {
                                                Icon(
                                                        imageVector = Icons.Default.ArrowBack,
                                                        contentDescription = "regresar",
                                                        tint = Color(0xFF0E1B14)
                                                )
                                        }
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                        text = "Recordatorio Diario",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0E1B14)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Spacer(modifier = Modifier.width(48.dp)) // Balance
                        }
                }
        ) { innerPadding ->
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(innerPadding)
                                        .verticalScroll(rememberScrollState())
                                        .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        // Headline
                        Box(
                                modifier =
                                        Modifier.size(64.dp)
                                                .clip(CircleShape)
                                                .background(SensuGreen.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                        ) {
                                Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = null,
                                        tint = SensuGreen,
                                        modifier = Modifier.size(32.dp)
                                )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                                text = "¿A qué hora quieres\nregistrar tu día?",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0E1B14),
                                textAlign = TextAlign.Center,
                                lineHeight = 36.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                                text =
                                        "¡Te daremos un empujoncito amigable para registrar tu ánimo cada día!",
                                fontSize = 16.sp,
                                color = Color(0xFF509573),
                                textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Wheel Picker
                        Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                                // Background Card
                                Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        shape = RoundedCornerShape(40.dp),
                                        color = Color.White,
                                        shadowElevation = 4.dp
                                ) {}

                                // Highlight Bar
                                Box(
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .height(64.dp)
                                                        .align(Alignment.Center)
                                                        .padding(horizontal = 24.dp)
                                                        .clip(RoundedCornerShape(16.dp))
                                                        .background(SensuGreen.copy(alpha = 0.1f))
                                                        .border(
                                                                1.dp,
                                                                SensuGreen.copy(alpha = 0.2f),
                                                                RoundedCornerShape(16.dp)
                                                        )
                                )

                                Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        // Hours (01-12)
                                        Box(modifier = Modifier.weight(1f)) {
                                                WheelPicker(
                                                        range = 1..12,
                                                        initialValue =
                                                                if (uiState.hour % 12 == 0) 12
                                                                else uiState.hour % 12,
                                                        onValueChange = { h ->
                                                                val isPm = uiState.hour >= 12
                                                                val newHour =
                                                                        if (h == 12) {
                                                                                if (isPm) 12 else 0
                                                                        } else {
                                                                                if (isPm) h + 12
                                                                                else h
                                                                        }
                                                                viewModel.onTimeChanged(
                                                                        newHour,
                                                                        uiState.minute
                                                                )
                                                        }
                                                )
                                        }

                                        // Separator
                                        Text(
                                                text = ":",
                                                fontSize = 32.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SensuGreen,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                        )

                                        // Minutes (00-59)
                                        Box(modifier = Modifier.weight(1f)) {
                                                WheelPicker(
                                                        range = 0..59,
                                                        initialValue = uiState.minute,
                                                        onValueChange = { m ->
                                                                viewModel.onTimeChanged(
                                                                        uiState.hour,
                                                                        m
                                                                )
                                                        },
                                                        format = { "%02d".format(it) }
                                                )
                                        }

                                        // AM/PM
                                        Box(
                                                modifier = Modifier.weight(1f),
                                                contentAlignment = Alignment.Center
                                        ) {
                                                Column(
                                                        verticalArrangement =
                                                                Arrangement.spacedBy(8.dp),
                                                        horizontalAlignment =
                                                                Alignment.CenterHorizontally,
                                                        modifier =
                                                                Modifier.padding(
                                                                        bottom = 12.dp
                                                                ) // Visual alignment adjustment
                                                ) {
                                                        val isPm = uiState.hour >= 12

                                                        // AM
                                                        Text(
                                                                text = "AM",
                                                                fontSize =
                                                                        if (!isPm) 20.sp else 16.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color =
                                                                        if (!isPm) SensuGreen
                                                                        else
                                                                                Color(0xFFAFD4C2)
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.6f
                                                                                        ),
                                                                modifier =
                                                                        Modifier.clickable {
                                                                                if (isPm) {
                                                                                        viewModel
                                                                                                .onTimeChanged(
                                                                                                        uiState.hour %
                                                                                                                12,
                                                                                                        uiState.minute
                                                                                                )
                                                                                }
                                                                        }
                                                                                .run {
                                                                                        if (!isPm)
                                                                                                this.background(
                                                                                                                Color.White,
                                                                                                                RoundedCornerShape(
                                                                                                                        12.dp
                                                                                                                )
                                                                                                        )
                                                                                                        .padding(
                                                                                                                horizontal =
                                                                                                                        12.dp,
                                                                                                                vertical =
                                                                                                                        6.dp
                                                                                                        )
                                                                                        else this
                                                                                }
                                                        )

                                                        // PM
                                                        Text(
                                                                text = "PM",
                                                                fontSize =
                                                                        if (isPm) 20.sp else 16.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color =
                                                                        if (isPm) SensuGreen
                                                                        else
                                                                                Color(0xFFAFD4C2)
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.6f
                                                                                        ),
                                                                modifier =
                                                                        Modifier.clickable {
                                                                                if (!isPm) {
                                                                                        viewModel
                                                                                                .onTimeChanged(
                                                                                                        (uiState.hour +
                                                                                                                12) %
                                                                                                                24,
                                                                                                        uiState.minute
                                                                                                )
                                                                                }
                                                                        }
                                                                                .run {
                                                                                        if (isPm)
                                                                                                this.background(
                                                                                                                Color.White,
                                                                                                                RoundedCornerShape(
                                                                                                                        12.dp
                                                                                                                )
                                                                                                        )
                                                                                                        .padding(
                                                                                                                horizontal =
                                                                                                                        12.dp,
                                                                                                                vertical =
                                                                                                                        6.dp
                                                                                                        )
                                                                                        else this
                                                                                }
                                                        )
                                                }
                                        }
                                }

                                // Gradients for fade effect
                                Box(
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .height(80.dp)
                                                        .align(Alignment.TopCenter)
                                                        .background(
                                                                Brush.verticalGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        Color.White,
                                                                                        Color.White
                                                                                                .copy(
                                                                                                        alpha =
                                                                                                                0f
                                                                                                )
                                                                                )
                                                                )
                                                        )
                                )
                                Box(
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .height(80.dp)
                                                        .align(Alignment.BottomCenter)
                                                        .background(
                                                                Brush.verticalGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        Color.White
                                                                                                .copy(
                                                                                                        alpha =
                                                                                                                0f
                                                                                                ),
                                                                                        Color.White
                                                                                )
                                                                )
                                                        )
                                )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Toggle Switch
                        Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White,
                                shadowElevation = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                        ) {
                                Row(
                                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                        Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                                Box(
                                                        modifier =
                                                                Modifier.size(40.dp)
                                                                        .background(
                                                                                Color(0xFFEBF8FC),
                                                                                RoundedCornerShape(
                                                                                        12.dp
                                                                                )
                                                                        ),
                                                        contentAlignment = Alignment.Center
                                                ) {
                                                        Icon(
                                                                imageVector =
                                                                        Icons.Outlined
                                                                                .Notifications,
                                                                contentDescription = null,
                                                                tint = Color(0xFF60A5FA)
                                                        )
                                                }
                                                Column {
                                                        Text(
                                                                text = "Notificaciones diarias",
                                                                fontSize = 16.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(0xFF0E1B14)
                                                        )
                                                        Text(
                                                                text = "Recordatorios amigables",
                                                                fontSize = 12.sp,
                                                                color = Color(0xFF509573)
                                                        )
                                                }
                                        }

                                        Switch(
                                                checked = uiState.isEnabled,
                                                onCheckedChange = viewModel::onToggleChanged,
                                                colors =
                                                        SwitchDefaults.colors(
                                                                checkedThumbColor = Color.White,
                                                                checkedTrackColor = SensuGreen,
                                                                uncheckedThumbColor = Color.White,
                                                                uncheckedTrackColor =
                                                                        Color(0xFFE5E7EB)
                                                        )
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Save Button
                        Button(
                                onClick = {
                                        if (uiState.isEnabled &&
                                                        Build.VERSION.SDK_INT >=
                                                                Build.VERSION_CODES.TIRAMISU
                                        ) {
                                                permissionLauncher.launch(
                                                        Manifest.permission.POST_NOTIFICATIONS
                                                )
                                        } else {
                                                viewModel.saveSettings()
                                                Toast.makeText(
                                                                context,
                                                                "Recordatorio guardado",
                                                                Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                onBack()
                                        }
                                },
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .padding(bottom = 24.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SensuGreen)
                        ) {
                                Text(
                                        text = "Guardar Recordatorio",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0E1B14)
                                )
                        }
                }
        }
}

@Composable
fun WheelPicker(
        range: IntRange,
        initialValue: Int,
        onValueChange: (Int) -> Unit,
        format: (Int) -> String = { it.toString() }
) {
        val itemHeight = 64.dp
        val itemCount = range.count()

        // 1. Calculamos el índice inicial para que esté "infinitamente" en el medio
        val firstIndex = remember {
                val mid = Int.MAX_VALUE / 2
                mid - (mid % itemCount) + (initialValue - range.first)
        }

        val listState = rememberLazyListState(initialFirstVisibleItemIndex = firstIndex)
        val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

        // 2. Detectar el valor central de forma eficiente
        val currentCenteredValue by remember {
                derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val visibleItems = layoutInfo.visibleItemsInfo
                        if (visibleItems.isEmpty()) initialValue
                        else {
                                // El ítem en el centro es el que está más cerca del medio del
                                // viewport
                                val viewportCenter =
                                        (layoutInfo.viewportStartOffset +
                                                layoutInfo.viewportEndOffset) / 2
                                val centerItem =
                                        visibleItems.minByOrNull {
                                                kotlin.math.abs(
                                                        (it.offset + it.size / 2) - viewportCenter
                                                )
                                        }
                                centerItem?.let { range.first + (it.index % itemCount) }
                                        ?: initialValue
                        }
                }
        }

        // Notificar al padre solo cuando el valor cambie
        LaunchedEffect(currentCenteredValue) { onValueChange(currentCenteredValue) }

        // 3. Usamos BoxWithConstraints para centrar dinámicamente
        BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                val halfHeight = maxHeight / 2

                LazyColumn(
                        state = listState,
                        flingBehavior = snapBehavior,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                        // El padding vertical es la mitad del alto total menos la mitad del alto
                        // del ítem
                        contentPadding = PaddingValues(vertical = halfHeight - (itemHeight / 2))
                ) {
                        items(count = Int.MAX_VALUE) { index ->
                                val value = range.first + (index % itemCount)
                                val isSelected = value == currentCenteredValue

                                Box(
                                        modifier = Modifier.height(itemHeight).fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text(
                                                text = format(value),
                                                fontSize = if (isSelected) 32.sp else 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color =
                                                        if (isSelected) Color(0xFF0E1B14)
                                                        else Color(0xFFAFD4C2).copy(alpha = 0.4f),
                                                modifier =
                                                        Modifier.scale(if (isSelected) 1.2f else 1f)
                                        )
                                }
                        }
                }
        }
}
