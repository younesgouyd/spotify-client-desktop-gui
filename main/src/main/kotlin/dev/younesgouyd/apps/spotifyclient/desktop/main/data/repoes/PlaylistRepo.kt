package dev.younesgouyd.apps.spotifyclient.desktop.main.data.repoes

import dev.younesgouyd.apps.spotifyclient.desktop.main.PlaylistId
import dev.younesgouyd.apps.spotifyclient.desktop.main.UserId
import dev.younesgouyd.apps.spotifyclient.desktop.main.data.models.playlist.Playlists
import dev.younesgouyd.apps.spotifyclient.desktop.main.data.models.playlist.UserPlaylists
import dev.younesgouyd.apps.spotifyclient.desktop.main.toModel
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.models.Playlist
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.models.PlaylistListItem
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.models.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class PlaylistRepo(
    private val client: HttpClient,
    private val authRepo: AuthRepo
) {
    /**
     * GET /me/playlists
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50
     * @param offset The index of the first playlist to return. Default: 0 (the first object). Maximum offset: 100.000. Use with limit to get the next set of playlists
     */
    suspend fun getCurrentUserPlaylists(limit: Int, offset: Int): List<PlaylistListItem?> {
        return client.get("me/playlists") {
            header("Authorization", "Bearer ${authRepo.getToken()}")
            parameter("limit", limit)
            parameter("offset", offset)
        }.body<Playlists>().toModel()
    }

    /**
     * GET /playlists/{playlist_id}
     * @param playlistId The Spotify ID of the playlist
     */
    suspend fun get(playlistId: PlaylistId): Playlist {
        return client.get("playlists/$playlistId") {
            header("Authorization", "Bearer ${authRepo.getToken()}")
        }.body<dev.younesgouyd.apps.spotifyclient.desktop.main.data.models.playlist.Playlist>().toModel()
    }

    /**
     * GET /users/{user_id}/playlists
     */
    suspend fun getUserPlaylists(userId: UserId, limit: Int, offset: Int): List<User.Playlist?> {
        return client.get("users/$userId/playlists") {
            header("Authorization", "Bearer ${authRepo.getToken()}")
            parameter("limit", limit)
            parameter("offset", offset)
        }.body<UserPlaylists>().toModel()
    }
}