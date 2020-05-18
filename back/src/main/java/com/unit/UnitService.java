package com.unit;

import java.util.*;

import com.relation.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitService {

	private String unitNameSeparator = "/";

	@Autowired
	private UnitRepository unitRepository;

	public List<Unit> findAll() {
		return unitRepository.findAll();
	}

	public Optional<Unit> findOne(long id) {
		return unitRepository.findById(id);
	}
	
    public void save(Unit unit) {
		unitRepository.save(unit);
	}

	public void delete(long id) {
		unitRepository.deleteById(id);
	}

	public List<Unit> findByName(String name) {
		return resolveDuplicateNames(unitRepository.findByName(name));
	}

	public List<Unit> findByNameContaining(String name) {
		return resolveDuplicateNames(unitRepository.findByNameContaining(name));
	}

	private List<Unit> resolveDuplicateNames(List<Unit> units) {
		Map<String, List<Unit>> duplicatesMap = findDuplicates(units);
		for (String key : duplicatesMap.keySet()) {
			List<Unit> duplicates = duplicatesMap.get(key);
			for (int i = 0; i < duplicates.size(); i++) {
				for (int j = i + 1; j < duplicates.size(); j++) {
					Unit unitI = duplicates.get(i);
					Unit unitJ = duplicates.get(j);
					String oldNameI = "";
					String oldNameJ = "";
					while (((!oldNameI.equals(unitI.getName())) && (!oldNameJ.equals(unitJ.getName()))) && (unitI.getName().contains(unitJ.getName()) || unitJ.getName().contains(unitI.getName()))) {
						if ((unitI.getName().contains(unitJ.getName())) && (getAncestor(unitJ, getLevel(unitJ.getName())) != null)) {
							oldNameJ = unitJ.getName();
							setParentOnName(unitJ);
						}
						if ((unitJ.getName().contains(unitI.getName())) && (getAncestor(unitI, getLevel(unitI.getName())) != null)) {
							oldNameI = unitI.getName();
							setParentOnName(unitI);
						}
					}
				}
			}
		}
		return units;
	}

	private Map<String, List<Unit>> findDuplicates(List<Unit> units) {
		Map<String, List<Unit>> duplicates = new HashMap<>();
		for (int i = 0; i < units.size(); i++) {
			for (int j = i + 1; j < units.size(); j++) {
				if (units.get(i).getName().equals(units.get(j).getName())) {
					List<Unit> ids = duplicates.get(units.get(i).getName());
					if (ids == null) {
						List<Unit> newList = new ArrayList<>();
						newList.add(units.get(i));
						newList.add(units.get(j));
						duplicates.put(units.get(i).getName(), newList);
					} else {
						if (!ids.contains(units.get(i))) {
							ids.add(units.get(i));
						}
						if (!ids.contains(units.get(j))) {
							ids.add(units.get(j));
						}
					}
				}
			}
		}
		return duplicates;
	}

	private void setParentOnName(Unit unit) {
		unit.setName(getAncestor(unit, getLevel(unit.getName())).getName() + unitNameSeparator + unit.getName());
	}

	private int getLevel(String name) {
		return name.split(unitNameSeparator).length - 1;
	}

	private Unit getAncestor(Unit unit, int level) {
		Unit parent = getParent(unit, new HashSet<>());
		while (level > 0) {
			parent = getParent(parent, new HashSet<>());
			level--;
		}
		return parent;
	}

	Unit getParent(Unit unit, Set<Unit> visited) {
		Unit parent;
		int position = 0;
		visited.add(unit);
		Optional<Unit> optional;
		while (unit.getOutgoingRelations().size() > position) {
			optional = findOne(unit.getOutgoingRelations().get(position).getIncoming());
			if(optional.isPresent()) {
				parent = optional.get();
				position++;
				if (!visited.contains(parent)) {
					return parent;
				}
			}
		}
		return null;
	}

	public String getUnambiguousName(Unit unit) {
		return findByNameContaining(unit.getName()).stream().filter(u -> (unit.getId() == u.getId())).findAny().orElse(unit).getName();
	}

	public String getAbsoluteName(Unit unit) {
		Unit parent = unit;
		String absoluteName = "";
		Set<Unit> visited = new HashSet<>();
		while (parent != null) {
			visited.add(parent);
			absoluteName = this.unitNameSeparator + parent.getName()  + absoluteName;
			parent = getParent(parent, visited);
		}
		return absoluteName;
	}

	public boolean isValidName(Unit unit) {
		for(Unit unitWithNameContaining : unitRepository.findByNameContaining(unit.getName())) {
			if ((unitWithNameContaining.getId() != unit.getId()) && (unitWithNameContaining.getName().equals(unit.getName())) &&
				((getAbsoluteName(unitWithNameContaining).contains(getAbsoluteName(unit))) || (getAbsoluteName(unit).contains(getAbsoluteName(unitWithNameContaining))))) {
				return false;
			}
		}
		for (Unit unitWithName : unitRepository.findByName(unit.getName())) {
			if (unit.getId() != unitWithName.getId()) {
				for (Relation unitOutgoingRelation : unit.getOutgoingRelations()) {
					for (Relation unitWithNameOutgoingRelation : unitWithName.getOutgoingRelations()) {
						if (unitOutgoingRelation.getIncoming().equals(unitWithNameOutgoingRelation.getIncoming())) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public boolean ableToDeleteUnit(Unit unit) {
		Optional<Unit> optional;
		for (Relation relation : unit.getIncomingRelations()) {
			optional = findOne(relation.getOutgoing());
			if(optional.isPresent()) {
				Unit outgoing = optional.get();
				if ((outgoing.getOutgoingRelations().size() <= 1) && (findByName(outgoing.getName()).size() > 1)) {
					return false;
				}
			}
		}
		return true;
	}

	public Long findLessonUnit(long lessonId){
		return this.unitRepository.findLessonUnit(lessonId);
	}

	public Long findModuleUnit(long moduleId){
		return this.unitRepository.findModuleUnit(moduleId);
	}

}
