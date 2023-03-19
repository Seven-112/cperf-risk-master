package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.Control;
import com.mshz.domain.ControlType;
import com.mshz.domain.ControlMaturity;
import com.mshz.domain.Risk;
import com.mshz.repository.ControlRepository;
import com.mshz.service.ControlService;
import com.mshz.service.dto.ControlCriteria;
import com.mshz.service.ControlQueryService;

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
 * Integration tests for the {@link ControlResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ControlResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VALIDATION_REQUIRED = false;
    private static final Boolean UPDATED_VALIDATION_REQUIRED = true;

    @Autowired
    private ControlRepository controlRepository;

    @Autowired
    private ControlService controlService;

    @Autowired
    private ControlQueryService controlQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restControlMockMvc;

    private Control control;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Control createEntity(EntityManager em) {
        Control control = new Control()
            .description(DEFAULT_DESCRIPTION)
            .validationRequired(DEFAULT_VALIDATION_REQUIRED);
        // Add required entity
        ControlType controlType;
        if (TestUtil.findAll(em, ControlType.class).isEmpty()) {
            controlType = ControlTypeResourceIT.createEntity(em);
            em.persist(controlType);
            em.flush();
        } else {
            controlType = TestUtil.findAll(em, ControlType.class).get(0);
        }
        control.setType(controlType);
        // Add required entity
        ControlMaturity controlMaturity;
        if (TestUtil.findAll(em, ControlMaturity.class).isEmpty()) {
            controlMaturity = ControlMaturityResourceIT.createEntity(em);
            em.persist(controlMaturity);
            em.flush();
        } else {
            controlMaturity = TestUtil.findAll(em, ControlMaturity.class).get(0);
        }
        control.setMaturity(controlMaturity);
        return control;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Control createUpdatedEntity(EntityManager em) {
        Control control = new Control()
            .description(UPDATED_DESCRIPTION)
            .validationRequired(UPDATED_VALIDATION_REQUIRED);
        // Add required entity
        ControlType controlType;
        if (TestUtil.findAll(em, ControlType.class).isEmpty()) {
            controlType = ControlTypeResourceIT.createUpdatedEntity(em);
            em.persist(controlType);
            em.flush();
        } else {
            controlType = TestUtil.findAll(em, ControlType.class).get(0);
        }
        control.setType(controlType);
        // Add required entity
        ControlMaturity controlMaturity;
        if (TestUtil.findAll(em, ControlMaturity.class).isEmpty()) {
            controlMaturity = ControlMaturityResourceIT.createUpdatedEntity(em);
            em.persist(controlMaturity);
            em.flush();
        } else {
            controlMaturity = TestUtil.findAll(em, ControlMaturity.class).get(0);
        }
        control.setMaturity(controlMaturity);
        return control;
    }

    @BeforeEach
    public void initTest() {
        control = createEntity(em);
    }

    @Test
    @Transactional
    public void createControl() throws Exception {
        int databaseSizeBeforeCreate = controlRepository.findAll().size();
        // Create the Control
        restControlMockMvc.perform(post("/api/controls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(control)))
            .andExpect(status().isCreated());

        // Validate the Control in the database
        List<Control> controlList = controlRepository.findAll();
        assertThat(controlList).hasSize(databaseSizeBeforeCreate + 1);
        Control testControl = controlList.get(controlList.size() - 1);
        assertThat(testControl.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testControl.isValidationRequired()).isEqualTo(DEFAULT_VALIDATION_REQUIRED);
    }

    @Test
    @Transactional
    public void createControlWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = controlRepository.findAll().size();

        // Create the Control with an existing ID
        control.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restControlMockMvc.perform(post("/api/controls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(control)))
            .andExpect(status().isBadRequest());

        // Validate the Control in the database
        List<Control> controlList = controlRepository.findAll();
        assertThat(controlList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = controlRepository.findAll().size();
        // set the field null
        control.setDescription(null);

        // Create the Control, which fails.


        restControlMockMvc.perform(post("/api/controls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(control)))
            .andExpect(status().isBadRequest());

        List<Control> controlList = controlRepository.findAll();
        assertThat(controlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllControls() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList
        restControlMockMvc.perform(get("/api/controls?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(control.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].validationRequired").value(hasItem(DEFAULT_VALIDATION_REQUIRED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getControl() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get the control
        restControlMockMvc.perform(get("/api/controls/{id}", control.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(control.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.validationRequired").value(DEFAULT_VALIDATION_REQUIRED.booleanValue()));
    }


    @Test
    @Transactional
    public void getControlsByIdFiltering() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        Long id = control.getId();

        defaultControlShouldBeFound("id.equals=" + id);
        defaultControlShouldNotBeFound("id.notEquals=" + id);

        defaultControlShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultControlShouldNotBeFound("id.greaterThan=" + id);

        defaultControlShouldBeFound("id.lessThanOrEqual=" + id);
        defaultControlShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllControlsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where description equals to DEFAULT_DESCRIPTION
        defaultControlShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the controlList where description equals to UPDATED_DESCRIPTION
        defaultControlShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllControlsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where description not equals to DEFAULT_DESCRIPTION
        defaultControlShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the controlList where description not equals to UPDATED_DESCRIPTION
        defaultControlShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllControlsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultControlShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the controlList where description equals to UPDATED_DESCRIPTION
        defaultControlShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllControlsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where description is not null
        defaultControlShouldBeFound("description.specified=true");

        // Get all the controlList where description is null
        defaultControlShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllControlsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where description contains DEFAULT_DESCRIPTION
        defaultControlShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the controlList where description contains UPDATED_DESCRIPTION
        defaultControlShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllControlsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where description does not contain DEFAULT_DESCRIPTION
        defaultControlShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the controlList where description does not contain UPDATED_DESCRIPTION
        defaultControlShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllControlsByValidationRequiredIsEqualToSomething() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where validationRequired equals to DEFAULT_VALIDATION_REQUIRED
        defaultControlShouldBeFound("validationRequired.equals=" + DEFAULT_VALIDATION_REQUIRED);

        // Get all the controlList where validationRequired equals to UPDATED_VALIDATION_REQUIRED
        defaultControlShouldNotBeFound("validationRequired.equals=" + UPDATED_VALIDATION_REQUIRED);
    }

    @Test
    @Transactional
    public void getAllControlsByValidationRequiredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where validationRequired not equals to DEFAULT_VALIDATION_REQUIRED
        defaultControlShouldNotBeFound("validationRequired.notEquals=" + DEFAULT_VALIDATION_REQUIRED);

        // Get all the controlList where validationRequired not equals to UPDATED_VALIDATION_REQUIRED
        defaultControlShouldBeFound("validationRequired.notEquals=" + UPDATED_VALIDATION_REQUIRED);
    }

    @Test
    @Transactional
    public void getAllControlsByValidationRequiredIsInShouldWork() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where validationRequired in DEFAULT_VALIDATION_REQUIRED or UPDATED_VALIDATION_REQUIRED
        defaultControlShouldBeFound("validationRequired.in=" + DEFAULT_VALIDATION_REQUIRED + "," + UPDATED_VALIDATION_REQUIRED);

        // Get all the controlList where validationRequired equals to UPDATED_VALIDATION_REQUIRED
        defaultControlShouldNotBeFound("validationRequired.in=" + UPDATED_VALIDATION_REQUIRED);
    }

    @Test
    @Transactional
    public void getAllControlsByValidationRequiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);

        // Get all the controlList where validationRequired is not null
        defaultControlShouldBeFound("validationRequired.specified=true");

        // Get all the controlList where validationRequired is null
        defaultControlShouldNotBeFound("validationRequired.specified=false");
    }

    @Test
    @Transactional
    public void getAllControlsByTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        ControlType type = control.getType();
        controlRepository.saveAndFlush(control);
        Long typeId = type.getId();

        // Get all the controlList where type equals to typeId
        defaultControlShouldBeFound("typeId.equals=" + typeId);

        // Get all the controlList where type equals to typeId + 1
        defaultControlShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }


    @Test
    @Transactional
    public void getAllControlsByMaturityIsEqualToSomething() throws Exception {
        // Get already existing entity
        ControlMaturity maturity = control.getMaturity();
        controlRepository.saveAndFlush(control);
        Long maturityId = maturity.getId();

        // Get all the controlList where maturity equals to maturityId
        defaultControlShouldBeFound("maturityId.equals=" + maturityId);

        // Get all the controlList where maturity equals to maturityId + 1
        defaultControlShouldNotBeFound("maturityId.equals=" + (maturityId + 1));
    }


    @Test
    @Transactional
    public void getAllControlsByRiskIsEqualToSomething() throws Exception {
        // Initialize the database
        controlRepository.saveAndFlush(control);
        Risk risk = RiskResourceIT.createEntity(em);
        em.persist(risk);
        em.flush();
        control.setRisk(risk);
        controlRepository.saveAndFlush(control);
        Long riskId = risk.getId();

        // Get all the controlList where risk equals to riskId
        defaultControlShouldBeFound("riskId.equals=" + riskId);

        // Get all the controlList where risk equals to riskId + 1
        defaultControlShouldNotBeFound("riskId.equals=" + (riskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultControlShouldBeFound(String filter) throws Exception {
        restControlMockMvc.perform(get("/api/controls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(control.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].validationRequired").value(hasItem(DEFAULT_VALIDATION_REQUIRED.booleanValue())));

        // Check, that the count call also returns 1
        restControlMockMvc.perform(get("/api/controls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultControlShouldNotBeFound(String filter) throws Exception {
        restControlMockMvc.perform(get("/api/controls?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restControlMockMvc.perform(get("/api/controls/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingControl() throws Exception {
        // Get the control
        restControlMockMvc.perform(get("/api/controls/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateControl() throws Exception {
        // Initialize the database
        controlService.save(control);

        int databaseSizeBeforeUpdate = controlRepository.findAll().size();

        // Update the control
        Control updatedControl = controlRepository.findById(control.getId()).get();
        // Disconnect from session so that the updates on updatedControl are not directly saved in db
        em.detach(updatedControl);
        updatedControl
            .description(UPDATED_DESCRIPTION)
            .validationRequired(UPDATED_VALIDATION_REQUIRED);

        restControlMockMvc.perform(put("/api/controls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedControl)))
            .andExpect(status().isOk());

        // Validate the Control in the database
        List<Control> controlList = controlRepository.findAll();
        assertThat(controlList).hasSize(databaseSizeBeforeUpdate);
        Control testControl = controlList.get(controlList.size() - 1);
        assertThat(testControl.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testControl.isValidationRequired()).isEqualTo(UPDATED_VALIDATION_REQUIRED);
    }

    @Test
    @Transactional
    public void updateNonExistingControl() throws Exception {
        int databaseSizeBeforeUpdate = controlRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControlMockMvc.perform(put("/api/controls")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(control)))
            .andExpect(status().isBadRequest());

        // Validate the Control in the database
        List<Control> controlList = controlRepository.findAll();
        assertThat(controlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteControl() throws Exception {
        // Initialize the database
        controlService.save(control);

        int databaseSizeBeforeDelete = controlRepository.findAll().size();

        // Delete the control
        restControlMockMvc.perform(delete("/api/controls/{id}", control.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Control> controlList = controlRepository.findAll();
        assertThat(controlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
