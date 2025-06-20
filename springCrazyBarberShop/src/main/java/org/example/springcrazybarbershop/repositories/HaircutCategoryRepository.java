package org.example.springcrazybarbershop.repositories;

import org.example.springcrazybarbershop.models.HaircutCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HaircutCategoryRepository extends JpaRepository<HaircutCategory, Long> {
}
