package patil.parshwa.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import patil.parshwa.blog.models.User;
import patil.parshwa.blog.repositories.UserRepository;

import java.util.List;

@RestController
public class UserController {
    @Autowired  UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
