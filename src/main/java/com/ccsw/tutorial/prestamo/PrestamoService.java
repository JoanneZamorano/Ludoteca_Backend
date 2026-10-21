package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author jzamoran
 */
public interface PrestamoService {
    /**
     * Recupera un {@link Prestamo} a través de su ID
     * @param id
     * @return Prestamo
     */
    Prestamo get(Long id);


    /**
     * Metodo para recuperar un listado paginado de {@link Prestamo}
     * @param dto
     * @return {@link Page} de {@link Prestamo}
     */
    Page<Prestamo> findPage(PrestamoSearchDto dto);

    /**
     * Metodo para crear o actualizar un {@link Prestamo}
     * @param id
     * @param dto
     */
    void save(Long id, PrestamoDto dto) throws Exception;

    /**
     * Metodo para eliminar un {@link Prestamo}
     * @param id
     * @throws Exception
     */
    void delete(Long id) throws Exception;

    /**
     * Recupera un listado de prestamos de {@link Prestamo}
     * @return- {@link List} de {@link Prestamo}
     */
    List<Prestamo> findAll();
}
