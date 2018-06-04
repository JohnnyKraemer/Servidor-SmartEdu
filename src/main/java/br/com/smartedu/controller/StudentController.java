/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.smartedu.controller;

import br.com.smartedu.model.Student;
import br.com.smartedu.repository.StudentRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository repository;

    private boolean existe = false;
    private String periodoCarga = null;

    @GetMapping("")
    public List getAll() {
        return repository.findAllByOrderByIdAsc();
    }


    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) {
        Student  object = repository.findOne(id);
        if (object == null) {
            return new ResponseEntity("Não existe Aluno para este ID: " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(object, HttpStatus.OK);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity getByCodigo(@PathVariable("codigo") Long codigo) {
        List<Student> object = repository.findByCode(codigo);
        if (object == null) {
            return new ResponseEntity("Não existe Aluno para este ID: " + codigo, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(object, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody Student object) {
        repository.save(object);
        return new ResponseEntity(object, HttpStatus.OK);
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        repository.delete(id);
        return new ResponseEntity(id, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Student object) {
        object = repository.save(object);
        if (null == object) {
            return new ResponseEntity("Não existe Aluno para este ID", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(object, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost")
    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody String data) {
        //JsonParser(data);
        System.out.println(data);
        return new ResponseEntity(data, HttpStatus.OK);
    }
}
