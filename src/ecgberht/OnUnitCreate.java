package ecgberht;
import java.util.Map;
import org.openbw.bwapi4j.unit.Unit;
public class OnUnitCreate extends OnUnitAction {
	public OnUnitCreate(Unit unit) {
		super(unit);
	}
	@Override
	void action() {
		if (!unit.getType().isBuilding()) return;
        pushCreatedAllyUnit(unit);
	}
}
