package com.appsfourlife.draftogo

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appsfourlife.draftogo.feature_generate_text.util.Screens
import com.appsfourlife.draftogo.helpers.HelperSharedPreference
import com.appsfourlife.draftogo.helpers.WindowInfo
import com.appsfourlife.draftogo.helpers.rememberWindowInfo
import com.appsfourlife.draftogo.ui.theme.Blue
import com.appsfourlife.draftogo.ui.theme.DrawerShape
import com.appsfourlife.draftogo.ui.theme.Shapes
import com.appsfourlife.draftogo.ui.theme.SpacersSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

@Composable
fun ScreenLaunch(
    modifier: Modifier = Modifier, navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    setSpacersSize()

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {

        Image(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "splash screen"
        )

        Timer().schedule(timerTask {
            coroutineScope.launch(Dispatchers.Main) {
                HelperSharedPreference.setBool(HelperSharedPreference.SP_SETTINGS, HelperSharedPreference.SP_SETTINGS_IS_FIRST_TIME_LAUNCHED, false, context)
                navController.navigate(Screens.ScreenHome.route)
            }
        }, 800)
    }
}

// region setting spacer size and shapes to be adaptable to different screen sizes
@Composable
fun setSpacersSize() {
    SpacersSize.small = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> 5.dp
        is WindowInfo.WindowType.Medium -> 10.dp
        else -> 15.dp
    }

    SpacersSize.medium = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> 15.dp
        is WindowInfo.WindowType.Medium -> 20.dp
        else -> 25.dp
    }

    SpacersSize.large = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> 30.dp
        is WindowInfo.WindowType.Medium -> 35.dp
        else -> 40.dp
    }

    Shapes = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> androidx.compose.material.Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(10.dp),
            large = RoundedCornerShape(15.dp)
        )
        is WindowInfo.WindowType.Medium -> androidx.compose.material.Shapes(
            small = RoundedCornerShape(7.dp),
            medium = RoundedCornerShape(13.dp),
            large = RoundedCornerShape(17.dp)
        )
        else -> androidx.compose.material.Shapes(
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(20.dp)
        )
    }

    DrawerShape = when (rememberWindowInfo().screenWidthInfo) {
        is WindowInfo.WindowType.Compact -> RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp, topStart = 0.dp, bottomStart = 0.dp)
        is WindowInfo.WindowType.Medium -> RoundedCornerShape(topEnd = 17.dp, bottomEnd = 17.dp, topStart = 0.dp, bottomStart = 0.dp)
        else -> RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp, topStart = 0.dp, bottomStart = 0.dp)
    }
}
// endregion
@Composable
fun drawGradientText(name: String, modifier: Modifier = Modifier) {

    val paint = Paint().asFrameworkPaint()
    val maxTextSize = 150f

    val gradientShader: Shader = LinearGradientShader(
        from = Offset(0f, 0f), to = Offset(0f, maxTextSize), listOf(Color.Blue, Blue)
    )

    Canvas(modifier) {
        paint.apply {
            isAntiAlias = true
            textSize = maxTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            style = android.graphics.Paint.Style.FILL
            color = android.graphics.Color.parseColor("#cdcdcd")
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
            maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
        }
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.nativeCanvas.translate(2f, 5f)
            canvas.nativeCanvas.drawText(name, 0f, maxTextSize, paint)
            canvas.restore()
            paint.shader = gradientShader
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            paint.maskFilter = null
            canvas.nativeCanvas.drawText(name, 0f, maxTextSize, paint)
            canvas.nativeCanvas.translate(2f, 5f)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
//            paint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
            canvas.nativeCanvas.drawText(name, 0f, maxTextSize, paint)
        }
        paint.reset()
    }
}