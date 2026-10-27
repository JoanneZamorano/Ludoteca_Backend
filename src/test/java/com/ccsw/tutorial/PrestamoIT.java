package com.ccsw.tutorial;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestRestTemplate
public class PrestamoIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/prestamo";

    public static final Long EXISTS_PRESTAMO_ID = 1L;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<PrestamoDto>> responseType =
            new ParameterizedTypeReference<List<PrestamoDto>>() {
            };

    /**
     * comprueba que se crea correctamente un préstamo correctamente
     */
    @Test
    public void saveWithoutIdShouldCreateNewPrestamo() {

        ResponseEntity<List<PrestamoDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.GET,
                        null,
                        responseType);

        assertNotNull(response);

        int initialSize = response.getBody().size();

        PrestamoDto dto = new PrestamoDto();

        Game game = new Game();
        game.setId(6L);

        Customer customer = new Customer();
        customer.setId(3L);

        dto.setGame(game);
        dto.setCustomer(customer);
        dto.setStartDate(LocalDate.of(2026, 12, 10));
        dto.setEndDate(LocalDate.of(2026, 12, 17));

        restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH,
                HttpMethod.PUT,
                new HttpEntity<>(dto),
                Void.class);

        response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.GET,
                        null,
                        responseType);

        assertEquals(initialSize + 1, response.getBody().size());
    }

    /**
     * comprueba que se modifica correctamente un préstamo que ya existe
     */
    @Test
    public void modifyWithExistIdShouldModifyPrestamo() {

        PrestamoDto dto = new PrestamoDto();

        Game game = new Game();
        game.setId(1L);

        Customer customer = new Customer();
        customer.setId(1L);

        dto.setGame(game);
        dto.setCustomer(customer);
        dto.setStartDate(LocalDate.of(2026, 11, 1));
        dto.setEndDate(LocalDate.of(2026, 11, 5));

        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_PRESTAMO_ID,
                        HttpMethod.PUT,
                        new HttpEntity<>(dto),
                        Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * comprueba que se elimina correctamente un préstamo
     */
    @Test
    public void deleteWithExistIdShouldDeletePrestamo() {

        ResponseEntity<List<PrestamoDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.GET,
                        null,
                        responseType);

        int initialSize = response.getBody().size();

        restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_PRESTAMO_ID,
                HttpMethod.DELETE,
                null,
                Void.class);

        response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.GET,
                        null,
                        responseType);

        assertEquals(initialSize - 1, response.getBody().size());
    }

    /**
     * comprueba que no se permite crear un préstamo superior a 14 días
     */
    @Test
    public void saveLoanGreaterThan14DaysShouldThrowException() {

        PrestamoDto dto = new PrestamoDto();

        Game game = new Game();
        game.setId(6L);

        Customer customer = new Customer();
        customer.setId(3L);

        dto.setGame(game);
        dto.setCustomer(customer);
        dto.setStartDate(LocalDate.of(2026, 12, 1));
        dto.setEndDate(LocalDate.of(2026, 12, 20));

        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.PUT,
                        new HttpEntity<>(dto),
                        String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * comprueba que no se permite prestar un juego que ya está reservado en las fechas seleccionadas
     */
    @Test
    public void saveLoanWithGameAlreadyReservedShouldThrowException() {

        PrestamoDto dto = new PrestamoDto();

        Game game = new Game();
        game.setId(1L);

        Customer customer = new Customer();
        customer.setId(4L);

        dto.setGame(game);
        dto.setCustomer(customer);
        dto.setStartDate(LocalDate.of(2026, 11, 5));
        dto.setEndDate(LocalDate.of(2026, 11, 7));

        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.PUT,
                        new HttpEntity<>(dto),
                        String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     *comprueba que un cliente no puede tener más de dos préstamos activos a la vez
     */
    @Test
    public void saveLoanWithCustomerHavingTwoActiveLoansShouldThrowException() {

        PrestamoDto dto = new PrestamoDto();

        Game game = new Game();
        game.setId(4L);

        Customer customer = new Customer();
        customer.setId(1L);

        dto.setGame(game);
        dto.setCustomer(customer);
        dto.setStartDate(LocalDate.of(2026, 11, 4));
        dto.setEndDate(LocalDate.of(2026, 11, 6));

        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.PUT,
                        new HttpEntity<>(dto),
                        String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}