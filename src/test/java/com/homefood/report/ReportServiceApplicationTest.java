package com.homefood.report;

import com.homefood.report.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReportServiceApplicationTest {

    @Mock
    ReportRepository reportRepository;

    @Test
    void contextLoads() {
        assertThat(reportRepository).isNotNull();
    }
}
