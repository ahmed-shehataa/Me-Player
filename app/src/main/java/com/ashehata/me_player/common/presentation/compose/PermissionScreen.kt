package com.ashehata.me_player.common.presentation.compose

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashehata.me_player.R

data class PermissionScreenModel(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
    val onRequest: () -> Unit,
)

@Composable
fun PermissionScreen(permissionScreenModel: PermissionScreenModel) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .padding(16.dp)

    ) {
        Column(
            Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Image(
                imageVector = ImageVector.vectorResource(id = permissionScreenModel.iconRes),
                contentDescription = stringResource(id = permissionScreenModel.titleRes)
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = permissionScreenModel.titleRes),
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                style = MaterialTheme.typography.body1.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                ),
                textAlign = TextAlign.Center
            )
        }

        Button(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            onClick = { permissionScreenModel.onRequest() }) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.activate),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 16.sp,
                style = MaterialTheme.typography.body1.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@PreviewScreenSizes
@Composable
fun PermissionScreenPreview() {
    PermissionScreen(
        PermissionScreenModel(
            titleRes = R.string.notification_title,
            iconRes = R.drawable.ic_media,
            onRequest = {

            }
        )
    )
}