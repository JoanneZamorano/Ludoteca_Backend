package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Joanne Zamorano
 */
@Tag(name = "Prestamo", description ="API de Prestamos")
@RequestMapping(value="/prestamos")
@RestController
@CrossOrigin(origins = "*")
public class PrestamoController {



}
