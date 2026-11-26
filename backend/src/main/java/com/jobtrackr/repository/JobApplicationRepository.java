package com.jobtrackr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jobtrackr.model.JobApplication;
import com.jobtrackr.model.Status;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStatusOrderByIdDesc(Status status);

    @Query("select j.status, count(j) from JobApplication j group by j.status")
    List<Object[]> countByStatus();
}
