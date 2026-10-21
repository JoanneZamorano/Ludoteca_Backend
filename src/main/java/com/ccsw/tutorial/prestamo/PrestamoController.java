package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Joanne Zamorano
 */
@Tag(name = "Prestamo", description ="API de Prestamos")
@RequestMapping(value="/prestamo")
@RestController
@CrossOrigin(origins = "*")
public class PrestamoController {
    @Autowired
    PrestamoService prestamoService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para recuperar un listado paginado de {@link Prestamo}
     * @param dto de busqueda
     * @return {@link Page}  de {@Link PrestamoDto}
     */
    @Operation(summary = "Find Page", description = "Method that return a page of Prestamos")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<PrestamoDto> findPage(@RequestBody PrestamoSearchDto dto) {
        Page<Prestamo> page = this.prestamoService.findPage(dto);

        return new PageImpl<>(page.getContent().stream()
                .map(e->mapper.map(e, PrestamoDto.class))
                .collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements());

    }


    @Operation(summary = "Save or Update", description = "Method that saves or updates a Prestamo")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody PrestamoDto dto) throws Exception {
        this.prestamoService.save(id, dto);
    }


    @Operation(summary = "Delete", description = "Method that deletes a Prestamo")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.prestamoService.delete(id);
    }


    @Operation(summary = "Find", description = "Method that return a list of Prestamos")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<PrestamoDto> findAll() {

        List<Prestamo> prestamos = this.prestamoService.findAll();

        return prestamos.stream().map(e -> mapper.map(e, PrestamoDto.class)).collect(Collectors.toList());
    }


}
