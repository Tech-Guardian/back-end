package techguardian.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import techguardian.api.entity.Authority;
import techguardian.api.entity.User;
import techguardian.api.repository.AuthorityRepository;
import techguardian.api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthorityRepository authRepo;

    @Autowired
    private PasswordEncoder encoder;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Authority findByID(Long id) {
        Optional<Authority> authorityOp = authRepo.findById(id);
        if(authorityOp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autorização não encontrada!");
        }
        return authorityOp.get();
    }

    // @PreAuthorize("hasRole('ADMIN')")
    public User createUser(User user) {
        if(user == null ||
                user.getNome() == null ||
                user.getNome().isBlank() ||
                user.getSenha() == null ||
                user.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos!");
        }
        if(!user.getAutorizacoes().isEmpty()) {
            Set<Authority> authorities = new HashSet<Authority>();
            for(Authority authority: user.getAutorizacoes()) {
                authority = findByID(authority.getId());
                authorities.add(authority);
            }
            user.setAutorizacoes(authorities);
        }
        user.setSenha(encoder.encode(user.getSenha()));
        return userRepo.save(user);
    }

    public User updateUser(Long id, User user) {
        User existUser = userRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado - ID: " + id));
        if (user.getNome() != null) {
            existUser.setNome(user.getNome());
        }
        if (user.getSenha() != null) {
            existUser.setSenha(user.getSenha());
        }
        return userRepo.save(user);
    }

    public User deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado - ID: " + id));
        userRepo.deleteById(id);
        return user;
    }
}
