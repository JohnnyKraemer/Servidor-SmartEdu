/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.smartedu.controller;

import br.com.smartedu.model.Course;
import br.com.smartedu.repository.CourseRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseRepository repository;

    @GetMapping("")
    public List getAll() {
        return repository.findAllByOrderByIdAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) {
        Course object = repository.findOne(id);
        if (object == null) {
            return new ResponseEntity("Não existe Curso para este ID: " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(object, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity getByNome(@PathVariable("name") String name) {
        Course object = repository.findByName(name);
        if (object == null) {
            return new ResponseEntity("Não existe Curso para este NOME: " + name, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(object, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody Course object) {
        repository.save(object);
        return new ResponseEntity(object, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        repository.delete(id);
        return new ResponseEntity(id, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Course object) {
        object = repository.save(object);
        if (null == object) {
            return new ResponseEntity("Não existe Curso para este ID", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(object, HttpStatus.OK);
    }
}
