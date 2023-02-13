package es.hitoadjohanacaro.servicios;

import es.hitoadjohanacaro.jpa.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositorio extends JpaRepository<Role, Integer> {
}
