package ua.kpi.fict.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.fict.cms.entity.Page;

import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {

    Optional<Page> findByCode(String code);
}
