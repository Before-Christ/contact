package com.example.contact

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.contact.dao.ContactDbHelper
import com.example.contact.pojo.ContactPerson
import com.example.contact.unit.CharacterParser
import kotlinx.android.synthetic.main.activity_person.*
import kotlin.random.Random

class PersonDetailActivity : AppCompatActivity() {

    private var personId = -1
    private val mParser: CharacterParser = CharacterParser.getInstance()
    private val imageList = intArrayOf(
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
    private var headerImage = imageList[Random.nextInt(imageList.size)]
    private var person: ContactPerson? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)
        personId = intent.getIntExtra("personId", -1)
        if (personId != -1){
            selectPerson()
            person?.headerImage?.let { headerImageView.setImageResource(it) }
            personName.setText(person?.name)
            personNumber.setText(person?.phoneNumber)
            personEmail.setText(person?.email)
            personRemark.setText(person?.remark)
            personAddress.setText(person?.address)
            addAndUpdatePerson()
            removePerson()
        }else{
            headerImageView.setImageResource(headerImage)
        }
        addAndUpdatePerson()
        removePerson()
    }

    private fun selectPerson(){
        val dbHelper = ContactDbHelper(this, "Contact.db", 1)
        val db = dbHelper.writableDatabase
        val cursor =
            db.rawQuery("select * from contact where id = ?", arrayOf(personId.toString()))
        if (cursor.moveToFirst()){
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"))
            val headerImage = cursor.getInt(cursor.getColumnIndex("headerImage"))
            val address = cursor.getString(cursor.getColumnIndex("address"))
            val letter = cursor.getString(cursor.getColumnIndex("letter"))
            val email = cursor.getString(cursor.getColumnIndex("email"))
            val remark = cursor.getString(cursor.getColumnIndex("remark"))
            person = ContactPerson(id, name, phoneNumber, headerImage, email, address, remark, letter)
        }
        cursor.close()

    }

    private fun addAndUpdatePerson(){
        val dbHelper = ContactDbHelper(this, "Contact.db", 1)
        val db = dbHelper.writableDatabase
        fab.setOnClickListener {
            if (personId == -1 && personName.text.toString() != ""){
                val name = personName.text.toString()
                val phoneNumber = personNumber.text.toString()
                val email = personEmail.text.toString()
                val remark = personRemark.text.toString()
                val address = personAddress.text.toString()
                val letter = mParser.getSelling(name)
                println("!!!!!!")
                println(letter)
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
                Toast.makeText(this, "恭喜添加联系人成功！", Toast.LENGTH_SHORT).show()
            }else if (personId != -1 && personName.text.toString() != "") {
                val name = personName.text.toString()
                val phoneNumber = personNumber.text.toString()
                val email = personEmail.text.toString()
                val remark = personRemark.text.toString()
                val address = personAddress.text.toString()
                val letter = mParser.getSelling(name)

                db.execSQL("update contact set name = ? + phoneNumber = ? + email = ? + remark = ? + address = ? + letter = ?",
                    arrayOf(name, phoneNumber, email, remark, address, letter))
                Toast.makeText(this, "恭喜修改联系人成功！", Toast.LENGTH_SHORT).show()
            }
            if (personName.text.toString() != ""){
                Toast.makeText(this, "请输入联系人信息", Toast.LENGTH_SHORT).show()
            }


        }

    }
    private fun  removePerson(){
        delete.setOnClickListener{
            if (personId == -1){
                Toast.makeText(this, "联系人不存在，无法删除!", Toast.LENGTH_SHORT).show()
            }else {
                val dbHelper = ContactDbHelper(this, "Contact.db", 1)
                val db = dbHelper.writableDatabase
                db.execSQL("delete from contact where id = ?", arrayOf(personId.toString()))
                finish()
            }
        }

    }


}