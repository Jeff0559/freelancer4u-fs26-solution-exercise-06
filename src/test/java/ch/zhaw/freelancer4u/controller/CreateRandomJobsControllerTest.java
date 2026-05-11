package ch.zhaw.freelancer4u.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.CompanyService;

class CreateRandomJobsControllerTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CreateRandomJobsController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Company testCompany = mock(Company.class);
        when(testCompany.getId()).thenReturn("test-company-id");
        when(companyService.getAllCompanies()).thenReturn(Arrays.asList(testCompany));
        when(jobRepository.findAll()).thenReturn(Arrays.asList());
    }

    @Test
    void testCreateRandomJobs() {
        ResponseEntity<List<Job>> response = controller.createRandomJobs(5);
        assertNotNull(response);
    }
}
