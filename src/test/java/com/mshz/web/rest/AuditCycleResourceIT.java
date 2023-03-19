package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.AuditCycle;
import com.mshz.repository.AuditCycleRepository;
import com.mshz.service.AuditCycleService;
import com.mshz.service.dto.AuditCycleCriteria;
import com.mshz.service.AuditCycleQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AuditCycleResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditCycleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AuditCycleRepository auditCycleRepository;

    @Autowired
    private AuditCycleService auditCycleService;

    @Autowired
    private AuditCycleQueryService auditCycleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditCycleMockMvc;

    private AuditCycle auditCycle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditCycle createEntity(EntityManager em) {
        AuditCycle auditCycle = new AuditCycle()
            .name(DEFAULT_NAME);
        return auditCycle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditCycle createUpdatedEntity(EntityManager em) {
        AuditCycle auditCycle = new AuditCycle()
            .name(UPDATED_NAME);
        return auditCycle;
    }

    @BeforeEach
    public void initTest() {
        auditCycle = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditCycle() throws Exception {
        int databaseSizeBeforeCreate = auditCycleRepository.findAll().size();
        // Create the AuditCycle
        restAuditCycleMockMvc.perform(post("/api/audit-cycles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditCycle)))
            .andExpect(status().isCreated());

        // Validate the AuditCycle in the database
        List<AuditCycle> auditCycleList = auditCycleRepository.findAll();
        assertThat(auditCycleList).hasSize(databaseSizeBeforeCreate + 1);
        AuditCycle testAuditCycle = auditCycleList.get(auditCycleList.size() - 1);
        assertThat(testAuditCycle.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAuditCycleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditCycleRepository.findAll().size();

        // Create the AuditCycle with an existing ID
        auditCycle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditCycleMockMvc.perform(post("/api/audit-cycles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditCycle)))
            .andExpect(status().isBadRequest());

        // Validate the AuditCycle in the database
        List<AuditCycle> auditCycleList = auditCycleRepository.findAll();
        assertThat(auditCycleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditCycleRepository.findAll().size();
        // set the field null
        auditCycle.setName(null);

        // Create the AuditCycle, which fails.


        restAuditCycleMockMvc.perform(post("/api/audit-cycles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditCycle)))
            .andExpect(status().isBadRequest());

        List<AuditCycle> auditCycleList = auditCycleRepository.findAll();
        assertThat(auditCycleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuditCycles() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        // Get all the auditCycleList
        restAuditCycleMockMvc.perform(get("/api/audit-cycles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditCycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getAuditCycle() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        // Get the auditCycle
        restAuditCycleMockMvc.perform(get("/api/audit-cycles/{id}", auditCycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditCycle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getAuditCyclesByIdFiltering() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        Long id = auditCycle.getId();

        defaultAuditCycleShouldBeFound("id.equals=" + id);
        defaultAuditCycleShouldNotBeFound("id.notEquals=" + id);

        defaultAuditCycleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditCycleShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditCycleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditCycleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditCyclesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        // Get all the auditCycleList where name equals to DEFAULT_NAME
        defaultAuditCycleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the auditCycleList where name equals to UPDATED_NAME
        defaultAuditCycleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditCyclesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        // Get all the auditCycleList where name not equals to DEFAULT_NAME
        defaultAuditCycleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the auditCycleList where name not equals to UPDATED_NAME
        defaultAuditCycleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditCyclesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        // Get all the auditCycleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAuditCycleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the auditCycleList where name equals to UPDATED_NAME
        defaultAuditCycleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditCyclesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        // Get all the auditCycleList where name is not null
        defaultAuditCycleShouldBeFound("name.specified=true");

        // Get all the auditCycleList where name is null
        defaultAuditCycleShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditCyclesByNameContainsSomething() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        // Get all the auditCycleList where name contains DEFAULT_NAME
        defaultAuditCycleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the auditCycleList where name contains UPDATED_NAME
        defaultAuditCycleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditCyclesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditCycleRepository.saveAndFlush(auditCycle);

        // Get all the auditCycleList where name does not contain DEFAULT_NAME
        defaultAuditCycleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the auditCycleList where name does not contain UPDATED_NAME
        defaultAuditCycleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditCycleShouldBeFound(String filter) throws Exception {
        restAuditCycleMockMvc.perform(get("/api/audit-cycles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditCycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restAuditCycleMockMvc.perform(get("/api/audit-cycles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditCycleShouldNotBeFound(String filter) throws Exception {
        restAuditCycleMockMvc.perform(get("/api/audit-cycles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditCycleMockMvc.perform(get("/api/audit-cycles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuditCycle() throws Exception {
        // Get the auditCycle
        restAuditCycleMockMvc.perform(get("/api/audit-cycles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditCycle() throws Exception {
        // Initialize the database
        auditCycleService.save(auditCycle);

        int databaseSizeBeforeUpdate = auditCycleRepository.findAll().size();

        // Update the auditCycle
        AuditCycle updatedAuditCycle = auditCycleRepository.findById(auditCycle.getId()).get();
        // Disconnect from session so that the updates on updatedAuditCycle are not directly saved in db
        em.detach(updatedAuditCycle);
        updatedAuditCycle
            .name(UPDATED_NAME);

        restAuditCycleMockMvc.perform(put("/api/audit-cycles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditCycle)))
            .andExpect(status().isOk());

        // Validate the AuditCycle in the database
        List<AuditCycle> auditCycleList = auditCycleRepository.findAll();
        assertThat(auditCycleList).hasSize(databaseSizeBeforeUpdate);
        AuditCycle testAuditCycle = auditCycleList.get(auditCycleList.size() - 1);
        assertThat(testAuditCycle.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditCycle() throws Exception {
        int databaseSizeBeforeUpdate = auditCycleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditCycleMockMvc.perform(put("/api/audit-cycles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditCycle)))
            .andExpect(status().isBadRequest());

        // Validate the AuditCycle in the database
        List<AuditCycle> auditCycleList = auditCycleRepository.findAll();
        assertThat(auditCycleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditCycle() throws Exception {
        // Initialize the database
        auditCycleService.save(auditCycle);

        int databaseSizeBeforeDelete = auditCycleRepository.findAll().size();

        // Delete the auditCycle
        restAuditCycleMockMvc.perform(delete("/api/audit-cycles/{id}", auditCycle.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditCycle> auditCycleList = auditCycleRepository.findAll();
        assertThat(auditCycleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
