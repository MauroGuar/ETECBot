package com.etec_bot.EDA

import com.etec_bot.EDA.schemes.Grade
import com.etec_bot.EDA.schemes.SchoolGrade
import com.etec_bot.EDA.schemes.Subject
import com.etec_bot.google_drive.Connection
import com.etec_bot.google_drive.ServiceHandler
import com.google.api.services.drive.Drive
import java.io.File

class EDA: ServiceHandler {
    private val googleDriveService: Drive = Connection.instance.getService()
    private val mainFolderID: String = checkAndCreateFolder(googleDriveService, "root", "ADE")

    private val firstGrade = Grade(googleDriveService, 1, arrayListOf(), SchoolGrade.FIRST, "1ero", mainFolderID)
    private val secondGrade = Grade(googleDriveService, 2, arrayListOf(), SchoolGrade.SECOND, "2do", mainFolderID)
    private val thirdITGrade = Grade(googleDriveService, 3, arrayListOf(), SchoolGrade.THIRD_IT, "3ro I", mainFolderID)
    private val thirdElectronicsGrade =
        Grade(googleDriveService, 3, arrayListOf(), SchoolGrade.THIRD_ELECTRONICS, "3ro E", mainFolderID)
    private val fourthITGrade = Grade(googleDriveService, 4, arrayListOf(), SchoolGrade.FOURTH_IT, "4to I", mainFolderID)
    private val fourthElectronicsGrade =
        Grade(googleDriveService, 4, arrayListOf(), SchoolGrade.FOURTH_ELECTRONICS, "4to E", mainFolderID)
    private val fifthITGrade = Grade(googleDriveService, 5, arrayListOf(), SchoolGrade.FIFTH_IT, "5to I", mainFolderID)
    private val fifthElectronicsGrade =
        Grade(googleDriveService, 5, arrayListOf(), SchoolGrade.FIFTH_ELECTRONICS, "5to E", mainFolderID)
    private val sixthITGrade = Grade(googleDriveService, 6, arrayListOf(), SchoolGrade.SIXTH_IT, "6to I", mainFolderID)
    private val sixthElectronicsGrade =
        Grade(googleDriveService, 6, arrayListOf(), SchoolGrade.SIXTH_ELECTRONICS, "6to E", mainFolderID)

    val grades: ArrayList<Grade> = arrayListOf(
        firstGrade,
        secondGrade,
        thirdITGrade,
        thirdElectronicsGrade,
        fourthITGrade,
        fourthElectronicsGrade,
        fifthITGrade,
        fifthElectronicsGrade,
        sixthITGrade,
        sixthElectronicsGrade
    )

    init {
        grades.forEach {
            it.addManySubjects(listOf("Matematica", "Lengua", "Ingles"))
        }
    }

    fun addSubject(subjectName: String, schoolGrade: SchoolGrade) {
        val grade = grades.find { it.schoolGradeEnum == schoolGrade }
        grade?.addSubject(Subject(subjectName))
    }
}