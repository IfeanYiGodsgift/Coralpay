package org.example.booksys.controller;

import org.example.booksys.model.Space;
import org.example.booksys.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @PostMapping
    public ResponseEntity<Space> createSpace(@RequestBody Space space) {
        Space newSpace = spaceService.createSpace(space);
        return new ResponseEntity<>(newSpace, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Space>> getAllSpaces() {
        List<Space> spaces = spaceService.getAllSpaces();
        return new ResponseEntity<>(spaces, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Space> getSpaceById(@PathVariable int id) {
        Optional<Space> space = spaceService.getSpaceById(id);
        return space.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Space> updateSpace(@PathVariable int id, @RequestBody Space space) {
        Space updatedSpace = spaceService.updateSpace(id, space);
        if (updatedSpace != null) {
            return new ResponseEntity<>(updatedSpace, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpace(@PathVariable int id) {
        if (spaceService.deleteSpace(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}