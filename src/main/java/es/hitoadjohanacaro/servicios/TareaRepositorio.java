package es.hitoadjohanacaro.servicios;


import es.hitoadjohanacaro.jpa.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TareaRepositorio extends JpaRepository<Tarea, Integer> {

    @Query("select count(t) as finalizadas from Tarea t where t.estado=3")
    public Integer finalizadas();

    @Query("select count(t) as cuenta from Tarea t where t.estado= ?1")
    public Integer findByEstado(Integer estado);
}
