package es.hitoadjohanacaro.servicios;

import es.hitoadjohanacaro.jpa.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

}
