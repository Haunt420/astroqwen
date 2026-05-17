package com.stellarpath.android.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.stellarpath.android.MainActivity

class DailyHoroscopeWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                DailyHoroscopeContent()
            }
        }
    }
}

@Composable
private fun DailyHoroscopeContent() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(Color(0xFF1A1A2E)))
            .clickable(actionStartActivity<MainActivity>())
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Daily Horoscope",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFE0E0E0)),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            
            Spacer(modifier = GlanceModifier.height(8.dp))
            
            Text(
                text = "Aries",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFF7B500)),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = GlanceModifier.height(4.dp))
            
            Text(
                text = "A productive rhythm returns",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFB0B0B0)),
                    fontSize = 12.sp
                ),
                maxLines = 2
            )
            
            Spacer(modifier = GlanceModifier.height(8.dp))
            
            Text(
                text = "Tap to open",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF6B6B8D)),
                    fontSize = 10.sp
                )
            )
        }
    }
}

class DailyHoroscopeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DailyHoroscopeWidget()
}