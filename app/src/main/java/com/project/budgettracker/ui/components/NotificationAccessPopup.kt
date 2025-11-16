import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun NotificationAccessPopup(
    hasNotificationReadAccess: Boolean,
    hasNotificationPostAccess: Boolean,
    onGrantNotificationRead: () -> Unit,
    onGrantNotificationPost:() -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Enable Notification Access", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Budget Tracker reads your bank SMS notifications to automatically detect expenses." +
                            "Notification texts are scanned to detect amount."+"\n"
                            +"Budget Tracker needs to send you Add Expense notification after detecting amount",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(text = "Allow Budget Tracker to Read Notifications",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f))

                    Button(onClick = onGrantNotificationRead, enabled = !hasNotificationReadAccess) {
                        if(!hasNotificationReadAccess) Text("Grant") else Text("Granted")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "Allow Budget Tracker to Send Notifications",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f))

                    Button(onClick = onGrantNotificationPost, enabled = !hasNotificationPostAccess) {
                        if(!hasNotificationPostAccess) Text("Grant") else Text("Granted")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onDismiss) { Text("No, thanks") }

            }
        }
    }
}

@Preview
@Composable
fun NotificationAccessPopupPreview() {
    NotificationAccessPopup(
        hasNotificationReadAccess = true,
        hasNotificationPostAccess = true,
        onGrantNotificationRead = {}, onGrantNotificationPost = {} , onDismiss = {})
}

@Preview
@Composable
fun NotificationAccessPopupDisbaledPreview() {
    NotificationAccessPopup(
        hasNotificationReadAccess = false,
        hasNotificationPostAccess = false,
        onGrantNotificationRead = {}, onGrantNotificationPost = {} , onDismiss = {})
}




