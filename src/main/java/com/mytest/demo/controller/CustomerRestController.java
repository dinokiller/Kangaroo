package com.mytest.demo.controller;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Stream;

import com.mytest.demo.Cat;
import com.mytest.demo.CatRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/cat")
public class CustomerRestController {
    @Autowired
    private CatRepository catRepository;

    @RequestMapping(method = RequestMethod.OPTIONS)
    ResponseEntity<?> options() {
        return ResponseEntity.ok()
        .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.HEAD, HttpMethod.OPTIONS, HttpMethod.PUT, HttpMethod.DELETE).build();
    }

    @GetMapping
    ResponseEntity<Collection<Cat>> getCollection() {
        Stream.of("Felix", "GarField","Whiskers").forEach(
			n -> catRepository.save(new Cat(n))
		);
        return ResponseEntity.ok(this.catRepository.findAll());
    }

    @GetMapping(value="/{id}")
    ResponseEntity<Cat> get(@PathVariable Long id) throws Exception {
        return this.catRepository.findById(id).map(ResponseEntity::ok).orElseThrow(()->new Exception());
    }

    @PostMapping
    ResponseEntity<Cat> post(@RequestBody Cat c) {
        Cat cat = this.catRepository.save(new Cat(c.getName()));
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}").buildAndExpand(cat.getId()).toUri();
        return ResponseEntity.created(uri).body(cat);
    }

    @DeleteMapping(value="/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) throws Exception {
        return this.catRepository.findById(id).map(c->{
            catRepository.delete(c);
            return ResponseEntity.noContent().build();
        }).orElseThrow(()->new Exception());
    }

    @RequestMapping(value="/{id}", method = RequestMethod.HEAD)
    ResponseEntity<?> head(@PathVariable Long id) throws Exception {
        return this.catRepository.findById(id).map(exists -> ResponseEntity.noContent().build()).
            orElseThrow(()-> new Exception());
    }

    @PutMapping(value="/{id}")
    ResponseEntity<Cat> put(@PathVariable Long id, @RequestBody Cat c) throws Exception {
        return this.catRepository.findById(id)
        .map(exists -> {
            Cat cat = this.catRepository.save(new Cat(exists.getName()));
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
            return ResponseEntity.created(uri).body(cat);
        }).orElseThrow(()->new Exception());
    }

}