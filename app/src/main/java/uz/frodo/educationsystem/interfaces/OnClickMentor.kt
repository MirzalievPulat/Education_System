package uz.frodo.educationsystem.interfaces

import uz.frodo.educationsystem.model.Mentor
import java.text.FieldPosition

interface OnClickMentor {
    fun onEditClick(mentor: Mentor,position:Int)
    fun onDeleteClick(mentor: Mentor)
}