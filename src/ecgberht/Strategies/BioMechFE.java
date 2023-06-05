package ecgberht.Strategies;

import ecgberht.Strategy;
import ecgberht.Strategies.BioMech.BioMechBuilder;
import ecgberht.Util.Util;

import java.util.HashSet;
import java.util.Set;

import org.openbw.bwapi4j.type.TechType;
import org.openbw.bwapi4j.type.UnitType;
import org.openbw.bwapi4j.type.UpgradeType;

public class BioMechFE extends Strategy {

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
        trainUnits.add(UnitType.Terran_Medic);
        trainUnits.add(UnitType.Terran_Siege_Tank_Tank_Mode);
        trainUnits.add(UnitType.Terran_Wraith);
    }

    @Override
    public void initBuildUnits() {
        buildUnits.add(UnitType.Terran_Academy);
        buildUnits.add(UnitType.Terran_Engineering_Bay);
        buildUnits.add(UnitType.Terran_Armory);
        buildUnits.add(UnitType.Terran_Missile_Turret);
        buildUnits.add(UnitType.Terran_Factory);
        buildUnits.add(UnitType.Terran_Starport);
        buildUnits.add(UnitType.Terran_Science_Facility);
    }

    @Override
    public void initBuildAddons() {
        buildAddons.add(UnitType.Terran_Comsat_Station);
        buildAddons.add(UnitType.Terran_Machine_Shop);
        buildAddons.add(UnitType.Terran_Control_Tower);
    }

    @Override
    public void initTechToResearch() {
        techToResearch.add(TechType.Stim_Packs);
        techToResearch.add(TechType.Tank_Siege_Mode);
    }

    @Override
    public void initUpgradesToResearch() {
        upgradesToResearch.add(UpgradeType.Terran_Infantry_Weapons);
        upgradesToResearch.add(UpgradeType.Terran_Infantry_Armor);
        upgradesToResearch.add(UpgradeType.U_238_Shells);
        upgradesToResearch.add(UpgradeType.Terran_Vehicle_Weapons);
    }

    @Override
    public boolean requiredUnitsForAttack() {
        return Util.countUnitTypeSelf(UnitType.Terran_Siege_Tank_Tank_Mode) >= 3;
    }
    public static class BioMechFEBuilder{
		protected boolean bunker = false;
		protected boolean proxy = false;
		protected boolean harass = true;
		protected int armyForAttack = 0;
		protected int armyForExpand = 20;
		protected int armyForTurret = 0;
		protected int facPerCC = 0;
		protected int numBays = 0;
		protected int numCCForPort = 0;
		protected int workerGas = 3;
		protected int numCCForScience = 0;
		protected int numRaxForAca = 2;
		protected int numRaxForFac = 0;
		protected int numFacForPort = 1;
		protected int extraSCVs = 3;
		protected int portPerCC = 0;
		protected int raxPerCC = 0;
		protected int supplyForFirstRefinery = 0;
		protected String name;
		protected int armyForBay = 0;
		protected int facForArmory = 0;
		protected int numArmories = 0;
		protected Set<TechType> techToResearch = new HashSet<>();
		protected Set<UnitType> buildAddons = new HashSet<>();
		protected Set<UnitType> buildUnits = new HashSet<>();
		protected Set<UnitType> trainUnits = new HashSet<>();
		protected Set<UpgradeType> upgradesToResearch = new HashSet<>();
		
		public BioMechFEBuilder(String name, int armyForBay, int armyForTurret, int numBays, int raxPerCC, int facPerCC,
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
		public BioMechFE build() {
			return new BioMechFE(this);
		}
    	
    }
	private BioMechFE(BioMechFEBuilder builder) 
	{
		super(builder.name,builder.armyForBay,builder.armyForTurret,builder.numBays,builder.raxPerCC
				,builder.facPerCC,builder.numRaxForAca,builder.numRaxForFac,builder.supplyForFirstRefinery,
				 builder.armyForAttack,builder.armyForExpand,builder.numCCForPort,builder.numCCForScience,
				 builder.portPerCC,builder.facForArmory,builder.numArmories,builder.bunker);
		
		initStrategy();
	}
}
