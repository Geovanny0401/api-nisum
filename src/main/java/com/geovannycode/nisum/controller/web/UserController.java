package com.geovannycode.nisum.controller.web;

import com.geovannycode.nisum.domain.UserService;
import com.geovannycode.nisum.domain.model.CreateUserRequest;
import com.geovannycode.nisum.domain.model.CreateUserResponse;
import com.geovannycode.nisum.domain.model.UserDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<CreateUserResponse<List<UserDTO>>> getAll() {
        CreateUserResponse<List<UserDTO>> response = new CreateUserResponse<>();
        response.setBody(userService.getAll());
        response.setMessage("Lista de usuarios obtenida correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<CreateUserResponse> createOrder(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating user: {}", request);
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }
}
