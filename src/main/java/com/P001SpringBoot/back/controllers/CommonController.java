package com.P001SpringBoot.back.controllers;


import com.P001SpringBoot.back.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommonController<E, S extends ICommonService<E>> {

    @Autowired
    protected S service;

    @GetMapping("/all")
    public ResponseEntity<?> listAllStudents() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<?> listPage(Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {

        Optional<E> optionalStudent = service.findById(id);

        if(optionalStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalStudent.get());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudent(@Valid @RequestBody E entity, BindingResult result) {

        if (result.hasErrors()) {
            return this.validar(result);
        }

        E newEntity = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEntity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    protected ResponseEntity<?> validar(BindingResult result) {
        Map<String, Object> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
