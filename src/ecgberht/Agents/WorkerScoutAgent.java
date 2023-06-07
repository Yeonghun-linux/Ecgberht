package ecgberht.Agents;

import bwem.Area;
import bwem.Base;
import ecgberht.BuildingMap;
import ecgberht.IntelligenceAgency;
import ecgberht.Simulation.SimInfo;
import ecgberht.UnitInfo;
import ecgberht.Util.ColorUtil;
import ecgberht.Util.Util;
import ecgberht.Util.UtilMicro;
import org.bk.ass.path.Result;
import org.openbw.bwapi4j.MapDrawer;
import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.TilePosition;
import org.openbw.bwapi4j.WalkPosition;
import org.openbw.bwapi4j.type.Order;
import org.openbw.bwapi4j.type.Race;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.unit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ecgberht.Ecgberht.getGs;

// Based on SteamHammer worker scout management, props to @JayScott
public class WorkerScoutAgent extends Agent {
    private SCV unit;
    private int currentVertex;
    private List<Position> enemyBaseBorders = new ArrayList<>();
    private Base enemyBase;
    private Status status = Status.IDLE;
    private int enemyNaturalIndex = -1;
    private Building disrupter = null;
    private Building proxier = null;
    private boolean stoppedDisrupting = false;
    private boolean finishedDisrupting = false;
    private SimInfo mySim;
    private List<TilePosition> validTiles = new ArrayList<>();
    private boolean removedIndex = false;
    private boolean ableToProxy = false;
    private TilePosition proxyTile = null;

    public WorkerScoutAgent(Unit unit, Base enemyBase) {
        super(unit);
        this.unit = (SCV) unit;
        this.enemyBase = enemyBase;
        canProxyInThisMap();
    }

    private void canProxyInThisMap() {
        Area enemyArea = this.enemyBase.getArea();
        Set<TilePosition> tilesArea = getGs().map.getTilesArea(enemyArea);
        if (tilesArea == null) return;
        Result path = getGs().silentCartographer.getWalkablePath(enemyBase.getLocation().toWalkPosition(), getGs().enemyNaturalBase.getLocation().toWalkPosition());
        for (TilePosition t : tilesArea) {
            if (!getGs().map.tileBuildable(t, UnitType.Terran_Factory)) continue;
            if (t.getDistance(Util.getUnitCenterPosition(enemyBase.getLocation().toPosition(), UnitType.Zerg_Hatchery).toTilePosition()) <= 13)
                continue;
            if (enemyBase.getGeysers().stream().anyMatch(u -> t.getDistance(u.getCenter().toTilePosition()) <= 9))
                continue;
            if (path.path.stream().anyMatch(u -> t.getDistance(new WalkPosition(u.x, u.y).toTilePosition()) <= 10))
                continue;
            validTiles.add(t);
        }
        if (validTiles.isEmpty()) return;
        double bestDist = 0.0;
        for (TilePosition p : validTiles) {
            double dist = p.getDistance(enemyBase.getLocation());
            if (dist > bestDist) {
                bestDist = dist;
                proxyTile = p;
            }
        }
        if (proxyTile != null) ableToProxy = true;
    }

    public boolean runAgent() {
        if (shouldExitEarly()) return true;

        if (shouldAddToArmy()) return true;

        updateBorders();
        mySim = getGs().sim.getSimulation(unitInfo, SimInfo.SimType.GROUND);

        handleEnemyBaseBorders();

        status = chooseNewStatus();
        cancelDisrupter();

        switch (status) {
            case EXPLORE:
                followPerimeter();
                break;
            case DISRUPTING:
                disrupt();
                break;
            case PROXYING:
                proxy();
                break;
            case IDLE:
                break;
        }

        drawDisrupterText();
        return false;
    }

    private boolean shouldExitEarly() {
        if (unit == null || !unit.exists() || unitInfo == null || !getGs().firstScout) {
            if (disrupter != null) {
                getGs().disrupterBuilding = disrupter;
            }
            getGs().firstScout = false;
            if (getGs().proxyBuilding != null && !getGs().proxyBuilding.isCompleted()) {
                getGs().proxyBuilding.cancelConstruction();
            }
            return true;
        }
        return false;
    }

    private boolean shouldAddToArmy() {
        if (status == Status.EXPLORE && getGs().getStrategyFromManager().getProxy() && mySim.allies.stream().anyMatch(u -> u.unit instanceof Marine)) {
            getGs().myArmy.add(unitInfo);
            getGs().firstScout = false;
            if (getGs().proxyBuilding != null && !getGs().proxyBuilding.isCompleted()) {
                getGs().proxyBuilding.cancelConstruction();
            }
            return true;
        }
        return false;
    }

    private void handleEnemyBaseBorders() {
        if (enemyBaseBorders.isEmpty()) {
            updateBorders();
        }

        if (shouldRemoveEnemyNaturalIndex()) {
            enemyBaseBorders.remove(enemyNaturalIndex);
            enemyNaturalIndex = -1;
            removedIndex = true;
        }
    }

    private boolean shouldRemoveEnemyNaturalIndex() {
        return enemyNaturalIndex != -1 && (
                IntelligenceAgency.getEnemyStrat() == IntelligenceAgency.EnemyStrats.EarlyPool ||
                        IntelligenceAgency.getEnemyStrat() == IntelligenceAgency.EnemyStrats.ZealotRush ||
                        getGs().learningManager.isNaughty() ||
                        getGs().basicCombatUnitsDetected(mySim.enemies) ||
                        IntelligenceAgency.getNumEnemyBases(getGs().getIH().enemy()) > 1
        );
    }

    private void drawDisrupterText() {
        if (disrupter != null) {
            getGs().getGame().getMapDrawer().drawTextMap(disrupter.getPosition().add(new Position(0, -16)), ColorUtil.formatText("BM!", ColorUtil.White));
        }
    }



    private void cancelDisrupter() {
        if (stoppedDisrupting && disrupter != null && disrupter.getHitPoints() <= 20) {
            disrupter.cancelConstruction();
            disrupter = null;
        }
    }

    private void proxy() {
        if (proxier == null) {
            if (unit.getBuildUnit() != null) {
                proxier = (Building) unit.getBuildUnit();
                return;
            }
            if (unit.getOrder() != Order.PlaceBuilding) {
                if (proxyTile.getDistance(unitInfo.tileposition) <= 3) unit.build(proxyTile, UnitType.Terran_Factory);
                else UtilMicro.move(unit, proxyTile.toPosition());
            }
        }
    }

    private void disrupt() {
        if (disrupter == null) {
            if (unit.getBuildUnit() != null) {
                disrupter = (Building) unit.getBuildUnit();
                return;
            }
            if (unit.getOrder() != Order.PlaceBuilding) {
                unit.build(getGs().enemyNaturalBase.getLocation(), UnitType.Terran_Engineering_Bay);
            }
        } else if (disrupter.getRemainingBuildTime() <= 25) {
            haltDisruption();
        } else if (!stoppedDisrupting && shouldStopDisruption()) {
            haltDisruption();
        } else if (shouldStopDisruptionBasedOnEnemyArea()) {
            haltDisruption();
        } else if (shouldResumeBuilding()) {
            resumeBuilding();
        }
    }

    private void haltDisruption() {
        unit.haltConstruction();
        finishedDisrupting = true;
        stoppedDisrupting = true;
        removedIndex = true;
    }

    private boolean shouldStopDisruption() {
        return mySim.enemies.stream()
                .anyMatch(u -> u.unit instanceof Zergling && unitInfo.toUnitInfoDistance().getDistance(u) <= 4 * 32);
    }

    private boolean shouldStopDisruptionBasedOnEnemyArea() {
        if (mySim.enemies.size() != 1) {
            return false;
        }
        UnitInfo closest = mySim.enemies.iterator().next();
        Area enemyArea = getGs().bwem.getMap().getArea(closest.tileposition);
        return closest.unit instanceof Drone
                && enemyArea != null
                && enemyArea.equals(getGs().enemyNaturalArea)
                && mySim.lose;
    }

    private boolean shouldResumeBuilding() {
        return mySim.enemies.isEmpty()
                && disrupter != null
                && !finishedDisrupting;
    }

    private void resumeBuilding() {
        unit.resumeBuilding(disrupter);
    }


    private Status chooseNewStatus() {
        if (status == Status.DISRUPTING) {
            return chooseNewStatusForDisrupting();
        }
        if (status == Status.PROXYING) {
            return chooseNewStatusForProxying();
        }
        if (finishedDisrupting) {
            return Status.EXPLORE;
        }
        String strategy = getGs().getStrategyFromManager().getName();
        if (getGs().luckyDraw >= 0.7 && ableToProxy && strategy.equals("TwoPortWraith") && !getGs().learningManager.isNaughty() && !getGs().MBs.isEmpty() && !getGs().refineriesAssigned.isEmpty()) {
            return Status.PROXYING;
        }
        if (getGs().luckyDraw >= 0.35 || strategy.equals("BioGreedyFE") || strategy.equals("MechGreedyFE")
                || strategy.equals("BioMechGreedyFE") || strategy.equals("ProxyBBS") || strategy.equals("ProxyEightRax") || getGs().learningManager.isNaughty()) {
            return Status.EXPLORE;
        }
        if (getGs().enemyRace != Race.Zerg || stoppedDisrupting || finishedDisrupting) {
            return Status.EXPLORE;
        }
        if (IntelligenceAgency.getNumEnemyBases(getGs().getIH().enemy()) == 1 && currentVertex == enemyNaturalIndex) {
            return Status.DISRUPTING;
        }
        return Status.EXPLORE;
    }

    private Status chooseNewStatusForDisrupting() {
        if (finishedDisrupting || (stoppedDisrupting && !unitInfo.attackers.isEmpty() && mySim.lose)) {
            return Status.EXPLORE;
        }
        return Status.DISRUPTING;
    }

    private Status chooseNewStatusForProxying() {
        if (proxyTile == null) {
            ableToProxy = false;
        } else {
            if (proxier != null && proxier.isCompleted()) {
                ableToProxy = false;
                getGs().proxyBuilding = proxier;
            } else {
                if (shouldHaltConstructionAndCancel()) {
                    return Status.EXPLORE;
                }
            }
        }
        return Status.PROXYING;
    }

    private boolean shouldHaltConstructionAndCancel() {
        double dist = unitInfo.tileposition.getDistance(proxyTile);
        if (dist <= 3 && !unitInfo.attackers.isEmpty() && proxier != null && proxier.isBeingConstructed()) {
            unit.haltConstruction();
            ableToProxy = false;
            proxier.cancelConstruction();
            proxier = null;
            return true;
        }
        return false;
    }

    private Position getNextPosition() {
        if (currentVertex == -1) {
            return handleClosestPolygonVertex();
        }
        if (currentVertex == enemyNaturalIndex && isEnemyNaturalVisible()) {
            return handleEnemyNaturalVertex();
        }
        return handleDistClosestVertex();
    }

    private Position handleClosestPolygonVertex(){
        int closestPolygonIndex = getClosestVertexIndex();
        if (closestPolygonIndex == -1) return getGs().getPlayer().getStartLocation().toPosition();
        currentVertex = closestPolygonIndex;
        return enemyBaseBorders.get(closestPolygonIndex);
    }

    private Position handleEnemyNaturalVertex() {
        currentVertex = (currentVertex + 1) % enemyBaseBorders.size();
        return enemyBaseBorders.get(currentVertex);
    }

    private Position handleDistClosestVertex(){
        double distanceFromCurrentVertex = enemyBaseBorders.get(currentVertex).getDistance(unit.getPosition());
        while (distanceFromCurrentVertex < 128) {
            currentVertex = (currentVertex + 1) % enemyBaseBorders.size();
            distanceFromCurrentVertex = enemyBaseBorders.get(currentVertex).getDistance(unit.getPosition());
        }
        return enemyBaseBorders.get(currentVertex);
    }

    private boolean isEnemyNaturalVisible() {
        return getGs().getGame().getBWMap().isVisible(getGs().enemyNaturalBase.getLocation());
    }

    private int getClosestVertexIndex() {
        int chosen = -1;
        double distMax = Double.MAX_VALUE;
        for (int ii = 0; ii < enemyBaseBorders.size(); ii++) {
            double dist = enemyBaseBorders.get(ii).getDistance(unit.getPosition());
            if (dist < distMax) {
                chosen = ii;
                distMax = dist;
            }
        }
        return chosen;
    }

    private void followPerimeter() {
        UtilMicro.move(unit, getNextPosition());
    }

    private void updateBorders() {
        final Area enemyRegion = enemyBase.getArea();
        if (enemyRegion == null) return;

        final Position enemyCenter = enemyBase.getLocation().toPosition().add(new Position(64, 48));
        List<Position> unsortedVertices = findUnsortedVertices(enemyRegion, enemyCenter);
        List<Position> sortedVertices = sortVertices(unsortedVertices);

        int distanceThreshold = 100;
        sortedVertices = refineVertices(sortedVertices, distanceThreshold);

        enemyBaseBorders = sortedVertices;
        currentVertex = 0;

        if (!getGs().learningManager.isNaughty()) {
            Base enemyNatural = getGs().enemyNaturalBase;
            if (enemyNatural != null) {
                Position enemyNaturalPos = enemyNatural.getLocation().toPosition();
                int index = getClosestVertexIndex();
                enemyBaseBorders.add(index, enemyNaturalPos);
                this.enemyNaturalIndex = index;
            }
        } else {
            enemyNaturalIndex = -1;
            removedIndex = true;
        }
    }


    private List<Position> findUnsortedVertices(Area enemyRegion, Position enemyCenter) {
        List<Position> unsortedVertices = new ArrayList<>();
        for (TilePosition tp : BuildingMap.tilesArea.get(enemyRegion)) {
            if (shouldSkipTilePosition(tp, enemyRegion)) continue;

            Position vertex = tp.toPosition().add(new Position(16, 16));
            double dist = enemyCenter.getDistance(vertex);

            if (isEdgeTile(tp, enemyRegion) && dist > 368.0) {
                double pullBy = Math.min(dist - 368.0, 120.0);
                if (vertex.getX() == enemyCenter.getX()) {
                    int verticalPull = vertex.getY() > enemyCenter.getY() ? (int) (-pullBy) : (int) pullBy;
                    vertex = vertex.add(new Position(0, verticalPull));
                } else {
                    vertex = calculateVertexPosition(pullBy, vertex, enemyCenter);
                }

                if (isWalkable(vertex)) {
                    unsortedVertices.add(vertex);
                }
            }
        }
        return unsortedVertices;
    }

    private Position calculateVertexPosition(double pullBy, Position vertex, Position enemyCenter) {
        double m = (double) (enemyCenter.getY() - vertex.getY()) / (double) (enemyCenter.getX() - vertex.getX());
        double x = vertex.getX() + (vertex.getX() > enemyCenter.getX() ? -1.0 : 1.0) * pullBy / (Math.sqrt(1 + m * m));
        double y = m * (x - vertex.getX()) + vertex.getY();
        return new Position((int) x, (int) y);
    }

    private List<Position> sortVertices(List<Position> unsortedVertices) {
        List<Position> sortedVertices = new ArrayList<>();
        Position current = unsortedVertices.get(0);
        sortedVertices.add(current);
        unsortedVertices.remove(current);
        while (!unsortedVertices.isEmpty()) {
            double bestDist = Double.POSITIVE_INFINITY;
            Position bestPos = null;
            for (Position pos : unsortedVertices) {
                double dist = pos.getDistance(current);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestPos = pos;
                }
            }
            current = bestPos;
            sortedVertices.add(bestPos);
            unsortedVertices.remove(bestPos);
        }
        return sortedVertices;
    }

    private List<Position> refineVertices(List<Position> sortedVertices, int distanceThreshold) {
        while (true) {
            int maxFarthest = 0;
            int maxFarthestStart = 0;
            int maxFarthestEnd = 0;
            for (int i = 0; i < sortedVertices.size(); ++i) {
                int farthest = 0;
                int farthestIndex = 0;
                for (int j = 1; j < sortedVertices.size() / 2; ++j) {
                    int jindex = (i + j) % sortedVertices.size();
                    if (sortedVertices.get(i).getDistance(sortedVertices.get(jindex)) < distanceThreshold) {
                        farthest = j;
                        farthestIndex = jindex;
                    }
                }
                if (farthest > maxFarthest) {
                    maxFarthest = farthest;
                    maxFarthestStart = i;
                    maxFarthestEnd = farthestIndex;
                }
            }
            if (maxFarthest < 4) break;
            List<Position> temp = new ArrayList<>();
            for (int s = maxFarthestEnd; s != maxFarthestStart; s = (s + 1) % sortedVertices.size()) {
                temp.add(sortedVertices.get(s));
            }
            sortedVertices = temp;
        }
        return sortedVertices;
    }

    private boolean shouldSkipTilePosition(TilePosition tilePosition, Area enemyRegion) {
        return getGs().bwem.getMap().getArea(tilePosition) != enemyRegion;
    }

    private boolean isEdgeTile(TilePosition tp, Area enemyRegion) {
        TilePosition right = new TilePosition(tp.getX() + 1, tp.getY());
        TilePosition bottom = new TilePosition(tp.getX(), tp.getY() + 1);
        TilePosition left = new TilePosition(tp.getX() - 1, tp.getY());
        TilePosition up = new TilePosition(tp.getX(), tp.getY() - 1);

        boolean isRightEdge = !isValidAndBuildable(right, enemyRegion);
        boolean isBottomEdge = !isValidAndBuildable(bottom, enemyRegion);
        boolean isLeftEdge = !isValidAndBuildable(left, enemyRegion);
        boolean isUpEdge = !isValidAndBuildable(up, enemyRegion);

        return isRightEdge || isBottomEdge || isLeftEdge || isUpEdge;
    }

    private boolean isValidAndBuildable(TilePosition tilePosition, Area enemyRegion) {
        return getGs().getGame().getBWMap().isValidPosition(tilePosition)
                && getGs().bwem.getMap().getArea(tilePosition) == enemyRegion
                && getGs().getGame().getBWMap().isBuildable(tilePosition);
    }


    private boolean isWalkable(Position position) {
        return getGs().getGame().getBWMap().isValidPosition(position)
                && getGs().getGame().getBWMap().isWalkable(position.toWalkPosition());
    }


    enum Status {
        EXPLORE, DISRUPTING, IDLE, PROXYING
    }

    public String statusToString() {
        if (status == Status.EXPLORE) return "Exploring";
        if (status == Status.DISRUPTING) return "Disrupting";
        if (status == Status.PROXYING) return "Proxying";
        if (status == Status.IDLE) return "Idle";
        return "None";
    }

    @Override
    public void drawAgentOnMap(Agent agent, MapDrawer mapDrawer) {
        WorkerScoutAgent worker = (WorkerScoutAgent) agent;
        mapDrawer.drawTextMap(worker.myUnit.getPosition().add(new Position(-16, UnitType.Terran_SCV.dimensionUp())), ColorUtil.formatText(agent.statusToString(), ColorUtil.White));
    }
}
