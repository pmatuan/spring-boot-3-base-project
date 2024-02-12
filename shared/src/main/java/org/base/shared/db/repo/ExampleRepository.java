package org.base.shared.db.repo;

import java.time.Instant;
import java.util.Optional;
import org.base.shared.db.entities.Example;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
  Optional<Example> findById(@NotNull Long id);

  @Query("""
        select e
        from Example e
        where (:description = '%%' or e.description like :description)
        and (:createdFrom is null or e.createdAt >= :createdFrom)
        and (:createdTo is null or e.createdAt <= :createdTo)
        and (:createdBy = '%%' or e.createdBy like :createdBy)
    """)
  Page<Example> getExamplePage(@Param("description") String description,
      @Param("createdFrom") Instant createdFrom,
      @Param("createdTo") Instant createdTo,
      @Param("createdBy") String createdBy,
      Pageable pageable
  );
}

