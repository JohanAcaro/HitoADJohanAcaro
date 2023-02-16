package es.hitoadjohanacaro.servicios;

import es.hitoadjohanacaro.jpa.Role;
import es.hitoadjohanacaro.jpa.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class RoleService {

    private RoleRepositorio roles;

    public RoleService(RoleRepositorio roles) {
        this.roles = roles;
    }

    public void guardarRole(Role rol) {
        roles.save(rol);
    }

    public void borrarRole(Role rol) {
        roles.delete(rol);
    }

}
