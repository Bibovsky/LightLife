package com.example.finalproject.models

data class DemoData(
    var title: String = "",
    var description: String = "",
    var nameSection: String = "",
    var rating: Double = 7.5,
    var numberPeople: Int = 0,
    var imageUrl: String = "",
    var lang: String = "",
    var hoursForPass: Int = 0,
    var dataVideoPassCourse: ArrayList<VideoModel>,
    var dataTextPassCourse: ArrayList<TextPassCourseModel>
)