package com.skinversity.backend.Repositories;

import com.skinversity.backend.Models.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Reviews, UUID> {
}
