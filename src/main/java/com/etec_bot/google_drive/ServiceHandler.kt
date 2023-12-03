package com.etec_bot.google_drive

import com.google.api.client.http.InputStreamContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import com.google.api.client.http.FileContent
import java.nio.file.Path


interface ServiceHandler {
    fun checkAndCreateFolder(googleDriveService: Drive, parentFolderID: String, folderPath: String): String {
        val parts: List<String> = folderPath.split("/")
        var parentId = parentFolderID
        for (part in parts) {
            val query =
                "mimeType='application/vnd.google-apps.folder' and name='$part' and '$parentId' in parents and trashed=false"
            val result: FileList = googleDriveService.files().list().setQ(query).execute()
            if (result.files.size > 0) {
                parentId = result.files[0].id
            } else {
                val fileMetadata = File()
                fileMetadata.setName(part);
                fileMetadata.setMimeType("application/vnd.google-apps.folder")
                fileMetadata.setParents(listOf<String>(parentId))
                val file: File = googleDriveService.files().create(fileMetadata)
                    .setFields("id")
                    .execute()
                parentId = file.id
            }
        }
        return parentId
    }

    fun checkAndCreateFolderFromParentID(googleDriveService: Drive, parentFolderID: String, folderName: String): String {
        val query =
            "mimeType='application/vnd.google-apps.folder' and name='$folderName' and '$parentFolderID' in parents and trashed=false"
        val result: FileList = googleDriveService.files().list().setQ(query).execute()
        if (result.files.size > 0) {
            return result.files[0].id;
        } else {
            val fileMetadata = File()
            fileMetadata.setName(folderName)
            fileMetadata.setMimeType("application/vnd.google-apps.folder")
            fileMetadata.setParents(listOf<String>(parentFolderID))
            val file: File = googleDriveService.files().create(fileMetadata).setFields("id").execute()
            return file.id
        }
    }

    fun getFolderId(googleDriveService: Drive, parentFolderID: String, folderPath: String): String {
        val parts: List<String> = folderPath.split("/")
        var parentId = parentFolderID
        for (part in parts) {
            val query =
                "mimeType='application/vnd.google-apps.folder' and name='$part' and '$parentId' in parents and trashed=false"
            val result: FileList = googleDriveService.files().list().setQ(query).execute()
            if (result.files.size > 0) {
                parentId = result.files[0].id
            }
        }
        return parentId
    }

    fun uploadFileToDrive(
        googleDriveService: Drive,
        folderID: String,
        fileToUpload: java.io.File
    ) {
        val fileMetadata = File()
        fileMetadata.name = fileToUpload.name
        fileMetadata.parents = listOf(folderID)
        val mediaContent = FileContent("application/${fileToUpload.extension}", fileToUpload)
        val driveFile = googleDriveService.files().create(fileMetadata, mediaContent)
            .setFields("id, parents")
            .execute()
        fileToUpload.delete()
    }
}