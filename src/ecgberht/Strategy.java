package ecgberht;
import org.openbw.bwapi4j.type.TechType;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.type.UpgradeType;

import ecgberht.Strategies.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
public abstract class Strategy implements Comparable<Strategy> {
	protected boolean bunker = false;
	protected  boolean proxy = false;
	protected  boolean harass = true;
	protected  int armyForAttack = 0;
	protected  int armyForExpand = 20;
	protected  int armyForTurret = 0;
	protected  int facPerCC = 0;
	protected  int numBays = 0;
	protected  int numCCForPort = 0;
	protected  int workerGas = 3;
	protected  int numCCForScience = 0;
	protected  int numRaxForAca = 2;
	protected  int numRaxForFac = 0;
	protected  int numFacForPort = 1;
	protected  int extraSCVs = 3;
	protected  int portPerCC = 0;
	protected  int raxPerCC = 0;
	protected  int supplyForFirstRefinery = 0;
	protected  String name;
	protected  int armyForBay = 0;
	protected  int facForArmory = 0;
	protected  int numArmories = 0;
	protected  Set<TechType> techToResearch = new HashSet<>();
	protected  Set<UnitType> buildAddons = new HashSet<>();
	protected  Set<UnitType> buildUnits = new HashSet<>();
	protected  Set<UnitType> trainUnits = new HashSet<>();
	protected Set<UpgradeType> upgradesToResearch = new HashSet<>();
    
	public Strategy(String name, int armyForBay, int armyForTurret, int numBays, int raxPerCC,int facPerCC
			, int numRaxForAca, int numRaxForFac, int supplyForFirstRefinery, int armyForAttack, int armyForExpand,
			int numCCForPort, int numCCForScience, int portPerCC, int facForArmory, int numArmories,boolean bunker) 
	{
		this.name=name;
		this.armyForBay=armyForBay;
		this.armyForTurret =armyForTurret;
		this.numBays = numBays;
		this.raxPerCC = raxPerCC;
		this.facPerCC = facPerCC;
		this.numRaxForAca = numRaxForAca;
		this.numRaxForFac = numRaxForFac;
		this.supplyForFirstRefinery = supplyForFirstRefinery;
		this.armyForAttack = armyForAttack;
		this.armyForExpand = armyForExpand;
		this.numCCForPort = numCCForPort;
		this.numCCForScience = numCCForScience;
		this.portPerCC = portPerCC;
		this.facForArmory =facForArmory;
		this.numArmories = numArmories;
		this.bunker = bunker;
	}

 

	public abstract void initStrategy();

    public abstract void initTrainUnits();

    public abstract void initBuildUnits();

    public abstract void initBuildAddons();

    public abstract void initTechToResearch();

    public abstract void initUpgradesToResearch();


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Strategy)) return false;
        Strategy strat = (Strategy) o;
        return strat.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public int compareTo(Strategy v1) {
        return this.getName().compareTo(v1.getName());
    }

    public boolean requiredUnitsForAttack() {
        return true;
    }

	public String getName() {
		return name;
	}
	
	public boolean isBunker() {
		return bunker;
	}

	public boolean isProxy() {
		return proxy;
	}

	public boolean isHarass() {
		return harass;
	}

	public int getArmyForAttack() {
		return armyForAttack;
	}

	public int getArmyForExpand() {
		return armyForExpand;
	}

	public int getArmyForTurret() {
		return armyForTurret;
	}

	public int getFacPerCC() {
		return facPerCC;
	}

	public int getNumBays() {
		return numBays;
	}

	public int getNumCCForPort() {
		return numCCForPort;
	}

	public int getWorkerGas() {
		return workerGas;
	}

	public int getNumCCForScience() {
		return numCCForScience;
	}

	public int getNumRaxForAca() {
		return numRaxForAca;
	}

	public int getNumRaxForFac() {
		return numRaxForFac;
	}

	public int getNumFacForPort() {
		return numFacForPort;
	}

	public int getExtraSCVs() {
		return extraSCVs;
	}

	public int getPortPerCC() {
		return portPerCC;
	}

	public int getRaxPerCC() {
		return raxPerCC;
	}
	public boolean getProxy() {
		return proxy;
	}

	public int getSupplyForFirstRefinery() {
		return supplyForFirstRefinery;
	}

	public int getArmyForBay() {
		return armyForBay;
	}

	public int getFacForArmory() {
		return facForArmory;
	}

	public int getNumArmories() {
		return numArmories;
	}

	public Set<TechType> getTechToResearch() {
		return techToResearch;
	}

	public Set<UnitType> getBuildAddons() {
		return buildAddons;
	}

	public Set<UnitType> getBuildUnits() {
		return buildUnits;
	}

	public Set<UnitType> getTrainUnits() {
		return trainUnits;
	}

	public Set<UpgradeType> getUpgradesToResearch() {
		return upgradesToResearch;
	}

}
