package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.AuditRecomUser;
import com.mshz.repository.AuditRecomUserRepository;
import com.mshz.service.AuditRecomUserService;
import com.mshz.service.dto.AuditRecomUserCriteria;
import com.mshz.service.AuditRecomUserQueryService;

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
 * Integration tests for the {@link AuditRecomUserResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditRecomUserResourceIT {

    private static final Long DEFAULT_RECOM_ID = 1L;
    private static final Long UPDATED_RECOM_ID = 2L;
    private static final Long SMALLER_RECOM_ID = 1L - 1L;

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
    private AuditRecomUserRepository auditRecomUserRepository;

    @Autowired
    private AuditRecomUserService auditRecomUserService;

    @Autowired
    private AuditRecomUserQueryService auditRecomUserQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditRecomUserMockMvc;

    private AuditRecomUser auditRecomUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditRecomUser createEntity(EntityManager em) {
        AuditRecomUser auditRecomUser = new AuditRecomUser()
            .recomId(DEFAULT_RECOM_ID)
            .userId(DEFAULT_USER_ID)
            .userFullName(DEFAULT_USER_FULL_NAME)
            .userEmail(DEFAULT_USER_EMAIL)
            .role(DEFAULT_ROLE);
        return auditRecomUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditRecomUser createUpdatedEntity(EntityManager em) {
        AuditRecomUser auditRecomUser = new AuditRecomUser()
            .recomId(UPDATED_RECOM_ID)
            .userId(UPDATED_USER_ID)
            .userFullName(UPDATED_USER_FULL_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .role(UPDATED_ROLE);
        return auditRecomUser;
    }

    @BeforeEach
    public void initTest() {
        auditRecomUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditRecomUser() throws Exception {
        int databaseSizeBeforeCreate = auditRecomUserRepository.findAll().size();
        // Create the AuditRecomUser
        restAuditRecomUserMockMvc.perform(post("/api/audit-recom-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecomUser)))
            .andExpect(status().isCreated());

        // Validate the AuditRecomUser in the database
        List<AuditRecomUser> auditRecomUserList = auditRecomUserRepository.findAll();
        assertThat(auditRecomUserList).hasSize(databaseSizeBeforeCreate + 1);
        AuditRecomUser testAuditRecomUser = auditRecomUserList.get(auditRecomUserList.size() - 1);
        assertThat(testAuditRecomUser.getRecomId()).isEqualTo(DEFAULT_RECOM_ID);
        assertThat(testAuditRecomUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testAuditRecomUser.getUserFullName()).isEqualTo(DEFAULT_USER_FULL_NAME);
        assertThat(testAuditRecomUser.getUserEmail()).isEqualTo(DEFAULT_USER_EMAIL);
        assertThat(testAuditRecomUser.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createAuditRecomUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditRecomUserRepository.findAll().size();

        // Create the AuditRecomUser with an existing ID
        auditRecomUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditRecomUserMockMvc.perform(post("/api/audit-recom-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecomUser)))
            .andExpect(status().isBadRequest());

        // Validate the AuditRecomUser in the database
        List<AuditRecomUser> auditRecomUserList = auditRecomUserRepository.findAll();
        assertThat(auditRecomUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAuditRecomUsers() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList
        restAuditRecomUserMockMvc.perform(get("/api/audit-recom-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditRecomUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].recomId").value(hasItem(DEFAULT_RECOM_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userFullName").value(hasItem(DEFAULT_USER_FULL_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }
    
    @Test
    @Transactional
    public void getAuditRecomUser() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get the auditRecomUser
        restAuditRecomUserMockMvc.perform(get("/api/audit-recom-users/{id}", auditRecomUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditRecomUser.getId().intValue()))
            .andExpect(jsonPath("$.recomId").value(DEFAULT_RECOM_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.userFullName").value(DEFAULT_USER_FULL_NAME))
            .andExpect(jsonPath("$.userEmail").value(DEFAULT_USER_EMAIL))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }


    @Test
    @Transactional
    public void getAuditRecomUsersByIdFiltering() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        Long id = auditRecomUser.getId();

        defaultAuditRecomUserShouldBeFound("id.equals=" + id);
        defaultAuditRecomUserShouldNotBeFound("id.notEquals=" + id);

        defaultAuditRecomUserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditRecomUserShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditRecomUserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditRecomUserShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditRecomUsersByRecomIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where recomId equals to DEFAULT_RECOM_ID
        defaultAuditRecomUserShouldBeFound("recomId.equals=" + DEFAULT_RECOM_ID);

        // Get all the auditRecomUserList where recomId equals to UPDATED_RECOM_ID
        defaultAuditRecomUserShouldNotBeFound("recomId.equals=" + UPDATED_RECOM_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRecomIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where recomId not equals to DEFAULT_RECOM_ID
        defaultAuditRecomUserShouldNotBeFound("recomId.notEquals=" + DEFAULT_RECOM_ID);

        // Get all the auditRecomUserList where recomId not equals to UPDATED_RECOM_ID
        defaultAuditRecomUserShouldBeFound("recomId.notEquals=" + UPDATED_RECOM_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRecomIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where recomId in DEFAULT_RECOM_ID or UPDATED_RECOM_ID
        defaultAuditRecomUserShouldBeFound("recomId.in=" + DEFAULT_RECOM_ID + "," + UPDATED_RECOM_ID);

        // Get all the auditRecomUserList where recomId equals to UPDATED_RECOM_ID
        defaultAuditRecomUserShouldNotBeFound("recomId.in=" + UPDATED_RECOM_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRecomIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where recomId is not null
        defaultAuditRecomUserShouldBeFound("recomId.specified=true");

        // Get all the auditRecomUserList where recomId is null
        defaultAuditRecomUserShouldNotBeFound("recomId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRecomIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where recomId is greater than or equal to DEFAULT_RECOM_ID
        defaultAuditRecomUserShouldBeFound("recomId.greaterThanOrEqual=" + DEFAULT_RECOM_ID);

        // Get all the auditRecomUserList where recomId is greater than or equal to UPDATED_RECOM_ID
        defaultAuditRecomUserShouldNotBeFound("recomId.greaterThanOrEqual=" + UPDATED_RECOM_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRecomIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where recomId is less than or equal to DEFAULT_RECOM_ID
        defaultAuditRecomUserShouldBeFound("recomId.lessThanOrEqual=" + DEFAULT_RECOM_ID);

        // Get all the auditRecomUserList where recomId is less than or equal to SMALLER_RECOM_ID
        defaultAuditRecomUserShouldNotBeFound("recomId.lessThanOrEqual=" + SMALLER_RECOM_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRecomIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where recomId is less than DEFAULT_RECOM_ID
        defaultAuditRecomUserShouldNotBeFound("recomId.lessThan=" + DEFAULT_RECOM_ID);

        // Get all the auditRecomUserList where recomId is less than UPDATED_RECOM_ID
        defaultAuditRecomUserShouldBeFound("recomId.lessThan=" + UPDATED_RECOM_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRecomIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where recomId is greater than DEFAULT_RECOM_ID
        defaultAuditRecomUserShouldNotBeFound("recomId.greaterThan=" + DEFAULT_RECOM_ID);

        // Get all the auditRecomUserList where recomId is greater than SMALLER_RECOM_ID
        defaultAuditRecomUserShouldBeFound("recomId.greaterThan=" + SMALLER_RECOM_ID);
    }


    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userId equals to DEFAULT_USER_ID
        defaultAuditRecomUserShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the auditRecomUserList where userId equals to UPDATED_USER_ID
        defaultAuditRecomUserShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userId not equals to DEFAULT_USER_ID
        defaultAuditRecomUserShouldNotBeFound("userId.notEquals=" + DEFAULT_USER_ID);

        // Get all the auditRecomUserList where userId not equals to UPDATED_USER_ID
        defaultAuditRecomUserShouldBeFound("userId.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultAuditRecomUserShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the auditRecomUserList where userId equals to UPDATED_USER_ID
        defaultAuditRecomUserShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userId is not null
        defaultAuditRecomUserShouldBeFound("userId.specified=true");

        // Get all the auditRecomUserList where userId is null
        defaultAuditRecomUserShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userId is greater than or equal to DEFAULT_USER_ID
        defaultAuditRecomUserShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the auditRecomUserList where userId is greater than or equal to UPDATED_USER_ID
        defaultAuditRecomUserShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userId is less than or equal to DEFAULT_USER_ID
        defaultAuditRecomUserShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the auditRecomUserList where userId is less than or equal to SMALLER_USER_ID
        defaultAuditRecomUserShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userId is less than DEFAULT_USER_ID
        defaultAuditRecomUserShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the auditRecomUserList where userId is less than UPDATED_USER_ID
        defaultAuditRecomUserShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userId is greater than DEFAULT_USER_ID
        defaultAuditRecomUserShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the auditRecomUserList where userId is greater than SMALLER_USER_ID
        defaultAuditRecomUserShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }


    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userFullName equals to DEFAULT_USER_FULL_NAME
        defaultAuditRecomUserShouldBeFound("userFullName.equals=" + DEFAULT_USER_FULL_NAME);

        // Get all the auditRecomUserList where userFullName equals to UPDATED_USER_FULL_NAME
        defaultAuditRecomUserShouldNotBeFound("userFullName.equals=" + UPDATED_USER_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userFullName not equals to DEFAULT_USER_FULL_NAME
        defaultAuditRecomUserShouldNotBeFound("userFullName.notEquals=" + DEFAULT_USER_FULL_NAME);

        // Get all the auditRecomUserList where userFullName not equals to UPDATED_USER_FULL_NAME
        defaultAuditRecomUserShouldBeFound("userFullName.notEquals=" + UPDATED_USER_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userFullName in DEFAULT_USER_FULL_NAME or UPDATED_USER_FULL_NAME
        defaultAuditRecomUserShouldBeFound("userFullName.in=" + DEFAULT_USER_FULL_NAME + "," + UPDATED_USER_FULL_NAME);

        // Get all the auditRecomUserList where userFullName equals to UPDATED_USER_FULL_NAME
        defaultAuditRecomUserShouldNotBeFound("userFullName.in=" + UPDATED_USER_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userFullName is not null
        defaultAuditRecomUserShouldBeFound("userFullName.specified=true");

        // Get all the auditRecomUserList where userFullName is null
        defaultAuditRecomUserShouldNotBeFound("userFullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditRecomUsersByUserFullNameContainsSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userFullName contains DEFAULT_USER_FULL_NAME
        defaultAuditRecomUserShouldBeFound("userFullName.contains=" + DEFAULT_USER_FULL_NAME);

        // Get all the auditRecomUserList where userFullName contains UPDATED_USER_FULL_NAME
        defaultAuditRecomUserShouldNotBeFound("userFullName.contains=" + UPDATED_USER_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userFullName does not contain DEFAULT_USER_FULL_NAME
        defaultAuditRecomUserShouldNotBeFound("userFullName.doesNotContain=" + DEFAULT_USER_FULL_NAME);

        // Get all the auditRecomUserList where userFullName does not contain UPDATED_USER_FULL_NAME
        defaultAuditRecomUserShouldBeFound("userFullName.doesNotContain=" + UPDATED_USER_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userEmail equals to DEFAULT_USER_EMAIL
        defaultAuditRecomUserShouldBeFound("userEmail.equals=" + DEFAULT_USER_EMAIL);

        // Get all the auditRecomUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultAuditRecomUserShouldNotBeFound("userEmail.equals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userEmail not equals to DEFAULT_USER_EMAIL
        defaultAuditRecomUserShouldNotBeFound("userEmail.notEquals=" + DEFAULT_USER_EMAIL);

        // Get all the auditRecomUserList where userEmail not equals to UPDATED_USER_EMAIL
        defaultAuditRecomUserShouldBeFound("userEmail.notEquals=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserEmailIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userEmail in DEFAULT_USER_EMAIL or UPDATED_USER_EMAIL
        defaultAuditRecomUserShouldBeFound("userEmail.in=" + DEFAULT_USER_EMAIL + "," + UPDATED_USER_EMAIL);

        // Get all the auditRecomUserList where userEmail equals to UPDATED_USER_EMAIL
        defaultAuditRecomUserShouldNotBeFound("userEmail.in=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userEmail is not null
        defaultAuditRecomUserShouldBeFound("userEmail.specified=true");

        // Get all the auditRecomUserList where userEmail is null
        defaultAuditRecomUserShouldNotBeFound("userEmail.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditRecomUsersByUserEmailContainsSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userEmail contains DEFAULT_USER_EMAIL
        defaultAuditRecomUserShouldBeFound("userEmail.contains=" + DEFAULT_USER_EMAIL);

        // Get all the auditRecomUserList where userEmail contains UPDATED_USER_EMAIL
        defaultAuditRecomUserShouldNotBeFound("userEmail.contains=" + UPDATED_USER_EMAIL);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByUserEmailNotContainsSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where userEmail does not contain DEFAULT_USER_EMAIL
        defaultAuditRecomUserShouldNotBeFound("userEmail.doesNotContain=" + DEFAULT_USER_EMAIL);

        // Get all the auditRecomUserList where userEmail does not contain UPDATED_USER_EMAIL
        defaultAuditRecomUserShouldBeFound("userEmail.doesNotContain=" + UPDATED_USER_EMAIL);
    }


    @Test
    @Transactional
    public void getAllAuditRecomUsersByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where role equals to DEFAULT_ROLE
        defaultAuditRecomUserShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the auditRecomUserList where role equals to UPDATED_ROLE
        defaultAuditRecomUserShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRoleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where role not equals to DEFAULT_ROLE
        defaultAuditRecomUserShouldNotBeFound("role.notEquals=" + DEFAULT_ROLE);

        // Get all the auditRecomUserList where role not equals to UPDATED_ROLE
        defaultAuditRecomUserShouldBeFound("role.notEquals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultAuditRecomUserShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the auditRecomUserList where role equals to UPDATED_ROLE
        defaultAuditRecomUserShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void getAllAuditRecomUsersByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecomUserRepository.saveAndFlush(auditRecomUser);

        // Get all the auditRecomUserList where role is not null
        defaultAuditRecomUserShouldBeFound("role.specified=true");

        // Get all the auditRecomUserList where role is null
        defaultAuditRecomUserShouldNotBeFound("role.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditRecomUserShouldBeFound(String filter) throws Exception {
        restAuditRecomUserMockMvc.perform(get("/api/audit-recom-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditRecomUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].recomId").value(hasItem(DEFAULT_RECOM_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userFullName").value(hasItem(DEFAULT_USER_FULL_NAME)))
            .andExpect(jsonPath("$.[*].userEmail").value(hasItem(DEFAULT_USER_EMAIL)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));

        // Check, that the count call also returns 1
        restAuditRecomUserMockMvc.perform(get("/api/audit-recom-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditRecomUserShouldNotBeFound(String filter) throws Exception {
        restAuditRecomUserMockMvc.perform(get("/api/audit-recom-users?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditRecomUserMockMvc.perform(get("/api/audit-recom-users/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuditRecomUser() throws Exception {
        // Get the auditRecomUser
        restAuditRecomUserMockMvc.perform(get("/api/audit-recom-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditRecomUser() throws Exception {
        // Initialize the database
        auditRecomUserService.save(auditRecomUser);

        int databaseSizeBeforeUpdate = auditRecomUserRepository.findAll().size();

        // Update the auditRecomUser
        AuditRecomUser updatedAuditRecomUser = auditRecomUserRepository.findById(auditRecomUser.getId()).get();
        // Disconnect from session so that the updates on updatedAuditRecomUser are not directly saved in db
        em.detach(updatedAuditRecomUser);
        updatedAuditRecomUser
            .recomId(UPDATED_RECOM_ID)
            .userId(UPDATED_USER_ID)
            .userFullName(UPDATED_USER_FULL_NAME)
            .userEmail(UPDATED_USER_EMAIL)
            .role(UPDATED_ROLE);

        restAuditRecomUserMockMvc.perform(put("/api/audit-recom-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditRecomUser)))
            .andExpect(status().isOk());

        // Validate the AuditRecomUser in the database
        List<AuditRecomUser> auditRecomUserList = auditRecomUserRepository.findAll();
        assertThat(auditRecomUserList).hasSize(databaseSizeBeforeUpdate);
        AuditRecomUser testAuditRecomUser = auditRecomUserList.get(auditRecomUserList.size() - 1);
        assertThat(testAuditRecomUser.getRecomId()).isEqualTo(UPDATED_RECOM_ID);
        assertThat(testAuditRecomUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testAuditRecomUser.getUserFullName()).isEqualTo(UPDATED_USER_FULL_NAME);
        assertThat(testAuditRecomUser.getUserEmail()).isEqualTo(UPDATED_USER_EMAIL);
        assertThat(testAuditRecomUser.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditRecomUser() throws Exception {
        int databaseSizeBeforeUpdate = auditRecomUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditRecomUserMockMvc.perform(put("/api/audit-recom-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecomUser)))
            .andExpect(status().isBadRequest());

        // Validate the AuditRecomUser in the database
        List<AuditRecomUser> auditRecomUserList = auditRecomUserRepository.findAll();
        assertThat(auditRecomUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditRecomUser() throws Exception {
        // Initialize the database
        auditRecomUserService.save(auditRecomUser);

        int databaseSizeBeforeDelete = auditRecomUserRepository.findAll().size();

        // Delete the auditRecomUser
        restAuditRecomUserMockMvc.perform(delete("/api/audit-recom-users/{id}", auditRecomUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditRecomUser> auditRecomUserList = auditRecomUserRepository.findAll();
        assertThat(auditRecomUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
