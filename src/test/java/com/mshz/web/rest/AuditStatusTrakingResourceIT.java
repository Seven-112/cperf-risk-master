package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.AuditStatusTraking;
import com.mshz.repository.AuditStatusTrakingRepository;
import com.mshz.service.AuditStatusTrakingService;
import com.mshz.service.dto.AuditStatusTrakingCriteria;
import com.mshz.service.AuditStatusTrakingQueryService;

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
 * Integration tests for the {@link AuditStatusTrakingResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditStatusTrakingResourceIT {

    private static final Long DEFAULT_AUDIT_ID = 1L;
    private static final Long UPDATED_AUDIT_ID = 2L;
    private static final Long SMALLER_AUDIT_ID = 1L - 1L;

    private static final AuditStatus DEFAULT_STATUS = AuditStatus.INITIAL;
    private static final AuditStatus UPDATED_STATUS = AuditStatus.STARTED;

    private static final Instant DEFAULT_TRACING_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRACING_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_JUSTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATION = "BBBBBBBBBB";

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final Boolean DEFAULT_EDITABLE = false;
    private static final Boolean UPDATED_EDITABLE = true;

    private static final Boolean DEFAULT_RECOM = false;
    private static final Boolean UPDATED_RECOM = true;

    @Autowired
    private AuditStatusTrakingRepository auditStatusTrakingRepository;

    @Autowired
    private AuditStatusTrakingService auditStatusTrakingService;

    @Autowired
    private AuditStatusTrakingQueryService auditStatusTrakingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditStatusTrakingMockMvc;

    private AuditStatusTraking auditStatusTraking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditStatusTraking createEntity(EntityManager em) {
        AuditStatusTraking auditStatusTraking = new AuditStatusTraking()
            .auditId(DEFAULT_AUDIT_ID)
            .status(DEFAULT_STATUS)
            .tracingAt(DEFAULT_TRACING_AT)
            .justification(DEFAULT_JUSTIFICATION)
            .userId(DEFAULT_USER_ID)
            .editable(DEFAULT_EDITABLE)
            .recom(DEFAULT_RECOM);
        return auditStatusTraking;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditStatusTraking createUpdatedEntity(EntityManager em) {
        AuditStatusTraking auditStatusTraking = new AuditStatusTraking()
            .auditId(UPDATED_AUDIT_ID)
            .status(UPDATED_STATUS)
            .tracingAt(UPDATED_TRACING_AT)
            .justification(UPDATED_JUSTIFICATION)
            .userId(UPDATED_USER_ID)
            .editable(UPDATED_EDITABLE)
            .recom(UPDATED_RECOM);
        return auditStatusTraking;
    }

    @BeforeEach
    public void initTest() {
        auditStatusTraking = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditStatusTraking() throws Exception {
        int databaseSizeBeforeCreate = auditStatusTrakingRepository.findAll().size();
        // Create the AuditStatusTraking
        restAuditStatusTrakingMockMvc.perform(post("/api/audit-status-trakings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTraking)))
            .andExpect(status().isCreated());

        // Validate the AuditStatusTraking in the database
        List<AuditStatusTraking> auditStatusTrakingList = auditStatusTrakingRepository.findAll();
        assertThat(auditStatusTrakingList).hasSize(databaseSizeBeforeCreate + 1);
        AuditStatusTraking testAuditStatusTraking = auditStatusTrakingList.get(auditStatusTrakingList.size() - 1);
        assertThat(testAuditStatusTraking.getAuditId()).isEqualTo(DEFAULT_AUDIT_ID);
        assertThat(testAuditStatusTraking.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAuditStatusTraking.getTracingAt()).isEqualTo(DEFAULT_TRACING_AT);
        assertThat(testAuditStatusTraking.getJustification()).isEqualTo(DEFAULT_JUSTIFICATION);
        assertThat(testAuditStatusTraking.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAuditStatusTraking.isEditable()).isEqualTo(DEFAULT_EDITABLE);
        assertThat(testAuditStatusTraking.isRecom()).isEqualTo(DEFAULT_RECOM);
    }

    @Test
    @Transactional
    public void createAuditStatusTrakingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditStatusTrakingRepository.findAll().size();

        // Create the AuditStatusTraking with an existing ID
        auditStatusTraking.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditStatusTrakingMockMvc.perform(post("/api/audit-status-trakings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTraking)))
            .andExpect(status().isBadRequest());

        // Validate the AuditStatusTraking in the database
        List<AuditStatusTraking> auditStatusTrakingList = auditStatusTrakingRepository.findAll();
        assertThat(auditStatusTrakingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAuditIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditStatusTrakingRepository.findAll().size();
        // set the field null
        auditStatusTraking.setAuditId(null);

        // Create the AuditStatusTraking, which fails.


        restAuditStatusTrakingMockMvc.perform(post("/api/audit-status-trakings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTraking)))
            .andExpect(status().isBadRequest());

        List<AuditStatusTraking> auditStatusTrakingList = auditStatusTrakingRepository.findAll();
        assertThat(auditStatusTrakingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakings() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList
        restAuditStatusTrakingMockMvc.perform(get("/api/audit-status-trakings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditStatusTraking.getId().intValue())))
            .andExpect(jsonPath("$.[*].auditId").value(hasItem(DEFAULT_AUDIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tracingAt").value(hasItem(DEFAULT_TRACING_AT.toString())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].editable").value(hasItem(DEFAULT_EDITABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].recom").value(hasItem(DEFAULT_RECOM.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAuditStatusTraking() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get the auditStatusTraking
        restAuditStatusTrakingMockMvc.perform(get("/api/audit-status-trakings/{id}", auditStatusTraking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditStatusTraking.getId().intValue()))
            .andExpect(jsonPath("$.auditId").value(DEFAULT_AUDIT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.tracingAt").value(DEFAULT_TRACING_AT.toString()))
            .andExpect(jsonPath("$.justification").value(DEFAULT_JUSTIFICATION.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.editable").value(DEFAULT_EDITABLE.booleanValue()))
            .andExpect(jsonPath("$.recom").value(DEFAULT_RECOM.booleanValue()));
    }


    @Test
    @Transactional
    public void getAuditStatusTrakingsByIdFiltering() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        Long id = auditStatusTraking.getId();

        defaultAuditStatusTrakingShouldBeFound("id.equals=" + id);
        defaultAuditStatusTrakingShouldNotBeFound("id.notEquals=" + id);

        defaultAuditStatusTrakingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditStatusTrakingShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditStatusTrakingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditStatusTrakingShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByAuditIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where auditId equals to DEFAULT_AUDIT_ID
        defaultAuditStatusTrakingShouldBeFound("auditId.equals=" + DEFAULT_AUDIT_ID);

        // Get all the auditStatusTrakingList where auditId equals to UPDATED_AUDIT_ID
        defaultAuditStatusTrakingShouldNotBeFound("auditId.equals=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByAuditIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where auditId not equals to DEFAULT_AUDIT_ID
        defaultAuditStatusTrakingShouldNotBeFound("auditId.notEquals=" + DEFAULT_AUDIT_ID);

        // Get all the auditStatusTrakingList where auditId not equals to UPDATED_AUDIT_ID
        defaultAuditStatusTrakingShouldBeFound("auditId.notEquals=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByAuditIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where auditId in DEFAULT_AUDIT_ID or UPDATED_AUDIT_ID
        defaultAuditStatusTrakingShouldBeFound("auditId.in=" + DEFAULT_AUDIT_ID + "," + UPDATED_AUDIT_ID);

        // Get all the auditStatusTrakingList where auditId equals to UPDATED_AUDIT_ID
        defaultAuditStatusTrakingShouldNotBeFound("auditId.in=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByAuditIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where auditId is not null
        defaultAuditStatusTrakingShouldBeFound("auditId.specified=true");

        // Get all the auditStatusTrakingList where auditId is null
        defaultAuditStatusTrakingShouldNotBeFound("auditId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByAuditIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where auditId is greater than or equal to DEFAULT_AUDIT_ID
        defaultAuditStatusTrakingShouldBeFound("auditId.greaterThanOrEqual=" + DEFAULT_AUDIT_ID);

        // Get all the auditStatusTrakingList where auditId is greater than or equal to UPDATED_AUDIT_ID
        defaultAuditStatusTrakingShouldNotBeFound("auditId.greaterThanOrEqual=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByAuditIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where auditId is less than or equal to DEFAULT_AUDIT_ID
        defaultAuditStatusTrakingShouldBeFound("auditId.lessThanOrEqual=" + DEFAULT_AUDIT_ID);

        // Get all the auditStatusTrakingList where auditId is less than or equal to SMALLER_AUDIT_ID
        defaultAuditStatusTrakingShouldNotBeFound("auditId.lessThanOrEqual=" + SMALLER_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByAuditIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where auditId is less than DEFAULT_AUDIT_ID
        defaultAuditStatusTrakingShouldNotBeFound("auditId.lessThan=" + DEFAULT_AUDIT_ID);

        // Get all the auditStatusTrakingList where auditId is less than UPDATED_AUDIT_ID
        defaultAuditStatusTrakingShouldBeFound("auditId.lessThan=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByAuditIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where auditId is greater than DEFAULT_AUDIT_ID
        defaultAuditStatusTrakingShouldNotBeFound("auditId.greaterThan=" + DEFAULT_AUDIT_ID);

        // Get all the auditStatusTrakingList where auditId is greater than SMALLER_AUDIT_ID
        defaultAuditStatusTrakingShouldBeFound("auditId.greaterThan=" + SMALLER_AUDIT_ID);
    }


    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where status equals to DEFAULT_STATUS
        defaultAuditStatusTrakingShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the auditStatusTrakingList where status equals to UPDATED_STATUS
        defaultAuditStatusTrakingShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where status not equals to DEFAULT_STATUS
        defaultAuditStatusTrakingShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the auditStatusTrakingList where status not equals to UPDATED_STATUS
        defaultAuditStatusTrakingShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAuditStatusTrakingShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the auditStatusTrakingList where status equals to UPDATED_STATUS
        defaultAuditStatusTrakingShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where status is not null
        defaultAuditStatusTrakingShouldBeFound("status.specified=true");

        // Get all the auditStatusTrakingList where status is null
        defaultAuditStatusTrakingShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByTracingAtIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where tracingAt equals to DEFAULT_TRACING_AT
        defaultAuditStatusTrakingShouldBeFound("tracingAt.equals=" + DEFAULT_TRACING_AT);

        // Get all the auditStatusTrakingList where tracingAt equals to UPDATED_TRACING_AT
        defaultAuditStatusTrakingShouldNotBeFound("tracingAt.equals=" + UPDATED_TRACING_AT);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByTracingAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where tracingAt not equals to DEFAULT_TRACING_AT
        defaultAuditStatusTrakingShouldNotBeFound("tracingAt.notEquals=" + DEFAULT_TRACING_AT);

        // Get all the auditStatusTrakingList where tracingAt not equals to UPDATED_TRACING_AT
        defaultAuditStatusTrakingShouldBeFound("tracingAt.notEquals=" + UPDATED_TRACING_AT);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByTracingAtIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where tracingAt in DEFAULT_TRACING_AT or UPDATED_TRACING_AT
        defaultAuditStatusTrakingShouldBeFound("tracingAt.in=" + DEFAULT_TRACING_AT + "," + UPDATED_TRACING_AT);

        // Get all the auditStatusTrakingList where tracingAt equals to UPDATED_TRACING_AT
        defaultAuditStatusTrakingShouldNotBeFound("tracingAt.in=" + UPDATED_TRACING_AT);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByTracingAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where tracingAt is not null
        defaultAuditStatusTrakingShouldBeFound("tracingAt.specified=true");

        // Get all the auditStatusTrakingList where tracingAt is null
        defaultAuditStatusTrakingShouldNotBeFound("tracingAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where userId equals to DEFAULT_USER_ID
        defaultAuditStatusTrakingShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the auditStatusTrakingList where userId equals to UPDATED_USER_ID
        defaultAuditStatusTrakingShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where userId not equals to DEFAULT_USER_ID
        defaultAuditStatusTrakingShouldNotBeFound("userId.notEquals=" + DEFAULT_USER_ID);

        // Get all the auditStatusTrakingList where userId not equals to UPDATED_USER_ID
        defaultAuditStatusTrakingShouldBeFound("userId.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultAuditStatusTrakingShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the auditStatusTrakingList where userId equals to UPDATED_USER_ID
        defaultAuditStatusTrakingShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where userId is not null
        defaultAuditStatusTrakingShouldBeFound("userId.specified=true");

        // Get all the auditStatusTrakingList where userId is null
        defaultAuditStatusTrakingShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where userId is greater than or equal to DEFAULT_USER_ID
        defaultAuditStatusTrakingShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the auditStatusTrakingList where userId is greater than or equal to UPDATED_USER_ID
        defaultAuditStatusTrakingShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where userId is less than or equal to DEFAULT_USER_ID
        defaultAuditStatusTrakingShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the auditStatusTrakingList where userId is less than or equal to SMALLER_USER_ID
        defaultAuditStatusTrakingShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where userId is less than DEFAULT_USER_ID
        defaultAuditStatusTrakingShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the auditStatusTrakingList where userId is less than UPDATED_USER_ID
        defaultAuditStatusTrakingShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where userId is greater than DEFAULT_USER_ID
        defaultAuditStatusTrakingShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the auditStatusTrakingList where userId is greater than SMALLER_USER_ID
        defaultAuditStatusTrakingShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByEditableIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where editable equals to DEFAULT_EDITABLE
        defaultAuditStatusTrakingShouldBeFound("editable.equals=" + DEFAULT_EDITABLE);

        // Get all the auditStatusTrakingList where editable equals to UPDATED_EDITABLE
        defaultAuditStatusTrakingShouldNotBeFound("editable.equals=" + UPDATED_EDITABLE);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByEditableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where editable not equals to DEFAULT_EDITABLE
        defaultAuditStatusTrakingShouldNotBeFound("editable.notEquals=" + DEFAULT_EDITABLE);

        // Get all the auditStatusTrakingList where editable not equals to UPDATED_EDITABLE
        defaultAuditStatusTrakingShouldBeFound("editable.notEquals=" + UPDATED_EDITABLE);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByEditableIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where editable in DEFAULT_EDITABLE or UPDATED_EDITABLE
        defaultAuditStatusTrakingShouldBeFound("editable.in=" + DEFAULT_EDITABLE + "," + UPDATED_EDITABLE);

        // Get all the auditStatusTrakingList where editable equals to UPDATED_EDITABLE
        defaultAuditStatusTrakingShouldNotBeFound("editable.in=" + UPDATED_EDITABLE);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByEditableIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where editable is not null
        defaultAuditStatusTrakingShouldBeFound("editable.specified=true");

        // Get all the auditStatusTrakingList where editable is null
        defaultAuditStatusTrakingShouldNotBeFound("editable.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByRecomIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where recom equals to DEFAULT_RECOM
        defaultAuditStatusTrakingShouldBeFound("recom.equals=" + DEFAULT_RECOM);

        // Get all the auditStatusTrakingList where recom equals to UPDATED_RECOM
        defaultAuditStatusTrakingShouldNotBeFound("recom.equals=" + UPDATED_RECOM);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByRecomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where recom not equals to DEFAULT_RECOM
        defaultAuditStatusTrakingShouldNotBeFound("recom.notEquals=" + DEFAULT_RECOM);

        // Get all the auditStatusTrakingList where recom not equals to UPDATED_RECOM
        defaultAuditStatusTrakingShouldBeFound("recom.notEquals=" + UPDATED_RECOM);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByRecomIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where recom in DEFAULT_RECOM or UPDATED_RECOM
        defaultAuditStatusTrakingShouldBeFound("recom.in=" + DEFAULT_RECOM + "," + UPDATED_RECOM);

        // Get all the auditStatusTrakingList where recom equals to UPDATED_RECOM
        defaultAuditStatusTrakingShouldNotBeFound("recom.in=" + UPDATED_RECOM);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingsByRecomIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingRepository.saveAndFlush(auditStatusTraking);

        // Get all the auditStatusTrakingList where recom is not null
        defaultAuditStatusTrakingShouldBeFound("recom.specified=true");

        // Get all the auditStatusTrakingList where recom is null
        defaultAuditStatusTrakingShouldNotBeFound("recom.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditStatusTrakingShouldBeFound(String filter) throws Exception {
        restAuditStatusTrakingMockMvc.perform(get("/api/audit-status-trakings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditStatusTraking.getId().intValue())))
            .andExpect(jsonPath("$.[*].auditId").value(hasItem(DEFAULT_AUDIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tracingAt").value(hasItem(DEFAULT_TRACING_AT.toString())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].editable").value(hasItem(DEFAULT_EDITABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].recom").value(hasItem(DEFAULT_RECOM.booleanValue())));

        // Check, that the count call also returns 1
        restAuditStatusTrakingMockMvc.perform(get("/api/audit-status-trakings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditStatusTrakingShouldNotBeFound(String filter) throws Exception {
        restAuditStatusTrakingMockMvc.perform(get("/api/audit-status-trakings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditStatusTrakingMockMvc.perform(get("/api/audit-status-trakings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuditStatusTraking() throws Exception {
        // Get the auditStatusTraking
        restAuditStatusTrakingMockMvc.perform(get("/api/audit-status-trakings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditStatusTraking() throws Exception {
        // Initialize the database
        auditStatusTrakingService.save(auditStatusTraking);

        int databaseSizeBeforeUpdate = auditStatusTrakingRepository.findAll().size();

        // Update the auditStatusTraking
        AuditStatusTraking updatedAuditStatusTraking = auditStatusTrakingRepository.findById(auditStatusTraking.getId()).get();
        // Disconnect from session so that the updates on updatedAuditStatusTraking are not directly saved in db
        em.detach(updatedAuditStatusTraking);
        updatedAuditStatusTraking
            .auditId(UPDATED_AUDIT_ID)
            .status(UPDATED_STATUS)
            .tracingAt(UPDATED_TRACING_AT)
            .justification(UPDATED_JUSTIFICATION)
            .userId(UPDATED_USER_ID)
            .editable(UPDATED_EDITABLE)
            .recom(UPDATED_RECOM);

        restAuditStatusTrakingMockMvc.perform(put("/api/audit-status-trakings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditStatusTraking)))
            .andExpect(status().isOk());

        // Validate the AuditStatusTraking in the database
        List<AuditStatusTraking> auditStatusTrakingList = auditStatusTrakingRepository.findAll();
        assertThat(auditStatusTrakingList).hasSize(databaseSizeBeforeUpdate);
        AuditStatusTraking testAuditStatusTraking = auditStatusTrakingList.get(auditStatusTrakingList.size() - 1);
        assertThat(testAuditStatusTraking.getAuditId()).isEqualTo(UPDATED_AUDIT_ID);
        assertThat(testAuditStatusTraking.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAuditStatusTraking.getTracingAt()).isEqualTo(UPDATED_TRACING_AT);
        assertThat(testAuditStatusTraking.getJustification()).isEqualTo(UPDATED_JUSTIFICATION);
        assertThat(testAuditStatusTraking.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAuditStatusTraking.isEditable()).isEqualTo(UPDATED_EDITABLE);
        assertThat(testAuditStatusTraking.isRecom()).isEqualTo(UPDATED_RECOM);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditStatusTraking() throws Exception {
        int databaseSizeBeforeUpdate = auditStatusTrakingRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditStatusTrakingMockMvc.perform(put("/api/audit-status-trakings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTraking)))
            .andExpect(status().isBadRequest());

        // Validate the AuditStatusTraking in the database
        List<AuditStatusTraking> auditStatusTrakingList = auditStatusTrakingRepository.findAll();
        assertThat(auditStatusTrakingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditStatusTraking() throws Exception {
        // Initialize the database
        auditStatusTrakingService.save(auditStatusTraking);

        int databaseSizeBeforeDelete = auditStatusTrakingRepository.findAll().size();

        // Delete the auditStatusTraking
        restAuditStatusTrakingMockMvc.perform(delete("/api/audit-status-trakings/{id}", auditStatusTraking.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditStatusTraking> auditStatusTrakingList = auditStatusTrakingRepository.findAll();
        assertThat(auditStatusTrakingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
