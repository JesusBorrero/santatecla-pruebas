package com.unit;

import java.util.List;
import java.util.Optional;

import com.card.Card;
import com.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    public List<Unit> findByName(String name);

    public List<Unit> findByNameContaining(String name);
    
    public List<Unit> findAll();

    @Query(value = "select unit.id, unit.name from unit left join unit_relations on unit.id = unit_relations.unit_id left join relation on unit_relations.relations_id = relation.id where related_to_id = ?1", nativeQuery = true)
    public Unit getParent(Long id);

    @Query(value = "SELECT unit_lessons.unit_id FROM unit_lessons WHERE unit_lessons.lessons_id = ?1", nativeQuery = true)
    Long findLessonUnit(long lessonId);

    @Query(value = "SELECT unit_modules.unit_id FROM unit_modules WHERE unit_modules.modules_id = ?1", nativeQuery = true)
    Long findModuleUnit(long lessonId);
}