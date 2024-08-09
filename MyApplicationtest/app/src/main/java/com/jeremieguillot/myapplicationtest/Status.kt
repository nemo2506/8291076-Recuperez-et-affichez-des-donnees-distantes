package com.jeremieguillot.myapplicationtest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun StatusText(status: TicketStatus) {
    val (text, color, backgroundColor, icon) = when (status) {
        TicketStatus.RECEIVED -> StatusAttributes("Reçu", Color(0xFF00B388), Color(0xFFE0F7F4), Icons.Default.CheckCircle)
        TicketStatus.IN_PROGRESS -> StatusAttributes("En cours d'envoi", Color(0xFFFFA726), Color(0xFFFFF3E0), Icons.Default.Close)
        TicketStatus.CANCELED -> StatusAttributes("Annulé", Color(0xFFF44336), Color(0xFFFFEBEE), Icons.Default.Close)
        TicketStatus.IN_PROGRESS_CANCELED -> StatusAttributes("En cours d'annulation", Color(0xFFFF7043), Color(0xFFFFF3F2), Icons.Default.Close)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = color, modifier = Modifier.padding(end = 4.dp))
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
        )
    }
}

enum class TicketStatus {
    RECEIVED,
    IN_PROGRESS,
    CANCELED,
    IN_PROGRESS_CANCELED
}

data class StatusAttributes(
    val text: String,
    val color: Color,
    val backgroundColor: Color,
    val icon: ImageVector
)

@Preview(showBackground = true)
@Composable
fun PreviewReceivedStatus() {
    StatusText(status = TicketStatus.RECEIVED)
}

@Preview(showBackground = true)
@Composable
fun PreviewInProgressStatus() {
    StatusText(status = TicketStatus.IN_PROGRESS)
}

@Preview(showBackground = true)
@Composable
fun PreviewCanceledStatus() {
    StatusText(status = TicketStatus.CANCELED)
}

@Preview(showBackground = true)
@Composable
fun PreviewInProgressCanceledStatus() {
    StatusText(status = TicketStatus.IN_PROGRESS_CANCELED)
}
