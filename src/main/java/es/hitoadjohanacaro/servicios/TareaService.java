package es.hitoadjohanacaro.servicios;


import es.hitoadjohanacaro.jpa.Tarea;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;

@Service
@ApplicationScope
public class TareaService {

    private TareaRepositorio tareas;

    public TareaService(TareaRepositorio tareas) {
        this.tareas = tareas;
    }

    public List<Tarea> listaTareas() {
        return tareas.findAll();
    }

    public Integer tareasFinalizadas() {
        return tareas.finalizadas();
    }

    public Integer cuentaTareas(Integer estado) {
        return tareas.findByEstado(estado);
    }

    public void guardar(Tarea t){
        tareas.save(t);
    }

    public void borrar(Tarea t){
        tareas.delete(t);
    }
}
