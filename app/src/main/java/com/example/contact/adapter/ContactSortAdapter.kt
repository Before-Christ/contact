package com.example.contact.adapter


import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.contact.PersonDetailActivity
import com.example.contact.R
import com.example.contact.pojo.ContactPerson



/**
 * @author caihn
 * @create 2020-06-28-14:31
 */
class ContactSortAdapter(private val context: Context) : RecyclerView.Adapter<ContactSortAdapter.ContactHolder>() {


    private var contactPersonList: MutableList<ContactPerson>? = null

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val name: TextView = itemView.findViewById(R.id.name)
            val headerImage: ImageView = itemView.findViewById(R.id.img)
            val phoneNumber: TextView = itemView.findViewById(R.id.phoneNumber)
            val letterTitle: TextView = itemView.findViewById(R.id.letter_title)
            val line: View = itemView.findViewById(R.id.line_view)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = View.inflate(parent.context, R.layout.friend_item,null)


        val viewHolder = ContactHolder(view)
        viewHolder.headerImage.setOnClickListener {
            val position = viewHolder.adapterPosition
            val person = contactPersonList?.get(position)
            val id = person?.id
            val intent = Intent(context, PersonDetailActivity::class.java)
            intent.putExtra("personId", id)
            context.startActivity(intent)
        }

        viewHolder.name.setOnClickListener {
            val position = viewHolder.adapterPosition
            val person = contactPersonList?.get(position)
            val id = person?.id
            val intent = Intent(context, PersonDetailActivity::class.java)
            intent.putExtra("personId", id)
            context.startActivity(intent)
        }
        viewHolder.phoneNumber.setOnClickListener {
            val position = viewHolder.adapterPosition
            val person = contactPersonList?.get(position)
            val id = person?.id
            val intent = Intent(context, PersonDetailActivity::class.java)
            intent.putExtra("personId", id)
            context.startActivity(intent)
        }

        return viewHolder


    }

    override fun getItemCount(): Int {
        return if (contactPersonList == null) 0 else contactPersonList!!.size
    }

    fun initData(data: MutableList<ContactPerson>?){
        this.contactPersonList = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val person = contactPersonList!![position]
        holder.name.text = person.name
        holder.phoneNumber.text = person.phoneNumber
        Glide.with(context).load(person.headerImage).into(holder.headerImage)
        if (!compareSection(position)){
            holder.letterTitle.visibility = View.VISIBLE
            holder.letterTitle.text = person.letter
            holder.line.visibility = View.GONE
        }else{
            holder.letterTitle.visibility = View.GONE
            holder.line.visibility = View.VISIBLE
        }


    }


    //如果是位置1，或者当前的首字母不等于前一个的首字母就返回false
    private fun compareSection(position: Int): Boolean {
        return if (position == 0) {
            false
        } else {
            val current = getSectionForPosition(position)
            val previous = getSectionForPosition(position - 1)
            current == previous
        }

    }

    // 获取当前位置的首字母(int表示ascii码)
    private fun getSectionForPosition(position: Int): Int {
        return contactPersonList!![position].letter[0].toInt()
    }


}