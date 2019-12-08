package com.example.finalproject.ui.detailcourse


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.finalproject.R
import com.example.finalproject.adapters.detailcourse.ViewPagerAdapter
import com.example.finalproject.models.DetailCourseModel
import com.example.finalproject.ui.passingcourse.PassCourseActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.firebase.database.*
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_course_show_detail.*


class CourseShowDetail : AppCompatActivity() {

    private val CHILD: String = "CourseData"
    private val DEFAULT_DATA_FOR_SHARE = "I recommend you this course for learn!"
    private lateinit var titleCourse: CollapsingToolbarLayout
    private lateinit var categories: TextView
    private lateinit var numberPeople: TextView
    private lateinit var rating: TextView
    private lateinit var imageView: ImageView
    private lateinit var descriptionCourseFragment: DescriptionCourseFragment
    private lateinit var reviewCourseFragment: ReviewCourseFragment
    private lateinit var startBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_show_detail)
        setSupportActionBar(toolbar)

        //style collapseToolBar
        val collapsingToolbarLayout: CollapsingToolbarLayout = findViewById(R.id.titleCourse)
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor)

        //enabled back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //for correct working viewPager in NestedScroll
        val scrollView = findViewById<View>(R.id.nestedScrollView) as NestedScrollView
        scrollView.isFillViewport = true


        initializeViews()

        //get ID for this course
        val id = intent.getStringExtra("ID")!!
        getDataCourse(id)

        //for passing course
        startBtn.setOnClickListener {
           val intent = Intent(this, PassCourseActivity::class.java)
            intent.putExtra("ID_COURSE", id)
            intent.putExtra("TITLE_COURSE", titleCourse.title)
            ContextCompat.startActivity(this, intent, bundleOf())
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun getDataCourse(id: String) {
        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(CHILD).child(id)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val model: DetailCourseModel? = p0.getValue(DetailCourseModel::class.java)
                setDataIntoParentActivity(model!!)
                setDataIntoViewPager(model)
            }
            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun setDataIntoParentActivity(model: DetailCourseModel) {
        titleCourse.title = model.title
        rating.text = model.rating.toString()
        categories.text = model.nameSection
        numberPeople.text = model.numberPeople.toString()

        Glide.with(this)
            .load(model.imageUrl)
            .apply(bitmapTransform(BlurTransformation(10, 3)))
            .into(imageView)
    }


    private fun initializeViews() {
        titleCourse = findViewById(R.id.titleCourse)
        categories = findViewById(R.id.categories)
        numberPeople = findViewById(R.id.number_people)
        rating = findViewById(R.id.ratingDetailCourse)
        imageView = findViewById(R.id.bgFon)
        startBtn = findViewById(R.id.startCourse)
    }


    private fun setDataIntoViewPager(model: DetailCourseModel) {
        val adapter =
            ViewPagerAdapter(supportFragmentManager)
        descriptionCourseFragment =
            DescriptionCourseFragment(model)
        reviewCourseFragment = ReviewCourseFragment()

        adapter.addFragment(descriptionCourseFragment, "Описание")
        adapter.addFragment(reviewCourseFragment, "Отзывы")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    /*Next three methods for SHARE*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //for share
        menuInflater.inflate(R.menu.detail_course_toolbar, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //for share
        val id = item.itemId
        return if (id == R.id.action_name) {
            share(DEFAULT_DATA_FOR_SHARE)
            true
        } else super.onOptionsItemSelected(item)

    }


    private fun share(shareBody: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }
}
