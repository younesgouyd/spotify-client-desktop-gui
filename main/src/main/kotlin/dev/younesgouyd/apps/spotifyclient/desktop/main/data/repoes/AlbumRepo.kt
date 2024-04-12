package dev.younesgouyd.apps.spotifyclient.desktop.main.data.repoes

import dev.younesgouyd.apps.spotifyclient.desktop.main.AlbumId
import dev.younesgouyd.apps.spotifyclient.desktop.main.ArtistId
import dev.younesgouyd.apps.spotifyclient.desktop.main.data.models.album.ArtistAlbums
import dev.younesgouyd.apps.spotifyclient.desktop.main.data.models.album.SavedAlbums
import dev.younesgouyd.apps.spotifyclient.desktop.main.toModel
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.models.Album
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.models.AlbumListItem
import dev.younesgouyd.apps.spotifyclient.desktop.main.ui.models.Artist
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumRepo(
    private val client: HttpClient,
    private val authRepo: AuthRepo
) {
    /**
     * GET /me/albums
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50.
     * @param offset The index of the first item to return. Default: 0 (the first item). Use with limit to get the next set of items.
     */
    suspend fun getSavedAlbums(limit: Int, offset: Int): List<AlbumListItem?> {
        return withContext(Dispatchers.IO) {
            client.get("me/albums") {
                header("Authorization", "Bearer ${authRepo.getToken()}")
                parameter("limit", limit)
                parameter("offset", offset)
            }.body<SavedAlbums>().toModel()
        }
    }

    /**
     * GET /artists/{id}/albums
     * @param id The Spotify ID of the artist
     * @param limit The maximum number of items to return. Default: 20. Minimum: 1. Maximum: 50
     * @param offset The index of the first item to return. Default: 0 (the first item). Use with limit to get the next set of items
     */
    suspend fun getArtistAlbums(id: ArtistId, limit: Int, offset: Int): List<Artist.Album> {
        return withContext(Dispatchers.IO) {
            client.get("artists/$id/albums") {
                header("Authorization", "Bearer ${authRepo.getToken()}")
                parameter("limit", limit)
                parameter("offset", offset)
            }.body<ArtistAlbums>().toModel()
        }
    }

    /**
     * GET /albums/{id}
     * @param id The Spotify ID of the album
     */
    suspend fun getAlbum(id: AlbumId): Album {
        return withContext(Dispatchers.IO) {
            client.get("albums/$id") {
                header("Authorization", "Bearer ${authRepo.getToken()}")
            }.body<dev.younesgouyd.apps.spotifyclient.desktop.main.data.models.album.Album>().toModel()
        }
    }
}