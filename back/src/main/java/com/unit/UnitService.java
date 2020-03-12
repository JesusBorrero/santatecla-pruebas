package com.unit;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitService {

	private String UNIT_NAME_SEPARATOR = "/";

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
		unit.setName(getAncestor(unit, getLevel(unit.getName())).getName() + UNIT_NAME_SEPARATOR + unit.getName());
	}

	private int getLevel(String name) {
		return name.split(UNIT_NAME_SEPARATOR).length - 1;
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
		while (unit.getOutgoingRelations().size() > position) {
			parent = findOne(unit.getOutgoingRelations().get(position).getIncoming()).get();
			position++;
			if (!visited.contains(parent)) {
				return parent;
			}
		}
		return null;
	}

	public String getAbsoluteName(Unit unit) {
		Unit parent = unit;
		String absoluteName = "";
		Set<Unit> visited = new HashSet<>();
		while (parent != null) {
			visited.add(parent);
			absoluteName = this.UNIT_NAME_SEPARATOR + parent.getName()  + absoluteName;
			parent = getParent(parent, visited);
		}
		return absoluteName;
	}

	public boolean isValidName(Unit unit) {
		for(Unit nameContaining : unitRepository.findByNameContaining(unit.getName())) {
			if ((nameContaining.getId() != unit.getId()) && (nameContaining.getName().equals(unit.getName())) &&
				((getAbsoluteName(nameContaining).contains(getAbsoluteName(unit))) || (getAbsoluteName(unit).contains(getAbsoluteName(nameContaining))))) {
				return false;
			}
		}
		return true;
	}

}
