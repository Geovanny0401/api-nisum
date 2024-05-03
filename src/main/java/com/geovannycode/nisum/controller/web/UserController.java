package com.geovannycode.nisum.controller.web;

import com.geovannycode.nisum.domain.UserService;
import com.geovannycode.nisum.domain.model.CreateUserRequest;
import com.geovannycode.nisum.domain.model.CreateUserResponse;
import com.geovannycode.nisum.domain.model.TokenBodyResponse;
import com.geovannycode.nisum.domain.model.UserDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/api/users",
        consumes = {"application/json"},
        produces = {"application/json"})
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateUserResponse<List<UserDTO>>> getAll() {
        log.info("Fetching all users");
        CreateUserResponse<List<UserDTO>> response = new CreateUserResponse<>();
        response.setBody(userService.getAll());
        response.setMessage("Lista de usuarios obtenida correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creating user: {}", request);
        return new ResponseEntity<>(userService.createUser(request, request.role()), HttpStatus.CREATED);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<CreateUserResponse<TokenBodyResponse>> authenticate(@RequestBody CreateUserRequest request) {
        log.info("Authenticating user: {}", request);
        CreateUserResponse<TokenBodyResponse> response = new CreateUserResponse<>();
        String token = userService.authenticate(request);
        response.setBody(new TokenBodyResponse(token));
        response.setMessage("Inicio de sesion satisfactoria.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
