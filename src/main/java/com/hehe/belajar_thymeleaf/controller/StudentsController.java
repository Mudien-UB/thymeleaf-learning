package com.hehe.belajar_thymeleaf.controller;

import com.hehe.belajar_thymeleaf.dto.StudentsRequest;
import com.hehe.belajar_thymeleaf.model.Students;
import com.hehe.belajar_thymeleaf.repository.StudentsRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private final StudentsRepository studentsRepository;

    public StudentsController(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    @GetMapping({"","/"})
    public String showCreateForm(Model model) {
        var listStudents = studentsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        listStudents.stream().map((item) -> {
            item.getCreatedAt().toLocalDateTime();
            return item;
        });
        model.addAttribute("listStudents", listStudents);
        return "students/index";
    }

    @GetMapping("/create")
    public String createStudent(Model model) {
        StudentsRequest studentsRequest = new StudentsRequest();
        model.addAttribute("studentsRequest", studentsRequest);

        return "students/create";
    }

    @PostMapping("/create")
    public String createStudent(@Valid @ModelAttribute StudentsRequest studentsRequest, BindingResult bindingResult) {

        if(studentsRepository.existsByEmail(studentsRequest.getEmail())) {
            bindingResult.addError(
                    new FieldError("studentsRequest", "email", "Email already exists!")
            );
        }

        if (bindingResult.hasErrors()) {
            return "students/create";
        }

        try{

        Students students = new Students();
        students.setFullname(studentsRequest.getFullname());
        students.setEmail(studentsRequest.getEmail());
        students.setPhone(studentsRequest.getPhone());
        students.setAddress(studentsRequest.getAddress());

        students.setBirthDate(studentsRequest.getBirthDate());

        students.setGender(studentsRequest.getGender());

        studentsRepository.save(students);
        } catch (Exception e) {
            return "students/create";
        }

        return "redirect:/students/";


    }

    @GetMapping("/edit")
    public String updateStudent(Model model, @RequestParam long id) {

        Students students = studentsRepository.findById(id).orElse(null);

        if(students == null) {
            return "redirect:/students/";
        }

            StudentsRequest studentsDto = new StudentsRequest();
        studentsDto.setId(students.getId());
        studentsDto.setFullname(students.getFullname());
            studentsDto.setEmail(students.getEmail());
            studentsDto.setPhone(students.getPhone());
            studentsDto.setAddress(students.getAddress());

            studentsDto.setBirthDate(students.getBirthDate());

            studentsDto.setGender(students.getGender());

            model.addAttribute("students", students);
            model.addAttribute("studentsRequest", studentsDto);

        return "students/edit";


    }

    @PutMapping("/edit")
    public String updateStudent( Model model, @Valid @ModelAttribute StudentsRequest studentsRequest, BindingResult bindingResult) {

        Students students = studentsRepository.findById(studentsRequest.getId()).orElse(null);
        if(students == null) {
            return "redirect:/students/";
        }

        model.addAttribute("students", students);
        if(bindingResult.hasErrors()) {
            return "students/edit";
        }


        students.setFullname(studentsRequest.getFullname());
        students.setEmail(studentsRequest.getEmail());
        students.setPhone(studentsRequest.getPhone());
        students.setAddress(studentsRequest.getAddress());

        students.setBirthDate(studentsRequest.getBirthDate());

        students.setGender(studentsRequest.getGender());

        try {
            studentsRepository.save(students);
        }catch (Exception e) {
            bindingResult.addError(new FieldError("students", "email", "Email already exists!"));
            return "students/edit";
        }


        return "redirect:/students/";


    }


    @GetMapping("/delete")
    public String deleteStudent(@RequestParam long id) {
        try {
            studentsRepository.deleteById(id);
        }catch (Exception e) {
            return "redirect:/students/";
        }
        return "redirect:/students/";
    }



}
