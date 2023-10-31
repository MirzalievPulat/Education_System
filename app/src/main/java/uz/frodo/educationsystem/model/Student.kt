package uz.frodo.educationsystem.model

data class Student(var groups: Groups, var mentor:Mentor, var name:String, var surname:String, var father:String, var date:String) {
    var id:Int? = null
    constructor(id:Int,groups: Groups,mentor: Mentor,name: String,surname: String,father: String,date: String):this(groups,
        mentor,name,surname,father,date){
        this.id = id
    }
}