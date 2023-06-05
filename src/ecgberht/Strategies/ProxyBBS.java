package ecgberht.Strategies;

import ecgberht.Strategy;
import ecgberht.Strategies.PlasmaWraithHell.PlasmaWraithHellBuilder;
import ecgberht.Util.Util;

import java.util.HashSet;
import java.util.Set;

import org.openbw.bwapi4j.type.TechType;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.type.UpgradeType;

public class ProxyBBS extends Strategy {

    @Override
    public void initStrategy() {
     
        initTrainUnits();
        initBuildUnits();
        initBuildAddons();
        initTechToResearch();
        initUpgradesToResearch();
    }

    @Override
    public void initTrainUnits() {
        trainUnits.add(UnitType.Terran_Marine);
    }

    @Override
    public void initBuildUnits() {
    }

    @Override
    public void initBuildAddons() {

    }

    @Override
    public void initTechToResearch() {

    }

    @Override
    public void initUpgradesToResearch() {

    }

    @Override
    public boolean requiredUnitsForAttack() {
        return Util.countUnitTypeSelf(UnitType.Terran_Marine) >= 4;
    }
    public static class ProxyBBSBuilder{
  		private boolean bunker = false;
  		private boolean proxy = false;
  		private boolean harass = true;
  		private int armyForAttack = 0;
  		private int armyForExpand = 20;
  		private int armyForTurret = 0;
  		private int facPerCC = 0;
  		private int numBays = 0;
  		private int numCCForPort = 0;
  		private int workerGas = 3;
  		private int numCCForScience = 0;
  		private int numRaxForAca = 2;
  		private int numRaxForFac = 0;
  		private int numFacForPort = 1;
  		private int extraSCVs = 3;
  		private int portPerCC = 0;
  		private int raxPerCC = 0;
  		private int supplyForFirstRefinery = 0;
  		private String name;
  		private int armyForBay = 0;
  		private int facForArmory = 0;
  		private int numArmories = 0;
  		private Set<TechType> techToResearch = new HashSet<>();
  		private Set<UnitType> buildAddons = new HashSet<>();
  		private Set<UnitType> buildUnits = new HashSet<>();
  		private Set<UnitType> trainUnits = new HashSet<>();
  		private Set<UpgradeType> upgradesToResearch = new HashSet<>();
  		
    public ProxyBBSBuilder(String name, int armyForBay, int armyForTurret, int numBays, int raxPerCC, int facPerCC,
				int numRaxForAca, int numRaxForFac, int supplyForFirstRefinery, int armyForAttack, int armyForExpand,
				int numCCForPort, int numCCForScience, int portPerCC, int facForArmory, int numArmories,
				boolean bunker) {
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
		public ProxyBBS build() {
			return new ProxyBBS(this);
		}
		public ProxyBBSBuilder setProxy(boolean proxy) {
			this.proxy= proxy;
			return this;
		}
  	
    }
		private ProxyBBS(ProxyBBSBuilder builder) 
	{
		super(builder.name,builder.armyForBay,builder.armyForTurret,builder.numBays,builder.raxPerCC
				,builder.facPerCC,builder.numRaxForAca,builder.numRaxForFac,builder.supplyForFirstRefinery,
				 builder.armyForAttack,builder.armyForExpand,builder.numCCForPort,builder.numCCForScience,
				 builder.portPerCC,builder.facForArmory,builder.numArmories,builder.bunker);
		initStrategy();
	}
}
