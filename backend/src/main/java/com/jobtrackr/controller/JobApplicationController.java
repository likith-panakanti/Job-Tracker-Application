package com.jobtrackr.controller;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jobtrackr.model.JobApplication;
import com.jobtrackr.model.Status;
import com.jobtrackr.repository.JobApplicationRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    /** Body of PATCH /{id}/status. */
    public record StatusUpdate(@NotNull Status status) { }

    private final JobApplicationRepository repo;

    public JobApplicationController(JobApplicationRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<JobApplication> list(@RequestParam(required = false) Status status) {
        return status == null
                ? repo.findAll(Sort.by(Sort.Direction.DESC, "id"))
                : repo.findByStatusOrderByIdDesc(status);
    }

    /** Number of applications per status, including zeros, for the summary bar. */
    @GetMapping("/stats")
    public Map<Status, Long> stats() {
        Map<Status, Long> result = new EnumMap<>(Status.class);
        for (Status s : Status.values()) result.put(s, 0L);
        for (Object[] row : repo.countByStatus()) result.put((Status) row[0], (Long) row[1]);
        return result;
    }

    @GetMapping("/{id}")
    public JobApplication get(@PathVariable Long id) {
        return find(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplication create(@Valid @RequestBody JobApplication body) {
        body.setId(null);
        return repo.save(body);
    }

    @PutMapping("/{id}")
    public JobApplication update(@PathVariable Long id, @Valid @RequestBody JobApplication body) {
        JobApplication job = find(id);
        job.setCompany(body.getCompany());
        job.setRole(body.getRole());
        job.setLocation(body.getLocation());
        job.setLink(body.getLink());
        job.setStatus(body.getStatus());
        job.setAppliedOn(body.getAppliedOn());
        job.setNotes(body.getNotes());
        return repo.save(job);
    }

    @PatchMapping("/{id}/status")
    public JobApplication updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdate body) {
        JobApplication job = find(id);
        job.setStatus(body.status());
        return repo.save(job);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.delete(find(id));
    }

    private JobApplication find(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application " + id + " not found"));
    }
}
