package uz.frodo.educationsystem.interfaces

import uz.frodo.educationsystem.model.Groups

interface OnClickGroup {
    fun onView(groups: Groups)
    fun onEdit(groups: Groups,position:Int)
    fun onDelete(groups:Groups)
}