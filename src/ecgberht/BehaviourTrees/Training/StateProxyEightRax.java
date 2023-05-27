package ecgberht.BehaviourTrees.Training;

import org.iaie.btree.BehavioralTree.State;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.TrainingFacility;

import ecgberht.GameState;
import ecgberht.Util.Util;

public class StateProxyEightRax extends StateProxy{
	public StateProxyEightRax(TrainingFacility currentFacility, int numberOfBarraks, boolean condition,
			GameState gameState) {
		super(currentFacility, numberOfBarraks, condition, gameState);
	}
}
