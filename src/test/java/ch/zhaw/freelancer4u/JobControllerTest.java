package ch.zhaw.freelancer4u;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.repository.CompanyRepository;
import ch.zhaw.freelancer4u.security.TestSecurityConfig;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JobControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CompanyRepository companyRepository;

    static String jobId;
    static String companyId;

    @BeforeAll
    static void setupCompany(@Autowired CompanyRepository repo) {
        if (repo.count() == 0) {
            repo.save(new Company("Test Company", "test@example.com"));
        }
    }

    @Test
    @Order(1)
    void createJob() throws Exception {
        companyId = getCompanyId();
        String jobJson = String.format("""
                {
                    "title": "Test Job",
                    "description": "Test Description",
                    "jobType": "IMPLEMENT",
                    "earnings": 100.0,
                    "companyId": "%s"
                }
                """, companyId);

        MvcResult result = mockMvc.perform(post("/api/job")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobJson)
                        .header("Authorization", "Bearer testtoken"))
                .andExpect(status().isCreated())
                .andReturn();

        jobId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    @Test
    @Order(2)
    void getJobById() throws Exception {
        mockMvc.perform(get("/api/job/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobType").value("IMPLEMENT"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.companyId").value(companyId));
    }

    @Test
    @Order(3)
    void deleteJob() throws Exception {
        mockMvc.perform(delete("/api/job/{id}", jobId)
                        .header("Authorization", "Bearer testtoken"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void getDeletedJob() throws Exception {
        mockMvc.perform(get("/api/job/{id}", jobId))
                .andExpect(status().isNotFound());
    }

    private String getCompanyId() {
        return companyRepository.findAll().get(0).getId();
    }
}
