package com.ccsw.tutorial.prestamo.model;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.game.model.Game;

import java.time.LocalDate;

/**
 * @author Joanne Zamorano
 */
public class PrestamoDto {
    private Long id;
    private Game game;
    private Customer customer;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     *
     * @return id
     */
    public Long getId(){
        return this.id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     *
     * @return game
     */
    public Game getGame(){
        return this.game;
    }

    /**
     *
     * @param game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     *
     * @return customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     *
     * @param customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     *
     * @return startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     *
     * @param endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
