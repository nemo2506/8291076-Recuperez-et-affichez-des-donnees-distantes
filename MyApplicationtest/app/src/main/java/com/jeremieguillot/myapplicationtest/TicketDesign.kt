import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremieguillot.myapplicationtest.R
import com.jeremieguillot.myapplicationtest.ui.theme.MyApplicationTestTheme

@Composable
fun OrderCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
//        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
//                Image(
//                    painter = painterResource(id = R.mipmap.ic_launcher), // Replace with your image resource
//                    contentDescription = null,
//                    modifier = Modifier.size(40.dp),
//                    contentScale = ContentScale.Crop
//                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Papa’s mesh cap",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Dior",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Received",
                    color = Color(0xFF00B388),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color(0xFFE0F7F4), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader(icon = Icons.Default.Info, title = "Order ID", value = "#tKt2341")
            SectionHeader(icon = Icons.Default.Call, title = "Date Paid", value = "May 15, 2022")
            CustomDivider()
            SectionHeader(icon = Icons.Default.AccountBox, title = "Colour", value = "Black")
            SectionHeader(icon = Icons.Default.AccountBox, title = "Size", value = "Medium")
            CustomDivider()
            SectionHeader(icon = Icons.Default.AccountBox, title = "Shipped via", value = "0 Street, Nothingness town")
            SectionHeader(icon = Icons.Default.AccountBox, title = "Shipping speed", value = "4-day delivery")
            SectionHeader(icon = Icons.Default.AccountBox, title = "Arrived", value = "Tuesday 02, June")
            CustomDivider()
            SectionHeader(icon = Icons.Default.AccountBox, title = "Subtotal", value = "$12.3")
            SectionHeader(icon = Icons.Default.AccountBox, title = "Quantity", value = "×1")
            SectionHeader(icon = Icons.Default.AccountBox, title = "You saved (37% off)", value = "-$4.6")
        }
    }
}

@Composable
fun SectionHeader(icon: ImageVector, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun CustomDivider() {
    BoxWithConstraints {
        val width = maxWidth
        Canvas(modifier = Modifier.fillMaxWidth()) {
            val cutSize = 16.dp.toPx()
            val middle = width.toPx() / 2
            val left = middle - cutSize / 2
            val right = middle + cutSize / 2

            drawLine(
                color = Color.LightGray,
                start = Offset(0f, 0f),
                end = Offset(left, 0f),
                strokeWidth = 1.dp.toPx()
            )

            drawCircle(
                color = Color.LightGray,
                center = Offset(middle, 0f),
                radius = cutSize / 2,
                style = Stroke(width = 1.dp.toPx())
            )

            drawLine(
                color = Color.LightGray,
                start = Offset(right, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}


@Preview
@Composable
private fun OrderCardPrev() {
    MyApplicationTestTheme {
        OrderCard()
    }
}