package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.customer.CustomerService;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author jzamoran
 */

@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService{
    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    GameService gameService;

    @Autowired
    CustomerService customerService;

    public Prestamo get(Long id){
        return this.prestamoRepository.findById(id).orElse(null);
    }

    /**
     *
     * @param dto
     * @return
     */
    @Override
    public Page<Prestamo> findPage(PrestamoSearchDto dto) {
        PrestamoSpecification gameSpec = new PrestamoSpecification(new SearchCriteria("game.id",":",dto.getGameId()));
        PrestamoSpecification customerSpec = new PrestamoSpecification(new SearchCriteria("customer.id",":",dto.getCustomerId()));
        PrestamoSpecification startDateSpec = new PrestamoSpecification(new SearchCriteria("startDate","<=",dto.getDate()));
        PrestamoSpecification endDateSpec = new PrestamoSpecification(new SearchCriteria("endDate",">=",dto.getDate()));

        Specification<Prestamo> spec = gameSpec.and(customerSpec).and(startDateSpec).and(endDateSpec);



        return this.prestamoRepository.findAll(spec, dto.getPageable().getPageable());
    }

    /**
     * Guarda o actualiza el prestamo
     * @param id
     * @param data
     */
    @Override
    public void save(Long id, PrestamoDto data) throws Exception{
        Prestamo prestamo;

        if(id==null){
            prestamo = new Prestamo();
        }else{
            prestamo = this.get(id);
        }

        BeanUtils.copyProperties(data, prestamo, "id","game","customer");


        prestamo.setCustomer(this.customerService.get(data.getCustomer().getId()));
        prestamo.setGame(this.gameService.get(data.getGame().getId()));

        //validación de max 14 días
        long days = ChronoUnit.DAYS.between(data.getStartDate(),data.getEndDate());
        if (days>14){
            throw new Exception("loans cannont exceed 14 days");
        }

        //validación un cliente->máximo 2 juegos activos a la vez

        List<Prestamo> prestamos = this.findAll();

        int contador = 0;
        for (Prestamo p: prestamos){
            if (id != null && p.getId().equals(id)) {
                continue;
            }
            boolean overlap = !data.getEndDate().isBefore(p.getStartDate()) && !data.getStartDate().isAfter(p.getEndDate());

            //juego ocupado
            if(p.getGame().getId().equals(data.getGame().getId()) && overlap){
                throw new Exception("Game not available");
            }
            //prestamos activos del customer
            if(p.getCustomer().getId().equals(data.getCustomer().getId()) && overlap){
               contador ++;
            }

        }

        if(contador >=2){
            throw new Exception("Customer has 2 loans");
        }



        this.prestamoRepository.save(prestamo);

    }

    /**
     * Elimina el prestamo
     * @param id
     * @throws Exception
     */
    @Override
    public void delete(Long id) throws Exception {
        if(this.get(id) == null){
            throw new Exception("Not exists");
        }

        this.prestamoRepository.deleteById(id);

    }

    /**
     *
     * @return todos los prestamos
     */
    @Override
    public List<Prestamo> findAll() {
        return (List<Prestamo>) this.prestamoRepository.findAll();
    }

}
