package techguardian.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import techguardian.api.security.JwtUtils;
import techguardian.api.security.Login;

@RestController
@CrossOrigin
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authManager;

    @PostMapping
    public Login autenticar(@RequestBody Login login) throws JsonProcessingException {
        // Adiciona log para verificar os dados recebidos
        System.out.println("Email recebido: " + login.getUserEmail());
        System.out.println("Senha recebida: " + login.getPassword());
    
        Authentication auth = new UsernamePasswordAuthenticationToken(login.getUserEmail(),
                login.getPassword());
    
        // Adiciona log para verificar se a autenticação está sendo chamada
        System.out.println("Chamando autenticação...");
    
        auth = authManager.authenticate(auth);
    
        // Adiciona log para verificar se a autenticação foi bem-sucedida
        System.out.println("Autenticação bem-sucedida.");
    
        login.setToken(JwtUtils.generateToken(auth));
        return login;
    }

    @GetMapping
    public String message() {
        return "Login page. Use POST.";
    }
}
