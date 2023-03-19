package com.mshz.web.rest;

import com.mshz.MicrorisqueApp;
import com.mshz.domain.AuditStatusTrakingFile;
import com.mshz.repository.AuditStatusTrakingFileRepository;
import com.mshz.service.AuditStatusTrakingFileService;
import com.mshz.service.dto.AuditStatusTrakingFileCriteria;
import com.mshz.service.AuditStatusTrakingFileQueryService;

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
 * Integration tests for the {@link AuditStatusTrakingFileResource} REST controller.
 */
@SpringBootTest(classes = MicrorisqueApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AuditStatusTrakingFileResourceIT {

    private static final Long DEFAULT_TRACK_ID = 1L;
    private static final Long UPDATED_TRACK_ID = 2L;
    private static final Long SMALLER_TRACK_ID = 1L - 1L;

    private static final Long DEFAULT_FILE_ID = 1L;
    private static final Long UPDATED_FILE_ID = 2L;
    private static final Long SMALLER_FILE_ID = 1L - 1L;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    @Autowired
    private AuditStatusTrakingFileRepository auditStatusTrakingFileRepository;

    @Autowired
    private AuditStatusTrakingFileService auditStatusTrakingFileService;

    @Autowired
    private AuditStatusTrakingFileQueryService auditStatusTrakingFileQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditStatusTrakingFileMockMvc;

    private AuditStatusTrakingFile auditStatusTrakingFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditStatusTrakingFile createEntity(EntityManager em) {
        AuditStatusTrakingFile auditStatusTrakingFile = new AuditStatusTrakingFile()
            .trackId(DEFAULT_TRACK_ID)
            .fileId(DEFAULT_FILE_ID)
            .fileName(DEFAULT_FILE_NAME);
        return auditStatusTrakingFile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditStatusTrakingFile createUpdatedEntity(EntityManager em) {
        AuditStatusTrakingFile auditStatusTrakingFile = new AuditStatusTrakingFile()
            .trackId(UPDATED_TRACK_ID)
            .fileId(UPDATED_FILE_ID)
            .fileName(UPDATED_FILE_NAME);
        return auditStatusTrakingFile;
    }

    @BeforeEach
    public void initTest() {
        auditStatusTrakingFile = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditStatusTrakingFile() throws Exception {
        int databaseSizeBeforeCreate = auditStatusTrakingFileRepository.findAll().size();
        // Create the AuditStatusTrakingFile
        restAuditStatusTrakingFileMockMvc.perform(post("/api/audit-status-traking-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTrakingFile)))
            .andExpect(status().isCreated());

        // Validate the AuditStatusTrakingFile in the database
        List<AuditStatusTrakingFile> auditStatusTrakingFileList = auditStatusTrakingFileRepository.findAll();
        assertThat(auditStatusTrakingFileList).hasSize(databaseSizeBeforeCreate + 1);
        AuditStatusTrakingFile testAuditStatusTrakingFile = auditStatusTrakingFileList.get(auditStatusTrakingFileList.size() - 1);
        assertThat(testAuditStatusTrakingFile.getTrackId()).isEqualTo(DEFAULT_TRACK_ID);
        assertThat(testAuditStatusTrakingFile.getFileId()).isEqualTo(DEFAULT_FILE_ID);
        assertThat(testAuditStatusTrakingFile.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
    }

    @Test
    @Transactional
    public void createAuditStatusTrakingFileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = auditStatusTrakingFileRepository.findAll().size();

        // Create the AuditStatusTrakingFile with an existing ID
        auditStatusTrakingFile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditStatusTrakingFileMockMvc.perform(post("/api/audit-status-traking-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTrakingFile)))
            .andExpect(status().isBadRequest());

        // Validate the AuditStatusTrakingFile in the database
        List<AuditStatusTrakingFile> auditStatusTrakingFileList = auditStatusTrakingFileRepository.findAll();
        assertThat(auditStatusTrakingFileList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFileIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditStatusTrakingFileRepository.findAll().size();
        // set the field null
        auditStatusTrakingFile.setFileId(null);

        // Create the AuditStatusTrakingFile, which fails.


        restAuditStatusTrakingFileMockMvc.perform(post("/api/audit-status-traking-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTrakingFile)))
            .andExpect(status().isBadRequest());

        List<AuditStatusTrakingFile> auditStatusTrakingFileList = auditStatusTrakingFileRepository.findAll();
        assertThat(auditStatusTrakingFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = auditStatusTrakingFileRepository.findAll().size();
        // set the field null
        auditStatusTrakingFile.setFileName(null);

        // Create the AuditStatusTrakingFile, which fails.


        restAuditStatusTrakingFileMockMvc.perform(post("/api/audit-status-traking-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTrakingFile)))
            .andExpect(status().isBadRequest());

        List<AuditStatusTrakingFile> auditStatusTrakingFileList = auditStatusTrakingFileRepository.findAll();
        assertThat(auditStatusTrakingFileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFiles() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList
        restAuditStatusTrakingFileMockMvc.perform(get("/api/audit-status-traking-files?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditStatusTrakingFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackId").value(hasItem(DEFAULT_TRACK_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)));
    }
    
    @Test
    @Transactional
    public void getAuditStatusTrakingFile() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get the auditStatusTrakingFile
        restAuditStatusTrakingFileMockMvc.perform(get("/api/audit-status-traking-files/{id}", auditStatusTrakingFile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditStatusTrakingFile.getId().intValue()))
            .andExpect(jsonPath("$.trackId").value(DEFAULT_TRACK_ID.intValue()))
            .andExpect(jsonPath("$.fileId").value(DEFAULT_FILE_ID.intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME));
    }


    @Test
    @Transactional
    public void getAuditStatusTrakingFilesByIdFiltering() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        Long id = auditStatusTrakingFile.getId();

        defaultAuditStatusTrakingFileShouldBeFound("id.equals=" + id);
        defaultAuditStatusTrakingFileShouldNotBeFound("id.notEquals=" + id);

        defaultAuditStatusTrakingFileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAuditStatusTrakingFileShouldNotBeFound("id.greaterThan=" + id);

        defaultAuditStatusTrakingFileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAuditStatusTrakingFileShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByTrackIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where trackId equals to DEFAULT_TRACK_ID
        defaultAuditStatusTrakingFileShouldBeFound("trackId.equals=" + DEFAULT_TRACK_ID);

        // Get all the auditStatusTrakingFileList where trackId equals to UPDATED_TRACK_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("trackId.equals=" + UPDATED_TRACK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByTrackIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where trackId not equals to DEFAULT_TRACK_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("trackId.notEquals=" + DEFAULT_TRACK_ID);

        // Get all the auditStatusTrakingFileList where trackId not equals to UPDATED_TRACK_ID
        defaultAuditStatusTrakingFileShouldBeFound("trackId.notEquals=" + UPDATED_TRACK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByTrackIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where trackId in DEFAULT_TRACK_ID or UPDATED_TRACK_ID
        defaultAuditStatusTrakingFileShouldBeFound("trackId.in=" + DEFAULT_TRACK_ID + "," + UPDATED_TRACK_ID);

        // Get all the auditStatusTrakingFileList where trackId equals to UPDATED_TRACK_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("trackId.in=" + UPDATED_TRACK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByTrackIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where trackId is not null
        defaultAuditStatusTrakingFileShouldBeFound("trackId.specified=true");

        // Get all the auditStatusTrakingFileList where trackId is null
        defaultAuditStatusTrakingFileShouldNotBeFound("trackId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByTrackIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where trackId is greater than or equal to DEFAULT_TRACK_ID
        defaultAuditStatusTrakingFileShouldBeFound("trackId.greaterThanOrEqual=" + DEFAULT_TRACK_ID);

        // Get all the auditStatusTrakingFileList where trackId is greater than or equal to UPDATED_TRACK_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("trackId.greaterThanOrEqual=" + UPDATED_TRACK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByTrackIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where trackId is less than or equal to DEFAULT_TRACK_ID
        defaultAuditStatusTrakingFileShouldBeFound("trackId.lessThanOrEqual=" + DEFAULT_TRACK_ID);

        // Get all the auditStatusTrakingFileList where trackId is less than or equal to SMALLER_TRACK_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("trackId.lessThanOrEqual=" + SMALLER_TRACK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByTrackIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where trackId is less than DEFAULT_TRACK_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("trackId.lessThan=" + DEFAULT_TRACK_ID);

        // Get all the auditStatusTrakingFileList where trackId is less than UPDATED_TRACK_ID
        defaultAuditStatusTrakingFileShouldBeFound("trackId.lessThan=" + UPDATED_TRACK_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByTrackIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where trackId is greater than DEFAULT_TRACK_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("trackId.greaterThan=" + DEFAULT_TRACK_ID);

        // Get all the auditStatusTrakingFileList where trackId is greater than SMALLER_TRACK_ID
        defaultAuditStatusTrakingFileShouldBeFound("trackId.greaterThan=" + SMALLER_TRACK_ID);
    }


    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileIdIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileId equals to DEFAULT_FILE_ID
        defaultAuditStatusTrakingFileShouldBeFound("fileId.equals=" + DEFAULT_FILE_ID);

        // Get all the auditStatusTrakingFileList where fileId equals to UPDATED_FILE_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("fileId.equals=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileId not equals to DEFAULT_FILE_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("fileId.notEquals=" + DEFAULT_FILE_ID);

        // Get all the auditStatusTrakingFileList where fileId not equals to UPDATED_FILE_ID
        defaultAuditStatusTrakingFileShouldBeFound("fileId.notEquals=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileIdIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileId in DEFAULT_FILE_ID or UPDATED_FILE_ID
        defaultAuditStatusTrakingFileShouldBeFound("fileId.in=" + DEFAULT_FILE_ID + "," + UPDATED_FILE_ID);

        // Get all the auditStatusTrakingFileList where fileId equals to UPDATED_FILE_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("fileId.in=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileId is not null
        defaultAuditStatusTrakingFileShouldBeFound("fileId.specified=true");

        // Get all the auditStatusTrakingFileList where fileId is null
        defaultAuditStatusTrakingFileShouldNotBeFound("fileId.specified=false");
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileId is greater than or equal to DEFAULT_FILE_ID
        defaultAuditStatusTrakingFileShouldBeFound("fileId.greaterThanOrEqual=" + DEFAULT_FILE_ID);

        // Get all the auditStatusTrakingFileList where fileId is greater than or equal to UPDATED_FILE_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("fileId.greaterThanOrEqual=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileId is less than or equal to DEFAULT_FILE_ID
        defaultAuditStatusTrakingFileShouldBeFound("fileId.lessThanOrEqual=" + DEFAULT_FILE_ID);

        // Get all the auditStatusTrakingFileList where fileId is less than or equal to SMALLER_FILE_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("fileId.lessThanOrEqual=" + SMALLER_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileIdIsLessThanSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileId is less than DEFAULT_FILE_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("fileId.lessThan=" + DEFAULT_FILE_ID);

        // Get all the auditStatusTrakingFileList where fileId is less than UPDATED_FILE_ID
        defaultAuditStatusTrakingFileShouldBeFound("fileId.lessThan=" + UPDATED_FILE_ID);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileId is greater than DEFAULT_FILE_ID
        defaultAuditStatusTrakingFileShouldNotBeFound("fileId.greaterThan=" + DEFAULT_FILE_ID);

        // Get all the auditStatusTrakingFileList where fileId is greater than SMALLER_FILE_ID
        defaultAuditStatusTrakingFileShouldBeFound("fileId.greaterThan=" + SMALLER_FILE_ID);
    }


    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileName equals to DEFAULT_FILE_NAME
        defaultAuditStatusTrakingFileShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the auditStatusTrakingFileList where fileName equals to UPDATED_FILE_NAME
        defaultAuditStatusTrakingFileShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileName not equals to DEFAULT_FILE_NAME
        defaultAuditStatusTrakingFileShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the auditStatusTrakingFileList where fileName not equals to UPDATED_FILE_NAME
        defaultAuditStatusTrakingFileShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultAuditStatusTrakingFileShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the auditStatusTrakingFileList where fileName equals to UPDATED_FILE_NAME
        defaultAuditStatusTrakingFileShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileName is not null
        defaultAuditStatusTrakingFileShouldBeFound("fileName.specified=true");

        // Get all the auditStatusTrakingFileList where fileName is null
        defaultAuditStatusTrakingFileShouldNotBeFound("fileName.specified=false");
    }
                @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileName contains DEFAULT_FILE_NAME
        defaultAuditStatusTrakingFileShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the auditStatusTrakingFileList where fileName contains UPDATED_FILE_NAME
        defaultAuditStatusTrakingFileShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void getAllAuditStatusTrakingFilesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        auditStatusTrakingFileRepository.saveAndFlush(auditStatusTrakingFile);

        // Get all the auditStatusTrakingFileList where fileName does not contain DEFAULT_FILE_NAME
        defaultAuditStatusTrakingFileShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the auditStatusTrakingFileList where fileName does not contain UPDATED_FILE_NAME
        defaultAuditStatusTrakingFileShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditStatusTrakingFileShouldBeFound(String filter) throws Exception {
        restAuditStatusTrakingFileMockMvc.perform(get("/api/audit-status-traking-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditStatusTrakingFile.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackId").value(hasItem(DEFAULT_TRACK_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileId").value(hasItem(DEFAULT_FILE_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)));

        // Check, that the count call also returns 1
        restAuditStatusTrakingFileMockMvc.perform(get("/api/audit-status-traking-files/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditStatusTrakingFileShouldNotBeFound(String filter) throws Exception {
        restAuditStatusTrakingFileMockMvc.perform(get("/api/audit-status-traking-files?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditStatusTrakingFileMockMvc.perform(get("/api/audit-status-traking-files/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAuditStatusTrakingFile() throws Exception {
        // Get the auditStatusTrakingFile
        restAuditStatusTrakingFileMockMvc.perform(get("/api/audit-status-traking-files/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditStatusTrakingFile() throws Exception {
        // Initialize the database
        auditStatusTrakingFileService.save(auditStatusTrakingFile);

        int databaseSizeBeforeUpdate = auditStatusTrakingFileRepository.findAll().size();

        // Update the auditStatusTrakingFile
        AuditStatusTrakingFile updatedAuditStatusTrakingFile = auditStatusTrakingFileRepository.findById(auditStatusTrakingFile.getId()).get();
        // Disconnect from session so that the updates on updatedAuditStatusTrakingFile are not directly saved in db
        em.detach(updatedAuditStatusTrakingFile);
        updatedAuditStatusTrakingFile
            .trackId(UPDATED_TRACK_ID)
            .fileId(UPDATED_FILE_ID)
            .fileName(UPDATED_FILE_NAME);

        restAuditStatusTrakingFileMockMvc.perform(put("/api/audit-status-traking-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuditStatusTrakingFile)))
            .andExpect(status().isOk());

        // Validate the AuditStatusTrakingFile in the database
        List<AuditStatusTrakingFile> auditStatusTrakingFileList = auditStatusTrakingFileRepository.findAll();
        assertThat(auditStatusTrakingFileList).hasSize(databaseSizeBeforeUpdate);
        AuditStatusTrakingFile testAuditStatusTrakingFile = auditStatusTrakingFileList.get(auditStatusTrakingFileList.size() - 1);
        assertThat(testAuditStatusTrakingFile.getTrackId()).isEqualTo(UPDATED_TRACK_ID);
        assertThat(testAuditStatusTrakingFile.getFileId()).isEqualTo(UPDATED_FILE_ID);
        assertThat(testAuditStatusTrakingFile.getFileName()).isEqualTo(UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAuditStatusTrakingFile() throws Exception {
        int databaseSizeBeforeUpdate = auditStatusTrakingFileRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditStatusTrakingFileMockMvc.perform(put("/api/audit-status-traking-files")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(auditStatusTrakingFile)))
            .andExpect(status().isBadRequest());

        // Validate the AuditStatusTrakingFile in the database
        List<AuditStatusTrakingFile> auditStatusTrakingFileList = auditStatusTrakingFileRepository.findAll();
        assertThat(auditStatusTrakingFileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuditStatusTrakingFile() throws Exception {
        // Initialize the database
        auditStatusTrakingFileService.save(auditStatusTrakingFile);

        int databaseSizeBeforeDelete = auditStatusTrakingFileRepository.findAll().size();

        // Delete the auditStatusTrakingFile
        restAuditStatusTrakingFileMockMvc.perform(delete("/api/audit-status-traking-files/{id}", auditStatusTrakingFile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuditStatusTrakingFile> auditStatusTrakingFileList = auditStatusTrakingFileRepository.findAll();
        assertThat(auditStatusTrakingFileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
