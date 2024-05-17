package com.geovannycode.nisum.controller;

import com.geovannycode.nisum.domain.dto.CreateUserRequest;
import com.geovannycode.nisum.domain.dto.CreateUserResponse;
import com.geovannycode.nisum.domain.dto.UserDTO;
import com.geovannycode.nisum.domain.model.TokenBodyResponse;
import com.geovannycode.nisum.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Ver una lista de usuarios disponibles",
            description = "Recupera una lista de usuarios disponibles.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Lista recuperada exitosamente",
                        content = @Content(schema = @Schema(implementation = CreateUserResponse.class))),
                @ApiResponse(responseCode = "401", description = "No autorizado para ver la lista de usuarios"),
                @ApiResponse(responseCode = "403", description = "Acceso prohibido para ver la lista de usuarios"),
                @ApiResponse(responseCode = "404", description = "Lista de usuarios no encontrada"),
            })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CreateUserResponse<List<UserDTO>>> getAll() {
        log.info("Recuperando a todos los usuarios");
        CreateUserResponse<List<UserDTO>> response = new CreateUserResponse<>();
        response.setBody(userService.getAll());
        response.setMessage("Lista de usuarios obtenida correctamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Creacion de Usuario", description = "Crea un nuevo usuario.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Creación de usuario: {}", request);
        return new ResponseEntity<>(userService.createUser(request, request.role()), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Autenticar a un usuario y devolver un token",
            description = "Autentica al usuario y proporciona un token")
    @ApiResponse(responseCode = "200", description = "Inicio de sesion satisfactoria")
    @PostMapping(value = "/authenticate")
    public ResponseEntity<CreateUserResponse<TokenBodyResponse>> authenticate(@RequestBody CreateUserRequest request) {
        log.info("Autenticación de usuario : {}", request);
        CreateUserResponse<TokenBodyResponse> response = new CreateUserResponse<>();
        String token = userService.authenticate(request);
        response.setBody(new TokenBodyResponse(token));
        response.setMessage("Inicio de sesion satisfactoria.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
