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
        else {
            mv.addObject("user", aut.getName());
            // Recupero el usuario autenticado
            String nif = aut.getName();
            // Busco el usuario en la base de datos
            Optional<Usuario> userOptional = usuarios.buscarUsuario(nif);
            // Obtengo el usuario de la base de datos
            Usuario user = userOptional.get();
            // Añado el usuario a la vista
            mv.addObject("usuario", user);
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
            // Recupero el usuario autenticado
            String nif = aut.getName();
            // Busco el usuario en la base de datos
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Obtengo el usuario de la base de datos
            Usuario user = usuarioOpt.get();
            // Añado el usuario a la vista
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
            // Recupero el usuario autenticado
            String nif = aut.getName();
            // Busco el usuario en la base de datos
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Obtengo el usuario de la base de datos
            Usuario user = usuarioOpt.get();
            // Añado el usuario a la vista
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
        // Comprobar si el usuario existe
        if (usuarioOpt.isPresent()) {
            // Obtener el usuario de la base de datos
            Usuario usuario = usuarioOpt.get();
            // Obtener el rol del usuario
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
            // Redirigir a la página de logout
            mv.setViewName("redirect:/logout");
        }
        else {
            mv.addObject("sms", "No se ha podido eliminar el usuario");
            mv.setViewName("informa");
        }
        return mv;
    }

    @RequestMapping("/user/tareas/listado") // URL: http://localhost:8083/usuario/tareas/listado
    public ModelAndView peticionListadoTareas(Authentication aut) {
        ModelAndView mv = new ModelAndView();
        if(aut==null)
            mv.addObject("user", "No se ha iniciado sesión");
        else{
            mv.addObject("user", aut.getName());
            // Recupero el nif del usuario autenticado
            String nif = aut.getName();
            // Busco el usuario en la base de datos
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Recupero el usuario
            Usuario user = usuarioOpt.get();
            // Asigno el usuario a la lista de tareas
            List<Tarea> listaTareas = user.getTareas();
            // Asigno la lista de tareas a la vista
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
            // Recupero el nif del usuario autenticado
            String nif = aut.getName();
            // Busco el usuario en la base de datos
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Recupero el usuario
            Usuario user = usuarioOpt.get();
            // Asigno el usuario a la vista
            mv.addObject("usuario", user);
            // Creo un objeto tarea
            Tarea tarea = new Tarea();
            // Asigno el objeto tarea a la vista
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
            // Recupero el nif del usuario elegido
            String nif = request.getParameter("nif");
            // Busco el usuario en la base de datos
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Recupero el usuario
            Usuario user = usuarioOpt.get();
            // Asigno el usuario a la tarea
            tarea.setUsuario(user);
            // Guardo la tarea en la base de datos
            tareas.guardar(tarea);
            if (tarea.getNombre() != null) {
                // Comprobar el rol del usuario para redirigirlo a la página correspondiente
                String nifAut = aut.getName();
                // Busco el usuario en la base de datos
                Optional<Usuario> usuarioAutOpt = usuarios.buscarUsuario(nifAut);
                // Recupero el usuario
                Usuario userAut = usuarioAutOpt.get();
                // Recupero el rol del usuario
                List<Role> rol = userAut.getRoles();
                if (rol.get(0).getRol().equals("ADMINISTRADOR")){
                    mv.addObject("nif", nif);
                    // Redirigir a la página de listado de tareas del usuario elegido
                    mv.setViewName("redirect:/admin/usuario/tareas/listado");
                }
                else
                    // Redirigir a la página de listado de tareas del usuario autenticado
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
            // Recupero el usuario autenticado
            String nif = aut.getName();
            // Recupero el usuario de la base de datos
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Recupero la lista de tareas del usuario
            Usuario user = usuarioOpt.get();
            // Recupero la lista de tareas del usuario
            List<Tarea> listaTareas = user.getTareas();
            // Recupero la tarea elegida
            mv.addObject("tareas", listaTareas);
            // Recupero el id de la tarea elegida
            int id = Integer.parseInt(request.getParameter("id"));
            // Busco la tarea elegida
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            // Recupero la tarea elegida
            Tarea tarea = tareaOpt.get();
            // Asigno la tarea elegida a la vista
            mv.addObject("usuario", user);
            // Asigno la tarea elegida a la vista
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
            // Recupero el nif del usuario elegido
            String nif = request.getParameter("nif");
            // Busco el usuario elegido
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Recupero el usuario elegido
            Usuario user = usuarioOpt.get();
            // Asigno el usuario elegido a la tarea
            tarea.setUsuario(user);
            // Actualizo la tarea
            tareas.guardar(tarea);
            if (tarea.getNombre() != null) {
                // Recupero el nif del usuario autenticado
                String nifAut = aut.getName();
                // Busco el usuario autenticado
                Optional<Usuario> usuarioAutOpt = usuarios.buscarUsuario(nifAut);
                // Recupero el usuario autenticado
                Usuario userAut = usuarioAutOpt.get();
                // Recupero el rol del usuario autenticado
                List<Role> rol = userAut.getRoles();
                // Comprobuebo el rol del usuario para redirigirlo a la página correspondiente
                if (rol.get(0).getRol().equals("ADMINISTRADOR")){
                    // Añado el nif del usuario al modelo
                    mv.addObject("nif", nif);
                    // Redirijo a la página de listado de tareas del usuario
                    mv.setViewName("redirect:/admin/usuario/tareas/listado");
                }
                else
                    // Redirijo a la página de listado de tareas del usuario
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
            // Recupero el usuario
            String nif = aut.getName();
            // Creo un objeto Optional para recuperar el usuario
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Creo un objeto Usuario
            Usuario user = usuarioOpt.get();
            // Recupero el listado de tareas del usuario
            List<Tarea> listaTareas = user.getTareas();
            // Añado el listado de tareas al modelo
            mv.addObject("tareas", listaTareas);
            // Recupero el id de la tarea a eliminar
            int id = Integer.parseInt(request.getParameter("id"));
            // Creo un objeto Optional para recuperar la tarea
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            // Creo un objeto Tarea
            Tarea tarea = tareaOpt.get();
            // Elimino la tarea
            tareas.borrar(tarea);
            // Redirijo a la página de listado de tareas
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
        // Listado de usuarios
        List<Usuario> listaUsuarios = usuarios.listaUsuarios();
        // Añado el listado de usuarios al modelo
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
        // Recupero la contraseña del usuario sin codificar
        String sinCodificar = usuario.getPw();
        // Codifico la contraseña
        String codificado = encoder.encode(sinCodificar);
        // Asigno la contraseña codificada al usuario
        usuario.setPw(codificado);

        if (aut==null) {
            mv.addObject("El nif " +usuario.getNif() +"ya existe");
        } else {
            // Guardo el usuario
            usuarios.guardarUsuario(usuario);
            // Creo objeto Role
            Role role = new Role();
            // Asigno el usuario al objeto Role
            role.setUsuario(usuario);
            // Asigno el rol al objeto Role
            role.setRol("USUARIO");
            // Guardo el objeto Role
            roles.guardarRole(role);
            if (usuario.getNif() == null) {
                mv.addObject("sms", "No se ha podido guardar el usuario");
            } else {
                // Redirijo a la página admin
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
        // Busco el usuario en la base de datos
        Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
        ModelAndView mv = new ModelAndView();
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Elimino las tareas del usuario de la base de datos
            List<Tarea> listaTareas = usuario.getTareas();
            for (Tarea tarea : listaTareas) {
                tareas.borrar(tarea);
            }
            List<Role> rol = usuario.getRoles();
            // Elimino el rol de la base de datos
            roles.borrarRole(rol.get(0));
            // Elimino el usuario de la base de datos
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
            // Recupero el usuario
            String nif = request.getParameter("nif");
            // Busco el usuario
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Creo un objeto usuario
            Usuario user = usuarioOpt.get();
            // Añado el usuario a la vista
            mv.addObject("usuario", user);
            // Recupero la lista de tareas del usuario
            List<Tarea> listaTareas = user.getTareas();
            // Añado la lista de tareas a la vista
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
            // Recupero el usuario
            String nif = request.getParameter("nif");
            // Busco el usuario
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Creo un objeto usuario
            Usuario user = usuarioOpt.get();
            // Creo un objeto tarea
            Tarea t = new Tarea();
            // Asigno el usuario a la tarea
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
            // Busco el usuario
            Optional<Usuario> usuarioOpt = usuarios.buscarUsuario(nif);
            // Creo un objeto usuario
            Usuario user = usuarioOpt.get();
            // Recupero el id de la tarea
            int id = Integer.parseInt(request.getParameter("id"));
            // Busco la tarea
            Optional<Tarea> tareaOpt = tareas.buscarTarea(id);
            // Creo un objeto tarea
            Tarea tarea = tareaOpt.get();
            // Añado el usuario y la tarea al modelo
            mv.addObject("usuario", user);
            mv.addObject("tarea", tarea);
            mv.setViewName("editartarea");
        }
        return mv;
    }

}
