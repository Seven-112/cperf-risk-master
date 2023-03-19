package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.Risk;
import com.mshz.domain.Control;
import com.mshz.domain.RiskType;
import com.mshz.repository.RiskRepository;
import com.mshz.service.RiskService;
import com.mshz.service.dto.RiskCriteria;
import com.mshz.service.RiskQueryService;

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
 * Integration tests for the {@link RiskResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RiskResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Float DEFAULT_PROBABILITY = 1F;
    private static final Float UPDATED_PROBABILITY = 2F;
    private static final Float SMALLER_PROBABILITY = 1F - 1F;

    private static final Float DEFAULT_GRAVITY = 1F;
    private static final Float UPDATED_GRAVITY = 2F;
    private static final Float SMALLER_GRAVITY = 1F - 1F;

    private static final String DEFAULT_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_CAUSE = "BBBBBBBBBB";

    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private RiskService riskService;

    @Autowired
    private RiskQueryService riskQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRiskMockMvc;

    private Risk risk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Risk createEntity(EntityManager em) {
        Risk risk = new Risk()
            .label(DEFAULT_LABEL)
            .probability(DEFAULT_PROBABILITY)
            .gravity(DEFAULT_GRAVITY)
            .cause(DEFAULT_CAUSE);
        // Add required entity
        RiskType riskType;
        if (TestUtil.findAll(em, RiskType.class).isEmpty()) {
            riskType = RiskTypeResourceIT.createEntity(em);
            em.persist(riskType);
            em.flush();
        } else {
            riskType = TestUtil.findAll(em, RiskType.class).get(0);
        }
        risk.setType(riskType);
        return risk;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Risk createUpdatedEntity(EntityManager em) {
        Risk risk = new Risk()
            .label(UPDATED_LABEL)
            .probability(UPDATED_PROBABILITY)
            .gravity(UPDATED_GRAVITY)
            .cause(UPDATED_CAUSE);
        // Add required entity
        RiskType riskType;
        if (TestUtil.findAll(em, RiskType.class).isEmpty()) {
            riskType = RiskTypeResourceIT.createUpdatedEntity(em);
            em.persist(riskType);
            em.flush();
        } else {
            riskType = TestUtil.findAll(em, RiskType.class).get(0);
        }
        risk.setType(riskType);
        return risk;
    }

    @BeforeEach
    public void initTest() {
        risk = createEntity(em);
    }

    @Test
    @Transactional
    public void createRisk() throws Exception {
        int databaseSizeBeforeCreate = riskRepository.findAll().size();
        // Create the Risk
        restRiskMockMvc.perform(post("/api/risks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isCreated());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeCreate + 1);
        Risk testRisk = riskList.get(riskList.size() - 1);
        assertThat(testRisk.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testRisk.getProbability()).isEqualTo(DEFAULT_PROBABILITY);
        assertThat(testRisk.getGravity()).isEqualTo(DEFAULT_GRAVITY);
        assertThat(testRisk.getCause()).isEqualTo(DEFAULT_CAUSE);
    }

    @Test
    @Transactional
    public void createRiskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = riskRepository.findAll().size();

        // Create the Risk with an existing ID
        risk.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRiskMockMvc.perform(post("/api/risks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isBadRequest());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskRepository.findAll().size();
        // set the field null
        risk.setLabel(null);

        // Create the Risk, which fails.


        restRiskMockMvc.perform(post("/api/risks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isBadRequest());

        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRisks() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList
        restRiskMockMvc.perform(get("/api/risks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(risk.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].probability").value(hasItem(DEFAULT_PROBABILITY.doubleValue())))
            .andExpect(jsonPath("$.[*].gravity").value(hasItem(DEFAULT_GRAVITY.doubleValue())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE)));
    }
    
    @Test
    @Transactional
    public void getRisk() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get the risk
        restRiskMockMvc.perform(get("/api/risks/{id}", risk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(risk.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.probability").value(DEFAULT_PROBABILITY.doubleValue()))
            .andExpect(jsonPath("$.gravity").value(DEFAULT_GRAVITY.doubleValue()))
            .andExpect(jsonPath("$.cause").value(DEFAULT_CAUSE));
    }


    @Test
    @Transactional
    public void getRisksByIdFiltering() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        Long id = risk.getId();

        defaultRiskShouldBeFound("id.equals=" + id);
        defaultRiskShouldNotBeFound("id.notEquals=" + id);

        defaultRiskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRiskShouldNotBeFound("id.greaterThan=" + id);

        defaultRiskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRiskShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRisksByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where label equals to DEFAULT_LABEL
        defaultRiskShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the riskList where label equals to UPDATED_LABEL
        defaultRiskShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllRisksByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where label not equals to DEFAULT_LABEL
        defaultRiskShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the riskList where label not equals to UPDATED_LABEL
        defaultRiskShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllRisksByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultRiskShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the riskList where label equals to UPDATED_LABEL
        defaultRiskShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllRisksByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where label is not null
        defaultRiskShouldBeFound("label.specified=true");

        // Get all the riskList where label is null
        defaultRiskShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllRisksByLabelContainsSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where label contains DEFAULT_LABEL
        defaultRiskShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the riskList where label contains UPDATED_LABEL
        defaultRiskShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllRisksByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where label does not contain DEFAULT_LABEL
        defaultRiskShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the riskList where label does not contain UPDATED_LABEL
        defaultRiskShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllRisksByProbabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where probability equals to DEFAULT_PROBABILITY
        defaultRiskShouldBeFound("probability.equals=" + DEFAULT_PROBABILITY);

        // Get all the riskList where probability equals to UPDATED_PROBABILITY
        defaultRiskShouldNotBeFound("probability.equals=" + UPDATED_PROBABILITY);
    }

    @Test
    @Transactional
    public void getAllRisksByProbabilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where probability not equals to DEFAULT_PROBABILITY
        defaultRiskShouldNotBeFound("probability.notEquals=" + DEFAULT_PROBABILITY);

        // Get all the riskList where probability not equals to UPDATED_PROBABILITY
        defaultRiskShouldBeFound("probability.notEquals=" + UPDATED_PROBABILITY);
    }

    @Test
    @Transactional
    public void getAllRisksByProbabilityIsInShouldWork() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where probability in DEFAULT_PROBABILITY or UPDATED_PROBABILITY
        defaultRiskShouldBeFound("probability.in=" + DEFAULT_PROBABILITY + "," + UPDATED_PROBABILITY);

        // Get all the riskList where probability equals to UPDATED_PROBABILITY
        defaultRiskShouldNotBeFound("probability.in=" + UPDATED_PROBABILITY);
    }

    @Test
    @Transactional
    public void getAllRisksByProbabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where probability is not null
        defaultRiskShouldBeFound("probability.specified=true");

        // Get all the riskList where probability is null
        defaultRiskShouldNotBeFound("probability.specified=false");
    }

    @Test
    @Transactional
    public void getAllRisksByProbabilityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where probability is greater than or equal to DEFAULT_PROBABILITY
        defaultRiskShouldBeFound("probability.greaterThanOrEqual=" + DEFAULT_PROBABILITY);

        // Get all the riskList where probability is greater than or equal to UPDATED_PROBABILITY
        defaultRiskShouldNotBeFound("probability.greaterThanOrEqual=" + UPDATED_PROBABILITY);
    }

    @Test
    @Transactional
    public void getAllRisksByProbabilityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where probability is less than or equal to DEFAULT_PROBABILITY
        defaultRiskShouldBeFound("probability.lessThanOrEqual=" + DEFAULT_PROBABILITY);

        // Get all the riskList where probability is less than or equal to SMALLER_PROBABILITY
        defaultRiskShouldNotBeFound("probability.lessThanOrEqual=" + SMALLER_PROBABILITY);
    }

    @Test
    @Transactional
    public void getAllRisksByProbabilityIsLessThanSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where probability is less than DEFAULT_PROBABILITY
        defaultRiskShouldNotBeFound("probability.lessThan=" + DEFAULT_PROBABILITY);

        // Get all the riskList where probability is less than UPDATED_PROBABILITY
        defaultRiskShouldBeFound("probability.lessThan=" + UPDATED_PROBABILITY);
    }

    @Test
    @Transactional
    public void getAllRisksByProbabilityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where probability is greater than DEFAULT_PROBABILITY
        defaultRiskShouldNotBeFound("probability.greaterThan=" + DEFAULT_PROBABILITY);

        // Get all the riskList where probability is greater than SMALLER_PROBABILITY
        defaultRiskShouldBeFound("probability.greaterThan=" + SMALLER_PROBABILITY);
    }


    @Test
    @Transactional
    public void getAllRisksByGravityIsEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where gravity equals to DEFAULT_GRAVITY
        defaultRiskShouldBeFound("gravity.equals=" + DEFAULT_GRAVITY);

        // Get all the riskList where gravity equals to UPDATED_GRAVITY
        defaultRiskShouldNotBeFound("gravity.equals=" + UPDATED_GRAVITY);
    }

    @Test
    @Transactional
    public void getAllRisksByGravityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where gravity not equals to DEFAULT_GRAVITY
        defaultRiskShouldNotBeFound("gravity.notEquals=" + DEFAULT_GRAVITY);

        // Get all the riskList where gravity not equals to UPDATED_GRAVITY
        defaultRiskShouldBeFound("gravity.notEquals=" + UPDATED_GRAVITY);
    }

    @Test
    @Transactional
    public void getAllRisksByGravityIsInShouldWork() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where gravity in DEFAULT_GRAVITY or UPDATED_GRAVITY
        defaultRiskShouldBeFound("gravity.in=" + DEFAULT_GRAVITY + "," + UPDATED_GRAVITY);

        // Get all the riskList where gravity equals to UPDATED_GRAVITY
        defaultRiskShouldNotBeFound("gravity.in=" + UPDATED_GRAVITY);
    }

    @Test
    @Transactional
    public void getAllRisksByGravityIsNullOrNotNull() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where gravity is not null
        defaultRiskShouldBeFound("gravity.specified=true");

        // Get all the riskList where gravity is null
        defaultRiskShouldNotBeFound("gravity.specified=false");
    }

    @Test
    @Transactional
    public void getAllRisksByGravityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where gravity is greater than or equal to DEFAULT_GRAVITY
        defaultRiskShouldBeFound("gravity.greaterThanOrEqual=" + DEFAULT_GRAVITY);

        // Get all the riskList where gravity is greater than or equal to UPDATED_GRAVITY
        defaultRiskShouldNotBeFound("gravity.greaterThanOrEqual=" + UPDATED_GRAVITY);
    }

    @Test
    @Transactional
    public void getAllRisksByGravityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where gravity is less than or equal to DEFAULT_GRAVITY
        defaultRiskShouldBeFound("gravity.lessThanOrEqual=" + DEFAULT_GRAVITY);

        // Get all the riskList where gravity is less than or equal to SMALLER_GRAVITY
        defaultRiskShouldNotBeFound("gravity.lessThanOrEqual=" + SMALLER_GRAVITY);
    }

    @Test
    @Transactional
    public void getAllRisksByGravityIsLessThanSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where gravity is less than DEFAULT_GRAVITY
        defaultRiskShouldNotBeFound("gravity.lessThan=" + DEFAULT_GRAVITY);

        // Get all the riskList where gravity is less than UPDATED_GRAVITY
        defaultRiskShouldBeFound("gravity.lessThan=" + UPDATED_GRAVITY);
    }

    @Test
    @Transactional
    public void getAllRisksByGravityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where gravity is greater than DEFAULT_GRAVITY
        defaultRiskShouldNotBeFound("gravity.greaterThan=" + DEFAULT_GRAVITY);

        // Get all the riskList where gravity is greater than SMALLER_GRAVITY
        defaultRiskShouldBeFound("gravity.greaterThan=" + SMALLER_GRAVITY);
    }


    @Test
    @Transactional
    public void getAllRisksByCauseIsEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where cause equals to DEFAULT_CAUSE
        defaultRiskShouldBeFound("cause.equals=" + DEFAULT_CAUSE);

        // Get all the riskList where cause equals to UPDATED_CAUSE
        defaultRiskShouldNotBeFound("cause.equals=" + UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void getAllRisksByCauseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where cause not equals to DEFAULT_CAUSE
        defaultRiskShouldNotBeFound("cause.notEquals=" + DEFAULT_CAUSE);

        // Get all the riskList where cause not equals to UPDATED_CAUSE
        defaultRiskShouldBeFound("cause.notEquals=" + UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void getAllRisksByCauseIsInShouldWork() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where cause in DEFAULT_CAUSE or UPDATED_CAUSE
        defaultRiskShouldBeFound("cause.in=" + DEFAULT_CAUSE + "," + UPDATED_CAUSE);

        // Get all the riskList where cause equals to UPDATED_CAUSE
        defaultRiskShouldNotBeFound("cause.in=" + UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void getAllRisksByCauseIsNullOrNotNull() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where cause is not null
        defaultRiskShouldBeFound("cause.specified=true");

        // Get all the riskList where cause is null
        defaultRiskShouldNotBeFound("cause.specified=false");
    }
                @Test
    @Transactional
    public void getAllRisksByCauseContainsSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where cause contains DEFAULT_CAUSE
        defaultRiskShouldBeFound("cause.contains=" + DEFAULT_CAUSE);

        // Get all the riskList where cause contains UPDATED_CAUSE
        defaultRiskShouldNotBeFound("cause.contains=" + UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void getAllRisksByCauseNotContainsSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);

        // Get all the riskList where cause does not contain DEFAULT_CAUSE
        defaultRiskShouldNotBeFound("cause.doesNotContain=" + DEFAULT_CAUSE);

        // Get all the riskList where cause does not contain UPDATED_CAUSE
        defaultRiskShouldBeFound("cause.doesNotContain=" + UPDATED_CAUSE);
    }


    @Test
    @Transactional
    public void getAllRisksByControlsIsEqualToSomething() throws Exception {
        // Initialize the database
        riskRepository.saveAndFlush(risk);
        Control controls = ControlResourceIT.createEntity(em);
        em.persist(controls);
        em.flush();
        risk.addControls(controls);
        riskRepository.saveAndFlush(risk);
        Long controlsId = controls.getId();

        // Get all the riskList where controls equals to controlsId
        defaultRiskShouldBeFound("controlsId.equals=" + controlsId);

        // Get all the riskList where controls equals to controlsId + 1
        defaultRiskShouldNotBeFound("controlsId.equals=" + (controlsId + 1));
    }


    @Test
    @Transactional
    public void getAllRisksByTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        RiskType type = risk.getType();
        riskRepository.saveAndFlush(risk);
        Long typeId = type.getId();

        // Get all the riskList where type equals to typeId
        defaultRiskShouldBeFound("typeId.equals=" + typeId);

        // Get all the riskList where type equals to typeId + 1
        defaultRiskShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRiskShouldBeFound(String filter) throws Exception {
        restRiskMockMvc.perform(get("/api/risks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(risk.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].probability").value(hasItem(DEFAULT_PROBABILITY.doubleValue())))
            .andExpect(jsonPath("$.[*].gravity").value(hasItem(DEFAULT_GRAVITY.doubleValue())))
            .andExpect(jsonPath("$.[*].cause").value(hasItem(DEFAULT_CAUSE)));

        // Check, that the count call also returns 1
        restRiskMockMvc.perform(get("/api/risks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRiskShouldNotBeFound(String filter) throws Exception {
        restRiskMockMvc.perform(get("/api/risks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRiskMockMvc.perform(get("/api/risks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRisk() throws Exception {
        // Get the risk
        restRiskMockMvc.perform(get("/api/risks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRisk() throws Exception {
        // Initialize the database
        riskService.save(risk);

        int databaseSizeBeforeUpdate = riskRepository.findAll().size();

        // Update the risk
        Risk updatedRisk = riskRepository.findById(risk.getId()).get();
        // Disconnect from session so that the updates on updatedRisk are not directly saved in db
        em.detach(updatedRisk);
        updatedRisk
            .label(UPDATED_LABEL)
            .probability(UPDATED_PROBABILITY)
            .gravity(UPDATED_GRAVITY)
            .cause(UPDATED_CAUSE);

        restRiskMockMvc.perform(put("/api/risks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRisk)))
            .andExpect(status().isOk());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
        Risk testRisk = riskList.get(riskList.size() - 1);
        assertThat(testRisk.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testRisk.getProbability()).isEqualTo(UPDATED_PROBABILITY);
        assertThat(testRisk.getGravity()).isEqualTo(UPDATED_GRAVITY);
        assertThat(testRisk.getCause()).isEqualTo(UPDATED_CAUSE);
    }

    @Test
    @Transactional
    public void updateNonExistingRisk() throws Exception {
        int databaseSizeBeforeUpdate = riskRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiskMockMvc.perform(put("/api/risks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(risk)))
            .andExpect(status().isBadRequest());

        // Validate the Risk in the database
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRisk() throws Exception {
        // Initialize the database
        riskService.save(risk);

        int databaseSizeBeforeDelete = riskRepository.findAll().size();

        // Delete the risk
        restRiskMockMvc.perform(delete("/api/risks/{id}", risk.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Risk> riskList = riskRepository.findAll();
        assertThat(riskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
