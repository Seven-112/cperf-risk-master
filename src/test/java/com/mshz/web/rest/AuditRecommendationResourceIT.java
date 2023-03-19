package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.AuditRecommendation;
import com.mshz.repository.AuditRecommendationRepository;
import com.mshz.service.AuditRecommendationService;
import com.mshz.service.dto.AuditRecommendationCriteria;
import com.mshz.service.AuditRecommendationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mshz.domain.enumeration.AuditStatus;
/**
 * Integration tests for the {@link AuditRecommendationResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditRecommendationResourceIT {

    private static final Long DEFAULT_AUDITOR_ID = 1L;
    private static final Long UPDATED_AUDITOR_ID = 2L;
    private static final Long SMALLER_AUDITOR_ID = 1L - 1L;

    private static final String DEFAULT_AUDITOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AUDITOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AUDITOR_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_AUDITOR_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_AUDIT_ID = 1L;
    private static final Long UPDATED_AUDIT_ID = 2L;
    private static final Long SMALLER_AUDIT_ID = 1L - 1L;

    private static final AuditStatus DEFAULT_STATUS = AuditStatus.INITIAL;
    private static final AuditStatus UPDATED_STATUS = AuditStatus.STARTED;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_RESPONSABLE_ID = 1L;
    private static final Long UPDATED_RESPONSABLE_ID = 2L;
    private static final Long SMALLER_RESPONSABLE_ID = 1L - 1L;

    private static final String DEFAULT_RESPONSABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_LIMIT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_LIMIT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EDIT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EDIT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXECUTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXECUTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;
    private static final Long SMALLER_ENTITY_ID = 1L - 1L;

    private static final String DEFAULT_ENTIY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTIY_NAME = "BBBBBBBBBB";

    @Autowired
    private AuditRecommendationRepository auditRecommendationRepository;

    @Autowired
    private AuditRecommendationService auditRecommendationService;

    @Autowired
    private AuditRecommendationQueryService auditRecommendationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditRecommendationMockMvc;

    private AuditRecommendation auditRecommendation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditRecommendation createEntity(EntityManager em) {
        AuditRecommendation auditRecommendation = new AuditRecommendation()
            .auditorId(DEFAULT_AUDITOR_ID)
            .auditorName(DEFAULT_AUDITOR_NAME)
            .auditorEmail(DEFAULT_AUDITOR_EMAIL)
            .auditId(DEFAULT_AUDIT_ID)
            .status(DEFAULT_STATUS)
            .content(DEFAULT_CONTENT)
            .responsableId(DEFAULT_RESPONSABLE_ID)
            .responsableName(DEFAULT_RESPONSABLE_NAME)
            .responsableEmail(DEFAULT_RESPONSABLE_EMAIL)
            .dateLimit(DEFAULT_DATE_LIMIT)
            .editAt(DEFAULT_EDIT_AT)
            .executedAt(DEFAULT_EXECUTED_AT)
            .entityId(DEFAULT_ENTITY_ID)
            .entiyName(DEFAULT_ENTIY_NAME);
        return auditRecommendation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditRecommendation createUpdatedEntity(EntityManager em) {
        AuditRecommendation auditRecommendation = new AuditRecommendation()
            .auditorId(UPDATED_AUDITOR_ID)
            .auditorName(UPDATED_AUDITOR_NAME)
            .auditorEmail(UPDATED_AUDITOR_EMAIL)
            .auditId(UPDATED_AUDIT_ID)
            .status(UPDATED_STATUS)
            .content(UPDATED_CONTENT)
            .responsableId(UPDATED_RESPONSABLE_ID)
            .responsableName(UPDATED_RESPONSABLE_NAME)
            .responsableEmail(UPDATED_RESPONSABLE_EMAIL)
            .dateLimit(UPDATED_DATE_LIMIT)
            .editAt(UPDATED_EDIT_AT)
            .executedAt(UPDATED_EXECUTED_AT)
            .entityId(UPDATED_ENTITY_ID)
            .entiyName(UPDATED_ENTIY_NAME);
        return auditRecommendation;
    }

    @BeforeEach
    public void initTest() {
        auditRecommendation = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditRecommendation() throws Exception {
        int databaseSizeBeforeCreate = auditRecommendationRepository.findAll().size();
        // Create the AuditRecommendation
        restAuditRecommendationMockMvc.perform(post("/api/audit-recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecommendation)))
            .andExpect(status().isCreated());

        // Validate the AuditRecommendation in the database
        List<AuditRecommendation> auditRecommendationList = auditRecommendationRepository.findAll();
        assertThat(auditRecommendationList).hasSize(databaseSizeBeforeCreate + 1);
        AuditRecommendation testAuditRecommendation = auditRecommendationList.get(auditRecommendationList.size() - 1);
        assertThat(testAuditRecommendation.getAuditorId()).isEqualTo(DEFAULT_AUDITOR_ID);
        assertThat(testAuditRecommendation.getAuditorName()).isEqualTo(DEFAULT_AUDITOR_NAME);
        assertThat(testAuditRecommendation.getAuditorEmail()).isEqualTo(DEFAULT_AUDITOR_EMAIL);
        assertThat(testAuditRecommendation.getAuditId()).isEqualTo(DEFAULT_AUDIT_ID);
        assertThat(testAuditRecommendation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAuditRecommendation.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAuditRecommendation.getResponsableId()).isEqualTo(DEFAULT_RESPONSABLE_ID);
        assertThat(testAuditRecommendation.getResponsableName()).isEqualTo(DEFAULT_RESPONSABLE_NAME);
        assertThat(testAuditRecommendation.getResponsableEmail()).isEqualTo(DEFAULT_RESPONSABLE_EMAIL);
        assertThat(testAuditRecommendation.getDateLimit()).isEqualTo(DEFAULT_DATE_LIMIT);
        assertThat(testAuditRecommendation.getEditAt()).isEqualTo(DEFAULT_EDIT_AT);
        assertThat(testAuditRecommendation.getExecutedAt()).isEqualTo(DEFAULT_EXECUTED_AT);
        assertThat(testAuditRecommendation.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testAuditRecommendation.getEntiyName()).isEqualTo(DEFAULT_ENTIY_NAME);
    }

    @Test
    @Transactional
    public void createAuditRecommendationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditRecommendationRepository.findAll().size();

        // Create the AuditRecommendation with an existing ID
        auditRecommendation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditRecommendationMockMvc.perform(post("/api/audit-recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecommendation)))
            .andExpect(status().isBadRequest());

        // Validate the AuditRecommendation in the database
        List<AuditRecommendation> auditRecommendationList = auditRecommendationRepository.findAll();
        assertThat(auditRecommendationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendations() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList
        restAuditRecommendationMockMvc.perform(get("/api/audit-recommendations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditRecommendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].auditorId").value(hasItem(DEFAULT_AUDITOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].auditorName").value(hasItem(DEFAULT_AUDITOR_NAME)))
            .andExpect(jsonPath("$.[*].auditorEmail").value(hasItem(DEFAULT_AUDITOR_EMAIL)))
            .andExpect(jsonPath("$.[*].auditId").value(hasItem(DEFAULT_AUDIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].responsableId").value(hasItem(DEFAULT_RESPONSABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].responsableName").value(hasItem(DEFAULT_RESPONSABLE_NAME)))
            .andExpect(jsonPath("$.[*].responsableEmail").value(hasItem(DEFAULT_RESPONSABLE_EMAIL)))
            .andExpect(jsonPath("$.[*].dateLimit").value(hasItem(DEFAULT_DATE_LIMIT.toString())))
            .andExpect(jsonPath("$.[*].editAt").value(hasItem(DEFAULT_EDIT_AT.toString())))
            .andExpect(jsonPath("$.[*].executedAt").value(hasItem(DEFAULT_EXECUTED_AT.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].entiyName").value(hasItem(DEFAULT_ENTIY_NAME)));
    }
    
    @Test
    @Transactional
    public void getAuditRecommendation() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get the auditRecommendation
        restAuditRecommendationMockMvc.perform(get("/api/audit-recommendations/{id}", auditRecommendation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditRecommendation.getId().intValue()))
            .andExpect(jsonPath("$.auditorId").value(DEFAULT_AUDITOR_ID.intValue()))
            .andExpect(jsonPath("$.auditorName").value(DEFAULT_AUDITOR_NAME))
            .andExpect(jsonPath("$.auditorEmail").value(DEFAULT_AUDITOR_EMAIL))
            .andExpect(jsonPath("$.auditId").value(DEFAULT_AUDIT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.responsableId").value(DEFAULT_RESPONSABLE_ID.intValue()))
            .andExpect(jsonPath("$.responsableName").value(DEFAULT_RESPONSABLE_NAME))
            .andExpect(jsonPath("$.responsableEmail").value(DEFAULT_RESPONSABLE_EMAIL))
            .andExpect(jsonPath("$.dateLimit").value(DEFAULT_DATE_LIMIT.toString()))
            .andExpect(jsonPath("$.editAt").value(DEFAULT_EDIT_AT.toString()))
            .andExpect(jsonPath("$.executedAt").value(DEFAULT_EXECUTED_AT.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.entiyName").value(DEFAULT_ENTIY_NAME));
    }


    @Test
    @Transactional
    public void getAuditRecommendationsByIdFiltering() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        Long id = auditRecommendation.getId();

        defaultAuditRecommendationShouldBeFound("id.equals=" + id);
        defaultAuditRecommendationShouldNotBeFound("id.notEquals=" + id);

        defaultAuditRecommendationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditRecommendationShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditRecommendationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditRecommendationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorId equals to DEFAULT_AUDITOR_ID
        defaultAuditRecommendationShouldBeFound("auditorId.equals=" + DEFAULT_AUDITOR_ID);

        // Get all the auditRecommendationList where auditorId equals to UPDATED_AUDITOR_ID
        defaultAuditRecommendationShouldNotBeFound("auditorId.equals=" + UPDATED_AUDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorId not equals to DEFAULT_AUDITOR_ID
        defaultAuditRecommendationShouldNotBeFound("auditorId.notEquals=" + DEFAULT_AUDITOR_ID);

        // Get all the auditRecommendationList where auditorId not equals to UPDATED_AUDITOR_ID
        defaultAuditRecommendationShouldBeFound("auditorId.notEquals=" + UPDATED_AUDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorId in DEFAULT_AUDITOR_ID or UPDATED_AUDITOR_ID
        defaultAuditRecommendationShouldBeFound("auditorId.in=" + DEFAULT_AUDITOR_ID + "," + UPDATED_AUDITOR_ID);

        // Get all the auditRecommendationList where auditorId equals to UPDATED_AUDITOR_ID
        defaultAuditRecommendationShouldNotBeFound("auditorId.in=" + UPDATED_AUDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorId is not null
        defaultAuditRecommendationShouldBeFound("auditorId.specified=true");

        // Get all the auditRecommendationList where auditorId is null
        defaultAuditRecommendationShouldNotBeFound("auditorId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorId is greater than or equal to DEFAULT_AUDITOR_ID
        defaultAuditRecommendationShouldBeFound("auditorId.greaterThanOrEqual=" + DEFAULT_AUDITOR_ID);

        // Get all the auditRecommendationList where auditorId is greater than or equal to UPDATED_AUDITOR_ID
        defaultAuditRecommendationShouldNotBeFound("auditorId.greaterThanOrEqual=" + UPDATED_AUDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorId is less than or equal to DEFAULT_AUDITOR_ID
        defaultAuditRecommendationShouldBeFound("auditorId.lessThanOrEqual=" + DEFAULT_AUDITOR_ID);

        // Get all the auditRecommendationList where auditorId is less than or equal to SMALLER_AUDITOR_ID
        defaultAuditRecommendationShouldNotBeFound("auditorId.lessThanOrEqual=" + SMALLER_AUDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorId is less than DEFAULT_AUDITOR_ID
        defaultAuditRecommendationShouldNotBeFound("auditorId.lessThan=" + DEFAULT_AUDITOR_ID);

        // Get all the auditRecommendationList where auditorId is less than UPDATED_AUDITOR_ID
        defaultAuditRecommendationShouldBeFound("auditorId.lessThan=" + UPDATED_AUDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorId is greater than DEFAULT_AUDITOR_ID
        defaultAuditRecommendationShouldNotBeFound("auditorId.greaterThan=" + DEFAULT_AUDITOR_ID);

        // Get all the auditRecommendationList where auditorId is greater than SMALLER_AUDITOR_ID
        defaultAuditRecommendationShouldBeFound("auditorId.greaterThan=" + SMALLER_AUDITOR_ID);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorName equals to DEFAULT_AUDITOR_NAME
        defaultAuditRecommendationShouldBeFound("auditorName.equals=" + DEFAULT_AUDITOR_NAME);

        // Get all the auditRecommendationList where auditorName equals to UPDATED_AUDITOR_NAME
        defaultAuditRecommendationShouldNotBeFound("auditorName.equals=" + UPDATED_AUDITOR_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorName not equals to DEFAULT_AUDITOR_NAME
        defaultAuditRecommendationShouldNotBeFound("auditorName.notEquals=" + DEFAULT_AUDITOR_NAME);

        // Get all the auditRecommendationList where auditorName not equals to UPDATED_AUDITOR_NAME
        defaultAuditRecommendationShouldBeFound("auditorName.notEquals=" + UPDATED_AUDITOR_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorName in DEFAULT_AUDITOR_NAME or UPDATED_AUDITOR_NAME
        defaultAuditRecommendationShouldBeFound("auditorName.in=" + DEFAULT_AUDITOR_NAME + "," + UPDATED_AUDITOR_NAME);

        // Get all the auditRecommendationList where auditorName equals to UPDATED_AUDITOR_NAME
        defaultAuditRecommendationShouldNotBeFound("auditorName.in=" + UPDATED_AUDITOR_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorName is not null
        defaultAuditRecommendationShouldBeFound("auditorName.specified=true");

        // Get all the auditRecommendationList where auditorName is null
        defaultAuditRecommendationShouldNotBeFound("auditorName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorNameContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorName contains DEFAULT_AUDITOR_NAME
        defaultAuditRecommendationShouldBeFound("auditorName.contains=" + DEFAULT_AUDITOR_NAME);

        // Get all the auditRecommendationList where auditorName contains UPDATED_AUDITOR_NAME
        defaultAuditRecommendationShouldNotBeFound("auditorName.contains=" + UPDATED_AUDITOR_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorName does not contain DEFAULT_AUDITOR_NAME
        defaultAuditRecommendationShouldNotBeFound("auditorName.doesNotContain=" + DEFAULT_AUDITOR_NAME);

        // Get all the auditRecommendationList where auditorName does not contain UPDATED_AUDITOR_NAME
        defaultAuditRecommendationShouldBeFound("auditorName.doesNotContain=" + UPDATED_AUDITOR_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorEmail equals to DEFAULT_AUDITOR_EMAIL
        defaultAuditRecommendationShouldBeFound("auditorEmail.equals=" + DEFAULT_AUDITOR_EMAIL);

        // Get all the auditRecommendationList where auditorEmail equals to UPDATED_AUDITOR_EMAIL
        defaultAuditRecommendationShouldNotBeFound("auditorEmail.equals=" + UPDATED_AUDITOR_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorEmail not equals to DEFAULT_AUDITOR_EMAIL
        defaultAuditRecommendationShouldNotBeFound("auditorEmail.notEquals=" + DEFAULT_AUDITOR_EMAIL);

        // Get all the auditRecommendationList where auditorEmail not equals to UPDATED_AUDITOR_EMAIL
        defaultAuditRecommendationShouldBeFound("auditorEmail.notEquals=" + UPDATED_AUDITOR_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorEmailIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorEmail in DEFAULT_AUDITOR_EMAIL or UPDATED_AUDITOR_EMAIL
        defaultAuditRecommendationShouldBeFound("auditorEmail.in=" + DEFAULT_AUDITOR_EMAIL + "," + UPDATED_AUDITOR_EMAIL);

        // Get all the auditRecommendationList where auditorEmail equals to UPDATED_AUDITOR_EMAIL
        defaultAuditRecommendationShouldNotBeFound("auditorEmail.in=" + UPDATED_AUDITOR_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorEmail is not null
        defaultAuditRecommendationShouldBeFound("auditorEmail.specified=true");

        // Get all the auditRecommendationList where auditorEmail is null
        defaultAuditRecommendationShouldNotBeFound("auditorEmail.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorEmailContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorEmail contains DEFAULT_AUDITOR_EMAIL
        defaultAuditRecommendationShouldBeFound("auditorEmail.contains=" + DEFAULT_AUDITOR_EMAIL);

        // Get all the auditRecommendationList where auditorEmail contains UPDATED_AUDITOR_EMAIL
        defaultAuditRecommendationShouldNotBeFound("auditorEmail.contains=" + UPDATED_AUDITOR_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditorEmailNotContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditorEmail does not contain DEFAULT_AUDITOR_EMAIL
        defaultAuditRecommendationShouldNotBeFound("auditorEmail.doesNotContain=" + DEFAULT_AUDITOR_EMAIL);

        // Get all the auditRecommendationList where auditorEmail does not contain UPDATED_AUDITOR_EMAIL
        defaultAuditRecommendationShouldBeFound("auditorEmail.doesNotContain=" + UPDATED_AUDITOR_EMAIL);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditId equals to DEFAULT_AUDIT_ID
        defaultAuditRecommendationShouldBeFound("auditId.equals=" + DEFAULT_AUDIT_ID);

        // Get all the auditRecommendationList where auditId equals to UPDATED_AUDIT_ID
        defaultAuditRecommendationShouldNotBeFound("auditId.equals=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditId not equals to DEFAULT_AUDIT_ID
        defaultAuditRecommendationShouldNotBeFound("auditId.notEquals=" + DEFAULT_AUDIT_ID);

        // Get all the auditRecommendationList where auditId not equals to UPDATED_AUDIT_ID
        defaultAuditRecommendationShouldBeFound("auditId.notEquals=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditId in DEFAULT_AUDIT_ID or UPDATED_AUDIT_ID
        defaultAuditRecommendationShouldBeFound("auditId.in=" + DEFAULT_AUDIT_ID + "," + UPDATED_AUDIT_ID);

        // Get all the auditRecommendationList where auditId equals to UPDATED_AUDIT_ID
        defaultAuditRecommendationShouldNotBeFound("auditId.in=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditId is not null
        defaultAuditRecommendationShouldBeFound("auditId.specified=true");

        // Get all the auditRecommendationList where auditId is null
        defaultAuditRecommendationShouldNotBeFound("auditId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditId is greater than or equal to DEFAULT_AUDIT_ID
        defaultAuditRecommendationShouldBeFound("auditId.greaterThanOrEqual=" + DEFAULT_AUDIT_ID);

        // Get all the auditRecommendationList where auditId is greater than or equal to UPDATED_AUDIT_ID
        defaultAuditRecommendationShouldNotBeFound("auditId.greaterThanOrEqual=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditId is less than or equal to DEFAULT_AUDIT_ID
        defaultAuditRecommendationShouldBeFound("auditId.lessThanOrEqual=" + DEFAULT_AUDIT_ID);

        // Get all the auditRecommendationList where auditId is less than or equal to SMALLER_AUDIT_ID
        defaultAuditRecommendationShouldNotBeFound("auditId.lessThanOrEqual=" + SMALLER_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditId is less than DEFAULT_AUDIT_ID
        defaultAuditRecommendationShouldNotBeFound("auditId.lessThan=" + DEFAULT_AUDIT_ID);

        // Get all the auditRecommendationList where auditId is less than UPDATED_AUDIT_ID
        defaultAuditRecommendationShouldBeFound("auditId.lessThan=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByAuditIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where auditId is greater than DEFAULT_AUDIT_ID
        defaultAuditRecommendationShouldNotBeFound("auditId.greaterThan=" + DEFAULT_AUDIT_ID);

        // Get all the auditRecommendationList where auditId is greater than SMALLER_AUDIT_ID
        defaultAuditRecommendationShouldBeFound("auditId.greaterThan=" + SMALLER_AUDIT_ID);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where status equals to DEFAULT_STATUS
        defaultAuditRecommendationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the auditRecommendationList where status equals to UPDATED_STATUS
        defaultAuditRecommendationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where status not equals to DEFAULT_STATUS
        defaultAuditRecommendationShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the auditRecommendationList where status not equals to UPDATED_STATUS
        defaultAuditRecommendationShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAuditRecommendationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the auditRecommendationList where status equals to UPDATED_STATUS
        defaultAuditRecommendationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where status is not null
        defaultAuditRecommendationShouldBeFound("status.specified=true");

        // Get all the auditRecommendationList where status is null
        defaultAuditRecommendationShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableId equals to DEFAULT_RESPONSABLE_ID
        defaultAuditRecommendationShouldBeFound("responsableId.equals=" + DEFAULT_RESPONSABLE_ID);

        // Get all the auditRecommendationList where responsableId equals to UPDATED_RESPONSABLE_ID
        defaultAuditRecommendationShouldNotBeFound("responsableId.equals=" + UPDATED_RESPONSABLE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableId not equals to DEFAULT_RESPONSABLE_ID
        defaultAuditRecommendationShouldNotBeFound("responsableId.notEquals=" + DEFAULT_RESPONSABLE_ID);

        // Get all the auditRecommendationList where responsableId not equals to UPDATED_RESPONSABLE_ID
        defaultAuditRecommendationShouldBeFound("responsableId.notEquals=" + UPDATED_RESPONSABLE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableId in DEFAULT_RESPONSABLE_ID or UPDATED_RESPONSABLE_ID
        defaultAuditRecommendationShouldBeFound("responsableId.in=" + DEFAULT_RESPONSABLE_ID + "," + UPDATED_RESPONSABLE_ID);

        // Get all the auditRecommendationList where responsableId equals to UPDATED_RESPONSABLE_ID
        defaultAuditRecommendationShouldNotBeFound("responsableId.in=" + UPDATED_RESPONSABLE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableId is not null
        defaultAuditRecommendationShouldBeFound("responsableId.specified=true");

        // Get all the auditRecommendationList where responsableId is null
        defaultAuditRecommendationShouldNotBeFound("responsableId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableId is greater than or equal to DEFAULT_RESPONSABLE_ID
        defaultAuditRecommendationShouldBeFound("responsableId.greaterThanOrEqual=" + DEFAULT_RESPONSABLE_ID);

        // Get all the auditRecommendationList where responsableId is greater than or equal to UPDATED_RESPONSABLE_ID
        defaultAuditRecommendationShouldNotBeFound("responsableId.greaterThanOrEqual=" + UPDATED_RESPONSABLE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableId is less than or equal to DEFAULT_RESPONSABLE_ID
        defaultAuditRecommendationShouldBeFound("responsableId.lessThanOrEqual=" + DEFAULT_RESPONSABLE_ID);

        // Get all the auditRecommendationList where responsableId is less than or equal to SMALLER_RESPONSABLE_ID
        defaultAuditRecommendationShouldNotBeFound("responsableId.lessThanOrEqual=" + SMALLER_RESPONSABLE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableId is less than DEFAULT_RESPONSABLE_ID
        defaultAuditRecommendationShouldNotBeFound("responsableId.lessThan=" + DEFAULT_RESPONSABLE_ID);

        // Get all the auditRecommendationList where responsableId is less than UPDATED_RESPONSABLE_ID
        defaultAuditRecommendationShouldBeFound("responsableId.lessThan=" + UPDATED_RESPONSABLE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableId is greater than DEFAULT_RESPONSABLE_ID
        defaultAuditRecommendationShouldNotBeFound("responsableId.greaterThan=" + DEFAULT_RESPONSABLE_ID);

        // Get all the auditRecommendationList where responsableId is greater than SMALLER_RESPONSABLE_ID
        defaultAuditRecommendationShouldBeFound("responsableId.greaterThan=" + SMALLER_RESPONSABLE_ID);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableName equals to DEFAULT_RESPONSABLE_NAME
        defaultAuditRecommendationShouldBeFound("responsableName.equals=" + DEFAULT_RESPONSABLE_NAME);

        // Get all the auditRecommendationList where responsableName equals to UPDATED_RESPONSABLE_NAME
        defaultAuditRecommendationShouldNotBeFound("responsableName.equals=" + UPDATED_RESPONSABLE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableName not equals to DEFAULT_RESPONSABLE_NAME
        defaultAuditRecommendationShouldNotBeFound("responsableName.notEquals=" + DEFAULT_RESPONSABLE_NAME);

        // Get all the auditRecommendationList where responsableName not equals to UPDATED_RESPONSABLE_NAME
        defaultAuditRecommendationShouldBeFound("responsableName.notEquals=" + UPDATED_RESPONSABLE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableName in DEFAULT_RESPONSABLE_NAME or UPDATED_RESPONSABLE_NAME
        defaultAuditRecommendationShouldBeFound("responsableName.in=" + DEFAULT_RESPONSABLE_NAME + "," + UPDATED_RESPONSABLE_NAME);

        // Get all the auditRecommendationList where responsableName equals to UPDATED_RESPONSABLE_NAME
        defaultAuditRecommendationShouldNotBeFound("responsableName.in=" + UPDATED_RESPONSABLE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableName is not null
        defaultAuditRecommendationShouldBeFound("responsableName.specified=true");

        // Get all the auditRecommendationList where responsableName is null
        defaultAuditRecommendationShouldNotBeFound("responsableName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableNameContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableName contains DEFAULT_RESPONSABLE_NAME
        defaultAuditRecommendationShouldBeFound("responsableName.contains=" + DEFAULT_RESPONSABLE_NAME);

        // Get all the auditRecommendationList where responsableName contains UPDATED_RESPONSABLE_NAME
        defaultAuditRecommendationShouldNotBeFound("responsableName.contains=" + UPDATED_RESPONSABLE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableName does not contain DEFAULT_RESPONSABLE_NAME
        defaultAuditRecommendationShouldNotBeFound("responsableName.doesNotContain=" + DEFAULT_RESPONSABLE_NAME);

        // Get all the auditRecommendationList where responsableName does not contain UPDATED_RESPONSABLE_NAME
        defaultAuditRecommendationShouldBeFound("responsableName.doesNotContain=" + UPDATED_RESPONSABLE_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableEmail equals to DEFAULT_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldBeFound("responsableEmail.equals=" + DEFAULT_RESPONSABLE_EMAIL);

        // Get all the auditRecommendationList where responsableEmail equals to UPDATED_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldNotBeFound("responsableEmail.equals=" + UPDATED_RESPONSABLE_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableEmail not equals to DEFAULT_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldNotBeFound("responsableEmail.notEquals=" + DEFAULT_RESPONSABLE_EMAIL);

        // Get all the auditRecommendationList where responsableEmail not equals to UPDATED_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldBeFound("responsableEmail.notEquals=" + UPDATED_RESPONSABLE_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableEmailIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableEmail in DEFAULT_RESPONSABLE_EMAIL or UPDATED_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldBeFound("responsableEmail.in=" + DEFAULT_RESPONSABLE_EMAIL + "," + UPDATED_RESPONSABLE_EMAIL);

        // Get all the auditRecommendationList where responsableEmail equals to UPDATED_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldNotBeFound("responsableEmail.in=" + UPDATED_RESPONSABLE_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableEmail is not null
        defaultAuditRecommendationShouldBeFound("responsableEmail.specified=true");

        // Get all the auditRecommendationList where responsableEmail is null
        defaultAuditRecommendationShouldNotBeFound("responsableEmail.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableEmailContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableEmail contains DEFAULT_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldBeFound("responsableEmail.contains=" + DEFAULT_RESPONSABLE_EMAIL);

        // Get all the auditRecommendationList where responsableEmail contains UPDATED_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldNotBeFound("responsableEmail.contains=" + UPDATED_RESPONSABLE_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByResponsableEmailNotContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where responsableEmail does not contain DEFAULT_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldNotBeFound("responsableEmail.doesNotContain=" + DEFAULT_RESPONSABLE_EMAIL);

        // Get all the auditRecommendationList where responsableEmail does not contain UPDATED_RESPONSABLE_EMAIL
        defaultAuditRecommendationShouldBeFound("responsableEmail.doesNotContain=" + UPDATED_RESPONSABLE_EMAIL);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByDateLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where dateLimit equals to DEFAULT_DATE_LIMIT
        defaultAuditRecommendationShouldBeFound("dateLimit.equals=" + DEFAULT_DATE_LIMIT);

        // Get all the auditRecommendationList where dateLimit equals to UPDATED_DATE_LIMIT
        defaultAuditRecommendationShouldNotBeFound("dateLimit.equals=" + UPDATED_DATE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByDateLimitIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where dateLimit not equals to DEFAULT_DATE_LIMIT
        defaultAuditRecommendationShouldNotBeFound("dateLimit.notEquals=" + DEFAULT_DATE_LIMIT);

        // Get all the auditRecommendationList where dateLimit not equals to UPDATED_DATE_LIMIT
        defaultAuditRecommendationShouldBeFound("dateLimit.notEquals=" + UPDATED_DATE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByDateLimitIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where dateLimit in DEFAULT_DATE_LIMIT or UPDATED_DATE_LIMIT
        defaultAuditRecommendationShouldBeFound("dateLimit.in=" + DEFAULT_DATE_LIMIT + "," + UPDATED_DATE_LIMIT);

        // Get all the auditRecommendationList where dateLimit equals to UPDATED_DATE_LIMIT
        defaultAuditRecommendationShouldNotBeFound("dateLimit.in=" + UPDATED_DATE_LIMIT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByDateLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where dateLimit is not null
        defaultAuditRecommendationShouldBeFound("dateLimit.specified=true");

        // Get all the auditRecommendationList where dateLimit is null
        defaultAuditRecommendationShouldNotBeFound("dateLimit.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEditAtIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where editAt equals to DEFAULT_EDIT_AT
        defaultAuditRecommendationShouldBeFound("editAt.equals=" + DEFAULT_EDIT_AT);

        // Get all the auditRecommendationList where editAt equals to UPDATED_EDIT_AT
        defaultAuditRecommendationShouldNotBeFound("editAt.equals=" + UPDATED_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEditAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where editAt not equals to DEFAULT_EDIT_AT
        defaultAuditRecommendationShouldNotBeFound("editAt.notEquals=" + DEFAULT_EDIT_AT);

        // Get all the auditRecommendationList where editAt not equals to UPDATED_EDIT_AT
        defaultAuditRecommendationShouldBeFound("editAt.notEquals=" + UPDATED_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEditAtIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where editAt in DEFAULT_EDIT_AT or UPDATED_EDIT_AT
        defaultAuditRecommendationShouldBeFound("editAt.in=" + DEFAULT_EDIT_AT + "," + UPDATED_EDIT_AT);

        // Get all the auditRecommendationList where editAt equals to UPDATED_EDIT_AT
        defaultAuditRecommendationShouldNotBeFound("editAt.in=" + UPDATED_EDIT_AT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEditAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where editAt is not null
        defaultAuditRecommendationShouldBeFound("editAt.specified=true");

        // Get all the auditRecommendationList where editAt is null
        defaultAuditRecommendationShouldNotBeFound("editAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByExecutedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where executedAt equals to DEFAULT_EXECUTED_AT
        defaultAuditRecommendationShouldBeFound("executedAt.equals=" + DEFAULT_EXECUTED_AT);

        // Get all the auditRecommendationList where executedAt equals to UPDATED_EXECUTED_AT
        defaultAuditRecommendationShouldNotBeFound("executedAt.equals=" + UPDATED_EXECUTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByExecutedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where executedAt not equals to DEFAULT_EXECUTED_AT
        defaultAuditRecommendationShouldNotBeFound("executedAt.notEquals=" + DEFAULT_EXECUTED_AT);

        // Get all the auditRecommendationList where executedAt not equals to UPDATED_EXECUTED_AT
        defaultAuditRecommendationShouldBeFound("executedAt.notEquals=" + UPDATED_EXECUTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByExecutedAtIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where executedAt in DEFAULT_EXECUTED_AT or UPDATED_EXECUTED_AT
        defaultAuditRecommendationShouldBeFound("executedAt.in=" + DEFAULT_EXECUTED_AT + "," + UPDATED_EXECUTED_AT);

        // Get all the auditRecommendationList where executedAt equals to UPDATED_EXECUTED_AT
        defaultAuditRecommendationShouldNotBeFound("executedAt.in=" + UPDATED_EXECUTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByExecutedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where executedAt is not null
        defaultAuditRecommendationShouldBeFound("executedAt.specified=true");

        // Get all the auditRecommendationList where executedAt is null
        defaultAuditRecommendationShouldNotBeFound("executedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entityId equals to DEFAULT_ENTITY_ID
        defaultAuditRecommendationShouldBeFound("entityId.equals=" + DEFAULT_ENTITY_ID);

        // Get all the auditRecommendationList where entityId equals to UPDATED_ENTITY_ID
        defaultAuditRecommendationShouldNotBeFound("entityId.equals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntityIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entityId not equals to DEFAULT_ENTITY_ID
        defaultAuditRecommendationShouldNotBeFound("entityId.notEquals=" + DEFAULT_ENTITY_ID);

        // Get all the auditRecommendationList where entityId not equals to UPDATED_ENTITY_ID
        defaultAuditRecommendationShouldBeFound("entityId.notEquals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entityId in DEFAULT_ENTITY_ID or UPDATED_ENTITY_ID
        defaultAuditRecommendationShouldBeFound("entityId.in=" + DEFAULT_ENTITY_ID + "," + UPDATED_ENTITY_ID);

        // Get all the auditRecommendationList where entityId equals to UPDATED_ENTITY_ID
        defaultAuditRecommendationShouldNotBeFound("entityId.in=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entityId is not null
        defaultAuditRecommendationShouldBeFound("entityId.specified=true");

        // Get all the auditRecommendationList where entityId is null
        defaultAuditRecommendationShouldNotBeFound("entityId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entityId is greater than or equal to DEFAULT_ENTITY_ID
        defaultAuditRecommendationShouldBeFound("entityId.greaterThanOrEqual=" + DEFAULT_ENTITY_ID);

        // Get all the auditRecommendationList where entityId is greater than or equal to UPDATED_ENTITY_ID
        defaultAuditRecommendationShouldNotBeFound("entityId.greaterThanOrEqual=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entityId is less than or equal to DEFAULT_ENTITY_ID
        defaultAuditRecommendationShouldBeFound("entityId.lessThanOrEqual=" + DEFAULT_ENTITY_ID);

        // Get all the auditRecommendationList where entityId is less than or equal to SMALLER_ENTITY_ID
        defaultAuditRecommendationShouldNotBeFound("entityId.lessThanOrEqual=" + SMALLER_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entityId is less than DEFAULT_ENTITY_ID
        defaultAuditRecommendationShouldNotBeFound("entityId.lessThan=" + DEFAULT_ENTITY_ID);

        // Get all the auditRecommendationList where entityId is less than UPDATED_ENTITY_ID
        defaultAuditRecommendationShouldBeFound("entityId.lessThan=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entityId is greater than DEFAULT_ENTITY_ID
        defaultAuditRecommendationShouldNotBeFound("entityId.greaterThan=" + DEFAULT_ENTITY_ID);

        // Get all the auditRecommendationList where entityId is greater than SMALLER_ENTITY_ID
        defaultAuditRecommendationShouldBeFound("entityId.greaterThan=" + SMALLER_ENTITY_ID);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntiyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entiyName equals to DEFAULT_ENTIY_NAME
        defaultAuditRecommendationShouldBeFound("entiyName.equals=" + DEFAULT_ENTIY_NAME);

        // Get all the auditRecommendationList where entiyName equals to UPDATED_ENTIY_NAME
        defaultAuditRecommendationShouldNotBeFound("entiyName.equals=" + UPDATED_ENTIY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntiyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entiyName not equals to DEFAULT_ENTIY_NAME
        defaultAuditRecommendationShouldNotBeFound("entiyName.notEquals=" + DEFAULT_ENTIY_NAME);

        // Get all the auditRecommendationList where entiyName not equals to UPDATED_ENTIY_NAME
        defaultAuditRecommendationShouldBeFound("entiyName.notEquals=" + UPDATED_ENTIY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntiyNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entiyName in DEFAULT_ENTIY_NAME or UPDATED_ENTIY_NAME
        defaultAuditRecommendationShouldBeFound("entiyName.in=" + DEFAULT_ENTIY_NAME + "," + UPDATED_ENTIY_NAME);

        // Get all the auditRecommendationList where entiyName equals to UPDATED_ENTIY_NAME
        defaultAuditRecommendationShouldNotBeFound("entiyName.in=" + UPDATED_ENTIY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntiyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entiyName is not null
        defaultAuditRecommendationShouldBeFound("entiyName.specified=true");

        // Get all the auditRecommendationList where entiyName is null
        defaultAuditRecommendationShouldNotBeFound("entiyName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditRecommendationsByEntiyNameContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entiyName contains DEFAULT_ENTIY_NAME
        defaultAuditRecommendationShouldBeFound("entiyName.contains=" + DEFAULT_ENTIY_NAME);

        // Get all the auditRecommendationList where entiyName contains UPDATED_ENTIY_NAME
        defaultAuditRecommendationShouldNotBeFound("entiyName.contains=" + UPDATED_ENTIY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationsByEntiyNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationRepository.saveAndFlush(auditRecommendation);

        // Get all the auditRecommendationList where entiyName does not contain DEFAULT_ENTIY_NAME
        defaultAuditRecommendationShouldNotBeFound("entiyName.doesNotContain=" + DEFAULT_ENTIY_NAME);

        // Get all the auditRecommendationList where entiyName does not contain UPDATED_ENTIY_NAME
        defaultAuditRecommendationShouldBeFound("entiyName.doesNotContain=" + UPDATED_ENTIY_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditRecommendationShouldBeFound(String filter) throws Exception {
        restAuditRecommendationMockMvc.perform(get("/api/audit-recommendations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditRecommendation.getId().intValue())))
            .andExpect(jsonPath("$.[*].auditorId").value(hasItem(DEFAULT_AUDITOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].auditorName").value(hasItem(DEFAULT_AUDITOR_NAME)))
            .andExpect(jsonPath("$.[*].auditorEmail").value(hasItem(DEFAULT_AUDITOR_EMAIL)))
            .andExpect(jsonPath("$.[*].auditId").value(hasItem(DEFAULT_AUDIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].responsableId").value(hasItem(DEFAULT_RESPONSABLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].responsableName").value(hasItem(DEFAULT_RESPONSABLE_NAME)))
            .andExpect(jsonPath("$.[*].responsableEmail").value(hasItem(DEFAULT_RESPONSABLE_EMAIL)))
            .andExpect(jsonPath("$.[*].dateLimit").value(hasItem(DEFAULT_DATE_LIMIT.toString())))
            .andExpect(jsonPath("$.[*].editAt").value(hasItem(DEFAULT_EDIT_AT.toString())))
            .andExpect(jsonPath("$.[*].executedAt").value(hasItem(DEFAULT_EXECUTED_AT.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].entiyName").value(hasItem(DEFAULT_ENTIY_NAME)));

        // Check, that the count call also returns 1
        restAuditRecommendationMockMvc.perform(get("/api/audit-recommendations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditRecommendationShouldNotBeFound(String filter) throws Exception {
        restAuditRecommendationMockMvc.perform(get("/api/audit-recommendations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditRecommendationMockMvc.perform(get("/api/audit-recommendations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuditRecommendation() throws Exception {
        // Get the auditRecommendation
        restAuditRecommendationMockMvc.perform(get("/api/audit-recommendations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditRecommendation() throws Exception {
        // Initialize the database
        auditRecommendationService.save(auditRecommendation);

        int databaseSizeBeforeUpdate = auditRecommendationRepository.findAll().size();

        // Update the auditRecommendation
        AuditRecommendation updatedAuditRecommendation = auditRecommendationRepository.findById(auditRecommendation.getId()).get();
        // Disconnect from session so that the updates on updatedAuditRecommendation are not directly saved in db
        em.detach(updatedAuditRecommendation);
        updatedAuditRecommendation
            .auditorId(UPDATED_AUDITOR_ID)
            .auditorName(UPDATED_AUDITOR_NAME)
            .auditorEmail(UPDATED_AUDITOR_EMAIL)
            .auditId(UPDATED_AUDIT_ID)
            .status(UPDATED_STATUS)
            .content(UPDATED_CONTENT)
            .responsableId(UPDATED_RESPONSABLE_ID)
            .responsableName(UPDATED_RESPONSABLE_NAME)
            .responsableEmail(UPDATED_RESPONSABLE_EMAIL)
            .dateLimit(UPDATED_DATE_LIMIT)
            .editAt(UPDATED_EDIT_AT)
            .executedAt(UPDATED_EXECUTED_AT)
            .entityId(UPDATED_ENTITY_ID)
            .entiyName(UPDATED_ENTIY_NAME);

        restAuditRecommendationMockMvc.perform(put("/api/audit-recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditRecommendation)))
            .andExpect(status().isOk());

        // Validate the AuditRecommendation in the database
        List<AuditRecommendation> auditRecommendationList = auditRecommendationRepository.findAll();
        assertThat(auditRecommendationList).hasSize(databaseSizeBeforeUpdate);
        AuditRecommendation testAuditRecommendation = auditRecommendationList.get(auditRecommendationList.size() - 1);
        assertThat(testAuditRecommendation.getAuditorId()).isEqualTo(UPDATED_AUDITOR_ID);
        assertThat(testAuditRecommendation.getAuditorName()).isEqualTo(UPDATED_AUDITOR_NAME);
        assertThat(testAuditRecommendation.getAuditorEmail()).isEqualTo(UPDATED_AUDITOR_EMAIL);
        assertThat(testAuditRecommendation.getAuditId()).isEqualTo(UPDATED_AUDIT_ID);
        assertThat(testAuditRecommendation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAuditRecommendation.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAuditRecommendation.getResponsableId()).isEqualTo(UPDATED_RESPONSABLE_ID);
        assertThat(testAuditRecommendation.getResponsableName()).isEqualTo(UPDATED_RESPONSABLE_NAME);
        assertThat(testAuditRecommendation.getResponsableEmail()).isEqualTo(UPDATED_RESPONSABLE_EMAIL);
        assertThat(testAuditRecommendation.getDateLimit()).isEqualTo(UPDATED_DATE_LIMIT);
        assertThat(testAuditRecommendation.getEditAt()).isEqualTo(UPDATED_EDIT_AT);
        assertThat(testAuditRecommendation.getExecutedAt()).isEqualTo(UPDATED_EXECUTED_AT);
        assertThat(testAuditRecommendation.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testAuditRecommendation.getEntiyName()).isEqualTo(UPDATED_ENTIY_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditRecommendation() throws Exception {
        int databaseSizeBeforeUpdate = auditRecommendationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditRecommendationMockMvc.perform(put("/api/audit-recommendations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecommendation)))
            .andExpect(status().isBadRequest());

        // Validate the AuditRecommendation in the database
        List<AuditRecommendation> auditRecommendationList = auditRecommendationRepository.findAll();
        assertThat(auditRecommendationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditRecommendation() throws Exception {
        // Initialize the database
        auditRecommendationService.save(auditRecommendation);

        int databaseSizeBeforeDelete = auditRecommendationRepository.findAll().size();

        // Delete the auditRecommendation
        restAuditRecommendationMockMvc.perform(delete("/api/audit-recommendations/{id}", auditRecommendation.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditRecommendation> auditRecommendationList = auditRecommendationRepository.findAll();
        assertThat(auditRecommendationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
