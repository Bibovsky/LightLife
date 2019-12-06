package com.example.finalproject.models

data class PreviewCourseData(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var nameSection: String = "",
    var rating: Double = 7.5,
    var numberPeople: Int = 0,
    var imageUrl: String = ""
)