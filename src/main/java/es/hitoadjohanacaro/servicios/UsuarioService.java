package es.hitoadjohanacaro.servicios;

import es.hitoadjohanacaro.jpa.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.Optional;

@Service
@ApplicationScope
public class UsuarioService {

    @Autowired
    private UsuarioRepositorio usuarios;

    public UsuarioService(UsuarioRepositorio usuarios) {
        this.usuarios = usuarios;
    }

    public List<Usuario> listaUsuarios() {
        return usuarios.findAll();
    }


    public Optional<Usuario> buscarUsuario(String nif) {
        return usuarios.findById(nif);
    }


    public void guardarUsuario(Usuario user) {
        usuarios.save(user);
    }


}
