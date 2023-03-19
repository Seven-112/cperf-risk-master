package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.Audit;
import com.mshz.domain.AuditCycle;
import com.mshz.repository.AuditRepository;
import com.mshz.service.AuditService;
import com.mshz.service.dto.AuditCriteria;
import com.mshz.service.AuditQueryService;

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

import com.mshz.domain.enumeration.AuditRiskLevel;
import com.mshz.domain.enumeration.AuditType;
import com.mshz.domain.enumeration.AuditStatus;
/**
 * Integration tests for the {@link AuditResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXECUTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXECUTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_PROCESS_ID = 1L;
    private static final Long UPDATED_PROCESS_ID = 2L;
    private static final Long SMALLER_PROCESS_ID = 1L - 1L;

    private static final String DEFAULT_PROCESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PROCESS_CATEGORY_ID = 1L;
    private static final Long UPDATED_PROCESS_CATEGORY_ID = 2L;
    private static final Long SMALLER_PROCESS_CATEGORY_ID = 1L - 1L;

    private static final String DEFAULT_PROCESS_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_CATEGORY_NAME = "BBBBBBBBBB";

    private static final AuditRiskLevel DEFAULT_RISK_LEVEL = AuditRiskLevel.MINOR;
    private static final AuditRiskLevel UPDATED_RISK_LEVEL = AuditRiskLevel.MEDIUM;

    private static final AuditType DEFAULT_TYPE = AuditType.INTERNAL;
    private static final AuditType UPDATED_TYPE = AuditType.PERMANENT;

    private static final AuditStatus DEFAULT_STATUS = AuditStatus.INITIAL;
    private static final AuditStatus UPDATED_STATUS = AuditStatus.STARTED;

    private static final Long DEFAULT_RISK_ID = 1L;
    private static final Long UPDATED_RISK_ID = 2L;
    private static final Long SMALLER_RISK_ID = 1L - 1L;

    private static final String DEFAULT_RISK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RISK_NAME = "BBBBBBBBBB";

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuditQueryService auditQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditMockMvc;

    private Audit audit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audit createEntity(EntityManager em) {
        Audit audit = new Audit()
            .title(DEFAULT_TITLE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .executedAt(DEFAULT_EXECUTED_AT)
            .processId(DEFAULT_PROCESS_ID)
            .processName(DEFAULT_PROCESS_NAME)
            .processCategoryId(DEFAULT_PROCESS_CATEGORY_ID)
            .processCategoryName(DEFAULT_PROCESS_CATEGORY_NAME)
            .riskLevel(DEFAULT_RISK_LEVEL)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .riskId(DEFAULT_RISK_ID)
            .riskName(DEFAULT_RISK_NAME);
        return audit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Audit createUpdatedEntity(EntityManager em) {
        Audit audit = new Audit()
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .executedAt(UPDATED_EXECUTED_AT)
            .processId(UPDATED_PROCESS_ID)
            .processName(UPDATED_PROCESS_NAME)
            .processCategoryId(UPDATED_PROCESS_CATEGORY_ID)
            .processCategoryName(UPDATED_PROCESS_CATEGORY_NAME)
            .riskLevel(UPDATED_RISK_LEVEL)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .riskId(UPDATED_RISK_ID)
            .riskName(UPDATED_RISK_NAME);
        return audit;
    }

    @BeforeEach
    public void initTest() {
        audit = createEntity(em);
    }

    @Test
    @Transactional
    public void createAudit() throws Exception {
        int databaseSizeBeforeCreate = auditRepository.findAll().size();
        // Create the Audit
        restAuditMockMvc.perform(post("/api/audits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isCreated());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeCreate + 1);
        Audit testAudit = auditList.get(auditList.size() - 1);
        assertThat(testAudit.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAudit.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAudit.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAudit.getExecutedAt()).isEqualTo(DEFAULT_EXECUTED_AT);
        assertThat(testAudit.getProcessId()).isEqualTo(DEFAULT_PROCESS_ID);
        assertThat(testAudit.getProcessName()).isEqualTo(DEFAULT_PROCESS_NAME);
        assertThat(testAudit.getProcessCategoryId()).isEqualTo(DEFAULT_PROCESS_CATEGORY_ID);
        assertThat(testAudit.getProcessCategoryName()).isEqualTo(DEFAULT_PROCESS_CATEGORY_NAME);
        assertThat(testAudit.getRiskLevel()).isEqualTo(DEFAULT_RISK_LEVEL);
        assertThat(testAudit.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAudit.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAudit.getRiskId()).isEqualTo(DEFAULT_RISK_ID);
        assertThat(testAudit.getRiskName()).isEqualTo(DEFAULT_RISK_NAME);
    }

    @Test
    @Transactional
    public void createAuditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditRepository.findAll().size();

        // Create the Audit with an existing ID
        audit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditMockMvc.perform(post("/api/audits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAudits() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList
        restAuditMockMvc.perform(get("/api/audits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audit.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].executedAt").value(hasItem(DEFAULT_EXECUTED_AT.toString())))
            .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].processName").value(hasItem(DEFAULT_PROCESS_NAME)))
            .andExpect(jsonPath("$.[*].processCategoryId").value(hasItem(DEFAULT_PROCESS_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].processCategoryName").value(hasItem(DEFAULT_PROCESS_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].riskId").value(hasItem(DEFAULT_RISK_ID.intValue())))
            .andExpect(jsonPath("$.[*].riskName").value(hasItem(DEFAULT_RISK_NAME)));
    }
    
    @Test
    @Transactional
    public void getAudit() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get the audit
        restAuditMockMvc.perform(get("/api/audits/{id}", audit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(audit.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.executedAt").value(DEFAULT_EXECUTED_AT.toString()))
            .andExpect(jsonPath("$.processId").value(DEFAULT_PROCESS_ID.intValue()))
            .andExpect(jsonPath("$.processName").value(DEFAULT_PROCESS_NAME))
            .andExpect(jsonPath("$.processCategoryId").value(DEFAULT_PROCESS_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.processCategoryName").value(DEFAULT_PROCESS_CATEGORY_NAME))
            .andExpect(jsonPath("$.riskLevel").value(DEFAULT_RISK_LEVEL.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.riskId").value(DEFAULT_RISK_ID.intValue()))
            .andExpect(jsonPath("$.riskName").value(DEFAULT_RISK_NAME));
    }


    @Test
    @Transactional
    public void getAuditsByIdFiltering() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        Long id = audit.getId();

        defaultAuditShouldBeFound("id.equals=" + id);
        defaultAuditShouldNotBeFound("id.notEquals=" + id);

        defaultAuditShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where title equals to DEFAULT_TITLE
        defaultAuditShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the auditList where title equals to UPDATED_TITLE
        defaultAuditShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAuditsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where title not equals to DEFAULT_TITLE
        defaultAuditShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the auditList where title not equals to UPDATED_TITLE
        defaultAuditShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAuditsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultAuditShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the auditList where title equals to UPDATED_TITLE
        defaultAuditShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAuditsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where title is not null
        defaultAuditShouldBeFound("title.specified=true");

        // Get all the auditList where title is null
        defaultAuditShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditsByTitleContainsSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where title contains DEFAULT_TITLE
        defaultAuditShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the auditList where title contains UPDATED_TITLE
        defaultAuditShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllAuditsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where title does not contain DEFAULT_TITLE
        defaultAuditShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the auditList where title does not contain UPDATED_TITLE
        defaultAuditShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllAuditsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where startDate equals to DEFAULT_START_DATE
        defaultAuditShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the auditList where startDate equals to UPDATED_START_DATE
        defaultAuditShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllAuditsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where startDate not equals to DEFAULT_START_DATE
        defaultAuditShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the auditList where startDate not equals to UPDATED_START_DATE
        defaultAuditShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllAuditsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultAuditShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the auditList where startDate equals to UPDATED_START_DATE
        defaultAuditShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllAuditsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where startDate is not null
        defaultAuditShouldBeFound("startDate.specified=true");

        // Get all the auditList where startDate is null
        defaultAuditShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where endDate equals to DEFAULT_END_DATE
        defaultAuditShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the auditList where endDate equals to UPDATED_END_DATE
        defaultAuditShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllAuditsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where endDate not equals to DEFAULT_END_DATE
        defaultAuditShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the auditList where endDate not equals to UPDATED_END_DATE
        defaultAuditShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllAuditsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultAuditShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the auditList where endDate equals to UPDATED_END_DATE
        defaultAuditShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllAuditsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where endDate is not null
        defaultAuditShouldBeFound("endDate.specified=true");

        // Get all the auditList where endDate is null
        defaultAuditShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByExecutedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where executedAt equals to DEFAULT_EXECUTED_AT
        defaultAuditShouldBeFound("executedAt.equals=" + DEFAULT_EXECUTED_AT);

        // Get all the auditList where executedAt equals to UPDATED_EXECUTED_AT
        defaultAuditShouldNotBeFound("executedAt.equals=" + UPDATED_EXECUTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditsByExecutedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where executedAt not equals to DEFAULT_EXECUTED_AT
        defaultAuditShouldNotBeFound("executedAt.notEquals=" + DEFAULT_EXECUTED_AT);

        // Get all the auditList where executedAt not equals to UPDATED_EXECUTED_AT
        defaultAuditShouldBeFound("executedAt.notEquals=" + UPDATED_EXECUTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditsByExecutedAtIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where executedAt in DEFAULT_EXECUTED_AT or UPDATED_EXECUTED_AT
        defaultAuditShouldBeFound("executedAt.in=" + DEFAULT_EXECUTED_AT + "," + UPDATED_EXECUTED_AT);

        // Get all the auditList where executedAt equals to UPDATED_EXECUTED_AT
        defaultAuditShouldNotBeFound("executedAt.in=" + UPDATED_EXECUTED_AT);
    }

    @Test
    @Transactional
    public void getAllAuditsByExecutedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where executedAt is not null
        defaultAuditShouldBeFound("executedAt.specified=true");

        // Get all the auditList where executedAt is null
        defaultAuditShouldNotBeFound("executedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processId equals to DEFAULT_PROCESS_ID
        defaultAuditShouldBeFound("processId.equals=" + DEFAULT_PROCESS_ID);

        // Get all the auditList where processId equals to UPDATED_PROCESS_ID
        defaultAuditShouldNotBeFound("processId.equals=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processId not equals to DEFAULT_PROCESS_ID
        defaultAuditShouldNotBeFound("processId.notEquals=" + DEFAULT_PROCESS_ID);

        // Get all the auditList where processId not equals to UPDATED_PROCESS_ID
        defaultAuditShouldBeFound("processId.notEquals=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processId in DEFAULT_PROCESS_ID or UPDATED_PROCESS_ID
        defaultAuditShouldBeFound("processId.in=" + DEFAULT_PROCESS_ID + "," + UPDATED_PROCESS_ID);

        // Get all the auditList where processId equals to UPDATED_PROCESS_ID
        defaultAuditShouldNotBeFound("processId.in=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processId is not null
        defaultAuditShouldBeFound("processId.specified=true");

        // Get all the auditList where processId is null
        defaultAuditShouldNotBeFound("processId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processId is greater than or equal to DEFAULT_PROCESS_ID
        defaultAuditShouldBeFound("processId.greaterThanOrEqual=" + DEFAULT_PROCESS_ID);

        // Get all the auditList where processId is greater than or equal to UPDATED_PROCESS_ID
        defaultAuditShouldNotBeFound("processId.greaterThanOrEqual=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processId is less than or equal to DEFAULT_PROCESS_ID
        defaultAuditShouldBeFound("processId.lessThanOrEqual=" + DEFAULT_PROCESS_ID);

        // Get all the auditList where processId is less than or equal to SMALLER_PROCESS_ID
        defaultAuditShouldNotBeFound("processId.lessThanOrEqual=" + SMALLER_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processId is less than DEFAULT_PROCESS_ID
        defaultAuditShouldNotBeFound("processId.lessThan=" + DEFAULT_PROCESS_ID);

        // Get all the auditList where processId is less than UPDATED_PROCESS_ID
        defaultAuditShouldBeFound("processId.lessThan=" + UPDATED_PROCESS_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processId is greater than DEFAULT_PROCESS_ID
        defaultAuditShouldNotBeFound("processId.greaterThan=" + DEFAULT_PROCESS_ID);

        // Get all the auditList where processId is greater than SMALLER_PROCESS_ID
        defaultAuditShouldBeFound("processId.greaterThan=" + SMALLER_PROCESS_ID);
    }


    @Test
    @Transactional
    public void getAllAuditsByProcessNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processName equals to DEFAULT_PROCESS_NAME
        defaultAuditShouldBeFound("processName.equals=" + DEFAULT_PROCESS_NAME);

        // Get all the auditList where processName equals to UPDATED_PROCESS_NAME
        defaultAuditShouldNotBeFound("processName.equals=" + UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processName not equals to DEFAULT_PROCESS_NAME
        defaultAuditShouldNotBeFound("processName.notEquals=" + DEFAULT_PROCESS_NAME);

        // Get all the auditList where processName not equals to UPDATED_PROCESS_NAME
        defaultAuditShouldBeFound("processName.notEquals=" + UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processName in DEFAULT_PROCESS_NAME or UPDATED_PROCESS_NAME
        defaultAuditShouldBeFound("processName.in=" + DEFAULT_PROCESS_NAME + "," + UPDATED_PROCESS_NAME);

        // Get all the auditList where processName equals to UPDATED_PROCESS_NAME
        defaultAuditShouldNotBeFound("processName.in=" + UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processName is not null
        defaultAuditShouldBeFound("processName.specified=true");

        // Get all the auditList where processName is null
        defaultAuditShouldNotBeFound("processName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditsByProcessNameContainsSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processName contains DEFAULT_PROCESS_NAME
        defaultAuditShouldBeFound("processName.contains=" + DEFAULT_PROCESS_NAME);

        // Get all the auditList where processName contains UPDATED_PROCESS_NAME
        defaultAuditShouldNotBeFound("processName.contains=" + UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processName does not contain DEFAULT_PROCESS_NAME
        defaultAuditShouldNotBeFound("processName.doesNotContain=" + DEFAULT_PROCESS_NAME);

        // Get all the auditList where processName does not contain UPDATED_PROCESS_NAME
        defaultAuditShouldBeFound("processName.doesNotContain=" + UPDATED_PROCESS_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryId equals to DEFAULT_PROCESS_CATEGORY_ID
        defaultAuditShouldBeFound("processCategoryId.equals=" + DEFAULT_PROCESS_CATEGORY_ID);

        // Get all the auditList where processCategoryId equals to UPDATED_PROCESS_CATEGORY_ID
        defaultAuditShouldNotBeFound("processCategoryId.equals=" + UPDATED_PROCESS_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryId not equals to DEFAULT_PROCESS_CATEGORY_ID
        defaultAuditShouldNotBeFound("processCategoryId.notEquals=" + DEFAULT_PROCESS_CATEGORY_ID);

        // Get all the auditList where processCategoryId not equals to UPDATED_PROCESS_CATEGORY_ID
        defaultAuditShouldBeFound("processCategoryId.notEquals=" + UPDATED_PROCESS_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryId in DEFAULT_PROCESS_CATEGORY_ID or UPDATED_PROCESS_CATEGORY_ID
        defaultAuditShouldBeFound("processCategoryId.in=" + DEFAULT_PROCESS_CATEGORY_ID + "," + UPDATED_PROCESS_CATEGORY_ID);

        // Get all the auditList where processCategoryId equals to UPDATED_PROCESS_CATEGORY_ID
        defaultAuditShouldNotBeFound("processCategoryId.in=" + UPDATED_PROCESS_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryId is not null
        defaultAuditShouldBeFound("processCategoryId.specified=true");

        // Get all the auditList where processCategoryId is null
        defaultAuditShouldNotBeFound("processCategoryId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryId is greater than or equal to DEFAULT_PROCESS_CATEGORY_ID
        defaultAuditShouldBeFound("processCategoryId.greaterThanOrEqual=" + DEFAULT_PROCESS_CATEGORY_ID);

        // Get all the auditList where processCategoryId is greater than or equal to UPDATED_PROCESS_CATEGORY_ID
        defaultAuditShouldNotBeFound("processCategoryId.greaterThanOrEqual=" + UPDATED_PROCESS_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryId is less than or equal to DEFAULT_PROCESS_CATEGORY_ID
        defaultAuditShouldBeFound("processCategoryId.lessThanOrEqual=" + DEFAULT_PROCESS_CATEGORY_ID);

        // Get all the auditList where processCategoryId is less than or equal to SMALLER_PROCESS_CATEGORY_ID
        defaultAuditShouldNotBeFound("processCategoryId.lessThanOrEqual=" + SMALLER_PROCESS_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryId is less than DEFAULT_PROCESS_CATEGORY_ID
        defaultAuditShouldNotBeFound("processCategoryId.lessThan=" + DEFAULT_PROCESS_CATEGORY_ID);

        // Get all the auditList where processCategoryId is less than UPDATED_PROCESS_CATEGORY_ID
        defaultAuditShouldBeFound("processCategoryId.lessThan=" + UPDATED_PROCESS_CATEGORY_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryId is greater than DEFAULT_PROCESS_CATEGORY_ID
        defaultAuditShouldNotBeFound("processCategoryId.greaterThan=" + DEFAULT_PROCESS_CATEGORY_ID);

        // Get all the auditList where processCategoryId is greater than SMALLER_PROCESS_CATEGORY_ID
        defaultAuditShouldBeFound("processCategoryId.greaterThan=" + SMALLER_PROCESS_CATEGORY_ID);
    }


    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryName equals to DEFAULT_PROCESS_CATEGORY_NAME
        defaultAuditShouldBeFound("processCategoryName.equals=" + DEFAULT_PROCESS_CATEGORY_NAME);

        // Get all the auditList where processCategoryName equals to UPDATED_PROCESS_CATEGORY_NAME
        defaultAuditShouldNotBeFound("processCategoryName.equals=" + UPDATED_PROCESS_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryName not equals to DEFAULT_PROCESS_CATEGORY_NAME
        defaultAuditShouldNotBeFound("processCategoryName.notEquals=" + DEFAULT_PROCESS_CATEGORY_NAME);

        // Get all the auditList where processCategoryName not equals to UPDATED_PROCESS_CATEGORY_NAME
        defaultAuditShouldBeFound("processCategoryName.notEquals=" + UPDATED_PROCESS_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryName in DEFAULT_PROCESS_CATEGORY_NAME or UPDATED_PROCESS_CATEGORY_NAME
        defaultAuditShouldBeFound("processCategoryName.in=" + DEFAULT_PROCESS_CATEGORY_NAME + "," + UPDATED_PROCESS_CATEGORY_NAME);

        // Get all the auditList where processCategoryName equals to UPDATED_PROCESS_CATEGORY_NAME
        defaultAuditShouldNotBeFound("processCategoryName.in=" + UPDATED_PROCESS_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryName is not null
        defaultAuditShouldBeFound("processCategoryName.specified=true");

        // Get all the auditList where processCategoryName is null
        defaultAuditShouldNotBeFound("processCategoryName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditsByProcessCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryName contains DEFAULT_PROCESS_CATEGORY_NAME
        defaultAuditShouldBeFound("processCategoryName.contains=" + DEFAULT_PROCESS_CATEGORY_NAME);

        // Get all the auditList where processCategoryName contains UPDATED_PROCESS_CATEGORY_NAME
        defaultAuditShouldNotBeFound("processCategoryName.contains=" + UPDATED_PROCESS_CATEGORY_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByProcessCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where processCategoryName does not contain DEFAULT_PROCESS_CATEGORY_NAME
        defaultAuditShouldNotBeFound("processCategoryName.doesNotContain=" + DEFAULT_PROCESS_CATEGORY_NAME);

        // Get all the auditList where processCategoryName does not contain UPDATED_PROCESS_CATEGORY_NAME
        defaultAuditShouldBeFound("processCategoryName.doesNotContain=" + UPDATED_PROCESS_CATEGORY_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditsByRiskLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskLevel equals to DEFAULT_RISK_LEVEL
        defaultAuditShouldBeFound("riskLevel.equals=" + DEFAULT_RISK_LEVEL);

        // Get all the auditList where riskLevel equals to UPDATED_RISK_LEVEL
        defaultAuditShouldNotBeFound("riskLevel.equals=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskLevel not equals to DEFAULT_RISK_LEVEL
        defaultAuditShouldNotBeFound("riskLevel.notEquals=" + DEFAULT_RISK_LEVEL);

        // Get all the auditList where riskLevel not equals to UPDATED_RISK_LEVEL
        defaultAuditShouldBeFound("riskLevel.notEquals=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskLevelIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskLevel in DEFAULT_RISK_LEVEL or UPDATED_RISK_LEVEL
        defaultAuditShouldBeFound("riskLevel.in=" + DEFAULT_RISK_LEVEL + "," + UPDATED_RISK_LEVEL);

        // Get all the auditList where riskLevel equals to UPDATED_RISK_LEVEL
        defaultAuditShouldNotBeFound("riskLevel.in=" + UPDATED_RISK_LEVEL);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskLevel is not null
        defaultAuditShouldBeFound("riskLevel.specified=true");

        // Get all the auditList where riskLevel is null
        defaultAuditShouldNotBeFound("riskLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where type equals to DEFAULT_TYPE
        defaultAuditShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the auditList where type equals to UPDATED_TYPE
        defaultAuditShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAuditsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where type not equals to DEFAULT_TYPE
        defaultAuditShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the auditList where type not equals to UPDATED_TYPE
        defaultAuditShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAuditsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAuditShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the auditList where type equals to UPDATED_TYPE
        defaultAuditShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAuditsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where type is not null
        defaultAuditShouldBeFound("type.specified=true");

        // Get all the auditList where type is null
        defaultAuditShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where status equals to DEFAULT_STATUS
        defaultAuditShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the auditList where status equals to UPDATED_STATUS
        defaultAuditShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where status not equals to DEFAULT_STATUS
        defaultAuditShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the auditList where status not equals to UPDATED_STATUS
        defaultAuditShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultAuditShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the auditList where status equals to UPDATED_STATUS
        defaultAuditShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllAuditsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where status is not null
        defaultAuditShouldBeFound("status.specified=true");

        // Get all the auditList where status is null
        defaultAuditShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskId equals to DEFAULT_RISK_ID
        defaultAuditShouldBeFound("riskId.equals=" + DEFAULT_RISK_ID);

        // Get all the auditList where riskId equals to UPDATED_RISK_ID
        defaultAuditShouldNotBeFound("riskId.equals=" + UPDATED_RISK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskId not equals to DEFAULT_RISK_ID
        defaultAuditShouldNotBeFound("riskId.notEquals=" + DEFAULT_RISK_ID);

        // Get all the auditList where riskId not equals to UPDATED_RISK_ID
        defaultAuditShouldBeFound("riskId.notEquals=" + UPDATED_RISK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskId in DEFAULT_RISK_ID or UPDATED_RISK_ID
        defaultAuditShouldBeFound("riskId.in=" + DEFAULT_RISK_ID + "," + UPDATED_RISK_ID);

        // Get all the auditList where riskId equals to UPDATED_RISK_ID
        defaultAuditShouldNotBeFound("riskId.in=" + UPDATED_RISK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskId is not null
        defaultAuditShouldBeFound("riskId.specified=true");

        // Get all the auditList where riskId is null
        defaultAuditShouldNotBeFound("riskId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskId is greater than or equal to DEFAULT_RISK_ID
        defaultAuditShouldBeFound("riskId.greaterThanOrEqual=" + DEFAULT_RISK_ID);

        // Get all the auditList where riskId is greater than or equal to UPDATED_RISK_ID
        defaultAuditShouldNotBeFound("riskId.greaterThanOrEqual=" + UPDATED_RISK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskId is less than or equal to DEFAULT_RISK_ID
        defaultAuditShouldBeFound("riskId.lessThanOrEqual=" + DEFAULT_RISK_ID);

        // Get all the auditList where riskId is less than or equal to SMALLER_RISK_ID
        defaultAuditShouldNotBeFound("riskId.lessThanOrEqual=" + SMALLER_RISK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskId is less than DEFAULT_RISK_ID
        defaultAuditShouldNotBeFound("riskId.lessThan=" + DEFAULT_RISK_ID);

        // Get all the auditList where riskId is less than UPDATED_RISK_ID
        defaultAuditShouldBeFound("riskId.lessThan=" + UPDATED_RISK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskId is greater than DEFAULT_RISK_ID
        defaultAuditShouldNotBeFound("riskId.greaterThan=" + DEFAULT_RISK_ID);

        // Get all the auditList where riskId is greater than SMALLER_RISK_ID
        defaultAuditShouldBeFound("riskId.greaterThan=" + SMALLER_RISK_ID);
    }


    @Test
    @Transactional
    public void getAllAuditsByRiskNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskName equals to DEFAULT_RISK_NAME
        defaultAuditShouldBeFound("riskName.equals=" + DEFAULT_RISK_NAME);

        // Get all the auditList where riskName equals to UPDATED_RISK_NAME
        defaultAuditShouldNotBeFound("riskName.equals=" + UPDATED_RISK_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskName not equals to DEFAULT_RISK_NAME
        defaultAuditShouldNotBeFound("riskName.notEquals=" + DEFAULT_RISK_NAME);

        // Get all the auditList where riskName not equals to UPDATED_RISK_NAME
        defaultAuditShouldBeFound("riskName.notEquals=" + UPDATED_RISK_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskName in DEFAULT_RISK_NAME or UPDATED_RISK_NAME
        defaultAuditShouldBeFound("riskName.in=" + DEFAULT_RISK_NAME + "," + UPDATED_RISK_NAME);

        // Get all the auditList where riskName equals to UPDATED_RISK_NAME
        defaultAuditShouldNotBeFound("riskName.in=" + UPDATED_RISK_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskName is not null
        defaultAuditShouldBeFound("riskName.specified=true");

        // Get all the auditList where riskName is null
        defaultAuditShouldNotBeFound("riskName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditsByRiskNameContainsSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskName contains DEFAULT_RISK_NAME
        defaultAuditShouldBeFound("riskName.contains=" + DEFAULT_RISK_NAME);

        // Get all the auditList where riskName contains UPDATED_RISK_NAME
        defaultAuditShouldNotBeFound("riskName.contains=" + UPDATED_RISK_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditsByRiskNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);

        // Get all the auditList where riskName does not contain DEFAULT_RISK_NAME
        defaultAuditShouldNotBeFound("riskName.doesNotContain=" + DEFAULT_RISK_NAME);

        // Get all the auditList where riskName does not contain UPDATED_RISK_NAME
        defaultAuditShouldBeFound("riskName.doesNotContain=" + UPDATED_RISK_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditsByCycleIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRepository.saveAndFlush(audit);
        AuditCycle cycle = AuditCycleResourceIT.createEntity(em);
        em.persist(cycle);
        em.flush();
        audit.setCycle(cycle);
        auditRepository.saveAndFlush(audit);
        Long cycleId = cycle.getId();

        // Get all the auditList where cycle equals to cycleId
        defaultAuditShouldBeFound("cycleId.equals=" + cycleId);

        // Get all the auditList where cycle equals to cycleId + 1
        defaultAuditShouldNotBeFound("cycleId.equals=" + (cycleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditShouldBeFound(String filter) throws Exception {
        restAuditMockMvc.perform(get("/api/audits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(audit.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].executedAt").value(hasItem(DEFAULT_EXECUTED_AT.toString())))
            .andExpect(jsonPath("$.[*].processId").value(hasItem(DEFAULT_PROCESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].processName").value(hasItem(DEFAULT_PROCESS_NAME)))
            .andExpect(jsonPath("$.[*].processCategoryId").value(hasItem(DEFAULT_PROCESS_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].processCategoryName").value(hasItem(DEFAULT_PROCESS_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].riskId").value(hasItem(DEFAULT_RISK_ID.intValue())))
            .andExpect(jsonPath("$.[*].riskName").value(hasItem(DEFAULT_RISK_NAME)));

        // Check, that the count call also returns 1
        restAuditMockMvc.perform(get("/api/audits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditShouldNotBeFound(String filter) throws Exception {
        restAuditMockMvc.perform(get("/api/audits?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditMockMvc.perform(get("/api/audits/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAudit() throws Exception {
        // Get the audit
        restAuditMockMvc.perform(get("/api/audits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAudit() throws Exception {
        // Initialize the database
        auditService.save(audit);

        int databaseSizeBeforeUpdate = auditRepository.findAll().size();

        // Update the audit
        Audit updatedAudit = auditRepository.findById(audit.getId()).get();
        // Disconnect from session so that the updates on updatedAudit are not directly saved in db
        em.detach(updatedAudit);
        updatedAudit
            .title(UPDATED_TITLE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .executedAt(UPDATED_EXECUTED_AT)
            .processId(UPDATED_PROCESS_ID)
            .processName(UPDATED_PROCESS_NAME)
            .processCategoryId(UPDATED_PROCESS_CATEGORY_ID)
            .processCategoryName(UPDATED_PROCESS_CATEGORY_NAME)
            .riskLevel(UPDATED_RISK_LEVEL)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .riskId(UPDATED_RISK_ID)
            .riskName(UPDATED_RISK_NAME);

        restAuditMockMvc.perform(put("/api/audits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAudit)))
            .andExpect(status().isOk());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);
        Audit testAudit = auditList.get(auditList.size() - 1);
        assertThat(testAudit.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAudit.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAudit.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAudit.getExecutedAt()).isEqualTo(UPDATED_EXECUTED_AT);
        assertThat(testAudit.getProcessId()).isEqualTo(UPDATED_PROCESS_ID);
        assertThat(testAudit.getProcessName()).isEqualTo(UPDATED_PROCESS_NAME);
        assertThat(testAudit.getProcessCategoryId()).isEqualTo(UPDATED_PROCESS_CATEGORY_ID);
        assertThat(testAudit.getProcessCategoryName()).isEqualTo(UPDATED_PROCESS_CATEGORY_NAME);
        assertThat(testAudit.getRiskLevel()).isEqualTo(UPDATED_RISK_LEVEL);
        assertThat(testAudit.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAudit.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAudit.getRiskId()).isEqualTo(UPDATED_RISK_ID);
        assertThat(testAudit.getRiskName()).isEqualTo(UPDATED_RISK_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAudit() throws Exception {
        int databaseSizeBeforeUpdate = auditRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditMockMvc.perform(put("/api/audits")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(audit)))
            .andExpect(status().isBadRequest());

        // Validate the Audit in the database
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAudit() throws Exception {
        // Initialize the database
        auditService.save(audit);

        int databaseSizeBeforeDelete = auditRepository.findAll().size();

        // Delete the audit
        restAuditMockMvc.perform(delete("/api/audits/{id}", audit.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Audit> auditList = auditRepository.findAll();
        assertThat(auditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
