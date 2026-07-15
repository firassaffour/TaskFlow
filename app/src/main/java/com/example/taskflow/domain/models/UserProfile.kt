package com.example.taskflow.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * There's only ever one row in this table - a single local user profile,
 * not a multi-account system. Pinning [id] to a fixed value (rather than
 * autoGenerate) means "get the profile" is always just "the row where
 * id = SINGLETON_ID", and @Upsert always overwrites that same row instead
 * of accumulating new ones every time the user hits save.
 *
 * [imagePath] is a path into this app's own private storage
 * (filesDir), not the raw content:// URI the picker returns - picker URIs
 * can lose read permission after the app restarts, so we copy the bytes
 * into a file we own the moment the user picks an image.
 */
@Entity
data class UserProfile(
    @PrimaryKey
    val id: Int = SINGLETON_ID,
    val name: String = "Alex",
    val imagePath: String? = null
) {
    companion object {
        const val SINGLETON_ID = 0
    }
}
