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
    // Inyección de dependencias
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UsuarioService usuarios;

    @Autowired
    RoleService roles;

    @Autowired
    TareaService tareas;

    // Métodos

    // Método que se ejecuta cuando se accede a la raíz de la aplicación
    @RequestMapping("/")
    public ModelAndView peticionRaiz(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
        }
        mv.setViewName("index"); //También llamados endpoints
        return mv;
    }

    @RequestMapping("login") // URL: http://localhost:8081/login
    public ModelAndView peticionSesion(Authentication aut) { // Nombre del método: peticionSesion
        ModelAndView mv = new ModelAndView(); // Objeto que contiene la vista y los datos
        mv.setViewName("login"); // La rutra del archivo: src/main/resources/templates/login.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("denegado") // URL: http://localhost:8083/login
    public ModelAndView peticionDenegado(Authentication aut) { // Nombre del método: peticionSesion
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("denegado"); // La rutra del archivo: src/main/resources/templates/login.html
        return mv; // Devuelve el objeto mv
    }

    @RequestMapping("/user") // URL: http://localhost:8083/user
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

    @RequestMapping("/user/perfil") // URL: http://localhost:8083/usuario/perfil
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

    @RequestMapping("/user/perfil/editar") // URL: http://localhost:8083/usuario/perfil/editar
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

    @RequestMapping("/user/perfil/eliminar") // URL: http://localhost:8083/usuario/perfil/eliminar
    public ModelAndView peticionEliminarPerfil(Authentication aut) {
        String nif = aut.getName();
        Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
        ModelAndView mv = new ModelAndView();
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            List<Role> rol = usuario.getRoles();
            // Eliminar el el rol de la base de datos
            roles.borrarRole(rol.get(0));
            // Eliminar las tareas del usuario de la base de datos
            List<Tarea> listaTareas = usuario.getTareas();
            for (Tarea tarea : listaTareas) {
                tareas.borrar(tarea);
            }
            // Eliminar el usuario de la base de datos
            usuarios.eliminarUsuario(usuario);
            mv.setViewName("redirect:/logout");
        }
        else {
            mv.addObject("sms", "No se ha podido eliminar el usuario");
            mv.setViewName("informa");
        }
        return mv;
    }

    @RequestMapping("/user/tareas/listado") // URL: http://localhost:8083/usuario/tareas/listado
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

    @RequestMapping("/user/tareas/nueva") // URL: http://localhost:8083/usuario/tareas/nueva
    public ModelAndView peticionNuevaTarea(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            mv.addObject("usuario", user);
            Tarea tarea = new Tarea();
            mv.addObject("tarea", tarea);
        }
        mv.setViewName("nuevatarea");
        return mv;
    }

    @RequestMapping("/guardar/tarea") // URL: http://localhost:8083/guardar/tarea
    public ModelAndView guardarTarea(Tarea tarea, Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            tarea.setUsuario(user);
            tareas.guardar(tarea);
            if (tarea.getNombre() != null) {
                // Comprobar el rol del usuario para redirigirlo a la página correspondiente
                String nifAut = aut.getName();
                Optional<Usuario> usuarioAutOpt = usuarios.buscarUsuario(nifAut);
                Usuario userAut = usuarioAutOpt.get();
                List<Role> rol = userAut.getRoles();
                if (rol.get(0).getRol().equals("ADMINISTRADOR")){
                    mv.setViewName("redirect:/admin/usuario/tareas/listado?nif=" + nif);
                }
                else
                    mv.setViewName("redirect:/user/tareas/listado");

            } else {
                mv.addObject("sms", "No se ha actualizado la tarea correctamente");
                mv.setViewName("informa");
            }
        }
        return mv;
    }

    @RequestMapping("/user/tareas/editar") // URL: http://localhost:8083/usuario/tareas/editar
    public ModelAndView peticionEditarTarea(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            List<Tarea> listaTareas = user.getTareas();
            mv.addObject("tareas", listaTareas);
            int id = Integer.parseInt(request.getParameter("id"));
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            Tarea tarea = tareaOpt.get();
            mv.addObject("usuario", user);
            mv.addObject("tarea", tarea);
        }
        mv.setViewName("editartarea");
        return mv;
    }

    @RequestMapping("/actualizar/tarea") // URL: http://localhost:8083/actualizar/tarea
    public ModelAndView actualizarTarea(Tarea tarea, Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            tarea.setUsuario(user);
            tareas.guardar(tarea);
            if (tarea.getNombre() != null) {
                // Comprobar el rol del usuario para redirigirlo a la página correspondiente
                String nifAut = aut.getName();
                Optional<Usuario> usuarioAutOpt = usuarios.buscarUsuario(nifAut);
                Usuario userAut = usuarioAutOpt.get();
                List<Role> rol = userAut.getRoles();
                if (rol.get(0).getRol().equals("ADMINISTRADOR")){
                    mv.addObject("nif", nif);
                    mv.setViewName("redirect:/admin/usuario/tareas/listado");
                }
                else
                    mv.setViewName("redirect:/user/tareas/listado");

            } else {
                mv.addObject("sms", "No se ha actualizado la tarea correctamente");
                mv.setViewName("informa");
            }
        }
        return mv;
    }

    @RequestMapping("/user/tareas/eliminar") // URL: http://localhost:8083/usuario/tareas/eliminar
    public ModelAndView peticionEliminarTarea(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else {
            mv.addObject("u ser", aut.getName());
            String nif = aut.getName();
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            List<Tarea> listaTareas = user.getTareas();
            mv.addObject("tareas", listaTareas);
            int id = Integer.parseInt(request.getParameter("id"));
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            Tarea tarea = tareaOpt.get();
            tareas.borrar(tarea);
            mv.setViewName("redirect:/user/tareas/listado");
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

    @RequestMapping("/admin/usuario/nuevo") // URL: http://localhost:8083/admin/usuario/nuevo
    public ModelAndView registro() {
        ModelAndView mv = new ModelAndView();
        Usuario c = new Usuario();
        mv.addObject("usuario", c);
        mv.setViewName("nuevousuario");
        return mv;
    }

    @RequestMapping("/guardar") // URL: http://localhost:8083/guardar
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

    @RequestMapping("/admin/dashboard") // URL: http://localhost:8083/admin/dashboard
    public ModelAndView peticionDashboard(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        // Recupero el numero de usuariosç
        List<Usuario> listaUsuarios = usuarios.listaUsuarios();
        mv.addObject("listaUsuarios", listaUsuarios);
        // Recupero el numero de tareas
        List<Tarea> listaTareas = tareas.listaTareas();
        // contador de tareas
        int numeroTareas = 0;
        for (Tarea tarea : listaTareas) {
            numeroTareas++;
        }
        mv.addObject("contadorTareas", numeroTareas);
        //  Recupero el numero de tareas pendientes
        int tareasPendientes = tareas.cuentaTareas(2);
        mv.addObject("tareasPendientes", tareasPendientes);
        // Recupero el numero de tareas anuladas
        int tareasAnuladas = tareas.cuentaTareas(1);
        mv.addObject("tareasAnuladas", tareasAnuladas);
        // Recupero el numero de tareas completadas
        int tareasCompletadas = tareas.tareasFinalizadas();
        mv.addObject("tareasCompletadas", tareasCompletadas);

        //Promedio de tareas por usuario
        Double promedioTareas = tareas.promedioTareas();
        mv.addObject("promedioTareas", promedioTareas);

        mv.setViewName("dashboard");
        return mv;
    }

    @RequestMapping("/admin/usuario/mostrar") // URL: http://localhost:8083/admin/usuario/mostrar
    public ModelAndView peticionUsuariosMostrar(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else
            mv.addObject("user", aut.getName());
        mv.setViewName("mostrarusuarios");
        return mv;
    }

    @RequestMapping("/admin/usuario/editar") // URL: http://localhost:8083/admin/usuario/editar
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


    @RequestMapping("/actualizar") // URL: http://localhost:8083/actualizar
    public String peticionActualizar(Usuario u, Authentication aut) {

        String sinCodificar = u.getPw();
        String codificado = encoder.encode(sinCodificar);
        u.setPw(codificado);

        usuarios.guardarUsuario(u);

        // Si el usuario es admin, redirigir a la página de administrador
        if (aut.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"))) {
            return "redirect:/admin";
        }
        // Si el usuario es usuario, redirigir a la página de usuario
        else {
            return "redirect:/user/perfil";
        }
    }

    @RequestMapping("admin/usuario/eliminar") // URL: http://localhost:8083/admin/usuario/eliminar
    public ModelAndView peticionUsuariosEliminar(Authentication aut, HttpServletRequest request){
        String nif = request.getParameter("nif");
        Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
        ModelAndView mv = new ModelAndView();
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Eliminar las tareas del usuario de la base de datos
            List<Tarea> listaTareas = usuario.getTareas();
            for (Tarea tarea : listaTareas) {
                tareas.borrar(tarea);
            }
            List<Role> rol = usuario.getRoles();
            // Eliminar el rol de la base de datos
            roles.borrarRole(rol.get(0));
            // Eliminar el usuario de la base de datos
            usuarios.eliminarUsuario(usuario);
            mv.setViewName("redirect:/admin");
        }
        else {
            mv.addObject("sms", "No se ha podido eliminar el usuario");
            mv.setViewName("informa");
        }
        return mv;
    }
    @RequestMapping("/admin/usuario/tareas/listado") // URL: http://localhost:8083/admin/usuario/tareas/listado
    public ModelAndView peticionUsuariosTareasListado(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else{
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            mv.addObject("usuario", user);
            List<Tarea> listaTareas = user.getTareas();
            mv.addObject("tareas", listaTareas);
        }
        mv.setViewName("listadotareasuser");
        return mv;
    }

    @RequestMapping("/admin/usuario/tareas/nueva") // URL: http://localhost:8083/admin/usuario/tareas/nueva
    public ModelAndView peticionUsuariosTareasNueva(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else{
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            Tarea t = new Tarea();
            t.setUsuario(user);
            mv.addObject("tarea", t);
            mv.addObject("usuario", user);
            mv.setViewName("nuevatarea");
        }
        return mv;
    }


    @RequestMapping("/admin/usuario/tareas/editar") // URL: http://localhost:8083/admin/usuario/tareas/editar
    public ModelAndView peticionUsuariosTareasEditar(Authentication aut, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(aut==null){
            mv.addObject("user", "No se ha iniciado sesión");
        }
        else{
            mv.addObject("user", aut.getName());
            String nif = request.getParameter("nif");
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            Usuario user = usuarioOpt.get();
            int id = Integer.parseInt(request.getParameter("id"));
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            Tarea tarea = tareaOpt.get();
            mv.addObject("usuario", user);
            mv.addObject("tarea", tarea);
            mv.setViewName("editartarea");
        }
        return mv;
    }

}
