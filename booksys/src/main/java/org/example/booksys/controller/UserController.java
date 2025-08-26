package org.example.booksys.controller;

import org.example.booksys.model.User;
import org.example.booksys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    // This endpoint should be public to allow new users to be created.
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    //@PreAuthorize("hasRole('admins')") // Correct role name from LDAP
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('admins')") // Correct role name from LDAP
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/role")
    //@PreAuthorize("hasRole('admins')") // Correct role name from LDAP
    public ResponseEntity<?> updateUserRole(@PathVariable int id, @RequestBody String newRole) {
        try {
            User.Role role = User.Role.valueOf(newRole.toUpperCase());
            Optional<User> updatedUser = userService.updateUserRole(id, role);
            if (updatedUser.isPresent()) {
                return new ResponseEntity<>(updatedUser.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid role provided.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('admins')") // Correct role name from LDAP
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
