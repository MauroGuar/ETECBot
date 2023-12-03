package com.etec_bot.EDA.schemes

import com.etec_bot.google_drive.ServiceHandler
import com.google.api.services.drive.Drive
import java.io.InputStream

data class Subject(val name: String) : ServiceHandler {
    lateinit var googleDriveService: Drive
    var folderPath: String = "null"
    var folderID: String = "null"
    fun initFolder(googleDriveService: Drive, folderID: String) {
        this.googleDriveService = googleDriveService
        this.folderID = this.checkAndCreateFolderFromParentID(googleDriveService, folderID, this.name)
    }

    fun uploadFile(fileToUpload: java.io.File) {
        uploadFileToDrive(googleDriveService, this.folderID, fileToUpload)
    }

    override fun toString(): String {
        return "Subject(name='$name', googleDriveService=$googleDriveService, folderPath='$folderPath', folderID='$folderID')"
    }
}