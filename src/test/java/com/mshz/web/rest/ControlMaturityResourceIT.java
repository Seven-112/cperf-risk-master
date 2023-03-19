package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.ControlMaturity;
import com.mshz.repository.ControlMaturityRepository;
import com.mshz.service.ControlMaturityService;
import com.mshz.service.dto.ControlMaturityCriteria;
import com.mshz.service.ControlMaturityQueryService;

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
 * Integration tests for the {@link ControlMaturityResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ControlMaturityResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private ControlMaturityRepository controlMaturityRepository;

    @Autowired
    private ControlMaturityService controlMaturityService;

    @Autowired
    private ControlMaturityQueryService controlMaturityQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restControlMaturityMockMvc;

    private ControlMaturity controlMaturity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControlMaturity createEntity(EntityManager em) {
        ControlMaturity controlMaturity = new ControlMaturity()
            .label(DEFAULT_LABEL);
        return controlMaturity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControlMaturity createUpdatedEntity(EntityManager em) {
        ControlMaturity controlMaturity = new ControlMaturity()
            .label(UPDATED_LABEL);
        return controlMaturity;
    }

    @BeforeEach
    public void initTest() {
        controlMaturity = createEntity(em);
    }

    @Test
    @Transactional
    public void createControlMaturity() throws Exception {
        int databaseSizeBeforeCreate = controlMaturityRepository.findAll().size();
        // Create the ControlMaturity
        restControlMaturityMockMvc.perform(post("/api/control-maturities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(controlMaturity)))
            .andExpect(status().isCreated());

        // Validate the ControlMaturity in the database
        List<ControlMaturity> controlMaturityList = controlMaturityRepository.findAll();
        assertThat(controlMaturityList).hasSize(databaseSizeBeforeCreate + 1);
        ControlMaturity testControlMaturity = controlMaturityList.get(controlMaturityList.size() - 1);
        assertThat(testControlMaturity.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createControlMaturityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = controlMaturityRepository.findAll().size();

        // Create the ControlMaturity with an existing ID
        controlMaturity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restControlMaturityMockMvc.perform(post("/api/control-maturities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(controlMaturity)))
            .andExpect(status().isBadRequest());

        // Validate the ControlMaturity in the database
        List<ControlMaturity> controlMaturityList = controlMaturityRepository.findAll();
        assertThat(controlMaturityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = controlMaturityRepository.findAll().size();
        // set the field null
        controlMaturity.setLabel(null);

        // Create the ControlMaturity, which fails.


        restControlMaturityMockMvc.perform(post("/api/control-maturities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(controlMaturity)))
            .andExpect(status().isBadRequest());

        List<ControlMaturity> controlMaturityList = controlMaturityRepository.findAll();
        assertThat(controlMaturityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllControlMaturities() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        // Get all the controlMaturityList
        restControlMaturityMockMvc.perform(get("/api/control-maturities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controlMaturity.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    @Transactional
    public void getControlMaturity() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        // Get the controlMaturity
        restControlMaturityMockMvc.perform(get("/api/control-maturities/{id}", controlMaturity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(controlMaturity.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }


    @Test
    @Transactional
    public void getControlMaturitiesByIdFiltering() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        Long id = controlMaturity.getId();

        defaultControlMaturityShouldBeFound("id.equals=" + id);
        defaultControlMaturityShouldNotBeFound("id.notEquals=" + id);

        defaultControlMaturityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultControlMaturityShouldNotBeFound("id.greaterThan=" + id);

        defaultControlMaturityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultControlMaturityShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllControlMaturitiesByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        // Get all the controlMaturityList where label equals to DEFAULT_LABEL
        defaultControlMaturityShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the controlMaturityList where label equals to UPDATED_LABEL
        defaultControlMaturityShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllControlMaturitiesByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        // Get all the controlMaturityList where label not equals to DEFAULT_LABEL
        defaultControlMaturityShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the controlMaturityList where label not equals to UPDATED_LABEL
        defaultControlMaturityShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllControlMaturitiesByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        // Get all the controlMaturityList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultControlMaturityShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the controlMaturityList where label equals to UPDATED_LABEL
        defaultControlMaturityShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllControlMaturitiesByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        // Get all the controlMaturityList where label is not null
        defaultControlMaturityShouldBeFound("label.specified=true");

        // Get all the controlMaturityList where label is null
        defaultControlMaturityShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllControlMaturitiesByLabelContainsSomething() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        // Get all the controlMaturityList where label contains DEFAULT_LABEL
        defaultControlMaturityShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the controlMaturityList where label contains UPDATED_LABEL
        defaultControlMaturityShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllControlMaturitiesByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        controlMaturityRepository.saveAndFlush(controlMaturity);

        // Get all the controlMaturityList where label does not contain DEFAULT_LABEL
        defaultControlMaturityShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the controlMaturityList where label does not contain UPDATED_LABEL
        defaultControlMaturityShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultControlMaturityShouldBeFound(String filter) throws Exception {
        restControlMaturityMockMvc.perform(get("/api/control-maturities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controlMaturity.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restControlMaturityMockMvc.perform(get("/api/control-maturities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultControlMaturityShouldNotBeFound(String filter) throws Exception {
        restControlMaturityMockMvc.perform(get("/api/control-maturities?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restControlMaturityMockMvc.perform(get("/api/control-maturities/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingControlMaturity() throws Exception {
        // Get the controlMaturity
        restControlMaturityMockMvc.perform(get("/api/control-maturities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateControlMaturity() throws Exception {
        // Initialize the database
        controlMaturityService.save(controlMaturity);

        int databaseSizeBeforeUpdate = controlMaturityRepository.findAll().size();

        // Update the controlMaturity
        ControlMaturity updatedControlMaturity = controlMaturityRepository.findById(controlMaturity.getId()).get();
        // Disconnect from session so that the updates on updatedControlMaturity are not directly saved in db
        em.detach(updatedControlMaturity);
        updatedControlMaturity
            .label(UPDATED_LABEL);

        restControlMaturityMockMvc.perform(put("/api/control-maturities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedControlMaturity)))
            .andExpect(status().isOk());

        // Validate the ControlMaturity in the database
        List<ControlMaturity> controlMaturityList = controlMaturityRepository.findAll();
        assertThat(controlMaturityList).hasSize(databaseSizeBeforeUpdate);
        ControlMaturity testControlMaturity = controlMaturityList.get(controlMaturityList.size() - 1);
        assertThat(testControlMaturity.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingControlMaturity() throws Exception {
        int databaseSizeBeforeUpdate = controlMaturityRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControlMaturityMockMvc.perform(put("/api/control-maturities")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(controlMaturity)))
            .andExpect(status().isBadRequest());

        // Validate the ControlMaturity in the database
        List<ControlMaturity> controlMaturityList = controlMaturityRepository.findAll();
        assertThat(controlMaturityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteControlMaturity() throws Exception {
        // Initialize the database
        controlMaturityService.save(controlMaturity);

        int databaseSizeBeforeDelete = controlMaturityRepository.findAll().size();

        // Delete the controlMaturity
        restControlMaturityMockMvc.perform(delete("/api/control-maturities/{id}", controlMaturity.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ControlMaturity> controlMaturityList = controlMaturityRepository.findAll();
        assertThat(controlMaturityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
