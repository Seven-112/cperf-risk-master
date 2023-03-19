package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.AuditEventTrigger;
import com.mshz.domain.Audit;
import com.mshz.repository.AuditEventTriggerRepository;
import com.mshz.service.AuditEventTriggerService;
import com.mshz.service.dto.AuditEventTriggerCriteria;
import com.mshz.service.AuditEventTriggerQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mshz.domain.enumeration.AuditEventRecurrence;
/**
 * Integration tests for the {@link AuditEventTriggerResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditEventTriggerResourceIT {

    private static final Long DEFAULT_EDITOR_ID = 1L;
    private static final Long UPDATED_EDITOR_ID = 2L;
    private static final Long SMALLER_EDITOR_ID = 1L - 1L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AuditEventRecurrence DEFAULT_RECURRENCE = AuditEventRecurrence.ONCE;
    private static final AuditEventRecurrence UPDATED_RECURRENCE = AuditEventRecurrence.ALLAWAYS;

    private static final Boolean DEFAULT_DISABLED = false;
    private static final Boolean UPDATED_DISABLED = true;

    private static final String DEFAULT_EDITOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EDITOR_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_FIRST_STARTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FIRST_STARTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NEXT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_START_COUNT = 1;
    private static final Integer UPDATED_START_COUNT = 2;
    private static final Integer SMALLER_START_COUNT = 1 - 1;

    @Autowired
    private AuditEventTriggerRepository auditEventTriggerRepository;

    @Autowired
    private AuditEventTriggerService auditEventTriggerService;

    @Autowired
    private AuditEventTriggerQueryService auditEventTriggerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditEventTriggerMockMvc;

    private AuditEventTrigger auditEventTrigger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditEventTrigger createEntity(EntityManager em) {
        AuditEventTrigger auditEventTrigger = new AuditEventTrigger()
            .editorId(DEFAULT_EDITOR_ID)
            .createdAt(DEFAULT_CREATED_AT)
            .name(DEFAULT_NAME)
            .recurrence(DEFAULT_RECURRENCE)
            .disabled(DEFAULT_DISABLED)
            .editorName(DEFAULT_EDITOR_NAME)
            .firstStartedAt(DEFAULT_FIRST_STARTED_AT)
            .nextStartAt(DEFAULT_NEXT_START_AT)
            .startCount(DEFAULT_START_COUNT);
        return auditEventTrigger;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditEventTrigger createUpdatedEntity(EntityManager em) {
        AuditEventTrigger auditEventTrigger = new AuditEventTrigger()
            .editorId(UPDATED_EDITOR_ID)
            .createdAt(UPDATED_CREATED_AT)
            .name(UPDATED_NAME)
            .recurrence(UPDATED_RECURRENCE)
            .disabled(UPDATED_DISABLED)
            .editorName(UPDATED_EDITOR_NAME)
            .firstStartedAt(UPDATED_FIRST_STARTED_AT)
            .nextStartAt(UPDATED_NEXT_START_AT)
            .startCount(UPDATED_START_COUNT);
        return auditEventTrigger;
    }

    @BeforeEach
    public void initTest() {
        auditEventTrigger = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditEventTrigger() throws Exception {
        int databaseSizeBeforeCreate = auditEventTriggerRepository.findAll().size();
        // Create the AuditEventTrigger
        restAuditEventTriggerMockMvc.perform(post("/api/audit-event-triggers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditEventTrigger)))
            .andExpect(status().isCreated());

        // Validate the AuditEventTrigger in the database
        List<AuditEventTrigger> auditEventTriggerList = auditEventTriggerRepository.findAll();
        assertThat(auditEventTriggerList).hasSize(databaseSizeBeforeCreate + 1);
        AuditEventTrigger testAuditEventTrigger = auditEventTriggerList.get(auditEventTriggerList.size() - 1);
        assertThat(testAuditEventTrigger.getEditorId()).isEqualTo(DEFAULT_EDITOR_ID);
        assertThat(testAuditEventTrigger.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAuditEventTrigger.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAuditEventTrigger.getRecurrence()).isEqualTo(DEFAULT_RECURRENCE);
        assertThat(testAuditEventTrigger.isDisabled()).isEqualTo(DEFAULT_DISABLED);
        assertThat(testAuditEventTrigger.getEditorName()).isEqualTo(DEFAULT_EDITOR_NAME);
        assertThat(testAuditEventTrigger.getFirstStartedAt()).isEqualTo(DEFAULT_FIRST_STARTED_AT);
        assertThat(testAuditEventTrigger.getNextStartAt()).isEqualTo(DEFAULT_NEXT_START_AT);
        assertThat(testAuditEventTrigger.getStartCount()).isEqualTo(DEFAULT_START_COUNT);
    }

    @Test
    @Transactional
    public void createAuditEventTriggerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditEventTriggerRepository.findAll().size();

        // Create the AuditEventTrigger with an existing ID
        auditEventTrigger.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditEventTriggerMockMvc.perform(post("/api/audit-event-triggers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditEventTrigger)))
            .andExpect(status().isBadRequest());

        // Validate the AuditEventTrigger in the database
        List<AuditEventTrigger> auditEventTriggerList = auditEventTriggerRepository.findAll();
        assertThat(auditEventTriggerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEditorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditEventTriggerRepository.findAll().size();
        // set the field null
        auditEventTrigger.setEditorId(null);

        // Create the AuditEventTrigger, which fails.


        restAuditEventTriggerMockMvc.perform(post("/api/audit-event-triggers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditEventTrigger)))
            .andExpect(status().isBadRequest());

        List<AuditEventTrigger> auditEventTriggerList = auditEventTriggerRepository.findAll();
        assertThat(auditEventTriggerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggers() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList
        restAuditEventTriggerMockMvc.perform(get("/api/audit-event-triggers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditEventTrigger.getId().intValue())))
            .andExpect(jsonPath("$.[*].editorId").value(hasItem(DEFAULT_EDITOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].recurrence").value(hasItem(DEFAULT_RECURRENCE.toString())))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].editorName").value(hasItem(DEFAULT_EDITOR_NAME)))
            .andExpect(jsonPath("$.[*].firstStartedAt").value(hasItem(DEFAULT_FIRST_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].nextStartAt").value(hasItem(DEFAULT_NEXT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].startCount").value(hasItem(DEFAULT_START_COUNT)));
    }
    
    @Test
    @Transactional
    public void getAuditEventTrigger() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get the auditEventTrigger
        restAuditEventTriggerMockMvc.perform(get("/api/audit-event-triggers/{id}", auditEventTrigger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditEventTrigger.getId().intValue()))
            .andExpect(jsonPath("$.editorId").value(DEFAULT_EDITOR_ID.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.recurrence").value(DEFAULT_RECURRENCE.toString()))
            .andExpect(jsonPath("$.disabled").value(DEFAULT_DISABLED.booleanValue()))
            .andExpect(jsonPath("$.editorName").value(DEFAULT_EDITOR_NAME))
            .andExpect(jsonPath("$.firstStartedAt").value(DEFAULT_FIRST_STARTED_AT.toString()))
            .andExpect(jsonPath("$.nextStartAt").value(DEFAULT_NEXT_START_AT.toString()))
            .andExpect(jsonPath("$.startCount").value(DEFAULT_START_COUNT));
    }


    @Test
    @Transactional
    public void getAuditEventTriggersByIdFiltering() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        Long id = auditEventTrigger.getId();

        defaultAuditEventTriggerShouldBeFound("id.equals=" + id);
        defaultAuditEventTriggerShouldNotBeFound("id.notEquals=" + id);

        defaultAuditEventTriggerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditEventTriggerShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditEventTriggerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditEventTriggerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorId equals to DEFAULT_EDITOR_ID
        defaultAuditEventTriggerShouldBeFound("editorId.equals=" + DEFAULT_EDITOR_ID);

        // Get all the auditEventTriggerList where editorId equals to UPDATED_EDITOR_ID
        defaultAuditEventTriggerShouldNotBeFound("editorId.equals=" + UPDATED_EDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorId not equals to DEFAULT_EDITOR_ID
        defaultAuditEventTriggerShouldNotBeFound("editorId.notEquals=" + DEFAULT_EDITOR_ID);

        // Get all the auditEventTriggerList where editorId not equals to UPDATED_EDITOR_ID
        defaultAuditEventTriggerShouldBeFound("editorId.notEquals=" + UPDATED_EDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorId in DEFAULT_EDITOR_ID or UPDATED_EDITOR_ID
        defaultAuditEventTriggerShouldBeFound("editorId.in=" + DEFAULT_EDITOR_ID + "," + UPDATED_EDITOR_ID);

        // Get all the auditEventTriggerList where editorId equals to UPDATED_EDITOR_ID
        defaultAuditEventTriggerShouldNotBeFound("editorId.in=" + UPDATED_EDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorId is not null
        defaultAuditEventTriggerShouldBeFound("editorId.specified=true");

        // Get all the auditEventTriggerList where editorId is null
        defaultAuditEventTriggerShouldNotBeFound("editorId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorId is greater than or equal to DEFAULT_EDITOR_ID
        defaultAuditEventTriggerShouldBeFound("editorId.greaterThanOrEqual=" + DEFAULT_EDITOR_ID);

        // Get all the auditEventTriggerList where editorId is greater than or equal to UPDATED_EDITOR_ID
        defaultAuditEventTriggerShouldNotBeFound("editorId.greaterThanOrEqual=" + UPDATED_EDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorId is less than or equal to DEFAULT_EDITOR_ID
        defaultAuditEventTriggerShouldBeFound("editorId.lessThanOrEqual=" + DEFAULT_EDITOR_ID);

        // Get all the auditEventTriggerList where editorId is less than or equal to SMALLER_EDITOR_ID
        defaultAuditEventTriggerShouldNotBeFound("editorId.lessThanOrEqual=" + SMALLER_EDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorId is less than DEFAULT_EDITOR_ID
        defaultAuditEventTriggerShouldNotBeFound("editorId.lessThan=" + DEFAULT_EDITOR_ID);

        // Get all the auditEventTriggerList where editorId is less than UPDATED_EDITOR_ID
        defaultAuditEventTriggerShouldBeFound("editorId.lessThan=" + UPDATED_EDITOR_ID);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorId is greater than DEFAULT_EDITOR_ID
        defaultAuditEventTriggerShouldNotBeFound("editorId.greaterThan=" + DEFAULT_EDITOR_ID);

        // Get all the auditEventTriggerList where editorId is greater than SMALLER_EDITOR_ID
        defaultAuditEventTriggerShouldBeFound("editorId.greaterThan=" + SMALLER_EDITOR_ID);
    }


    @Test
    @Transactional
    public void getAllAuditEventTriggersByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where createdAt equals to DEFAULT_CREATED_AT
        defaultAuditEventTriggerShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the auditEventTriggerList where createdAt equals to UPDATED_CREATED_AT
        defaultAuditEventTriggerShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where createdAt not equals to DEFAULT_CREATED_AT
        defaultAuditEventTriggerShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the auditEventTriggerList where createdAt not equals to UPDATED_CREATED_AT
        defaultAuditEventTriggerShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultAuditEventTriggerShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the auditEventTriggerList where createdAt equals to UPDATED_CREATED_AT
        defaultAuditEventTriggerShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where createdAt is not null
        defaultAuditEventTriggerShouldBeFound("createdAt.specified=true");

        // Get all the auditEventTriggerList where createdAt is null
        defaultAuditEventTriggerShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where name equals to DEFAULT_NAME
        defaultAuditEventTriggerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the auditEventTriggerList where name equals to UPDATED_NAME
        defaultAuditEventTriggerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where name not equals to DEFAULT_NAME
        defaultAuditEventTriggerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the auditEventTriggerList where name not equals to UPDATED_NAME
        defaultAuditEventTriggerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAuditEventTriggerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the auditEventTriggerList where name equals to UPDATED_NAME
        defaultAuditEventTriggerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where name is not null
        defaultAuditEventTriggerShouldBeFound("name.specified=true");

        // Get all the auditEventTriggerList where name is null
        defaultAuditEventTriggerShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditEventTriggersByNameContainsSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where name contains DEFAULT_NAME
        defaultAuditEventTriggerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the auditEventTriggerList where name contains UPDATED_NAME
        defaultAuditEventTriggerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where name does not contain DEFAULT_NAME
        defaultAuditEventTriggerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the auditEventTriggerList where name does not contain UPDATED_NAME
        defaultAuditEventTriggerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditEventTriggersByRecurrenceIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where recurrence equals to DEFAULT_RECURRENCE
        defaultAuditEventTriggerShouldBeFound("recurrence.equals=" + DEFAULT_RECURRENCE);

        // Get all the auditEventTriggerList where recurrence equals to UPDATED_RECURRENCE
        defaultAuditEventTriggerShouldNotBeFound("recurrence.equals=" + UPDATED_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByRecurrenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where recurrence not equals to DEFAULT_RECURRENCE
        defaultAuditEventTriggerShouldNotBeFound("recurrence.notEquals=" + DEFAULT_RECURRENCE);

        // Get all the auditEventTriggerList where recurrence not equals to UPDATED_RECURRENCE
        defaultAuditEventTriggerShouldBeFound("recurrence.notEquals=" + UPDATED_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByRecurrenceIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where recurrence in DEFAULT_RECURRENCE or UPDATED_RECURRENCE
        defaultAuditEventTriggerShouldBeFound("recurrence.in=" + DEFAULT_RECURRENCE + "," + UPDATED_RECURRENCE);

        // Get all the auditEventTriggerList where recurrence equals to UPDATED_RECURRENCE
        defaultAuditEventTriggerShouldNotBeFound("recurrence.in=" + UPDATED_RECURRENCE);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByRecurrenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where recurrence is not null
        defaultAuditEventTriggerShouldBeFound("recurrence.specified=true");

        // Get all the auditEventTriggerList where recurrence is null
        defaultAuditEventTriggerShouldNotBeFound("recurrence.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByDisabledIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where disabled equals to DEFAULT_DISABLED
        defaultAuditEventTriggerShouldBeFound("disabled.equals=" + DEFAULT_DISABLED);

        // Get all the auditEventTriggerList where disabled equals to UPDATED_DISABLED
        defaultAuditEventTriggerShouldNotBeFound("disabled.equals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByDisabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where disabled not equals to DEFAULT_DISABLED
        defaultAuditEventTriggerShouldNotBeFound("disabled.notEquals=" + DEFAULT_DISABLED);

        // Get all the auditEventTriggerList where disabled not equals to UPDATED_DISABLED
        defaultAuditEventTriggerShouldBeFound("disabled.notEquals=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByDisabledIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where disabled in DEFAULT_DISABLED or UPDATED_DISABLED
        defaultAuditEventTriggerShouldBeFound("disabled.in=" + DEFAULT_DISABLED + "," + UPDATED_DISABLED);

        // Get all the auditEventTriggerList where disabled equals to UPDATED_DISABLED
        defaultAuditEventTriggerShouldNotBeFound("disabled.in=" + UPDATED_DISABLED);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByDisabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where disabled is not null
        defaultAuditEventTriggerShouldBeFound("disabled.specified=true");

        // Get all the auditEventTriggerList where disabled is null
        defaultAuditEventTriggerShouldNotBeFound("disabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorName equals to DEFAULT_EDITOR_NAME
        defaultAuditEventTriggerShouldBeFound("editorName.equals=" + DEFAULT_EDITOR_NAME);

        // Get all the auditEventTriggerList where editorName equals to UPDATED_EDITOR_NAME
        defaultAuditEventTriggerShouldNotBeFound("editorName.equals=" + UPDATED_EDITOR_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorName not equals to DEFAULT_EDITOR_NAME
        defaultAuditEventTriggerShouldNotBeFound("editorName.notEquals=" + DEFAULT_EDITOR_NAME);

        // Get all the auditEventTriggerList where editorName not equals to UPDATED_EDITOR_NAME
        defaultAuditEventTriggerShouldBeFound("editorName.notEquals=" + UPDATED_EDITOR_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorName in DEFAULT_EDITOR_NAME or UPDATED_EDITOR_NAME
        defaultAuditEventTriggerShouldBeFound("editorName.in=" + DEFAULT_EDITOR_NAME + "," + UPDATED_EDITOR_NAME);

        // Get all the auditEventTriggerList where editorName equals to UPDATED_EDITOR_NAME
        defaultAuditEventTriggerShouldNotBeFound("editorName.in=" + UPDATED_EDITOR_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorName is not null
        defaultAuditEventTriggerShouldBeFound("editorName.specified=true");

        // Get all the auditEventTriggerList where editorName is null
        defaultAuditEventTriggerShouldNotBeFound("editorName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorNameContainsSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorName contains DEFAULT_EDITOR_NAME
        defaultAuditEventTriggerShouldBeFound("editorName.contains=" + DEFAULT_EDITOR_NAME);

        // Get all the auditEventTriggerList where editorName contains UPDATED_EDITOR_NAME
        defaultAuditEventTriggerShouldNotBeFound("editorName.contains=" + UPDATED_EDITOR_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByEditorNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where editorName does not contain DEFAULT_EDITOR_NAME
        defaultAuditEventTriggerShouldNotBeFound("editorName.doesNotContain=" + DEFAULT_EDITOR_NAME);

        // Get all the auditEventTriggerList where editorName does not contain UPDATED_EDITOR_NAME
        defaultAuditEventTriggerShouldBeFound("editorName.doesNotContain=" + UPDATED_EDITOR_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditEventTriggersByFirstStartedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where firstStartedAt equals to DEFAULT_FIRST_STARTED_AT
        defaultAuditEventTriggerShouldBeFound("firstStartedAt.equals=" + DEFAULT_FIRST_STARTED_AT);

        // Get all the auditEventTriggerList where firstStartedAt equals to UPDATED_FIRST_STARTED_AT
        defaultAuditEventTriggerShouldNotBeFound("firstStartedAt.equals=" + UPDATED_FIRST_STARTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByFirstStartedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where firstStartedAt not equals to DEFAULT_FIRST_STARTED_AT
        defaultAuditEventTriggerShouldNotBeFound("firstStartedAt.notEquals=" + DEFAULT_FIRST_STARTED_AT);

        // Get all the auditEventTriggerList where firstStartedAt not equals to UPDATED_FIRST_STARTED_AT
        defaultAuditEventTriggerShouldBeFound("firstStartedAt.notEquals=" + UPDATED_FIRST_STARTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByFirstStartedAtIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where firstStartedAt in DEFAULT_FIRST_STARTED_AT or UPDATED_FIRST_STARTED_AT
        defaultAuditEventTriggerShouldBeFound("firstStartedAt.in=" + DEFAULT_FIRST_STARTED_AT + "," + UPDATED_FIRST_STARTED_AT);

        // Get all the auditEventTriggerList where firstStartedAt equals to UPDATED_FIRST_STARTED_AT
        defaultAuditEventTriggerShouldNotBeFound("firstStartedAt.in=" + UPDATED_FIRST_STARTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByFirstStartedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where firstStartedAt is not null
        defaultAuditEventTriggerShouldBeFound("firstStartedAt.specified=true");

        // Get all the auditEventTriggerList where firstStartedAt is null
        defaultAuditEventTriggerShouldNotBeFound("firstStartedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNextStartAtIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where nextStartAt equals to DEFAULT_NEXT_START_AT
        defaultAuditEventTriggerShouldBeFound("nextStartAt.equals=" + DEFAULT_NEXT_START_AT);

        // Get all the auditEventTriggerList where nextStartAt equals to UPDATED_NEXT_START_AT
        defaultAuditEventTriggerShouldNotBeFound("nextStartAt.equals=" + UPDATED_NEXT_START_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNextStartAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where nextStartAt not equals to DEFAULT_NEXT_START_AT
        defaultAuditEventTriggerShouldNotBeFound("nextStartAt.notEquals=" + DEFAULT_NEXT_START_AT);

        // Get all the auditEventTriggerList where nextStartAt not equals to UPDATED_NEXT_START_AT
        defaultAuditEventTriggerShouldBeFound("nextStartAt.notEquals=" + UPDATED_NEXT_START_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNextStartAtIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where nextStartAt in DEFAULT_NEXT_START_AT or UPDATED_NEXT_START_AT
        defaultAuditEventTriggerShouldBeFound("nextStartAt.in=" + DEFAULT_NEXT_START_AT + "," + UPDATED_NEXT_START_AT);

        // Get all the auditEventTriggerList where nextStartAt equals to UPDATED_NEXT_START_AT
        defaultAuditEventTriggerShouldNotBeFound("nextStartAt.in=" + UPDATED_NEXT_START_AT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByNextStartAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where nextStartAt is not null
        defaultAuditEventTriggerShouldBeFound("nextStartAt.specified=true");

        // Get all the auditEventTriggerList where nextStartAt is null
        defaultAuditEventTriggerShouldNotBeFound("nextStartAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByStartCountIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where startCount equals to DEFAULT_START_COUNT
        defaultAuditEventTriggerShouldBeFound("startCount.equals=" + DEFAULT_START_COUNT);

        // Get all the auditEventTriggerList where startCount equals to UPDATED_START_COUNT
        defaultAuditEventTriggerShouldNotBeFound("startCount.equals=" + UPDATED_START_COUNT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByStartCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where startCount not equals to DEFAULT_START_COUNT
        defaultAuditEventTriggerShouldNotBeFound("startCount.notEquals=" + DEFAULT_START_COUNT);

        // Get all the auditEventTriggerList where startCount not equals to UPDATED_START_COUNT
        defaultAuditEventTriggerShouldBeFound("startCount.notEquals=" + UPDATED_START_COUNT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByStartCountIsInShouldWork() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where startCount in DEFAULT_START_COUNT or UPDATED_START_COUNT
        defaultAuditEventTriggerShouldBeFound("startCount.in=" + DEFAULT_START_COUNT + "," + UPDATED_START_COUNT);

        // Get all the auditEventTriggerList where startCount equals to UPDATED_START_COUNT
        defaultAuditEventTriggerShouldNotBeFound("startCount.in=" + UPDATED_START_COUNT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByStartCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where startCount is not null
        defaultAuditEventTriggerShouldBeFound("startCount.specified=true");

        // Get all the auditEventTriggerList where startCount is null
        defaultAuditEventTriggerShouldNotBeFound("startCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByStartCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where startCount is greater than or equal to DEFAULT_START_COUNT
        defaultAuditEventTriggerShouldBeFound("startCount.greaterThanOrEqual=" + DEFAULT_START_COUNT);

        // Get all the auditEventTriggerList where startCount is greater than or equal to UPDATED_START_COUNT
        defaultAuditEventTriggerShouldNotBeFound("startCount.greaterThanOrEqual=" + UPDATED_START_COUNT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByStartCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where startCount is less than or equal to DEFAULT_START_COUNT
        defaultAuditEventTriggerShouldBeFound("startCount.lessThanOrEqual=" + DEFAULT_START_COUNT);

        // Get all the auditEventTriggerList where startCount is less than or equal to SMALLER_START_COUNT
        defaultAuditEventTriggerShouldNotBeFound("startCount.lessThanOrEqual=" + SMALLER_START_COUNT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByStartCountIsLessThanSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where startCount is less than DEFAULT_START_COUNT
        defaultAuditEventTriggerShouldNotBeFound("startCount.lessThan=" + DEFAULT_START_COUNT);

        // Get all the auditEventTriggerList where startCount is less than UPDATED_START_COUNT
        defaultAuditEventTriggerShouldBeFound("startCount.lessThan=" + UPDATED_START_COUNT);
    }

    @Test
    @Transactional
    public void getAllAuditEventTriggersByStartCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);

        // Get all the auditEventTriggerList where startCount is greater than DEFAULT_START_COUNT
        defaultAuditEventTriggerShouldNotBeFound("startCount.greaterThan=" + DEFAULT_START_COUNT);

        // Get all the auditEventTriggerList where startCount is greater than SMALLER_START_COUNT
        defaultAuditEventTriggerShouldBeFound("startCount.greaterThan=" + SMALLER_START_COUNT);
    }


    @Test
    @Transactional
    public void getAllAuditEventTriggersByAuditIsEqualToSomething() throws Exception {
        // Initialize the database
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);
        Audit audit = AuditResourceIT.createEntity(em);
        em.persist(audit);
        em.flush();
        auditEventTrigger.setAudit(audit);
        auditEventTriggerRepository.saveAndFlush(auditEventTrigger);
        Long auditId = audit.getId();

        // Get all the auditEventTriggerList where audit equals to auditId
        defaultAuditEventTriggerShouldBeFound("auditId.equals=" + auditId);

        // Get all the auditEventTriggerList where audit equals to auditId + 1
        defaultAuditEventTriggerShouldNotBeFound("auditId.equals=" + (auditId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditEventTriggerShouldBeFound(String filter) throws Exception {
        restAuditEventTriggerMockMvc.perform(get("/api/audit-event-triggers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditEventTrigger.getId().intValue())))
            .andExpect(jsonPath("$.[*].editorId").value(hasItem(DEFAULT_EDITOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].recurrence").value(hasItem(DEFAULT_RECURRENCE.toString())))
            .andExpect(jsonPath("$.[*].disabled").value(hasItem(DEFAULT_DISABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].editorName").value(hasItem(DEFAULT_EDITOR_NAME)))
            .andExpect(jsonPath("$.[*].firstStartedAt").value(hasItem(DEFAULT_FIRST_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].nextStartAt").value(hasItem(DEFAULT_NEXT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].startCount").value(hasItem(DEFAULT_START_COUNT)));

        // Check, that the count call also returns 1
        restAuditEventTriggerMockMvc.perform(get("/api/audit-event-triggers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditEventTriggerShouldNotBeFound(String filter) throws Exception {
        restAuditEventTriggerMockMvc.perform(get("/api/audit-event-triggers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditEventTriggerMockMvc.perform(get("/api/audit-event-triggers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuditEventTrigger() throws Exception {
        // Get the auditEventTrigger
        restAuditEventTriggerMockMvc.perform(get("/api/audit-event-triggers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditEventTrigger() throws Exception {
        // Initialize the database
        auditEventTriggerService.save(auditEventTrigger);

        int databaseSizeBeforeUpdate = auditEventTriggerRepository.findAll().size();

        // Update the auditEventTrigger
        AuditEventTrigger updatedAuditEventTrigger = auditEventTriggerRepository.findById(auditEventTrigger.getId()).get();
        // Disconnect from session so that the updates on updatedAuditEventTrigger are not directly saved in db
        em.detach(updatedAuditEventTrigger);
        updatedAuditEventTrigger
            .editorId(UPDATED_EDITOR_ID)
            .createdAt(UPDATED_CREATED_AT)
            .name(UPDATED_NAME)
            .recurrence(UPDATED_RECURRENCE)
            .disabled(UPDATED_DISABLED)
            .editorName(UPDATED_EDITOR_NAME)
            .firstStartedAt(UPDATED_FIRST_STARTED_AT)
            .nextStartAt(UPDATED_NEXT_START_AT)
            .startCount(UPDATED_START_COUNT);

        restAuditEventTriggerMockMvc.perform(put("/api/audit-event-triggers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditEventTrigger)))
            .andExpect(status().isOk());

        // Validate the AuditEventTrigger in the database
        List<AuditEventTrigger> auditEventTriggerList = auditEventTriggerRepository.findAll();
        assertThat(auditEventTriggerList).hasSize(databaseSizeBeforeUpdate);
        AuditEventTrigger testAuditEventTrigger = auditEventTriggerList.get(auditEventTriggerList.size() - 1);
        assertThat(testAuditEventTrigger.getEditorId()).isEqualTo(UPDATED_EDITOR_ID);
        assertThat(testAuditEventTrigger.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAuditEventTrigger.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAuditEventTrigger.getRecurrence()).isEqualTo(UPDATED_RECURRENCE);
        assertThat(testAuditEventTrigger.isDisabled()).isEqualTo(UPDATED_DISABLED);
        assertThat(testAuditEventTrigger.getEditorName()).isEqualTo(UPDATED_EDITOR_NAME);
        assertThat(testAuditEventTrigger.getFirstStartedAt()).isEqualTo(UPDATED_FIRST_STARTED_AT);
        assertThat(testAuditEventTrigger.getNextStartAt()).isEqualTo(UPDATED_NEXT_START_AT);
        assertThat(testAuditEventTrigger.getStartCount()).isEqualTo(UPDATED_START_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditEventTrigger() throws Exception {
        int databaseSizeBeforeUpdate = auditEventTriggerRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditEventTriggerMockMvc.perform(put("/api/audit-event-triggers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditEventTrigger)))
            .andExpect(status().isBadRequest());

        // Validate the AuditEventTrigger in the database
        List<AuditEventTrigger> auditEventTriggerList = auditEventTriggerRepository.findAll();
        assertThat(auditEventTriggerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditEventTrigger() throws Exception {
        // Initialize the database
        auditEventTriggerService.save(auditEventTrigger);

        int databaseSizeBeforeDelete = auditEventTriggerRepository.findAll().size();

        // Delete the auditEventTrigger
        restAuditEventTriggerMockMvc.perform(delete("/api/audit-event-triggers/{id}", auditEventTrigger.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditEventTrigger> auditEventTriggerList = auditEventTriggerRepository.findAll();
        assertThat(auditEventTriggerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
