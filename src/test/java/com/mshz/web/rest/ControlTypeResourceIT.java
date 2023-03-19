package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.ControlType;
import com.mshz.repository.ControlTypeRepository;
import com.mshz.service.ControlTypeService;
import com.mshz.service.dto.ControlTypeCriteria;
import com.mshz.service.ControlTypeQueryService;

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
 * Integration tests for the {@link ControlTypeResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ControlTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private ControlTypeRepository controlTypeRepository;

    @Autowired
    private ControlTypeService controlTypeService;

    @Autowired
    private ControlTypeQueryService controlTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restControlTypeMockMvc;

    private ControlType controlType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControlType createEntity(EntityManager em) {
        ControlType controlType = new ControlType()
            .type(DEFAULT_TYPE);
        return controlType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControlType createUpdatedEntity(EntityManager em) {
        ControlType controlType = new ControlType()
            .type(UPDATED_TYPE);
        return controlType;
    }

    @BeforeEach
    public void initTest() {
        controlType = createEntity(em);
    }

    @Test
    @Transactional
    public void createControlType() throws Exception {
        int databaseSizeBeforeCreate = controlTypeRepository.findAll().size();
        // Create the ControlType
        restControlTypeMockMvc.perform(post("/api/control-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(controlType)))
            .andExpect(status().isCreated());

        // Validate the ControlType in the database
        List<ControlType> controlTypeList = controlTypeRepository.findAll();
        assertThat(controlTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ControlType testControlType = controlTypeList.get(controlTypeList.size() - 1);
        assertThat(testControlType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createControlTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = controlTypeRepository.findAll().size();

        // Create the ControlType with an existing ID
        controlType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restControlTypeMockMvc.perform(post("/api/control-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(controlType)))
            .andExpect(status().isBadRequest());

        // Validate the ControlType in the database
        List<ControlType> controlTypeList = controlTypeRepository.findAll();
        assertThat(controlTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = controlTypeRepository.findAll().size();
        // set the field null
        controlType.setType(null);

        // Create the ControlType, which fails.


        restControlTypeMockMvc.perform(post("/api/control-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(controlType)))
            .andExpect(status().isBadRequest());

        List<ControlType> controlTypeList = controlTypeRepository.findAll();
        assertThat(controlTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllControlTypes() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        // Get all the controlTypeList
        restControlTypeMockMvc.perform(get("/api/control-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controlType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getControlType() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        // Get the controlType
        restControlTypeMockMvc.perform(get("/api/control-types/{id}", controlType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(controlType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }


    @Test
    @Transactional
    public void getControlTypesByIdFiltering() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        Long id = controlType.getId();

        defaultControlTypeShouldBeFound("id.equals=" + id);
        defaultControlTypeShouldNotBeFound("id.notEquals=" + id);

        defaultControlTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultControlTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultControlTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultControlTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllControlTypesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        // Get all the controlTypeList where type equals to DEFAULT_TYPE
        defaultControlTypeShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the controlTypeList where type equals to UPDATED_TYPE
        defaultControlTypeShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllControlTypesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        // Get all the controlTypeList where type not equals to DEFAULT_TYPE
        defaultControlTypeShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the controlTypeList where type not equals to UPDATED_TYPE
        defaultControlTypeShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllControlTypesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        // Get all the controlTypeList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultControlTypeShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the controlTypeList where type equals to UPDATED_TYPE
        defaultControlTypeShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllControlTypesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        // Get all the controlTypeList where type is not null
        defaultControlTypeShouldBeFound("type.specified=true");

        // Get all the controlTypeList where type is null
        defaultControlTypeShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllControlTypesByTypeContainsSomething() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        // Get all the controlTypeList where type contains DEFAULT_TYPE
        defaultControlTypeShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the controlTypeList where type contains UPDATED_TYPE
        defaultControlTypeShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllControlTypesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        controlTypeRepository.saveAndFlush(controlType);

        // Get all the controlTypeList where type does not contain DEFAULT_TYPE
        defaultControlTypeShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the controlTypeList where type does not contain UPDATED_TYPE
        defaultControlTypeShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultControlTypeShouldBeFound(String filter) throws Exception {
        restControlTypeMockMvc.perform(get("/api/control-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controlType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restControlTypeMockMvc.perform(get("/api/control-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultControlTypeShouldNotBeFound(String filter) throws Exception {
        restControlTypeMockMvc.perform(get("/api/control-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restControlTypeMockMvc.perform(get("/api/control-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingControlType() throws Exception {
        // Get the controlType
        restControlTypeMockMvc.perform(get("/api/control-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateControlType() throws Exception {
        // Initialize the database
        controlTypeService.save(controlType);

        int databaseSizeBeforeUpdate = controlTypeRepository.findAll().size();

        // Update the controlType
        ControlType updatedControlType = controlTypeRepository.findById(controlType.getId()).get();
        // Disconnect from session so that the updates on updatedControlType are not directly saved in db
        em.detach(updatedControlType);
        updatedControlType
            .type(UPDATED_TYPE);

        restControlTypeMockMvc.perform(put("/api/control-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedControlType)))
            .andExpect(status().isOk());

        // Validate the ControlType in the database
        List<ControlType> controlTypeList = controlTypeRepository.findAll();
        assertThat(controlTypeList).hasSize(databaseSizeBeforeUpdate);
        ControlType testControlType = controlTypeList.get(controlTypeList.size() - 1);
        assertThat(testControlType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingControlType() throws Exception {
        int databaseSizeBeforeUpdate = controlTypeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControlTypeMockMvc.perform(put("/api/control-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(controlType)))
            .andExpect(status().isBadRequest());

        // Validate the ControlType in the database
        List<ControlType> controlTypeList = controlTypeRepository.findAll();
        assertThat(controlTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteControlType() throws Exception {
        // Initialize the database
        controlTypeService.save(controlType);

        int databaseSizeBeforeDelete = controlTypeRepository.findAll().size();

        // Delete the controlType
        restControlTypeMockMvc.perform(delete("/api/control-types/{id}", controlType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ControlType> controlTypeList = controlTypeRepository.findAll();
        assertThat(controlTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
