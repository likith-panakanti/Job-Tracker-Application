package com.jobtrackr;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.jobtrackr.model.JobApplication;
import com.jobtrackr.model.Status;
import com.jobtrackr.repository.JobApplicationRepository;

@SpringBootApplication
public class JobTrackrApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobTrackrApplication.class, args);
    }

    /** Adds a few sample cards the first time the app starts so the board is not empty. */
    @Bean
    CommandLineRunner seed(JobApplicationRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.save(sample("Acme Cloud", "Java Backend Developer", "Remote", Status.WISHLIST, null));
            repo.save(sample("Northwind Labs", "Full Stack Engineer", "St. Louis, MO", Status.APPLIED, LocalDate.now().minusDays(3)));
            repo.save(sample("Globex", "Spring Boot Developer", "Chicago, IL", Status.INTERVIEW, LocalDate.now().minusDays(9)));
        };
    }

    private static JobApplication sample(String company, String role, String location, Status status, LocalDate appliedOn) {
        JobApplication job = new JobApplication();
        job.setCompany(company);
        job.setRole(role);
        job.setLocation(location);
        job.setStatus(status);
        job.setAppliedOn(appliedOn);
        return job;
    }
}
