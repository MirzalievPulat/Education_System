package uz.frodo.educationsystem.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uz.frodo.educationsystem.model.Course
import uz.frodo.educationsystem.model.Groups
import uz.frodo.educationsystem.model.Mentor
import uz.frodo.educationsystem.model.Student

class MyDbHelper(cotext:Context):SQLiteOpenHelper(cotext, DB_NAME,null, VERSION) {

    companion object{
        const val DB_NAME = "education_db"
        const val VERSION = 1

        const val COURSE_TABLE = "course_table"
        const val COURSE_ID = "id"
        const val COURSE_NAME = "name"
        const val COURSE_ABOUT = "about"

        const val MENTOR_TABLE = "mentor_table"
        const val MENTOR_ID = "id"
        const val MENTOR_COURSE_ID = "course_id"
        const val MENTOR_SURNAME = "surname"
        const val MENTOR_NAME = "name"
        const val MENTOR_FATHER = "father_name"

        const val GROUP_TABLE = "group_table"
        const val GROUP_ID = "id"
        const val GROUP_COURSE_ID = "course_id"
        const val GROUP_MENTOR_ID = "mentor_id"
        const val GROUP_NAME = "name"
        const val GROUP_DAYS = "days"
        const val GROUP_HOURS = "hours"
        const val GROUP_STARTED = "started"

        const val STUDENT_TABLE = "student_table"
        const val STUDENT_ID = "id"
        const val STUDENT_MENTOR_ID = "mentor_id"
        const val STUDENT_GROUP_ID = "group_id"
        const val STUDENT_NAME = "name"
        const val STUDENT_SURNAME = "surname"
        const val STUDENT_FATHER = "father_name"
        const val STUDENT_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query1 = "create table $COURSE_TABLE ($COURSE_ID integer not null primary key autoincrement,$COURSE_NAME text not null,$COURSE_ABOUT text not null)"
        val query2 = "create table $MENTOR_TABLE ($MENTOR_ID integer not null primary key autoincrement,$MENTOR_COURSE_ID integer not null,$MENTOR_SURNAME text not null,$MENTOR_NAME text not null, $MENTOR_FATHER TEXT not null, foreign key($MENTOR_COURSE_ID) references $COURSE_TABLE($COURSE_ID))"
        val query3 = "create table $GROUP_TABLE ($GROUP_ID integer not null primary key autoincrement, $GROUP_COURSE_ID integer not null, $GROUP_MENTOR_ID integer not null, $GROUP_NAME text not null, $GROUP_DAYS text not null, $GROUP_HOURS text not null, $GROUP_STARTED int not null, foreign key($GROUP_COURSE_ID) references $COURSE_TABLE($COURSE_ID),foreign key($GROUP_MENTOR_ID) references $MENTOR_TABLE($MENTOR_ID))"
        val query4 = "create table $STUDENT_TABLE (" +
                "$STUDENT_ID integer not null primary key autoincrement, " +
                "$STUDENT_MENTOR_ID integer not null, " +
                "$STUDENT_GROUP_ID integer not null, " +
                "$STUDENT_NAME text not null, " +
                "$STUDENT_SURNAME text not null, " +
                "$STUDENT_FATHER text not null, " +
                "$STUDENT_DATE text not null, " +
                "foreign key ($STUDENT_MENTOR_ID) references $MENTOR_TABLE($MENTOR_ID), " +
                "foreign key ($STUDENT_GROUP_ID) references $GROUP_TABLE($GROUP_ID))"

        db?.execSQL(query1)
        db?.execSQL(query2)
        db?.execSQL(query3)
        db?.execSQL(query4)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertCourse(course: Course){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(COURSE_NAME,course.name)
            put(COURSE_ABOUT,course.about)
        }
        db.insert(COURSE_TABLE,null,cv)
        db.close()
    }

    fun insertMentor(mentor: Mentor){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(MENTOR_SURNAME,mentor.surname)
            put(MENTOR_NAME,mentor.name)
            put(MENTOR_FATHER,mentor.father)
            put(MENTOR_COURSE_ID,mentor.course.id)
        }
        db.insert(MENTOR_TABLE,null,cv)
        db.close()
    }

    fun insertGroup(groups: Groups){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(GROUP_COURSE_ID,groups.course.id)
            put(GROUP_MENTOR_ID,groups.mentor.id)
            put(GROUP_NAME,groups.name)
            put(GROUP_DAYS,groups.day)
            put(GROUP_HOURS,groups.hours)
            put(GROUP_STARTED,groups.started)
        }
        db.insert(GROUP_TABLE,null,cv)
        db.close()
    }

    fun insertStudent(student: Student){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(STUDENT_GROUP_ID,student.groups.id)
            put(STUDENT_MENTOR_ID,student.mentor.id)
            put(STUDENT_FATHER,student.father)
            put(STUDENT_NAME,student.name)
            put(STUDENT_SURNAME,student.surname)
            put(STUDENT_DATE,student.date)
        }
        db.insert(STUDENT_TABLE,null,cv)
        db.close()
    }

    fun getAllCourses():ArrayList<Course>{
        val list = ArrayList<Course>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("select * from $COURSE_TABLE",null)
        if (cursor.moveToFirst()){
            do {
                val course = Course(cursor.getInt(0),cursor.getString(1),cursor.getString(2))
                list.add(course)
            }while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getAllMentors(course_id:Int):ArrayList<Mentor>{
        val list = ArrayList<Mentor>()
        val db = this.readableDatabase
        val cursor = db.query(MENTOR_TABLE, arrayOf(MENTOR_ID, MENTOR_COURSE_ID, MENTOR_NAME,
            MENTOR_SURNAME, MENTOR_FATHER),"$MENTOR_COURSE_ID = ?",
            arrayOf(course_id.toString()),null,null,null
        )
        while (cursor.moveToNext()){
            list.add(Mentor(cursor.getInt(0),getCourseById(cursor.getInt(1)),cursor.getString(2),cursor.getString(3),cursor.getString(4)))
        }
        cursor.close()
        db.close()
        return list
    }

    fun getAllGroups(course_id:Int,started:Int):ArrayList<Groups>{
        val list = ArrayList<Groups>()
        val db = this.readableDatabase
        val cursor = db.query(
            GROUP_TABLE,
            arrayOf(GROUP_ID, GROUP_COURSE_ID, GROUP_MENTOR_ID,
            GROUP_NAME, GROUP_DAYS, GROUP_HOURS, GROUP_STARTED),"$GROUP_COURSE_ID = ? and $GROUP_STARTED = ?",
            arrayOf(course_id.toString(), started.toString()),null,null,null
        )
        while (cursor.moveToNext()){
            list.add(Groups(cursor.getInt(0),getCourseById(cursor.getInt(1)),getMentorById(cursor.getInt(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getInt(6)))
        }
        cursor.close()
        db.close()
        return list
    }

    fun getAllGroupsByMentorId(course_id:Int,mentorId:Int):ArrayList<Groups>{
        val list = ArrayList<Groups>()
        val db = this.readableDatabase
        val cursor = db.query(
            GROUP_TABLE,
            arrayOf(GROUP_ID, GROUP_COURSE_ID, GROUP_MENTOR_ID,
                GROUP_NAME, GROUP_DAYS, GROUP_HOURS, GROUP_STARTED),"$GROUP_COURSE_ID = ? and $GROUP_MENTOR_ID = ?",
            arrayOf(course_id.toString(), mentorId.toString()),null,null,null
        )
        while (cursor.moveToNext()){
            list.add(Groups(cursor.getInt(0),getCourseById(cursor.getInt(1)),getMentorById(cursor.getInt(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getInt(6)))
        }
        cursor.close()
        db.close()
        return list
    }

    fun getAllStudents(group_id:Int):ArrayList<Student>{
        val list = ArrayList<Student>()
        val db = this.readableDatabase
        val cursor = db.query(
            STUDENT_TABLE,
            arrayOf(
                STUDENT_ID, STUDENT_GROUP_ID, STUDENT_MENTOR_ID,
                STUDENT_NAME, STUDENT_SURNAME, STUDENT_FATHER, STUDENT_DATE),"$STUDENT_GROUP_ID = ?",
            arrayOf(group_id.toString()),null,null,null
        )
        while (cursor.moveToNext()){
            list.add(Student(cursor.getInt(0),getGroupById(cursor.getInt(1)),getMentorById(cursor.getInt(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6)))
        }
        cursor.close()
        db.close()
        return list
    }

    fun getCourseById(id:Int):Course{
        val db = this.readableDatabase
        val cursor = db.query(COURSE_TABLE, arrayOf(COURSE_ID, COURSE_NAME, COURSE_ABOUT),"$COURSE_ID = ?",
            arrayOf(id.toString()),null,null,null
        )
        cursor.moveToFirst()
        val course = Course(cursor.getInt(0),cursor.getString(1),cursor.getString(2))
        cursor.close()
        db.close()
        return course
    }

    fun getMentorById(id:Int):Mentor{
        val db = this.readableDatabase
        val cursor = db.query(MENTOR_TABLE, arrayOf(MENTOR_ID, MENTOR_COURSE_ID, MENTOR_NAME,
            MENTOR_SURNAME, MENTOR_FATHER),"$MENTOR_ID = ?",
            arrayOf(id.toString()),null,null,null
        )
        cursor.moveToFirst()
        val mentor = Mentor(cursor.getInt(0),getCourseById(cursor.getInt(1)),cursor.getString(2),cursor.getString(3),cursor.getString(4))
        cursor.close()
        db.close()
        return mentor
    }

    fun getGroupById(id:Int):Groups{
        val db = this.readableDatabase
        val cursor = db.query(
            GROUP_TABLE, arrayOf(
                GROUP_ID, GROUP_COURSE_ID, GROUP_MENTOR_ID,
            GROUP_NAME, GROUP_DAYS, GROUP_HOURS, GROUP_STARTED),"$GROUP_ID = ?",
            arrayOf(id.toString()),null,null,null
        )
        cursor.moveToFirst()
        val groups = Groups(cursor.getInt(0),getCourseById(cursor.getInt(1)),getMentorById(cursor.getInt(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getInt(6))
        cursor.close()
        db.close()
        return groups
    }

    fun getStudentById(id:Int):Student{
        val db = this.readableDatabase
        val cursor = db.query(
            STUDENT_TABLE, arrayOf(
                STUDENT_ID, STUDENT_GROUP_ID, STUDENT_MENTOR_ID,
                STUDENT_NAME, STUDENT_SURNAME, STUDENT_FATHER, STUDENT_DATE),"$STUDENT_ID = ?",
            arrayOf(id.toString()),null,null,null
        )
        cursor.moveToFirst()
        val student = Student(cursor.getInt(0),getGroupById(cursor.getInt(1)),getMentorById(cursor.getInt(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6))
        cursor.close()
        db.close()
        return student
    }

    fun getStudentByGroupId(group_id: Int):ArrayList<Student>{
        val list = ArrayList<Student>()
        val db = this.readableDatabase
        val cursor = db.query(
            STUDENT_TABLE, arrayOf(
                STUDENT_ID, STUDENT_GROUP_ID, STUDENT_MENTOR_ID,
                STUDENT_NAME, STUDENT_SURNAME, STUDENT_FATHER, STUDENT_DATE),"$STUDENT_GROUP_ID = ?",
            arrayOf(group_id.toString()),null,null,null
        )
        while (cursor.moveToNext()){
            val student = Student(cursor.getInt(0),getGroupById(cursor.getInt(1)),getMentorById(cursor.getInt(2)),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6))
            list.add(student)
        }
        cursor.close()
        db.close()
        return list
    }


    fun updateMentor(mentor: Mentor){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(MENTOR_NAME,mentor.name)
            put(MENTOR_SURNAME,mentor.surname)
            put(MENTOR_FATHER,mentor.father)
        }
        db.update(MENTOR_TABLE,cv,"$MENTOR_ID = ?", arrayOf("${mentor.id}"))
        db.close()
    }

    fun updateGroup(groups: Groups){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(GROUP_NAME,groups.name)
            put(GROUP_DAYS,groups.day)
            put(GROUP_HOURS,groups.hours)
            put(GROUP_STARTED,groups.started)
        }
        db.update(GROUP_TABLE,cv,"$GROUP_ID = ?", arrayOf("${groups.id}"))
        db.close()
    }

    fun updateStudent(student: Student){
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put(STUDENT_NAME,student.name)
            put(STUDENT_SURNAME,student.surname)
            put(STUDENT_FATHER,student.father)
            put(STUDENT_DATE,student.date)
        }
        db.update(STUDENT_TABLE,cv,"$STUDENT_ID = ?", arrayOf("${student.id}"))
        db.close()
    }


    fun deleteMentor(mentor: Mentor){
        val db = this.writableDatabase
        db.delete(MENTOR_TABLE,"$MENTOR_ID =?", arrayOf("${mentor.id}"))
        db.delete(GROUP_TABLE,"$GROUP_MENTOR_ID = ?", arrayOf("${mentor.id}"))
        db.close()
    }

    fun deleteGroup(groups: Groups){
        val db = this.writableDatabase
        db.delete(GROUP_TABLE,"$GROUP_ID =?", arrayOf("${groups.id}"))
        db.close()
    }

    fun deleteStudent(student: Student){
        val db = this.writableDatabase
        db.delete(STUDENT_TABLE,"$STUDENT_ID =?", arrayOf("${student.id}"))
        db.close()
    }
}