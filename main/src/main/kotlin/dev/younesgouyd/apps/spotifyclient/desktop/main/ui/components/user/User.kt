package dev.younesgouyd.apps.spotifyclient.desktop.main.ui.components.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.younesgouyd.apps.spotifyclient.desktop.main.PlaylistId
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.Image
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.Item
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.ScrollToTopFloatingActionButton
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.VerticalScrollbar
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.models.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun User(state: UserState) {
    when (state) {
        is UserState.Loading -> Text("Loading...")
        is UserState.State -> User(state)
    }
}

@Composable
private fun User(state: UserState.State) {
    User(
        user = state.user,
        loadingPlaylists = state.loadingPlaylists,
        playlists = state.playlists,
        onLoadPlaylists = state.onLoadPlaylists,
        onPlaylistClick = state.onPlaylistClick
    )
}

@Composable
private fun User(
    user: User,
    loadingPlaylists: StateFlow<Boolean>,
    playlists: StateFlow<List<User.Playlist>>,
    onLoadPlaylists: () -> Unit,
    onPlaylistClick: (PlaylistId) -> Unit
) {
    val loadingPlaylists by loadingPlaylists.collectAsState()
    val playlists by playlists.collectAsState()
    val lazyGridState = rememberLazyGridState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                VerticalScrollbar(lazyGridState)
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize().padding(end = 16.dp),
                    state = lazyGridState,
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    columns = GridCells.Adaptive(250.dp)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        UserInfo(user = user)
                    }
                    playlists(playlists, onPlaylistClick)
                    if (loadingPlaylists) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(modifier = Modifier.fillMaxWidth().padding(10.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(modifier = Modifier.size(50.dp), strokeWidth = 2.dp)
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = { ScrollToTopFloatingActionButton(lazyGridState) }
    )

    LaunchedEffect(lazyGridState) {
        snapshotFlow {
            lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.map { it == null || it >= (playlists.size + 2) - 5 }
            .filter { it }
            .collect { onLoadPlaylists() }
    }
}

@Composable
private fun UserInfo(
    modifier: Modifier = Modifier,
    user: User
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = 8.dp,
            alignment = Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            url = user.profilePictureUrl
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = user.displayName ?: "",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "${user.followerCount} followers",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

private fun LazyGridScope.playlists(
    playlists: List<User.Playlist>,
    onPlaylistClick: (PlaylistId) -> Unit
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Playlists",
            style = MaterialTheme.typography.headlineMedium
        )
    }
    items(
        items = playlists,
        key = { it.id }
    ) { item ->
        Item(onClick = { onPlaylistClick(item.id) }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    modifier = Modifier.aspectRatio(1f),
                    url = item.images.preferablyMedium(),
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.TopCenter
                )
                Text(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    text = item.name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}