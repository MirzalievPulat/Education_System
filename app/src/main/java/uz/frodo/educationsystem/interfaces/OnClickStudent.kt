package uz.frodo.educationsystem.interfaces

import uz.frodo.educationsystem.model.Student

interface OnClickStudent {
    fun editStudent(student: Student)
    fun deleteStudent(student: Student)
}