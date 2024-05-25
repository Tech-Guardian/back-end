package techguardian.api.interfaces;

import java.util.List;

import techguardian.api.entity.Usuario;

public interface IUsuarioService {

    List<Usuario> findAll();
    Usuario createUser(Usuario user);
    Usuario updateUser(Long id, Usuario updateUser);
    Usuario deleteUser(Long id);
}
