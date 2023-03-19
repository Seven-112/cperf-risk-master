package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.RiskType;
import com.mshz.repository.RiskTypeRepository;
import com.mshz.service.RiskTypeService;
import com.mshz.service.dto.RiskTypeCriteria;
import com.mshz.service.RiskTypeQueryService;

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
 * Integration tests for the {@link RiskTypeResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RiskTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private RiskTypeRepository riskTypeRepository;

    @Autowired
    private RiskTypeService riskTypeService;

    @Autowired
    private RiskTypeQueryService riskTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRiskTypeMockMvc;

    private RiskType riskType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskType createEntity(EntityManager em) {
        RiskType riskType = new RiskType()
            .name(DEFAULT_NAME);
        return riskType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskType createUpdatedEntity(EntityManager em) {
        RiskType riskType = new RiskType()
            .name(UPDATED_NAME);
        return riskType;
    }

    @BeforeEach
    public void initTest() {
        riskType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRiskType() throws Exception {
        int databaseSizeBeforeCreate = riskTypeRepository.findAll().size();
        // Create the RiskType
        restRiskTypeMockMvc.perform(post("/api/risk-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskType)))
            .andExpect(status().isCreated());

        // Validate the RiskType in the database
        List<RiskType> riskTypeList = riskTypeRepository.findAll();
        assertThat(riskTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RiskType testRiskType = riskTypeList.get(riskTypeList.size() - 1);
        assertThat(testRiskType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRiskTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = riskTypeRepository.findAll().size();

        // Create the RiskType with an existing ID
        riskType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRiskTypeMockMvc.perform(post("/api/risk-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskType)))
            .andExpect(status().isBadRequest());

        // Validate the RiskType in the database
        List<RiskType> riskTypeList = riskTypeRepository.findAll();
        assertThat(riskTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskTypeRepository.findAll().size();
        // set the field null
        riskType.setName(null);

        // Create the RiskType, which fails.


        restRiskTypeMockMvc.perform(post("/api/risk-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskType)))
            .andExpect(status().isBadRequest());

        List<RiskType> riskTypeList = riskTypeRepository.findAll();
        assertThat(riskTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRiskTypes() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        // Get all the riskTypeList
        restRiskTypeMockMvc.perform(get("/api/risk-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(riskType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getRiskType() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        // Get the riskType
        restRiskTypeMockMvc.perform(get("/api/risk-types/{id}", riskType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(riskType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getRiskTypesByIdFiltering() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        Long id = riskType.getId();

        defaultRiskTypeShouldBeFound("id.equals=" + id);
        defaultRiskTypeShouldNotBeFound("id.notEquals=" + id);

        defaultRiskTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRiskTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultRiskTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRiskTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRiskTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        // Get all the riskTypeList where name equals to DEFAULT_NAME
        defaultRiskTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the riskTypeList where name equals to UPDATED_NAME
        defaultRiskTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRiskTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        // Get all the riskTypeList where name not equals to DEFAULT_NAME
        defaultRiskTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the riskTypeList where name not equals to UPDATED_NAME
        defaultRiskTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRiskTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        // Get all the riskTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRiskTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the riskTypeList where name equals to UPDATED_NAME
        defaultRiskTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRiskTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        // Get all the riskTypeList where name is not null
        defaultRiskTypeShouldBeFound("name.specified=true");

        // Get all the riskTypeList where name is null
        defaultRiskTypeShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllRiskTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        // Get all the riskTypeList where name contains DEFAULT_NAME
        defaultRiskTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the riskTypeList where name contains UPDATED_NAME
        defaultRiskTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRiskTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        riskTypeRepository.saveAndFlush(riskType);

        // Get all the riskTypeList where name does not contain DEFAULT_NAME
        defaultRiskTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the riskTypeList where name does not contain UPDATED_NAME
        defaultRiskTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRiskTypeShouldBeFound(String filter) throws Exception {
        restRiskTypeMockMvc.perform(get("/api/risk-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(riskType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restRiskTypeMockMvc.perform(get("/api/risk-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRiskTypeShouldNotBeFound(String filter) throws Exception {
        restRiskTypeMockMvc.perform(get("/api/risk-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRiskTypeMockMvc.perform(get("/api/risk-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRiskType() throws Exception {
        // Get the riskType
        restRiskTypeMockMvc.perform(get("/api/risk-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRiskType() throws Exception {
        // Initialize the database
        riskTypeService.save(riskType);

        int databaseSizeBeforeUpdate = riskTypeRepository.findAll().size();

        // Update the riskType
        RiskType updatedRiskType = riskTypeRepository.findById(riskType.getId()).get();
        // Disconnect from session so that the updates on updatedRiskType are not directly saved in db
        em.detach(updatedRiskType);
        updatedRiskType
            .name(UPDATED_NAME);

        restRiskTypeMockMvc.perform(put("/api/risk-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRiskType)))
            .andExpect(status().isOk());

        // Validate the RiskType in the database
        List<RiskType> riskTypeList = riskTypeRepository.findAll();
        assertThat(riskTypeList).hasSize(databaseSizeBeforeUpdate);
        RiskType testRiskType = riskTypeList.get(riskTypeList.size() - 1);
        assertThat(testRiskType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRiskType() throws Exception {
        int databaseSizeBeforeUpdate = riskTypeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiskTypeMockMvc.perform(put("/api/risk-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskType)))
            .andExpect(status().isBadRequest());

        // Validate the RiskType in the database
        List<RiskType> riskTypeList = riskTypeRepository.findAll();
        assertThat(riskTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRiskType() throws Exception {
        // Initialize the database
        riskTypeService.save(riskType);

        int databaseSizeBeforeDelete = riskTypeRepository.findAll().size();

        // Delete the riskType
        restRiskTypeMockMvc.perform(delete("/api/risk-types/{id}", riskType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RiskType> riskTypeList = riskTypeRepository.findAll();
        assertThat(riskTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
