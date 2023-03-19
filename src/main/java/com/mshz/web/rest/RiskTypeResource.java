package com.mshz.web.rest;

import com.mshz.domain.RiskType;
import com.mshz.service.RiskTypeService;
import com.mshz.web.rest.errors.BadRequestAlertException;
import com.mshz.service.dto.RiskTypeCriteria;
import com.mshz.service.RiskTypeQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mshz.domain.RiskType}.
 */
@RestController
@RequestMapping("/api")
public class RiskTypeResource {

    private final Logger log = LoggerFactory.getLogger(RiskTypeResource.class);

    private static final String ENTITY_NAME = "microrisqueRiskType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RiskTypeService riskTypeService;

    private final RiskTypeQueryService riskTypeQueryService;

    public RiskTypeResource(RiskTypeService riskTypeService, RiskTypeQueryService riskTypeQueryService) {
        this.riskTypeService = riskTypeService;
        this.riskTypeQueryService = riskTypeQueryService;
    }

    /**
     * {@code POST  /risk-types} : Create a new riskType.
     *
     * @param riskType the riskType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new riskType, or with status {@code 400 (Bad Request)} if the riskType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/risk-types")
    public ResponseEntity<RiskType> createRiskType(@Valid @RequestBody RiskType riskType) throws URISyntaxException {
        log.debug("REST request to save RiskType : {}", riskType);
        if (riskType.getId() != null) {
            throw new BadRequestAlertException("A new riskType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RiskType result = riskTypeService.save(riskType);
        return ResponseEntity.created(new URI("/api/risk-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /risk-types} : Updates an existing riskType.
     *
     * @param riskType the riskType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated riskType,
     * or with status {@code 400 (Bad Request)} if the riskType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the riskType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/risk-types")
    public ResponseEntity<RiskType> updateRiskType(@Valid @RequestBody RiskType riskType) throws URISyntaxException {
        log.debug("REST request to update RiskType : {}", riskType);
        if (riskType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RiskType result = riskTypeService.save(riskType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, riskType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /risk-types} : get all the riskTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of riskTypes in body.
     */
    @GetMapping("/risk-types")
    public ResponseEntity<List<RiskType>> getAllRiskTypes(RiskTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RiskTypes by criteria: {}", criteria);
        Page<RiskType> page = riskTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /risk-types/count} : count all the riskTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/risk-types/count")
    public ResponseEntity<Long> countRiskTypes(RiskTypeCriteria criteria) {
        log.debug("REST request to count RiskTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(riskTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /risk-types/:id} : get the "id" riskType.
     *
     * @param id the id of the riskType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the riskType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/risk-types/{id}")
    public ResponseEntity<RiskType> getRiskType(@PathVariable Long id) {
        log.debug("REST request to get RiskType : {}", id);
        Optional<RiskType> riskType = riskTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(riskType);
    }

    /**
     * {@code DELETE  /risk-types/:id} : delete the "id" riskType.
     *
     * @param id the id of the riskType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/risk-types/{id}")
    public ResponseEntity<Void> deleteRiskType(@PathVariable Long id) {
        log.debug("REST request to delete RiskType : {}", id);
        riskTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
