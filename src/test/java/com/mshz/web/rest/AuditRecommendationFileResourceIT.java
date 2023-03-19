package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.AuditRecommendationFile;
import com.mshz.repository.AuditRecommendationFileRepository;
import com.mshz.service.AuditRecommendationFileService;
import com.mshz.service.dto.AuditRecommendationFileCriteria;
import com.mshz.service.AuditRecommendationFileQueryService;

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
 * Integration tests for the {@link AuditRecommendationFileResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditRecommendationFileResourceIT {

    private static final Long DEFAULT_RECOMMENDATION_ID = 1L;
    private static final Long UPDATED_RECOMMENDATION_ID = 2L;
    private static final Long SMALLER_RECOMMENDATION_ID = 1L - 1L;

    private static final Long DEFAULT_FILE_ID = 1L;
    private static final Long UPDATED_FILE_ID = 2L;
    private static final Long SMALLER_FILE_ID = 1L - 1L;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    @Autowired
    private AuditRecommendationFileRepository auditRecommendationFileRepository;

    @Autowired
    private AuditRecommendationFileService auditRecommendationFileService;

    @Autowired
    private AuditRecommendationFileQueryService auditRecommendationFileQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditRecommendationFileMockMvc;

    private AuditRecommendationFile auditRecommendationFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditRecommendationFile createEntity(EntityManager em) {
        AuditRecommendationFile auditRecommendationFile = new AuditRecommendationFile()
            .recommendationId(DEFAULT_RECOMMENDATION_ID)
            .fileId(DEFAULT_FILE_ID)
            .fileName(DEFAULT_FILE_NAME);
        return auditRecommendationFile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditRecommendationFile createUpdatedEntity(EntityManager em) {
        AuditRecommendationFile auditRecommendationFile = new AuditRecommendationFile()
            .recommendationId(UPDATED_RECOMMENDATION_ID)
            .fileId(UPDATED_FILE_ID)
            .fileName(UPDATED_FILE_NAME);
        return auditRecommendationFile;
    }

    @BeforeEach
    public void initTest() {
        auditRecommendationFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditRecommendationFile() throws Exception {
        int databaseSizeBeforeCreate = auditRecommendationFileRepository.findAll().size();
        // Create the AuditRecommendationFile
        restAuditRecommendationFileMockMvc.perform(post("/api/audit-recommendation-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecommendationFile)))
            .andExpect(status().isCreated());

        // Validate the AuditRecommendationFile in the database
        List<AuditRecommendationFile> auditRecommendationFileList = auditRecommendationFileRepository.findAll();
        assertThat(auditRecommendationFileList).hasSize(databaseSizeBeforeCreate + 1);
        AuditRecommendationFile testAuditRecommendationFile = auditRecommendationFileList.get(auditRecommendationFileList.size() - 1);
        assertThat(testAuditRecommendationFile.getRecommendationId()).isEqualTo(DEFAULT_RECOMMENDATION_ID);
        assertThat(testAuditRecommendationFile.getFileId()).isEqualTo(DEFAULT_FILE_ID);
        assertThat(testAuditRecommendationFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
    }

    @Test
    @Transactional
    public void createAuditRecommendationFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditRecommendationFileRepository.findAll().size();

        // Create the AuditRecommendationFile with an existing ID
        auditRecommendationFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditRecommendationFileMockMvc.perform(post("/api/audit-recommendation-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecommendationFile)))
            .andExpect(status().isBadRequest());

        // Validate the AuditRecommendationFile in the database
        List<AuditRecommendationFile> auditRecommendationFileList = auditRecommendationFileRepository.findAll();
        assertThat(auditRecommendationFileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationFiles() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList
        restAuditRecommendationFileMockMvc.perform(get("/api/audit-recommendation-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditRecommendationFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].recommendationId").value(hasItem(DEFAULT_RECOMMENDATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)));
    }
    
    @Test
    @Transactional
    public void getAuditRecommendationFile() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get the auditRecommendationFile
        restAuditRecommendationFileMockMvc.perform(get("/api/audit-recommendation-files/{id}", auditRecommendationFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditRecommendationFile.getId().intValue()))
            .andExpect(jsonPath("$.recommendationId").value(DEFAULT_RECOMMENDATION_ID.intValue()))
            .andExpect(jsonPath("$.fileId").value(DEFAULT_FILE_ID.intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME));
    }


    @Test
    @Transactional
    public void getAuditRecommendationFilesByIdFiltering() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        Long id = auditRecommendationFile.getId();

        defaultAuditRecommendationFileShouldBeFound("id.equals=" + id);
        defaultAuditRecommendationFileShouldNotBeFound("id.notEquals=" + id);

        defaultAuditRecommendationFileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditRecommendationFileShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditRecommendationFileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditRecommendationFileShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByRecommendationIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where recommendationId equals to DEFAULT_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldBeFound("recommendationId.equals=" + DEFAULT_RECOMMENDATION_ID);

        // Get all the auditRecommendationFileList where recommendationId equals to UPDATED_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldNotBeFound("recommendationId.equals=" + UPDATED_RECOMMENDATION_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByRecommendationIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where recommendationId not equals to DEFAULT_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldNotBeFound("recommendationId.notEquals=" + DEFAULT_RECOMMENDATION_ID);

        // Get all the auditRecommendationFileList where recommendationId not equals to UPDATED_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldBeFound("recommendationId.notEquals=" + UPDATED_RECOMMENDATION_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByRecommendationIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where recommendationId in DEFAULT_RECOMMENDATION_ID or UPDATED_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldBeFound("recommendationId.in=" + DEFAULT_RECOMMENDATION_ID + "," + UPDATED_RECOMMENDATION_ID);

        // Get all the auditRecommendationFileList where recommendationId equals to UPDATED_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldNotBeFound("recommendationId.in=" + UPDATED_RECOMMENDATION_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByRecommendationIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where recommendationId is not null
        defaultAuditRecommendationFileShouldBeFound("recommendationId.specified=true");

        // Get all the auditRecommendationFileList where recommendationId is null
        defaultAuditRecommendationFileShouldNotBeFound("recommendationId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByRecommendationIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where recommendationId is greater than or equal to DEFAULT_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldBeFound("recommendationId.greaterThanOrEqual=" + DEFAULT_RECOMMENDATION_ID);

        // Get all the auditRecommendationFileList where recommendationId is greater than or equal to UPDATED_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldNotBeFound("recommendationId.greaterThanOrEqual=" + UPDATED_RECOMMENDATION_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByRecommendationIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where recommendationId is less than or equal to DEFAULT_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldBeFound("recommendationId.lessThanOrEqual=" + DEFAULT_RECOMMENDATION_ID);

        // Get all the auditRecommendationFileList where recommendationId is less than or equal to SMALLER_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldNotBeFound("recommendationId.lessThanOrEqual=" + SMALLER_RECOMMENDATION_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByRecommendationIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where recommendationId is less than DEFAULT_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldNotBeFound("recommendationId.lessThan=" + DEFAULT_RECOMMENDATION_ID);

        // Get all the auditRecommendationFileList where recommendationId is less than UPDATED_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldBeFound("recommendationId.lessThan=" + UPDATED_RECOMMENDATION_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByRecommendationIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where recommendationId is greater than DEFAULT_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldNotBeFound("recommendationId.greaterThan=" + DEFAULT_RECOMMENDATION_ID);

        // Get all the auditRecommendationFileList where recommendationId is greater than SMALLER_RECOMMENDATION_ID
        defaultAuditRecommendationFileShouldBeFound("recommendationId.greaterThan=" + SMALLER_RECOMMENDATION_ID);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileId equals to DEFAULT_FILE_ID
        defaultAuditRecommendationFileShouldBeFound("fileId.equals=" + DEFAULT_FILE_ID);

        // Get all the auditRecommendationFileList where fileId equals to UPDATED_FILE_ID
        defaultAuditRecommendationFileShouldNotBeFound("fileId.equals=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileId not equals to DEFAULT_FILE_ID
        defaultAuditRecommendationFileShouldNotBeFound("fileId.notEquals=" + DEFAULT_FILE_ID);

        // Get all the auditRecommendationFileList where fileId not equals to UPDATED_FILE_ID
        defaultAuditRecommendationFileShouldBeFound("fileId.notEquals=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileId in DEFAULT_FILE_ID or UPDATED_FILE_ID
        defaultAuditRecommendationFileShouldBeFound("fileId.in=" + DEFAULT_FILE_ID + "," + UPDATED_FILE_ID);

        // Get all the auditRecommendationFileList where fileId equals to UPDATED_FILE_ID
        defaultAuditRecommendationFileShouldNotBeFound("fileId.in=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileId is not null
        defaultAuditRecommendationFileShouldBeFound("fileId.specified=true");

        // Get all the auditRecommendationFileList where fileId is null
        defaultAuditRecommendationFileShouldNotBeFound("fileId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileId is greater than or equal to DEFAULT_FILE_ID
        defaultAuditRecommendationFileShouldBeFound("fileId.greaterThanOrEqual=" + DEFAULT_FILE_ID);

        // Get all the auditRecommendationFileList where fileId is greater than or equal to UPDATED_FILE_ID
        defaultAuditRecommendationFileShouldNotBeFound("fileId.greaterThanOrEqual=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileId is less than or equal to DEFAULT_FILE_ID
        defaultAuditRecommendationFileShouldBeFound("fileId.lessThanOrEqual=" + DEFAULT_FILE_ID);

        // Get all the auditRecommendationFileList where fileId is less than or equal to SMALLER_FILE_ID
        defaultAuditRecommendationFileShouldNotBeFound("fileId.lessThanOrEqual=" + SMALLER_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileId is less than DEFAULT_FILE_ID
        defaultAuditRecommendationFileShouldNotBeFound("fileId.lessThan=" + DEFAULT_FILE_ID);

        // Get all the auditRecommendationFileList where fileId is less than UPDATED_FILE_ID
        defaultAuditRecommendationFileShouldBeFound("fileId.lessThan=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileId is greater than DEFAULT_FILE_ID
        defaultAuditRecommendationFileShouldNotBeFound("fileId.greaterThan=" + DEFAULT_FILE_ID);

        // Get all the auditRecommendationFileList where fileId is greater than SMALLER_FILE_ID
        defaultAuditRecommendationFileShouldBeFound("fileId.greaterThan=" + SMALLER_FILE_ID);
    }


    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileName equals to DEFAULT_FILE_NAME
        defaultAuditRecommendationFileShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the auditRecommendationFileList where fileName equals to UPDATED_FILE_NAME
        defaultAuditRecommendationFileShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileName not equals to DEFAULT_FILE_NAME
        defaultAuditRecommendationFileShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the auditRecommendationFileList where fileName not equals to UPDATED_FILE_NAME
        defaultAuditRecommendationFileShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultAuditRecommendationFileShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the auditRecommendationFileList where fileName equals to UPDATED_FILE_NAME
        defaultAuditRecommendationFileShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileName is not null
        defaultAuditRecommendationFileShouldBeFound("fileName.specified=true");

        // Get all the auditRecommendationFileList where fileName is null
        defaultAuditRecommendationFileShouldNotBeFound("fileName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileName contains DEFAULT_FILE_NAME
        defaultAuditRecommendationFileShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the auditRecommendationFileList where fileName contains UPDATED_FILE_NAME
        defaultAuditRecommendationFileShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditRecommendationFilesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditRecommendationFileRepository.saveAndFlush(auditRecommendationFile);

        // Get all the auditRecommendationFileList where fileName does not contain DEFAULT_FILE_NAME
        defaultAuditRecommendationFileShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the auditRecommendationFileList where fileName does not contain UPDATED_FILE_NAME
        defaultAuditRecommendationFileShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditRecommendationFileShouldBeFound(String filter) throws Exception {
        restAuditRecommendationFileMockMvc.perform(get("/api/audit-recommendation-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditRecommendationFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].recommendationId").value(hasItem(DEFAULT_RECOMMENDATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)));

        // Check, that the count call also returns 1
        restAuditRecommendationFileMockMvc.perform(get("/api/audit-recommendation-files/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditRecommendationFileShouldNotBeFound(String filter) throws Exception {
        restAuditRecommendationFileMockMvc.perform(get("/api/audit-recommendation-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditRecommendationFileMockMvc.perform(get("/api/audit-recommendation-files/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuditRecommendationFile() throws Exception {
        // Get the auditRecommendationFile
        restAuditRecommendationFileMockMvc.perform(get("/api/audit-recommendation-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditRecommendationFile() throws Exception {
        // Initialize the database
        auditRecommendationFileService.save(auditRecommendationFile);

        int databaseSizeBeforeUpdate = auditRecommendationFileRepository.findAll().size();

        // Update the auditRecommendationFile
        AuditRecommendationFile updatedAuditRecommendationFile = auditRecommendationFileRepository.findById(auditRecommendationFile.getId()).get();
        // Disconnect from session so that the updates on updatedAuditRecommendationFile are not directly saved in db
        em.detach(updatedAuditRecommendationFile);
        updatedAuditRecommendationFile
            .recommendationId(UPDATED_RECOMMENDATION_ID)
            .fileId(UPDATED_FILE_ID)
            .fileName(UPDATED_FILE_NAME);

        restAuditRecommendationFileMockMvc.perform(put("/api/audit-recommendation-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditRecommendationFile)))
            .andExpect(status().isOk());

        // Validate the AuditRecommendationFile in the database
        List<AuditRecommendationFile> auditRecommendationFileList = auditRecommendationFileRepository.findAll();
        assertThat(auditRecommendationFileList).hasSize(databaseSizeBeforeUpdate);
        AuditRecommendationFile testAuditRecommendationFile = auditRecommendationFileList.get(auditRecommendationFileList.size() - 1);
        assertThat(testAuditRecommendationFile.getRecommendationId()).isEqualTo(UPDATED_RECOMMENDATION_ID);
        assertThat(testAuditRecommendationFile.getFileId()).isEqualTo(UPDATED_FILE_ID);
        assertThat(testAuditRecommendationFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditRecommendationFile() throws Exception {
        int databaseSizeBeforeUpdate = auditRecommendationFileRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditRecommendationFileMockMvc.perform(put("/api/audit-recommendation-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditRecommendationFile)))
            .andExpect(status().isBadRequest());

        // Validate the AuditRecommendationFile in the database
        List<AuditRecommendationFile> auditRecommendationFileList = auditRecommendationFileRepository.findAll();
        assertThat(auditRecommendationFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditRecommendationFile() throws Exception {
        // Initialize the database
        auditRecommendationFileService.save(auditRecommendationFile);

        int databaseSizeBeforeDelete = auditRecommendationFileRepository.findAll().size();

        // Delete the auditRecommendationFile
        restAuditRecommendationFileMockMvc.perform(delete("/api/audit-recommendation-files/{id}", auditRecommendationFile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditRecommendationFile> auditRecommendationFileList = auditRecommendationFileRepository.findAll();
        assertThat(auditRecommendationFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
