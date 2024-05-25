package techguardian.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Autorizacao;
import techguardian.api.entity.Usuario;
import techguardian.api.repository.AutorizacaoRepository;
import techguardian.api.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepo;

    @Autowired
    private AutorizacaoRepository authRepo;

    @Autowired
    private PasswordEncoder encoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> findAll() {
        return userRepo.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Usuario createUser(Usuario user) {
        if(user == null ||
                user.getNome() == null ||
                user.getNome().isBlank() ||
                user.getEmail() == null ||
                user.getEmail().isBlank() ||
                user.getSenha() == null ||
                user.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos!");
        }
        if(!user.getAutorizacoes().isEmpty()) {
            Set<Autorizacao> authorities = new HashSet<Autorizacao>();
            for(Autorizacao authority: user.getAutorizacoes()) {
                authority = findByID(authority.getId());
                authorities.add(authority);
            }
            user.setAutorizacoes(authorities);
        }
        user.setSenha(encoder.encode(user.getSenha()));
        return userRepo.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Usuario updateUser(Long id, Usuario user) {
        Usuario existUser = userRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado - ID: " + id));
        if (user.getNome() != null) {
            existUser.setNome(user.getNome());
        }
        if (user.getSenha() != null) {
            existUser.setSenha(user.getSenha());
        }
        if (user.getEmail() != null) {
            existUser.setEmail(user.getEmail());
        }
        return userRepo.save(existUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Usuario deleteUser(Long id) {
        Usuario user = userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado - ID: " + id));
        userRepo.deleteById(id);
        return user;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Autorizacao findByID(Long id) {
        Optional<Autorizacao> authorityOp = authRepo.findById(id);
        if(authorityOp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autorização não encontrada!");
        }
        return authorityOp.get();
    }
}
