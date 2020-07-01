package com.example.contact

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contact.adapter.ContactSortAdapter
import com.example.contact.dao.ContactDbHelper
import com.example.contact.pojo.ContactPerson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycle_view.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var contactAdapter: ContactSortAdapter? = null
    private var contactPersonList: MutableList<ContactPerson>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // createData()
        contactPersonList = queryData()
        //search_friend.requestFocusFromTouch()
        initView()
        initData()
    }

    override fun onRestart() {
        super.onRestart()
        contactPersonList = queryData()
        initData()
    }


    private fun initView() {
        search_friend.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
//                sideBarView.visibility = View.INVISIBLE
                //每次查找栏位空的时候，就显示所有的数据。
                if (TextUtils.isEmpty(s.toString().trim())) {
                    contact_view?.initData(contactPersonList)
                    contactAdapter?.initData(contactPersonList)
                } else {
                    //如果输入数据，就根据输入的数据更新列表
                    contactAdapter?.initData(contact_view?.updateData(s.toString()))
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
//                search_friend.setOnClickListener {
//                    sideBarView.visibility = View.INVISIBLE
//                }


            }

            override fun afterTextChanged(arg0: Editable) {
//                sideBarView.visibility = View.VISIBLE
            }
        })

//         search_friend.setOnClickListener {
//             sideBarView.visibility = View.INVISIBLE
//         }
//        search_friend.setOnTouchListener(object: View.OnTouchListener{
//            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//
//                sideBarView.visibility = View.INVISIBLE
//                search_friend.requestFocus()
//                val input: InputMethodManager= search_friend.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                input.showSoftInput(search_friend, 0)
//                return true
//            }
//
//        })
//        search_friend.onFocusChangeListener =
//            View.OnFocusChangeListener { p0, p1 -> sideBarView.visibility = View.VISIBLE }
        sideBarView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 0)) {
                    sideBarView.visibility = View.INVISIBLE
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 0)) {
                    sideBarView.visibility = View.VISIBLE
                }
            }

        })

    }

    private fun initData() {
        contactAdapter = ContactSortAdapter(this)
        val recyclerView = contact_view?.getRecycler()
        val layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = contactAdapter
        contactPersonList = contactPersonList?.let { contact_view?.sortData(it) }
        contact_view?.initData(contactPersonList)
        contactAdapter?.initData(contactPersonList)

    }


    /**
     * 用于从数据库获得数据的方法
     */
    private fun queryData(): ArrayList<ContactPerson> {
        val dbHelper = ContactDbHelper(this, "Contact.db", 1)
        val db = dbHelper.writableDatabase
        val cursor = db.query("contact", null, null, null, null, null, null)
        val contactPersonList = ArrayList<ContactPerson>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"))
                val headerImage = cursor.getInt(cursor.getColumnIndex("headerImage"))
                val address = cursor.getString(cursor.getColumnIndex("address"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                val remark = cursor.getString(cursor.getColumnIndex("remark"))
                val letter = cursor.getString(cursor.getColumnIndex("letter"))
                val person = ContactPerson(
                    id,
                    name,
                    phoneNumber,
                    headerImage,
                    email,
                    address,
                    remark,
                    letter
                )
                contactPersonList.add(person)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contactPersonList
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toobar, menu)
        return true
    }

    /**
     * 用于处理菜单的方法
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> jump()
        }
        return true
    }

    private fun jump() {
        val intent = Intent(this, PersonDetailActivity::class.java)
        startActivity(intent)
    }

    //用于创造数据，只有第一次进入程序需要
    private fun createData() {
        val imageList = intArrayOf(
            R.drawable.apple,
            R.drawable.banana,
            R.drawable.cherry,
            R.drawable.grape,
            R.drawable.orange,
            R.drawable.mango,
            R.drawable.strawberry,
            R.drawable.watermelon,
            R.drawable.pineapple,
            R.drawable.nav_icon,
            R.drawable.yihao,
            R.drawable.erhao,
            R.drawable.sanhao,
            R.drawable.sihao,
            R.drawable.wuhao
        )
        val emailList = arrayListOf<String>(
            "aisi.@163.com", "liubei@163.com", "caocao@163.com", "sunquan@163.com",
            "zhugeliang@163.com", "baiyujing@163.com", "dabai@163.com", "xiaohei@163.com",
            "alice@163.com", "jiujiu@163.com"
        )
        val addressList = arrayListOf<String>(
            "安徽合肥", "江苏苏州", "广东广州", "北京三里屯", "上海陆家嘴", "合肥工业大学",
            "中国科学技术大学", "上海交通大学", "广州网易互娱"
        )
        val phoneNumberList = arrayListOf<String>(
            "13956261486", "13866567856", "16856892345", "17523698548", "15945876325",
            "16826871458", "16856944578", "18866542356", "13652685729"
        )
        val hashMapList = hashMapOf<String, String>(
            "艾斯" to "aisi",
            "贝利" to "beili",
            "蔡伦" to "cailun",
            "德国" to "deguo",
            "鹅鹅鹅" to "eee",
            "伏羲" to "fuxi",
            "高振" to "gaozhen",
            "和珅" to "heshen",
            "iiiiii" to "iiiii",
            "杰克" to "jieke",
            "珂珂" to "keke",
            "栗子" to "lizi",
            "孟祥宇" to "mengxiangyu",
            "牛顿" to "niudun",
            "欧珀" to "oupo",
            "皮皮鲁" to "pipilu",
            "秋意浓" to "qiuyinong",
            "任天天" to "rentiantian",
            "申屠" to "shentu",
            "腾宇" to "tengyu",
            "鱼一支" to "yuyizhi",
            "uu加速器" to "uujiasuqi",
            "vvvv" to "vvvv",
            "王五" to "wangwu",
            "嘻嘻嘻" to "xixixi",
            "张天保" to "zhangtianbao"
        )
        val dbHelper = ContactDbHelper(this, "Contact.db", 1)
        val db = dbHelper.writableDatabase
        repeat(5) {
            for (key in hashMapList.keys) {
                val name = key
                val letter = hashMapList[key]
                val address = addressList[Random.nextInt(addressList.size)]
                val phoneNumber = phoneNumberList[Random.nextInt(phoneNumberList.size)]
                val headerImage = imageList[Random.nextInt(imageList.size)]
                val email = emailList[Random.nextInt(emailList.size)]
                val remark = "没有什么好说的"
                val values = ContentValues().apply {
                    put("name", name)
                    put("phoneNumber", phoneNumber)
                    put("email", email)
                    put("remark", remark)
                    put("address", address)
                    put("letter", letter)
                    put("headerImage", headerImage)
                }
                db.insert("contact", null, values)
            }
        }
    }
}