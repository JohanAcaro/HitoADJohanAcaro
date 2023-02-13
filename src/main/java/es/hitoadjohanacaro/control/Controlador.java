package es.hitoadjohanacaro.control;



import es.hitoadjohanacaro.jpa.Role;
import es.hitoadjohanacaro.jpa.Usuario;
import es.hitoadjohanacaro.servicios.RoleRepositorio;
import es.hitoadjohanacaro.servicios.RoleService;
import es.hitoadjohanacaro.servicios.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class Controlador {

    @Autowired
    PasswordEncoder contrasenaEncriptada;

    @Autowired
    UsuarioService usuarios;

    @Autowired
    RoleService roles;

    @RequestMapping("/")
    public ModelAndView peticionRaiz(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());

        String texto = "123";
        String encriptado = contrasenaEncriptada.encode(texto);
        System.out.println("Texto original: " + texto);
        System.out.println("Texto emcriptado: " + encriptado);

        mv.setViewName("index"); //TAmbién llamados endpoints
        return mv;
    }

    @RequestMapping("login") // URL: http://localhost:8081/login
    public ModelAndView peticionSesion(Authentication aut) { // Nombre del método: peticionSesion
        ModelAndView mv = new ModelAndView(); // Objeto que contiene la vista y los datos
        mv.setViewName("login"); // La rutra del archivo: src/main/resources/templates/login.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("denegado") // URL: http://localhost:8081/login
    public ModelAndView peticionDenegado(Authentication aut) { // Nombre del método: peticionSesion
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("denegado"); // La rutra del archivo: src/main/resources/templates/login.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("/user") // URL: http://localhost:8081/user
    public ModelAndView peticionUser(Authentication aut) { // Nombre del método: peticionUser
        ModelAndView mv = new ModelAndView(); // Objeto que contiene la vista y los datos
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());

        Optional<Usuario> userOptional = usuarios.buscarUsuario(aut.getName());
        Usuario user = null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }

        mv.setViewName("usuario"); // La rutra del archivo: src/main/resources/templates/user.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("/user/perfil") // URL: http://localhost:8081/usuario/perfil
    public ModelAndView peticionUPerfil(Authentication aut) { // Nombre del método: peticionUPerfil
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("perfil"); // La rutra del archivo: src/main/resources/templates/usuario.html
        return mv;
    }

    @RequestMapping("/user/tareas/nueva")
    public ModelAndView peticioNuevaTarea(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("nuevatarea");
        return mv;
    }

    @RequestMapping("/user/tareas/listado")
    public ModelAndView peticioListdoTareas(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("listadotareas");
        return mv;
    }

    @RequestMapping("/admin") // URL: http://localhost:8081/admin
    public ModelAndView peticionAdmin(Authentication aut) { // Nombre del método: peticionAdmin
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        List<Usuario> listaUsuarios = usuarios.listaUsuarios();
        mv.addObject("listaUsuarios", listaUsuarios);
        mv.setViewName("administrador");
        return mv;
    }

    @RequestMapping("/admin/usuario/nuevo")
    public ModelAndView registro() {
        ModelAndView mv = new ModelAndView();
        Usuario c = new Usuario();
        mv.addObject("usuario", c);
        mv.setViewName("nuevousuario");
        return mv;
    }

    @RequestMapping("/guardar")
    public ModelAndView peticionGuardar(Usuario u, Authentication aut) {
        ModelAndView mv = new ModelAndView();
        System.out.println(u);

        String sincifrar = u.getPw();
        String encriptado = contrasenaEncriptada.encode(sincifrar);
        u.setPw(encriptado);

        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());


        Optional<Usuario> usuarioBuscado = usuarios.buscarUsuario(u.getNif());
        if (usuarioBuscado.isPresent()) {
            mv.addObject("sms", "El nif " + u.getNif() + " ya está registrado");
        } else {
            usuarios.guardarUsuario(u);
            Role rol = new Role();
            rol.setUsuario(u);
            rol.setRol("USUARIO");
            roles.guardarRole(rol);
            mv.addObject("sms", "Usuario " + u.getNombre() + " registrado correctamente");
        }
        mv.setViewName("informa");
        return mv;
    }

    @RequestMapping("/admin/dashboard")
    public ModelAndView peticioDashboard(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("dashboard");
        return mv;
    }

    @RequestMapping("/admin/usuario/mostrar")
    public ModelAndView peticioUsuariosMostrar(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("mostrarusuarios");
        return mv;
    }

    @RequestMapping("/admin/usuario/editar")
    public ModelAndView peticioUsuariosEditar(Authentication aut, HttpServletRequest request) {

        String nif = request.getParameter("nif");
        Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
        Usuario usuario = usuarioOpt.get();
        ModelAndView mv = new ModelAndView();
        if (aut == null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("usuario", usuario);
        mv.setViewName("editarusuario");
        return mv;
    }

    @RequestMapping("/actualizar")
    public String peticionActualizar(Usuario u, Authentication aut) {
        usuarios.guardarUsuario(u);
        return "redirect:/admin";
    }
}
