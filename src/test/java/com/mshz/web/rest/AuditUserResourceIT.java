package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.AuditUser;
import com.mshz.repository.AuditUserRepository;
import com.mshz.service.AuditUserService;
import com.mshz.service.dto.AuditUserCriteria;
import com.mshz.service.AuditUserQueryService;

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

import com.mshz.domain.enumeration.AuditUserRole;
/**
 * Integration tests for the {@link AuditUserResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditUserResourceIT {

    private static final Long DEFAULT_AUDIT_ID = 1L;
    private static final Long UPDATED_AUDIT_ID = 2L;
    private static final Long SMALLER_AUDIT_ID = 1L - 1L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String DEFAULT_USER_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_USER_EMAIL = "BBBBBBBBBB";

    private static final AuditUserRole DEFAULT_ROLE = AuditUserRole.EXECUTOR;
    private static final AuditUserRole UPDATED_ROLE = AuditUserRole.SUBMITOR;

    @Autowired
    private AuditUserRepository auditUserRepository;

    @Autowired
    private AuditUserService auditUserService;

    @Autowired
    private AuditUserQueryService auditUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditUserMockMvc;

    private AuditUser auditUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditUser createEntity(EntityManager em) {
        AuditUser auditUser = new AuditUser()
            .auditId(DEFAULT_AUDIT_ID)
            .userId(DEFAULT_USER_ID)
            .userFullName(DEFAULT_USER_FULL_NAME)
            .userEmail(DEFAULT_USER_EMAIL)
            .role(DEFAULT_ROLE);
        return auditUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditUser createUpdatedEntity(EntityManager em) {
        AuditUser auditUser = new AuditUser()
            .auditId(UPDATED_AUDIT_ID)
            .userId(UPDATED_USER_ID)
            .userFullName(UPDATED_USER_FULL_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .role(UPDATED_ROLE);
        return auditUser;
    }

    @BeforeEach
    public void initTest() {
        auditUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditUser() throws Exception {
        int databaseSizeBeforeCreate = auditUserRepository.findAll().size();
        // Create the AuditUser
        restAuditUserMockMvc.perform(post("/api/audit-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditUser)))
            .andExpect(status().isCreated());

        // Validate the AuditUser in the database
        List<AuditUser> auditUserList = auditUserRepository.findAll();
        assertThat(auditUserList).hasSize(databaseSizeBeforeCreate + 1);
        AuditUser testAuditUser = auditUserList.get(auditUserList.size() - 1);
        assertThat(testAuditUser.getAuditId()).isEqualTo(DEFAULT_AUDIT_ID);
        assertThat(testAuditUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAuditUser.getUserFullName()).isEqualTo(DEFAULT_USER_FULL_NAME);
        assertThat(testAuditUser.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testAuditUser.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createAuditUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditUserRepository.findAll().size();

        // Create the AuditUser with an existing ID
        auditUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditUserMockMvc.perform(post("/api/audit-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditUser)))
            .andExpect(status().isBadRequest());

        // Validate the AuditUser in the database
        List<AuditUser> auditUserList = auditUserRepository.findAll();
        assertThat(auditUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAuditUsers() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList
        restAuditUserMockMvc.perform(get("/api/audit-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].auditId").value(hasItem(DEFAULT_AUDIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userFullName").value(hasItem(DEFAULT_USER_FULL_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }
    
    @Test
    @Transactional
    public void getAuditUser() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get the auditUser
        restAuditUserMockMvc.perform(get("/api/audit-users/{id}", auditUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditUser.getId().intValue()))
            .andExpect(jsonPath("$.auditId").value(DEFAULT_AUDIT_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.userFullName").value(DEFAULT_USER_FULL_NAME))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }


    @Test
    @Transactional
    public void getAuditUsersByIdFiltering() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        Long id = auditUser.getId();

        defaultAuditUserShouldBeFound("id.equals=" + id);
        defaultAuditUserShouldNotBeFound("id.notEquals=" + id);

        defaultAuditUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditUserShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditUsersByAuditIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where auditId equals to DEFAULT_AUDIT_ID
        defaultAuditUserShouldBeFound("auditId.equals=" + DEFAULT_AUDIT_ID);

        // Get all the auditUserList where auditId equals to UPDATED_AUDIT_ID
        defaultAuditUserShouldNotBeFound("auditId.equals=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByAuditIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where auditId not equals to DEFAULT_AUDIT_ID
        defaultAuditUserShouldNotBeFound("auditId.notEquals=" + DEFAULT_AUDIT_ID);

        // Get all the auditUserList where auditId not equals to UPDATED_AUDIT_ID
        defaultAuditUserShouldBeFound("auditId.notEquals=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByAuditIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where auditId in DEFAULT_AUDIT_ID or UPDATED_AUDIT_ID
        defaultAuditUserShouldBeFound("auditId.in=" + DEFAULT_AUDIT_ID + "," + UPDATED_AUDIT_ID);

        // Get all the auditUserList where auditId equals to UPDATED_AUDIT_ID
        defaultAuditUserShouldNotBeFound("auditId.in=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByAuditIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where auditId is not null
        defaultAuditUserShouldBeFound("auditId.specified=true");

        // Get all the auditUserList where auditId is null
        defaultAuditUserShouldNotBeFound("auditId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditUsersByAuditIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where auditId is greater than or equal to DEFAULT_AUDIT_ID
        defaultAuditUserShouldBeFound("auditId.greaterThanOrEqual=" + DEFAULT_AUDIT_ID);

        // Get all the auditUserList where auditId is greater than or equal to UPDATED_AUDIT_ID
        defaultAuditUserShouldNotBeFound("auditId.greaterThanOrEqual=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByAuditIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where auditId is less than or equal to DEFAULT_AUDIT_ID
        defaultAuditUserShouldBeFound("auditId.lessThanOrEqual=" + DEFAULT_AUDIT_ID);

        // Get all the auditUserList where auditId is less than or equal to SMALLER_AUDIT_ID
        defaultAuditUserShouldNotBeFound("auditId.lessThanOrEqual=" + SMALLER_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByAuditIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where auditId is less than DEFAULT_AUDIT_ID
        defaultAuditUserShouldNotBeFound("auditId.lessThan=" + DEFAULT_AUDIT_ID);

        // Get all the auditUserList where auditId is less than UPDATED_AUDIT_ID
        defaultAuditUserShouldBeFound("auditId.lessThan=" + UPDATED_AUDIT_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByAuditIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where auditId is greater than DEFAULT_AUDIT_ID
        defaultAuditUserShouldNotBeFound("auditId.greaterThan=" + DEFAULT_AUDIT_ID);

        // Get all the auditUserList where auditId is greater than SMALLER_AUDIT_ID
        defaultAuditUserShouldBeFound("auditId.greaterThan=" + SMALLER_AUDIT_ID);
    }


    @Test
    @Transactional
    public void getAllAuditUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userId equals to DEFAULT_USER_ID
        defaultAuditUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the auditUserList where userId equals to UPDATED_USER_ID
        defaultAuditUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userId not equals to DEFAULT_USER_ID
        defaultAuditUserShouldNotBeFound("userId.notEquals=" + DEFAULT_USER_ID);

        // Get all the auditUserList where userId not equals to UPDATED_USER_ID
        defaultAuditUserShouldBeFound("userId.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultAuditUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the auditUserList where userId equals to UPDATED_USER_ID
        defaultAuditUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userId is not null
        defaultAuditUserShouldBeFound("userId.specified=true");

        // Get all the auditUserList where userId is null
        defaultAuditUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userId is greater than or equal to DEFAULT_USER_ID
        defaultAuditUserShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the auditUserList where userId is greater than or equal to UPDATED_USER_ID
        defaultAuditUserShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userId is less than or equal to DEFAULT_USER_ID
        defaultAuditUserShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the auditUserList where userId is less than or equal to SMALLER_USER_ID
        defaultAuditUserShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userId is less than DEFAULT_USER_ID
        defaultAuditUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the auditUserList where userId is less than UPDATED_USER_ID
        defaultAuditUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userId is greater than DEFAULT_USER_ID
        defaultAuditUserShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the auditUserList where userId is greater than SMALLER_USER_ID
        defaultAuditUserShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllAuditUsersByUserFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userFullName equals to DEFAULT_USER_FULL_NAME
        defaultAuditUserShouldBeFound("userFullName.equals=" + DEFAULT_USER_FULL_NAME);

        // Get all the auditUserList where userFullName equals to UPDATED_USER_FULL_NAME
        defaultAuditUserShouldNotBeFound("userFullName.equals=" + UPDATED_USER_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userFullName not equals to DEFAULT_USER_FULL_NAME
        defaultAuditUserShouldNotBeFound("userFullName.notEquals=" + DEFAULT_USER_FULL_NAME);

        // Get all the auditUserList where userFullName not equals to UPDATED_USER_FULL_NAME
        defaultAuditUserShouldBeFound("userFullName.notEquals=" + UPDATED_USER_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userFullName in DEFAULT_USER_FULL_NAME or UPDATED_USER_FULL_NAME
        defaultAuditUserShouldBeFound("userFullName.in=" + DEFAULT_USER_FULL_NAME + "," + UPDATED_USER_FULL_NAME);

        // Get all the auditUserList where userFullName equals to UPDATED_USER_FULL_NAME
        defaultAuditUserShouldNotBeFound("userFullName.in=" + UPDATED_USER_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userFullName is not null
        defaultAuditUserShouldBeFound("userFullName.specified=true");

        // Get all the auditUserList where userFullName is null
        defaultAuditUserShouldNotBeFound("userFullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditUsersByUserFullNameContainsSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userFullName contains DEFAULT_USER_FULL_NAME
        defaultAuditUserShouldBeFound("userFullName.contains=" + DEFAULT_USER_FULL_NAME);

        // Get all the auditUserList where userFullName contains UPDATED_USER_FULL_NAME
        defaultAuditUserShouldNotBeFound("userFullName.contains=" + UPDATED_USER_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userFullName does not contain DEFAULT_USER_FULL_NAME
        defaultAuditUserShouldNotBeFound("userFullName.doesNotContain=" + DEFAULT_USER_FULL_NAME);

        // Get all the auditUserList where userFullName does not contain UPDATED_USER_FULL_NAME
        defaultAuditUserShouldBeFound("userFullName.doesNotContain=" + UPDATED_USER_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditUsersByUserEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userEmail equals to DEFAULT_USER_EMAIL
        defaultAuditUserShouldBeFound("userEmail.equals=" + DEFAULT_USER_EMAIL);

        // Get all the auditUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultAuditUserShouldNotBeFound("userEmail.equals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userEmail not equals to DEFAULT_USER_EMAIL
        defaultAuditUserShouldNotBeFound("userEmail.notEquals=" + DEFAULT_USER_EMAIL);

        // Get all the auditUserList where userEmail not equals to UPDATED_USER_EMAIL
        defaultAuditUserShouldBeFound("userEmail.notEquals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserEmailIsInShouldWork() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userEmail in DEFAULT_USER_EMAIL or UPDATED_USER_EMAIL
        defaultAuditUserShouldBeFound("userEmail.in=" + DEFAULT_USER_EMAIL + "," + UPDATED_USER_EMAIL);

        // Get all the auditUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultAuditUserShouldNotBeFound("userEmail.in=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userEmail is not null
        defaultAuditUserShouldBeFound("userEmail.specified=true");

        // Get all the auditUserList where userEmail is null
        defaultAuditUserShouldNotBeFound("userEmail.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditUsersByUserEmailContainsSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userEmail contains DEFAULT_USER_EMAIL
        defaultAuditUserShouldBeFound("userEmail.contains=" + DEFAULT_USER_EMAIL);

        // Get all the auditUserList where userEmail contains UPDATED_USER_EMAIL
        defaultAuditUserShouldNotBeFound("userEmail.contains=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByUserEmailNotContainsSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where userEmail does not contain DEFAULT_USER_EMAIL
        defaultAuditUserShouldNotBeFound("userEmail.doesNotContain=" + DEFAULT_USER_EMAIL);

        // Get all the auditUserList where userEmail does not contain UPDATED_USER_EMAIL
        defaultAuditUserShouldBeFound("userEmail.doesNotContain=" + UPDATED_USER_EMAIL);
    }


    @Test
    @Transactional
    public void getAllAuditUsersByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where role equals to DEFAULT_ROLE
        defaultAuditUserShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the auditUserList where role equals to UPDATED_ROLE
        defaultAuditUserShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where role not equals to DEFAULT_ROLE
        defaultAuditUserShouldNotBeFound("role.notEquals=" + DEFAULT_ROLE);

        // Get all the auditUserList where role not equals to UPDATED_ROLE
        defaultAuditUserShouldBeFound("role.notEquals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultAuditUserShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the auditUserList where role equals to UPDATED_ROLE
        defaultAuditUserShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllAuditUsersByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditUserRepository.saveAndFlush(auditUser);

        // Get all the auditUserList where role is not null
        defaultAuditUserShouldBeFound("role.specified=true");

        // Get all the auditUserList where role is null
        defaultAuditUserShouldNotBeFound("role.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditUserShouldBeFound(String filter) throws Exception {
        restAuditUserMockMvc.perform(get("/api/audit-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].auditId").value(hasItem(DEFAULT_AUDIT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userFullName").value(hasItem(DEFAULT_USER_FULL_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));

        // Check, that the count call also returns 1
        restAuditUserMockMvc.perform(get("/api/audit-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditUserShouldNotBeFound(String filter) throws Exception {
        restAuditUserMockMvc.perform(get("/api/audit-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditUserMockMvc.perform(get("/api/audit-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuditUser() throws Exception {
        // Get the auditUser
        restAuditUserMockMvc.perform(get("/api/audit-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditUser() throws Exception {
        // Initialize the database
        auditUserService.save(auditUser);

        int databaseSizeBeforeUpdate = auditUserRepository.findAll().size();

        // Update the auditUser
        AuditUser updatedAuditUser = auditUserRepository.findById(auditUser.getId()).get();
        // Disconnect from session so that the updates on updatedAuditUser are not directly saved in db
        em.detach(updatedAuditUser);
        updatedAuditUser
            .auditId(UPDATED_AUDIT_ID)
            .userId(UPDATED_USER_ID)
            .userFullName(UPDATED_USER_FULL_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .role(UPDATED_ROLE);

        restAuditUserMockMvc.perform(put("/api/audit-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditUser)))
            .andExpect(status().isOk());

        // Validate the AuditUser in the database
        List<AuditUser> auditUserList = auditUserRepository.findAll();
        assertThat(auditUserList).hasSize(databaseSizeBeforeUpdate);
        AuditUser testAuditUser = auditUserList.get(auditUserList.size() - 1);
        assertThat(testAuditUser.getAuditId()).isEqualTo(UPDATED_AUDIT_ID);
        assertThat(testAuditUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAuditUser.getUserFullName()).isEqualTo(UPDATED_USER_FULL_NAME);
        assertThat(testAuditUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testAuditUser.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditUser() throws Exception {
        int databaseSizeBeforeUpdate = auditUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditUserMockMvc.perform(put("/api/audit-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditUser)))
            .andExpect(status().isBadRequest());

        // Validate the AuditUser in the database
        List<AuditUser> auditUserList = auditUserRepository.findAll();
        assertThat(auditUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditUser() throws Exception {
        // Initialize the database
        auditUserService.save(auditUser);

        int databaseSizeBeforeDelete = auditUserRepository.findAll().size();

        // Delete the auditUser
        restAuditUserMockMvc.perform(delete("/api/audit-users/{id}", auditUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditUser> auditUserList = auditUserRepository.findAll();
        assertThat(auditUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
