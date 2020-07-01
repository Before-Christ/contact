package com.example.contact.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.contact.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.pojo.ContactPerson
import com.example.contact.unit.CharacterParser
import com.example.contact.unit.PinyinComparator
import com.example.contact.unit.SortModel
import kotlinx.android.synthetic.main.letter_item.view.*
import kotlinx.android.synthetic.main.recycle_view.view.*
import java.util.*


/**
 * @author caihn
 * @create 2020-06-28-15:07
 */
class ContactRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) , LetterTouchListener{

    private var mLetterHeight = 0
    private var mCurrentPosition = 0
    private var mLayoutManager: LinearLayoutManager? = null
    var letter_top: LinearLayout? = null
    var letter_title: TextView? = null
    private var contactPersonList: MutableList<ContactPerson> = mutableListOf()
    private val mParser: CharacterParser = CharacterParser.getInstance()


    init {
        LayoutInflater.from(context).inflate(R.layout.recycle_view, this)
        letter_top = findViewById(R.id.letter_top)
        letter_title = findViewById(R.id.letter_title)
        sideBarView.setLetterTouchListener(this)
        //添加滚动事件监听机制
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mLetterHeight = letter_top?.height!!
            }
            //dx 水平滚动距离，dy垂直滚动距离
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                //找到列表下一个可见的View
                var view = mLayoutManager?.findViewByPosition(mCurrentPosition + 1)
                // 检查列表中的letter布局是否显示
                if (view != null && view.top <= mLetterHeight && view?.findViewById<TextView>(R.id.letter_title)?.visibility == View.VISIBLE){
                    //被顶掉的效果
                    letter_top?.y = (-(mLetterHeight - view.top)).toFloat()
                }else{
                    letter_top?.y = 0f
                }
                //判断是否需要更新悬浮条
                if (mCurrentPosition != mLayoutManager?.findFirstVisibleItemPosition()){
                    letter_top?.y = 0f
                    updateLetter()
                }
            }
        })

    }
    //返回当前的recycle view
    fun getRecycler(): RecyclerView {
        return recyclerView
    }
    //侧边栏点击事件
    override fun setLetterVisibility(visibility: Int) {
        letterShow?.visibility = visibility
    }
    //侧边栏点击事件
    override fun setLetter(letter: String?) {
        if (TextUtils.isEmpty(letter) || letter!!.isEmpty()){
            return
        }
        letterShow?.text = letter
        var position = getPositionForSection(letter!![0].toInt())
        if (position != -1) {
            updateLetter()
            mLetterHeight = letter_title?.height!!
            mLayoutManager?.scrollToPositionWithOffset(position,0) // 使当前位置处于最顶端
        }
    }

    /**
     * 更新 字母title
     */
    fun updateLetter(){
        mCurrentPosition = mLayoutManager?.findFirstVisibleItemPosition()?: -1
        if (contactPersonList.size > 0 && mCurrentPosition > -1 && mCurrentPosition < contactPersonList.size){
            letter_title?.text = contactPersonList[mCurrentPosition].letter
        }
    }

    /**
     * 数据按照字母进行排序
     */
    fun sortData(data: MutableList<ContactPerson>): MutableList<ContactPerson> {
        val list = mutableListOf<ContactPerson>()
        for (person in data) {
            val pinyin = mParser.getSelling(person.name)
            val sortString = pinyin.substring(0, 1).toUpperCase()
            if (sortString.matches("[A-Z]".toRegex())) {
                person.letter = sortString
            } else {
                person.letter = "#"
            }
            list.add(person)
        }
        Collections.sort(list, PinyinComparator())
        return list
    }

    /**
     * 初始化数据, 每次输入查询置空的时候也需要
     */
    fun initData(data: MutableList<ContactPerson>?){
        contactPersonList.clear()
        contactPersonList.addAll(data ?: mutableListOf())
        updateLetter()
    }


    /**
     * 根据输入的内容刷新数据
     */
    fun updateData(filterStr: String): MutableList<ContactPerson> {
        var newData = mutableListOf<ContactPerson>()
        if (contactPersonList != null && contactPersonList!!.size > 0){
            if ("" == filterStr) {
                newData = contactPersonList!!
            } else {
                for (person in contactPersonList!!) {
                    val name = person.name
                    if (name.indexOf(filterStr) != -1 || mParser.getSelling(name).startsWith(filterStr)) {
                        newData.add(person)
                    }
                }
            }
        }
        contactPersonList?.clear()
        contactPersonList?.addAll(newData)
        updateLetter()
        return contactPersonList
    }

    /**
     * 获取字母首次出现的位置
     */
    fun getPositionForSection(section: Int): Int {
        if (contactPersonList == null || contactPersonList!!.size == 0){
            return -1
        }
        for (i in 0 until contactPersonList!!.size) {
            val s = contactPersonList!![i].letter
            val firstChar = s.toUpperCase()[0]
            if (firstChar.toInt() == section) {
                return i
            }
        }
        return -1
    }

    private fun hideSideBarView(){
        sideBarView.visibility = View.GONE
    }

}

