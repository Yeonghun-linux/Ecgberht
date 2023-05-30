package ecgberht.BehaviourTrees.Defense;

import java.util.Set;

import org.openbw.bwapi4j.unit.Unit;

public interface PerimeterObserver {
    void update(Set<Unit> enemyUnits);
}
