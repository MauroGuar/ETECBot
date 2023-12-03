package com.etec_bot.EDA.schemes

import com.etec_bot.google_drive.ServiceHandler
import com.google.api.services.drive.Drive
import java.io.InputStream

data class Grade(
    val googleDriveService: Drive,
    val year: Int,
    val subjects: ArrayList<Subject>,
    val schoolGradeEnum: SchoolGrade,
    val folderPath: String,
    val parentFolderID: String
) : ServiceHandler {
    var folderID: String = "null"

    init {
        folderID = this.checkAndCreateFolder(googleDriveService, parentFolderID, folderPath)
        initSubjects()
    }

    fun initSubjects() {
        subjects.forEach {
            it.folderPath = this.folderPath + "/" + it.name
            it.initFolder(googleDriveService, folderID)
        }
    }

    fun addSubject(subject: Subject) {
        if (subjects.contains(subject)) return
        subject.initFolder(googleDriveService, folderID)
        subjects.add(subject)
    }
    fun addSubject(subjects: List<Subject>) {
        subjects.forEach {
            if (!subjects.contains(it)) {
                it.initFolder(googleDriveService, folderID)
                this.subjects.add(it)
            }
        }
    }

    fun addManySubjects(subjectsName: List<String>) {
        subjectsName.forEach {
            val subjectToAdd = Subject(it)
            if (!subjects.contains(subjectToAdd)) {
                subjectToAdd.initFolder(googleDriveService, folderID)
                this.subjects.add(subjectToAdd)
            }
        }
    }

    fun uploadFile(fileToUpload: java.io.File) {
        uploadFileToDrive(googleDriveService, this.folderID, fileToUpload)
    }

    override fun toString(): String {
        return "Grade(googleDriveService=$googleDriveService, year=$year, subjects=$subjects, schoolGradeEnum=$schoolGradeEnum, folderPath='$folderPath', parentFolderID='$parentFolderID', folderID='$folderID')"
    }
}