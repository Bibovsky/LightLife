package com.example.finalproject.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.adapters.searchcourse.CategoriesAdapter
import com.example.finalproject.adapters.searchcourse.CoursesSectionAdapter
import com.example.finalproject.models.*
import com.example.finalproject.models.Constant.Companion.context
import com.google.android.youtube.player.internal.i
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.video_item.*
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mReference: DatabaseReference
    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private val listHeaderSections: ArrayList<String> = arrayListOf("Питание", "Спорт", "Режим дня")
    private val CHILD: String = "CourseData"
    private val CHILD_MY_COURSES = "MyCourses"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(container!!.context)
            .inflate(R.layout.fragment_search, container, false)

        mReference = FirebaseDatabase.getInstance().reference.child(CHILD)


        val dataVideo: ArrayList<VideoModel> = arrayListOf()
        dataVideo.add(
            VideoModel(
                "Про режим дня",
                "ve2fEg0C7qg",
                "https://downloader.disk.yandex.ru/preview/e3920bb48d3a62692fa425d0bb62450da1e73de77b061d404d3732e9328f3255/5ded7925/8OfGmGB4dyxlOeHPnz2OGUx6h3QQg8wBva94RjTwErOKLBSuFqgPbDOnDG6aOy-y0Ob6b_E4CRn66YbnQ0bSNw==?uid=0&filename=2019-12-08_21-27-58.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&tknv=v2&owner_uid=422633291&size=2048x2048"
            )
        )
        dataVideo.add(
            VideoModel(
                "Идеальный режим дня",
                "XKv8TmllDzE&t",
                "https://downloader.disk.yandex.ru/preview/e3920bb48d3a62692fa425d0bb62450da1e73de77b061d404d3732e9328f3255/5ded7925/8OfGmGB4dyxlOeHPnz2OGUx6h3QQg8wBva94RjTwErOKLBSuFqgPbDOnDG6aOy-y0Ob6b_E4CRn66YbnQ0bSNw==?uid=0&filename=2019-12-08_21-27-58.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&tknv=v2&owner_uid=422633291&size=2048x2048"
            )
        )
        val dataText: ArrayList<TextPassCourseModel> = arrayListOf()
        dataText.add(
            TextPassCourseModel(
                "Режим дня - это определенный распорядок труда, отдыха, питания и сна. Режим - это в первую очередь самодисциплина, добровольное выполнение принятых на себя обязательств. Он приучает каждого к организованности, разумному использованию своего времени",
                "https://healthy-kids.ru/wp-content/uploads/2016/09/1200x790_ZD_Vospitanie_06-09_01.jpg"
            )
        )
        dataText.add(
            TextPassCourseModel(
                "Ритмичность свойственна человеческому организму, отдельным его органам и системам. Поэтому организация и строгое соблюдение режима дня, предусматривающего переход от бодрствования ко сну и наоборот, выполнение гигиенических процедур, различные виды деятельности, отдых, прием пищи в одно и тоже время и др., в соответствии с возрастными особенностями, создают наилучшие условия для жизнедеятельности организма детей и подростков.",
                "https://avatars.mds.yandex.net/get-zen_doc/103153/pub_5ba9939d25dbcd00aaf7e9db_5ba993c74e9adf00abea2c64/scale_1200"
            )
        )
        dataText.add(
            TextPassCourseModel(
                "Физиологический режим дня обоснован выработкой условных рефлексов, которые со временем закрепляются подчас на всю жизнь в виде устойчивых навыков и привычек и оказывают положительное влияние на функции организма: привычка ложиться спать в одно и то же время способствует быстрому засыпанию, организм лучше восстанавливает силы; прием пищи в определенное время способствует хорошему аппетиту и помогает лучше усвоению пищи и т.д\n" +
                        "\n" +
                        "Установить единый распорядок дня для всех учащихся, естественно, невозможно, но основные его моменты должны соблюдаться каждым.\n" +
                        "\n" +
                        "Каждый свой день необходимо начинать с утренней гимнастики (зарядки). Она облегчает переход от сна к рабочему состоянию, позволяет «зарядить» организм бодростью на целый день. Родители и педагоги должны понимать, что кроме утренней гимнастики, физкультпаузы, подвижных игр на переменах, необходимы регулярные занятия спортом и физическим трудом.",
                "https://resh.edu.ru/uploads/lesson_extract/5736/20190517112415/OEBPS/objects/m_ptls_1_3_1/5c656f2c8b141757fe1e8a7a.jpg"
            )
        )
        dataText.add(
            TextPassCourseModel(
                "Формирование позитивного отношения к режиму дня. Как и воспитание любых гигиенических навыков, успешнее проходит в начальной школе. От детей нужно мягко, но настойчиво требовать выполнения режима ежедневно без принуждения, т.к. вызывает у большинства из них внутреннее сопротивление. Составить режим дня с учетом особенностей семьи и интересов ребенка не столь трудно. Труднее научит школьника выполнить его. Разъяснительная работа в школе, твердость и повседневный контроль со стороны родителей помогут ребенку соблюдать режим и это станет обязательным в поведенческом стереотипе ребенка.\n" +
                        "\n" +
                        "Одним из важнейших моментов режима дня является отдых, так называемый активный, который заключается в смене одного вида деятельности другим ( занятие физкультурой и спортом, прогулки, игры, пребывание на свежем воздухе.",
                "https://g0.sunmag.me/sunmag.me/wp-content/uploads/2018/02/sunmag-sunmag-055-anons-1260x600-1519452864.png"
            )
        )
/*

        sendDemoData(
            "Курс по режиму дня",
            "Как правильно составить режим дня – одна из самых важных тем здорового образа жизни. Каждый человек сталкивается с необходимостью распределять свое время. Иногда, как в случае с работой, это необходимость. Иногда, например, при планировании максимально продуктивного времяпрепровождения или отдыха – это целесообразность. Правильный режим дня подразумевает рациональное использование времени сна, личной гигиены, питания, работы, отдыха, занятий спортом и физической активности. Планирование распорядка дня и следование ему делает человека дисциплинированным, развивает организованность и целенаправленность. В результате чего вырабатывается и режим жизни, в котором минимизированы затраты времени и энергии на несущественные вещи.",
            "Режим дня",
            6.7,
            553,
            "https://firebasestorage.googleapis.com/v0/b/finalproject-757d3.appspot.com/o/courses_images%2Fdownload.jpg?alt=media&token=5a16d9a4-ef96-488c-85bc-5794e98ef49a", "Русский", 5, dataVideo, dataText
        )*/


        initializeViews(view)
        setListenerSearchViaEditText()
        initCategoriesRecycler()
        setListCourse(false, "")

        return view
    }


    private fun setListenerSearchViaEditText() {
        //for search via EditText field
        searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val textSearch = searchEditText.text.toString().trim().toLowerCase()
                setListCourse(true, textSearch)

                val imm = v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })
    }


    private fun filteredSearchedCourses(
        list: ArrayList<PreviewCourseModel>,
        titleForSearch: String
    ): ArrayList<PreviewCourseModel> {
        val resultList: ArrayList<PreviewCourseModel> = arrayListOf()
        val strIterable: Iterator<PreviewCourseModel> = list.iterator()

        while (strIterable.hasNext()) {
            val value = strIterable.next()
            if (value.title.trim().toLowerCase() == titleForSearch || titleForSearch == "") {
                resultList.add(value)
            }
        }
        return resultList
    }


    private fun setListCourse(isSearch: Boolean, titleForSearch: String) {
        var list: ArrayList<PreviewCourseModel> = arrayListOf()
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child(CHILD)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val dataCourseModel: PreviewCourseModel? =
                        it.getValue(PreviewCourseModel::class.java)
                    list.add(
                        PreviewCourseModel(
                            it.key!!,
                            dataCourseModel!!.title,
                            dataCourseModel.description,
                            dataCourseModel.nameSection,
                            dataCourseModel.rating,
                            dataCourseModel.numberPeople,
                            dataCourseModel.imageUrl
                        )
                    )
                }
                if (isSearch) {
                    list = filteredSearchedCourses(list, titleForSearch)
                }
                //delete courses that have already started by user
                deleteSelectedCourse(list)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun createSectionCoursesRecycler(list: ArrayList<PreviewCourseModel>) {
        val sectionedAdapter = SectionedRecyclerViewAdapter()
        val map = LinkedHashMap<String, ArrayList<PreviewCourseModel>>()

        for (i in 0 until listHeaderSections.size) {
            val filteredCourses = getCoursesOneSection(list, listHeaderSections[i])
            map[listHeaderSections[i]] = filteredCourses
        }

        for (entry in map.entries) {
            if (entry.value.isNotEmpty()) {
                sectionedAdapter.addSection(
                    CoursesSectionAdapter(
                        entry.key,
                        entry.value,
                        context!!
                    )
                )
            }
        }

        initCoursesRecycler(sectionedAdapter)
    }


    private fun getCoursesOneSection(
        list: ArrayList<PreviewCourseModel>,
        nameSection: String
    ): ArrayList<PreviewCourseModel> {
        val listOneSection: ArrayList<PreviewCourseModel> = arrayListOf()
        val strIterable: Iterator<PreviewCourseModel> = list.iterator()

        while (strIterable.hasNext()) {
            val value = strIterable.next()
            if (value.nameSection == nameSection) {
                listOneSection.add(value)
            }
        }
        return listOneSection
    }


    private fun deleteSelectedCourse(list: ArrayList<PreviewCourseModel>) {
        mAuth = FirebaseAuth.getInstance()

        val resultList: ArrayList<PreviewCourseModel> = ArrayList(list)

        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child(CHILD_MY_COURSES).child(mAuth.uid!!)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val id: String = it.value.toString()

                    for (i in 0 until resultList.size){
                        if(id == resultList[i].id){
                            list.remove(resultList[i])
                        }
                    }
                }

                //creating RecyclerView with sections
                createSectionCoursesRecycler(list)
            }

            override fun onCancelled(p0: DatabaseError) {
                //not implemented
            }
        })
    }


    private fun initCoursesRecycler(sectionedAdapter: SectionedRecyclerViewAdapter) {
        coursesRecyclerView.layoutManager = LinearLayoutManager(context)
        coursesRecyclerView.adapter = sectionedAdapter
    }


    private fun initCategoriesRecycler() {
        categoriesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoriesRecyclerView.adapter =
            CategoriesAdapter(
                listHeaderSections,
                context!!,
                coursesRecyclerView
            )
    }


    private fun initializeViews(view: View) {
        searchEditText = view.findViewById(R.id.searchEditText)
        coursesRecyclerView = view.findViewById(R.id.coursesRecycler)
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecycler)
    }


    //DEMO TEST
    private fun sendDemoData(
        title: String,
        description: String,
        nameSection: String,
        rating: Double,
        numberPeople: Int,
        imageUrl: String,
        lang: String,
        hoursForPass: Int,
        dataVideo: ArrayList<VideoModel>,
        dataText: ArrayList<TextPassCourseModel>
    ) {
        val data = DemoData(
            title,
            description,
            nameSection,
            rating,
            numberPeople,
            imageUrl,
            lang,
            hoursForPass,
            dataVideo,
            dataText
        )
        mReference.push().setValue(data)
    }

}

