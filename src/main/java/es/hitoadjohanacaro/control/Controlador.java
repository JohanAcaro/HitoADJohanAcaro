package es.hitoadjohanacaro.control;



import es.hitoadjohanacaro.jpa.Role;
import es.hitoadjohanacaro.jpa.Tarea;
import es.hitoadjohanacaro.jpa.Usuario;
import es.hitoadjohanacaro.servicios.RoleService;
import es.hitoadjohanacaro.servicios.TareaService;
import es.hitoadjohanacaro.servicios.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class Controlador {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UsuarioService usuarios;

    @Autowired
    RoleService roles;

    @Autowired
    TareaService tareas;

    @RequestMapping("/")
    public ModelAndView peticionRaiz(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());

        mv.setViewName("index"); //También llamados endpoints
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
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("denegado"); // La rutra del archivo: src/main/resources/templates/login.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("/user") // URL: http://localhost:8081/user
    public ModelAndView peticionUser(Authentication aut) { // Nombre del método: peticionUser
        ModelAndView mv = new ModelAndView(); // Objeto que contiene la vista y los datos
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());

        assert aut != null;
        Optional<Usuario> userOptional = usuarios.buscarUsuario(aut.getName());
        Usuario user=null;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }

        mv.setViewName("usuario"); // La rutra del archivo: src/main/resources/templates/user.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("/user/perfil") // URL: http://localhost:8081/usuario/perfil
    public ModelAndView peticionUPerfil(Authentication aut) { // Nombre del método: peticionUPerfil
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else{
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            mv.addObject("usuario", user);
        }
        mv.setViewName("perfil"); // La rutra del archivo: src/main/resources/templates/usuario.html
        return mv;
    }

    @RequestMapping("/user/perfil/editar")
    public ModelAndView peticionEditarPerfil(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else{
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            mv.addObject("usuario", user);
        }
        mv.setViewName("editarusuario"); // La rutra del archivo: src/main/resources/templates/usuario.html
        return mv;
    }

    @RequestMapping("/user/perfil/eliminar")
    public ModelAndView peticionEliminarPerfil(Authentication aut) {
        String nif = aut.getName();
        Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
        ModelAndView mv = new ModelAndView();
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            List<Role> rol = usuario.getRoles();
            // Eliminar el usuario y el rol de la base de datos
            roles.borrarRole(rol.get(0));
            usuarios.eliminarUsuario(usuario);
            mv.addObject("sms", "Se ha eliminado el usuario correctamente");
            mv.setViewName("informa");
        }
        else {
            mv.addObject("sms", "No se ha podido eliminar el usuario");
            mv.setViewName("informa");
        }
        return mv;
    }

    @RequestMapping("/user/tareas/listado")
    public ModelAndView peticionListdoTareas(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else{
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            List<Tarea> listaTareas = user.getTareas();
            mv.addObject("tareas", listaTareas);
        }
        mv.setViewName("listadotareas");
        return mv;
    }

    @RequestMapping("/user/tareas/nueva")
    public ModelAndView peticionNuevaTarea(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            Tarea tarea = new Tarea();
            mv.addObject("tarea", tarea);
        }
        mv.setViewName("nuevatarea");
        return mv;
    }

    @RequestMapping("/guardar/tarea")
    public ModelAndView guardarTarea(Tarea tarea, Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            tarea.setUsuario(user);
            tareas.guardar(tarea);
            if (tarea.getNombre() != null) {
                mv.setViewName("redirect:/user/tareas/listado");
            } else {
                mv.addObject("sms", "Se ha guardado la tarea correctamente");
                mv.setViewName("informa");
            }
        }
        return mv;
    }



    @RequestMapping("/admin") // URL: http://localhost:8081/admin
    public ModelAndView peticionAdmin(Authentication aut) { // Nombre del método: peticionAdmin
        ModelAndView mv = new ModelAndView();
        if(aut==null)
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
    public ModelAndView guardarUsuario(Usuario usuario, Authentication aut) {
        ModelAndView mv = new ModelAndView();
        System.out.println(usuario);

        String sinCodificar = usuario.getPw();
        String codificado = encoder.encode(sinCodificar);
        usuario.setPw(codificado);

        if (aut==null) {
            mv.addObject("El nif " +usuario.getNif() +"ya existe");
        } else {
            usuarios.guardarUsuario(usuario);
            Role role = new Role();
            role.setUsuario(usuario);
            role.setRol("USUARIO");
            roles.guardarRole(role);
            if (usuario.getNif() == null) {
                mv.addObject("sms", "No se ha podido guardar el usuario");
            } else {
                mv.addObject("El usuario con el nif:" +usuario.getNif() +" y el nombre: "+usuario.getNombre() +"se ha guardado correctamente");
                mv.setViewName("redirect:/admin");
            }
        }
        return mv;
    }

    @RequestMapping("/admin/dashboard")
    public ModelAndView peticionDashboard(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("dashboard");
        return mv;
    }

    @RequestMapping("/admin/usuario/mostrar")
    public ModelAndView peticionUsuariosMostrar(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("mostrarusuarios");
        return mv;
    }

    @RequestMapping("/admin/usuario/editar")
    public ModelAndView peticionUsuariosEditar(Authentication aut, HttpServletRequest request) {
        String nif = request.getParameter("nif");
        Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
        Usuario user = usuarioOpt.get();

        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.addObject("usuario", user);
        mv.setViewName("editarusuario");
        return mv;
    }


    @RequestMapping("/actualizar")
    public String peticionActualizar(Usuario u, Authentication aut) {

        String sinCodificar = u.getPw();
        String codificado = encoder.encode(sinCodificar);
        u.setPw(codificado);

        usuarios.guardarUsuario(u);

        // Si el usuario es admin, redirigir a la página de administrador
        if (aut.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "redirect:/admin";
        }
        // Si el usuario es usuario, redirigir a la página de usuario
        else {
            return "redirect:/user";
        }
    }

    @RequestMapping("admin/usuario/eliminar")
    public ModelAndView peticionUsuariosEliminar(Authentication aut, HttpServletRequest request){
        String nif = request.getParameter("nif");
        Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
        ModelAndView mv = new ModelAndView();
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            List<Role> rol = usuario.getRoles();
            // Eliminar el usuario y el rol de la base de datos
            roles.borrarRole(rol.get(0));
            usuarios.eliminarUsuario(usuario);
            mv.setViewName("redirect:/admin");
        }
        else {
            mv.addObject("sms", "No se ha podido eliminar el usuario");
            mv.setViewName("informa");
        }
        return mv;
    }








}
