/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package me.samuki.bassbeat.presentation

import android.Manifest
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberScalingLazyListState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import me.samuki.bassbeat.R
import me.samuki.bassbeat.presentation.domain.LightState
import me.samuki.bassbeat.presentation.theme.BassbeatTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            WearApp()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WearApp() {
    BassbeatTheme {
        val permission = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)
        var epilepsyWarningAccepted by remember {
            mutableStateOf(false)
        }

        if (permission.status.isGranted && epilepsyWarningAccepted) {
            PartyFlasher {
                epilepsyWarningAccepted = false
            }
        } else {
            PermissionAsk(permission.status.isGranted.not()) {
                epilepsyWarningAccepted = true
                permission.launchPermissionRequest()
            }
        }

    }
}

@Composable
fun PartyFlasher(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: MainViewModel = koinViewModel(),
    revokeAcceptedWarning: () -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_START -> {
                    viewModel.startObserving()
                    }
                    Lifecycle.Event.ON_STOP -> {
                    viewModel.stopObserving()
                        revokeAcceptedWarning()
                        lifecycleOwner.lifecycle.removeObserver(this)
                    }
                    else -> {}
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        when (val light = viewModel.showLight) {
            LightState.TurnedOff -> {}
            is LightState.TurnedOn -> drawRect(
                color = light.color
            )
        }
    }
}

@Composable
fun PermissionAsk(
    showRationale: Boolean,
    modifier: Modifier = Modifier,
    acceptEpilepsyAndPermission: () -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    LaunchedEffect(Unit) {
        scrollState.scrollToItem(0)
    }

    ScalingLazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        autoCentering = AutoCenteringParams(0),
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Text(text = stringResource(id = R.string.epileptic_warning))
        }
        if (showRationale) {
            item {
                Text(text = stringResource(id = R.string.permission_rationale))
            }
        }
        item {
            Button(onClick = {
                acceptEpilepsyAndPermission()
            }, modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    stringResource(id = R.string.accept),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

