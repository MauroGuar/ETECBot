package com.etec_bot.EDA.schemes

enum class SchoolGrade(val value: String, val orientation: Orientation?) {
    FIRST("1ero", null),
    SECOND("2do", null),
    THIRD_IT("3ro Informatica", Orientation.IT),
    THIRD_ELECTRONICS("3ro Electronica", Orientation.ELECTRONICS),
    FOURTH_IT("4to Informatica", Orientation.IT),
    FOURTH_ELECTRONICS("4to Electronica", Orientation.ELECTRONICS),
    FIFTH_IT("5to Informatica", Orientation.IT),
    FIFTH_ELECTRONICS("5to Electronica", Orientation.ELECTRONICS),
    SIXTH_IT("6to Informatica", Orientation.IT),
    SIXTH_ELECTRONICS("6to Electronica", Orientation.ELECTRONICS)
}
enum class Orientation() {
    IT,
    ELECTRONICS
}
