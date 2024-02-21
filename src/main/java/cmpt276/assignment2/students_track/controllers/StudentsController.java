package cmpt276.assignment2.students_track.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.assignment2.students_track.models.Student;
import cmpt276.assignment2.students_track.models.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class StudentsController {

    @Autowired
    private StudentRepository studentRepo;

    @PostMapping("/students/add")
    public String addStudent(@RequestParam Map<String, String> newStudent, HttpServletResponse response, Model model){
        System.out.println("ADD student");
        String newName = newStudent.get("name");
        int newWeight = Integer.parseInt(newStudent.get("weight"));
        int newHeight = Integer.parseInt(newStudent.get("height"));
        String newHairColor = newStudent.get("hairColor");
        double newGpa = Double.parseDouble(newStudent.get("gpa"));
        studentRepo.save(new Student(newName, newWeight, newHeight, newHairColor, newGpa));
        response.setStatus(201);
        model.addAttribute("message", newName + " has been added to StudenTrack");
        return "students/addedStudent";
    }

    @GetMapping("/students/view")
    public String getAllStudents(Model model){
        System.out.println("Getting all students");
        // get all students from database
        List<Student> students = studentRepo.findAllByOrderBySid();
        // end of database call
        model.addAttribute("stu", students);
        return "students/displayAll";
    }

    // opens the update form (requested from findToUpdate form)
    @PostMapping("/students/findToUpdate")
    public String findStudentToUpdate(@RequestParam Map<String, String> Student, Model model) {
        int studentId = Integer.parseInt(Student.get("sid"));

        Student student = studentRepo.findBySid(studentId);
        if (student != null) {
            model.addAttribute("stu", student);
            return "students/update";
        } 
        else {
            model.addAttribute("error", "Student with ID " + studentId + " not found");
            return "students/errorFind"; // Provide the appropriate error page name
        }
    }

    // opens update form when update requested from displayAll page
    @GetMapping("/students/update/{sid}")
    public String showUpdateForm(@PathVariable("sid") int studentId, Model model) {
        // Retrieve the student from the database based on the ID
        Student student = studentRepo.findBySid(studentId);

        // Add the student to the model
        model.addAttribute("stu", student);

        return "students/update";
    }

    @PostMapping("/students/update")
    public String updateStudent(@RequestParam Map<String, String> updatedStudent, Model model) {
        int studentId = Integer.parseInt(updatedStudent.get("sid"));

        Student student = studentRepo.findBySid(studentId);
        
        student.setName(updatedStudent.get("name"));
        student.setWeight(Integer.parseInt(updatedStudent.get("weight")));
        student.setHeight(Integer.parseInt(updatedStudent.get("height")));
        student.setHairColor(updatedStudent.get("hairColor"));
        student.setGpa(Double.parseDouble(updatedStudent.get("gpa")));
        studentRepo.save(student);

        model.addAttribute("message", updatedStudent.get("name") + "'s information has been updated");

        return "students/updatedStudent";
    }

    // delete students (request from displayAll page)
    @GetMapping("/students/delete/{sid}")
    public String deleteStudent(@PathVariable("sid") int sid, Model model) {
        Student student = studentRepo.findBySid(sid);
        model.addAttribute("message", student.getName() + " has been deleted from StudenTrack");
        studentRepo.deleteById(sid);
        return "students/deletedStudent";
    }

    // delete students (request from findToDelete form)
    @PostMapping("/students/findAndDelete")
    public String findStudentToDelete(@RequestParam Map<String, String> Student, Model model) {
        int studentId = Integer.parseInt(Student.get("sid"));

        Student student = studentRepo.findBySid(studentId);
        if (student != null) {
            model.addAttribute("message", student.getName() + " has been deleted from StudenTrack");
            studentRepo.deleteById(studentId);
            return "students/deletedStudent";
        } 
        else {
            model.addAttribute("error", "Student with ID " + studentId + " not found");
            return "students/errorDelete"; // Provide the appropriate error page name
        }
    }
    
}
