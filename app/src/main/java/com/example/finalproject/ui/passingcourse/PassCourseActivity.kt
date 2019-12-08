package com.example.finalproject.ui.passingcourse

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.finalproject.R
import com.example.finalproject.adapters.passingcourse.ViewPagerAdapter
import com.example.finalproject.models.DetailCourseModel
import com.example.finalproject.models.TextPassCourseModel
import com.example.finalproject.models.VideoModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_pass_course.*


class PassCourseActivity : AppCompatActivity() {

    private val tabIcons: IntArray = intArrayOf(R.drawable.ic_book, R.drawable.ic_play)
    private lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass_course)

        val toolbar: Toolbar = findViewById(R.id.passCourseToolbar)
        val idCourse = intent.getStringExtra("ID_COURSE")!!
        val titleCourse = intent.getStringExtra("TITLE_COURSE")
        toolbar.title = titleCourse
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        setColorStatusBar()
        initViewPager(idCourse)
    }


    private fun initViewPager(idCourse: String) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TextContentFragment(idCourse))
        adapter.addFragment(VideoContentFragment(idCourse))

        viewPagerPassingCourse.adapter = adapter
        tabLayout = findViewById(R.id.passCourseTabs)
        tabLayout.setupWithViewPager(viewPagerPassingCourse)
        setupTabIcons()
    }


    private fun setupTabIcons() {
        tabLayout.getTabAt(0)!!.setIcon(tabIcons[0])
        tabLayout.getTabAt(1)!!.setIcon(tabIcons[1])
    }


    private fun setColorStatusBar() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.StatusBar)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
