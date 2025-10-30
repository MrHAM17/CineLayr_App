/* UI Version - 01 */
//package com.example.core.designsystem.ui
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Brightness2
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CineLayrTopBar(
//    title: String,
//    isDark: Boolean,
//    onToggleTheme: () -> Unit,
//    onBackClick: (() -> Unit)? = null,  // optional back button
//    modifier: Modifier = Modifier        // For baseline profile test - added here
//
//) {
//    TopAppBar(
//        modifier = modifier,
//        title = { Text(title) },
//        navigationIcon = {
//            if (onBackClick != null) {
//                IconButton(onClick = onBackClick) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back"
//                    )
//                }
//            }
//            // else do nothing
//        },
//        actions = {
//            IconButton(onClick = onToggleTheme,
//            modifier = Modifier
//                .testTag("topbar_theme_toggle")    // For Unit Test - added here
//                .semantics { contentDescription = "topbar_theme_toggle" }
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Brightness2,
//                    contentDescription = if (isDark) "Switch to Light Mode" else "Switch to Dark Mode"
//                )
//            }
//        }
//    )
//}

package com.example.core.designsystem.theme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CineLayrTopBar(
    title: String,
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    onBackClick: (() -> Unit)? = null,  // optional back button
    modifier: Modifier = Modifier     // For baseline profile test - added here
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .height(56.dp), // standard height 56 (or 43.dp if you want slim)
//            .windowInsetsPadding(WindowInsets.statusBars),  // pending resolving ui issues - android 12+
        windowInsets = WindowInsets(0, 0, 0, 0), // disable system bar padding

        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = it) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            IconButton(
                onClick = onToggleTheme,
                modifier = Modifier
                    .testTag("topbar_theme_toggle")  // For Tests - added here
                    .semantics { contentDescription = "topbar_theme_toggle" }
            ) {
                Icon(
                    imageVector = Icons.Default.Brightness2,
                    contentDescription = if (isDark) "Switch to Light Mode" else "Switch to Dark Mode"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background, // surface
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
