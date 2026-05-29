package me.sathish.dbcleaner.base.jobcleaner.repos;

import me.sathish.dbcleaner.base.jobcleaner.domain.Jobcleaner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JobcleanerRepository extends JpaRepository<Jobcleaner, Long> {

    Page<Jobcleaner> findAllById(Long id, Pageable pageable);

    Page<Jobcleaner> findAllByOrderByDeletedAtDesc(Pageable pageable);

    boolean existsByRunsServiceNameIgnoreCase(String runsServiceName);

}
