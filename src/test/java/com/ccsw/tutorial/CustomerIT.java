package com.ccsw.tutorial;

import com.ccsw.tutorial.customer.model.CustomerDto;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;


import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerIT {
    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/customer";

    public static final Long EXISTS_CUSTOMER_ID = 1L;
    public static final Long NOT_EXISTS_CUSTOMER_ID = 0L;

    public static final int EXPECTED_SIZE_AFTER_DELETE = 5;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<CustomerDto>> responseType =
            new ParameterizedTypeReference<List<CustomerDto>>() {};


    /**
     * comprueba que se recuperan todos los clientes
     */
    @Test
    public void findAllShouldReturnAllCustomers() {

        int TOTAL_CUSTOMERS = 6;

        ResponseEntity<List<CustomerDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.GET,
                        null,
                        responseType);

        assertNotNull(response);
        assertEquals(TOTAL_CUSTOMERS, response.getBody().size());
    }


    /**
     * comprueba que se crea un nuevo cliente cuando no se informa un id
     */
    @Test
    public void saveWithoutIdShouldCreateNewCustomer() {

        CustomerDto dto = new CustomerDto();
        dto.setName("Nuevo Cliente");

        ResponseEntity<List<CustomerDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.GET,
                        null,
                        responseType);

        int initialSize = response.getBody().size();

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
     * comprueba que se modifica correctamente un cliente existente
     */
    @Test
    public void modifyWithExistIdShouldModifyCustomer() {

        CustomerDto dto = new CustomerDto();
        dto.setName("Cliente Modificado");

        restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_CUSTOMER_ID,
                HttpMethod.PUT,
                new HttpEntity<>(dto),
                Void.class);

        ResponseEntity<List<CustomerDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.GET,
                        null,
                        responseType);

        CustomerDto customer =
                response.getBody()
                        .stream()
                        .filter(c -> c.getId().equals(EXISTS_CUSTOMER_ID))
                        .findFirst()
                        .orElse(null);

        assertNotNull(customer);
        assertEquals("Cliente Modificado", customer.getName());
    }


    /**
     * comprueba que no se puede crear un cliente con un nombre duplicado
     */
    @Test
    public void saveDuplicatedNameShouldThrowException() {

        CustomerDto dto = new CustomerDto();
        dto.setName("Ander");

        ResponseEntity<?> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.PUT,
                        new HttpEntity<>(dto),
                        String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * comprueba que se elimina correctamente un cliente existente
     */
    @Test
    public void deleteWithExistIdShouldDeleteCustomer() {

        restTemplate.exchange(
                LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_CUSTOMER_ID,
                HttpMethod.DELETE,
                null,
                Void.class);

        ResponseEntity<List<CustomerDto>> response =
                restTemplate.exchange(
                        LOCALHOST + port + SERVICE_PATH,
                        HttpMethod.GET,
                        null,
                        responseType);

        assertEquals(EXPECTED_SIZE_AFTER_DELETE,
                response.getBody().size());
    }



}
