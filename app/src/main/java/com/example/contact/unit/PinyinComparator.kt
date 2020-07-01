package com.example.contact.unit

import com.example.contact.pojo.ContactPerson
import java.util.*

class PinyinComparator : Comparator<ContactPerson> {
    override fun compare(arg0: ContactPerson, arg1: ContactPerson): Int {
        // TODO Auto-generated method stub
        return if (arg0.letter == "@" || arg1.letter == "#") {
            -1
        } else if (arg0.letter == "#" || arg1.letter == "@") {
            1
        } else {
            // 升序
            arg0.letter.compareTo(arg1.letter)
            //    return arg1.getLetter().compareTo(arg0.getLetter()); // 降序
        }
    }
}