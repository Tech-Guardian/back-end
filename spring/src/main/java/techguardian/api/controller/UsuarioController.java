package techguardian.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import techguardian.api.entity.Usuario;
import techguardian.api.service.UsuarioService;

@RestController
@CrossOrigin
public class UsuarioController {

    @Autowired
    private UsuarioService userService;

    @GetMapping("/usuarios")
    public List<Usuario> findAll() {
        return userService.findAll();
    }

    @PostMapping("/cadastro")
    public Usuario createUser(@RequestBody Usuario user) {
        return userService.createUser(user);
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<Usuario> updateUser(@PathVariable("id") Long id, @RequestBody Usuario user) {
        Usuario updatedUserEntity = userService.updateUser(id, user);
        if (updatedUserEntity != null) {
            return ResponseEntity.ok(updatedUserEntity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
