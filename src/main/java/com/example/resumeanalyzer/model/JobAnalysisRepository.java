package com.example.resumeanalyzer.repository;

import com.example.resumeanalyzer.model.JobAnalysis;
import com.example.resumeanalyzer.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAnalysisRepository extends JpaRepository<JobAnalysis, Long> {

    List<JobAnalysis> findByUser(User user);

}