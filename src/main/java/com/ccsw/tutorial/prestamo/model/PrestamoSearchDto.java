package com.ccsw.tutorial.prestamo.model;

import com.ccsw.tutorial.common.pagination.PageableRequest;

import java.time.LocalDate;

/**
 * @author jzamoran
 */
public class PrestamoSearchDto {

    private PageableRequest pageable;
    private Long gameId;
    private Long customerId;
    private LocalDate date;

    /**
     *
     * @return pageable
     */
    public PageableRequest getPageable() {
        return pageable;
    }

    /**
     *
     * @param pageable
     */
    public void setPageable(PageableRequest pageable) {
        this.pageable = pageable;
    }

    /**
     *
     * @return game id
     */
    public Long getGameId() {
        return gameId;
    }

    /**
     *
     * @param gameId
     */
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    /**
     *
     * @return customer id
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     *
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     *
     * @return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
