package techguardian.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import techguardian.api.entity.Autorizacao;
import techguardian.api.entity.Usuario;
import techguardian.api.repository.UsuarioRepository;

@Service
public class SecurityService implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<Usuario> usuarioOp = usuarioRepo.findByEmail(email);

        if(usuarioOp.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        Usuario usuario = usuarioOp.get();
        String autorizacoes[] = new String[usuario.getAutorizacoes().size()];
        Integer i = 0;
        for(Autorizacao aut: usuario.getAutorizacoes()) {
            autorizacoes[i++] = aut.getNome();
        }

        return User.builder()
            .username(email)
            .password(usuario.getSenha())
            .authorities(autorizacoes).build();
    }
}
