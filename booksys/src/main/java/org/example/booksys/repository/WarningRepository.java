package org.example.booksys.repository;

import org.example.booksys.model.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Integer> {
}