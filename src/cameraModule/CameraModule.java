package cameraModule;

import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.unit.Unit;

public interface CameraModule {

    void onFrame();
    boolean isNearStartLocation(Position pos);

    boolean isNearOwnStartLocation(Position pos);

    boolean isArmyUnit(Unit unit);

    boolean shouldMoveCamera(int priority);

    void moveCamera(Position pos, int priority);

    void moveCamera(Unit unit, int priority);

    void moveCameraIsAttacking();

    void moveCameraIsUnderAttack();

    void moveCameraScoutWorker();

    void moveCameraFallingNuke();

    void moveCameraNukeDetect(Position target);

    void moveCameraDrop();

    void moveCameraArmy();

    void moveCameraUnitCompleted(Unit unit);

    void updateCameraPosition();

    void toggle();
}
