package com.ccsw.tutorial.prestamo.model;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.game.model.Game;
import jakarta.persistence.*;

import java.time.LocalDate;


/**
 * @author Joanne Zamorano
 */

@Entity
@Table(name = "prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    /**
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
        return  this.game;
    }

    /**
     *
     * @param game
     */
    public void setGame(Game game){
        this.game = game;
    }

    /**
     *
     * @return customer
     */
    public Customer getCustomer(){
        return this.customer;
    }

    /**
     *
     * @param customer
     */
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    /**
     *
     * @return startDate
     */
    public LocalDate getStartDate(){
        return this.startDate;
    }

    /**
     *
     * @param startDate
     */
    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    /**
     *
     * @return endDate
     */
    public LocalDate getEndDate(){
        return this.endDate;
    }

    /**
     *
     * @param endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
