package ecgberht;

import ecgberht.Strategies.*;
import ecgberht.Util.MutablePair;
import ecgberht.Util.Util;

import org.openbw.bwapi4j.Position;
import org.openbw.bwapi4j.type.Race;
import org.openbw.bwapi4j.type.UnitType;

import java.util.*;
import java.util.function.Consumer;

import static ecgberht.Ecgberht.getGs;

public class StrategyManager {
	private static StrategyManager managerInstance;
	  private StrategyManager() {
	        initBaseStrategies();
	        this.setStrategy(initStrat());
	        AddSpecialUnitsIfParticularMap();
	    }
	  public static StrategyManager getInstance() {
		  if(!managerUsed) {
			  managerInstance = new StrategyManager();
			  managerUsed = true;
		  }
			return managerInstance;
		}
	
	private static boolean managerUsed = false;

    private Map<String, MutablePair<Integer, Integer>> strategies = new LinkedHashMap<>();
    private Map<String, Strategy> nameOfStrategies = new LinkedHashMap<>();
    private Strategy strategy; 
    private Strategy fullBio = new FullBio
    		.FullBioBuilder("FullBio",15,10,2,3,0,2,4,36,32,15,2,2,0,0,0,false)
    		.build();
    private Strategy proxyBBS = new ProxyBBS
    		.ProxyBBSBuilder("ProxyBBS",0,0,0,2,0,0,0,400,8,100,0,0,0,0,0,false)
    		.setProxy(true)
    		.build();
    private Strategy bioMech = new BioMech
    		.BioMechBuilder("BioMech",24,10,1,2,1,2,1,2,2,0,36,30,18,2,1,false)
    		.build();
    private Strategy fullBioFE = new FullBioFE
    		.FullBioFEBuilder("FullBioFE",15,10,2,3,0,2,4,36,28,8,2,2,0,0,0,false)
    		.build();
    private Strategy bioMechFE = new BioMechFE
    		.BioMechFEBuilder("BioMechFE",15,10,1,3,1,2,1,38,25,9,2,2,0,2,1,false)
    		.build();
    private Strategy fullMech = new FullMech
    		.FullMechBuilder("FullMech",15,10,1,1,2,1,1,28,30,8,1,2,1,2,1,false)
    		.build();
    private Strategy bioGreedyFE = new BioGreedyFE
    		.BioGreedyFEBuilder("BioGreedyFE", 15, 10, 2, 3, 0, 2, 3, 38, 25, 0, 2, 2, 0, 0, 0, true)
    		.build();
    private Strategy mechGreedyFE = new MechGreedyFE
    		.MechGreedyFEBuilder("MechGreedyFE",15,10,1,1,2,2,1,36,30,0,2,2,1,2,1,true).
    		build();
    private Strategy bioMechGreedyFE = new BioMechGreedyFE
    		.BioMechGreedyFEBuilder("BioMechGreedyFE",15,10,1,2,1,2,2,38,30,0,3,3,0,2,1,true)
    		.build();
    private Strategy twoPortWraith = new TwoPortWraith
    		.TwoPortWraithBuilder("TwoPortWraith",15,10,1,1,1,1,1,26,25,14,1,2,2,2,1,false)
    		.setExtraSCVs(1)
    		.setWorkerGas(3)
    		.setHarass(false)
    		.build();
    private Strategy proxyEightRax = new ProxyEightRax
    		.ProxyEightRaxBuilder("ProxyEightRax",0,0,0,1,0,0,0,400,7,100,0,0,0,0,0,false)
    		.setProxy(true)
    		.build();
    private Strategy vultureRush = new VultureRush
    		.VultureRushBuilder("VultureRush",15,10,1,1,3,1,1,24,5,14,1,2,1,2,1,false)
    		.setNumFacForPort(2)
    		.setWorkerGas(3)
    		.build();
    private Strategy theNiteKat = new TheNitekat
    		.TheNitekatBuilder("TheNitekat",30,30,0,1,2,3,1,26,5,25,2,2,0,3,1,false)
    		.setExtraSCVs(1)
    		.build();
    private Strategy joyORush = new JoyORush
    		.JoyORushBuilder("JoyORush",30,30,0,1,2,3,1,26,5,25,2,2,0,3,1,false)
    		.setExtraSCVs(1)
    		.build();
    private Strategy fastCC = new FastCC
    		.FastCCBuilder("14CC",15,10,1,2,1,2,2,38,30,0,3,3,0,2,1,true)
    		.build();
    
    private void AddSpecialUnitsIfParticularMap() {
        if (checkMapHash()) { // GoldRush
            this.getStrategy().trainUnits.add(UnitType.Terran_Wraith);
        }
    }

	public boolean checkMapHash() {
		return getGs().getGame().getBWMap().mapHash().equals("666dd28cd3c85223ebc749a481fc281e58221e4a");
	}

    private void initBaseStrategies() {
        Consumer<Strategy> addStrategy = (strategy) -> {
            strategies.put(strategy.name, new MutablePair<>(0, 0));
            nameOfStrategies.put(strategy.name, strategy);
        };
        switch (getGs().enemyRace) {
            case Zerg:
			addZergStrategy(addStrategy);
                break;

            case Terran:
			addTerranStrategy(addStrategy);
                break;

            case Protoss:
			addProtossStrategy(addStrategy);
                break;

            case Unknown:
			addUnknownStrategy(addStrategy);
                break;
        }
    }

	public void addUnknownStrategy(Consumer<Strategy> addStrat) {
		addStrat.accept(fullBio);
		addStrat.accept(fullMech);
		addStrat.accept(bioMech);
		addStrat.accept(bioGreedyFE);
		addStrat.accept(bioMechGreedyFE);
		addStrat.accept(proxyBBS);
		addStrat.accept(mechGreedyFE);
		addStrat.accept(joyORush);
		addStrat.accept(bioMechFE);
		addStrat.accept(fullBioFE);
	}

	public void addProtossStrategy(Consumer<Strategy> addStrat) {
		addStrat.accept(fullMech);
		addStrat.accept(joyORush);
		addStrat.accept(proxyEightRax);
		addStrat.accept(fastCC);
		addStrat.accept(mechGreedyFE);
		addStrat.accept(bioMech);
		addStrat.accept(bioMechGreedyFE);
		addStrat.accept(fullBio);
		addStrat.accept(bioGreedyFE);
		addStrat.accept(bioMechFE);
		addStrat.accept(fullBioFE);
		addStrat.accept(vultureRush);
		addStrat.accept(theNiteKat);
	}

	public void addTerranStrategy(Consumer<Strategy> addStrat) {
		addStrat.accept(fullMech);
		addStrat.accept(fastCC);
		addStrat.accept(proxyEightRax);
		addStrat.accept(bioMechGreedyFE);
		addStrat.accept(twoPortWraith);
		addStrat.accept(bioMech);
		addStrat.accept(mechGreedyFE);
		addStrat.accept(proxyBBS);
		addStrat.accept(fullBioFE);
		addStrat.accept(bioGreedyFE);
		addStrat.accept(fullBio);
		addStrat.accept(bioMechFE);
		addStrat.accept(vultureRush);
		addStrat.accept(theNiteKat);
	}

	public void addZergStrategy(Consumer<Strategy> addStrat) {
		addStrat.accept(bioGreedyFE);
		addStrat.accept(twoPortWraith);
		addStrat.accept(proxyEightRax);
		addStrat.accept(fullBioFE);
		addStrat.accept(fastCC);
		addStrat.accept(bioMechGreedyFE);
		addStrat.accept(proxyBBS);
		addStrat.accept(bioMech);
		addStrat.accept(fullMech);
		addStrat.accept(fullBio);
		addStrat.accept(bioMechFE);
		addStrat.accept(vultureRush);
		addStrat.accept(theNiteKat);
	}
	public boolean forTerranAndProtossTransition(List<String> validTransitions) {
		return validTransitions.addAll(Arrays.asList(fullMech.name, bioMechGreedyFE.name, bioGreedyFE.name));
	}
	public boolean forZergTransition(List<String> validTransitions) {
		return validTransitions.addAll(Arrays.asList(bioGreedyFE.name, bioMechGreedyFE.name, twoPortWraith.name));
	}
    void chooseTransitionForEnemy() {
        double C = 0.75;
        int totalGamesPlayed = getGs().learningManager.getEnemyInfo().wins + getGs().learningManager.getEnemyInfo().losses;
        List<String> validTransitions = new ArrayList<>();
        switch (getGs().enemyRace) {
            case Zerg:
                forZergTransition(validTransitions);
                break;
                
            case Terran:
            case Protoss:
                forTerranAndProtossTransition(validTransitions);
                break;
        }
        String bestUCBStrategy = null;
        double bestUCBStrategyVal = Double.MIN_VALUE;
        for (Map.Entry<String, MutablePair<Integer, Integer>> strat : strategies.entrySet()) {
            final boolean transitionHasNoKey = !validTransitions.contains(strat.getKey());
			if (transitionHasNoKey) continue;
            int sGamesPlayed = strat.getValue().first + strat.getValue().second;
            
            double sWinRate = getWinRate(sGamesPlayed, strat);
            double ucbVal = getUcbVal(C, totalGamesPlayed, sGamesPlayed);
            double val = sWinRate + ucbVal;
            if (val > bestUCBStrategyVal) {
                bestUCBStrategy = strat.getKey();
                bestUCBStrategyVal = val;
            }
        }
        Util.sendText("Transitioning from 14CC to " + bestUCBStrategy + " with UCB: " + bestUCBStrategyVal);
        setStrategy(nameOfStrategies.get(bestUCBStrategy));
        final boolean naturalChokeAllocated = getGs().naturalChoke != null;
		if (naturalChokeAllocated) {
			final Position naturalChokeToCenter = getGs().naturalChoke.getCenter().toPosition();
			getGs().defendPosition = naturalChokeToCenter;
		}
    }
	public double getUcbVal(double C, int totalGamesPlayed, int sGamesPlayed) {
		if(sGamesPlayed == 0) {
			return 0.85;
		}
		else {
			return C * Math.sqrt(Math.log(((double) totalGamesPlayed / (double) sGamesPlayed)));
		}
	}
    public double getWinRate(int sGamesPlayed,Map.Entry<String, MutablePair<Integer, Integer>> strat) {
  	  if (sGamesPlayed > 0) {
        	return (strat.getValue().first / (double) (sGamesPlayed));
        }
        else {
        	return 0;
        }
    }

    void chooseProxyTransition() {
        double C = 0.8;
        int totalGamesPlayed = getGs().learningManager.getEnemyInfo().wins + getGs().learningManager.getEnemyInfo().losses;
        List<String> validTransitions = new ArrayList<>(Arrays.asList(fullBio.name, bioMechFE.name, fullMech.name));
        if (getGs().enemyRace == Race.Zerg) validTransitions.add(twoPortWraith.name);
        String bestUCBStrategy = null;
        double bestUCBStrategyVal = Double.MIN_VALUE;
       
        for (Map.Entry<String, MutablePair<Integer, Integer>> strat : strategies.entrySet()) {
        	final boolean transitionHasNoKey = !validTransitions.contains(strat.getKey());
            if (transitionHasNoKey) continue;
            int sGamesPlayed = strat.getValue().first + strat.getValue().second;
            double sWinRate = getWinRate(sGamesPlayed, strat);
            double ucbVal = getUcbVal(C, totalGamesPlayed, sGamesPlayed);
            double val = sWinRate + ucbVal;
            if (val > bestUCBStrategyVal) {
                bestUCBStrategy = strat.getKey();
                bestUCBStrategyVal = val;
            }
        }
        Util.sendText("Transitioning from Proxy to " + bestUCBStrategy + " with UCB: " + bestUCBStrategyVal);
        setStrategy(nameOfStrategies.get(bestUCBStrategy));
        if (getGs().naturalChoke != null) getGs().defendPosition = getGs().naturalChoke.getCenter().toPosition();
    }

    void updateStrat() {
        final boolean canTrainFirebat = getStrategy().trainUnits.contains(UnitType.Terran_Firebat);
		final boolean enemyZerg = getGs().enemyRace == Race.Zerg;
		 final boolean canTrainGoliath = getStrategy().trainUnits.contains(UnitType.Terran_Goliath);
		if (canTrainFirebat && enemyZerg) getGs().maxBats = 3;
		else getGs().maxBats = 0;
       if (canTrainGoliath) getGs().maxGoliaths = 0;
    }

    private boolean alwaysZealotRushes() {
        final boolean enemyNotProtoss = getGs().enemyRace != Race.Protoss;
		if (enemyNotProtoss) return false;
        List<String> zealots = new ArrayList<>(Arrays.asList("purplewavelet", "wulibot", "flash", "carstennielsen"));
        final String enemyInfo = getGs().learningManager.getEnemyInfo().opponent.toLowerCase().replace(" ", "");
		return zealots.contains(enemyInfo);
    }

    private Strategy getRandomStrategy() {
        int index = new Random().nextInt(nameOfStrategies.entrySet().size());
        Iterator<Map.Entry<String, Strategy>> iterator = nameOfStrategies.entrySet().iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
       return iterator.next().getValue();
    }

    private Strategy initStrat() {
        try {
            EnemyInfo EI = getGs().learningManager.getEnemyInfo();
            String forcedStrat = ConfigManager.getConfig().ecgConfig.forceStrat;
            if (!forcedStrat.equals("")) {
                if (forcedStrat.toLowerCase().equals("random")) {
                    Strategy randomStrategy = this.getRandomStrategy();
                    Util.sendText("Picked random strategy " + randomStrategy.name);
                    return randomStrategy;
                } else if (nameOfStrategies.containsKey(forcedStrat)) {
                    Util.sendText("Picked forced strategy " + forcedStrat);
                    if (forcedStrat.equals("14CC")) {
                        for (EnemyInfo.StrategyOpponentHistory r : EI.history) {
                            if (strategies.containsKey(r.strategyName)) {
                                strategies.get(r.strategyName).first += r.wins;
                                strategies.get(r.strategyName).second += r.losses;
                            }
                        }
                    }
                    return nameOfStrategies.get(forcedStrat);
                }
            }
            final boolean isHumanMode = ConfigManager.getConfig().ecgConfig.humanMode;
			final boolean isRandomLtFive = Math.round(Math.random() * 10) < 5;
			if (isHumanMode && isRandomLtFive) {
                return this.getRandomStrategy();
            }
            if (getGs().enemyRace == Race.Zerg && EI.naughty) return fullBio;
            final boolean checkTempMapHash = getGs().bw.getBWMap().mapHash().equals("6f5295624a7e3887470f3f2e14727b1411321a67");
			if (checkTempMapHash) {
                getGs().maxWraiths = 200;
                return new PlasmaWraithHell
                		.PlasmaWraithHellBuilder("PlasmaWraithHell",20,10,1,2,0,2,1,28,15,0,1,2,2,1,1,false)
                		.setNumFacForPort(1)
                		.build();
            }
            if (alwaysZealotRushes()) {
                IntelligenceAgency.setEnemyStrat(IntelligenceAgency.EnemyStrats.ZealotRush);
                fullBioFE.armyForExpand += 5;
                fullBioFE.workerGas = 2;
                return fullBioFE;
            }
            removeStrategiesMapSpecific();
            int totalGamesPlayed = EI.wins + EI.losses;
            for (EnemyInfo.StrategyOpponentHistory r : EI.history) {
                if (strategies.containsKey(r.strategyName)) {
                    strategies.get(r.strategyName).first += r.wins;
                    strategies.get(r.strategyName).second += r.losses;
                }
            }
            double maxWinRate = 0.0;
            String bestStrategy = null;
            int totalGamesBestS = 0;
            for (Map.Entry<String, MutablePair<Integer, Integer>> s : strategies.entrySet()) {
                int totalGames = s.getValue().first + s.getValue().second;
                final boolean gameLtTwo = totalGames < 2;
				if (gameLtTwo) continue;
                double winRate = (double) s.getValue().first / totalGames;
                if (winRate >= 0.75 && winRate > maxWinRate) {
                    maxWinRate = winRate;
                    bestStrategy = s.getKey();
                    totalGamesBestS = totalGames;
                }
            }
            final boolean maxWinRateNotZero = maxWinRate != 0.0;
			final boolean bestStrategyNotNull = bestStrategy != null;
			if (maxWinRateNotZero && bestStrategyNotNull) {
                Util.sendText("Using best Strategy: " + bestStrategy + " with winrate " + maxWinRate * 100 + "% and " + totalGamesBestS + " games played");
                return nameOfStrategies.get(bestStrategy);
            }
            double C = 0.7;
            String bestUCBStrategy = null;
            double bestUCBStrategyVal = Double.MIN_VALUE;
            for (Map.Entry<String, MutablePair<Integer, Integer>> strat : strategies.entrySet()) {
                int sGamesPlayed = strat.getValue().first + strat.getValue().second;
                double sWinRate = getWinRate(sGamesPlayed, strat);
                double ucbVal = getUcbVal(C, totalGamesPlayed, sGamesPlayed);
                final boolean moreThanOneGamePlayed = totalGamesPlayed > 0;
				final boolean isMapSizeTwo = getGs().mapSize == 2;
				final boolean isStrategyKey14CC = strat.getKey().equals("14CC");
				final boolean isMapSizeFour = getGs().mapSize == 4;
				final boolean isStrategyKeyProxy = nameOfStrategies.get(strat.getKey()).proxy;
				if (moreThanOneGamePlayed && isStrategyKeyProxy && isMapSizeTwo) 
					ucbVal += 0.03;
				if (moreThanOneGamePlayed && isStrategyKey14CC && isMapSizeFour) 
					ucbVal += 0.03;
                double val = sWinRate + ucbVal;
                if (val > bestUCBStrategyVal) {
                    bestUCBStrategy = strat.getKey();
                    bestUCBStrategyVal = val;
                }
            }
            final boolean gameNotPlayed = totalGamesPlayed < 1;
			if (gameNotPlayed)
                Util.sendText("I dont know you that well yet, lets pick " + bestUCBStrategy);
            else Util.sendText("Chose: " + bestUCBStrategy + " with UCB: " + bestUCBStrategyVal);
            return nameOfStrategies.get(bestUCBStrategy);
        } catch (Exception e) {
            System.err.println("Error initStrat, using default strategy");
            e.printStackTrace();
            return fullBio;
        }
    }

    private void removeStrategiesMapSpecific() {
        Consumer<Strategy> removeStrategy = (strategy) -> {
            strategies.remove(strategy.name);
            nameOfStrategies.remove(strategy.name);
        };
        if (checkMapHash()) { // GoldRush
            removeStrategy.accept(proxyEightRax);
            removeStrategy.accept(proxyBBS);
            removeStrategy.accept(theNiteKat);
            removeStrategy.accept(joyORush);
        }
    }
    public Strategy getStrategy() {
    	return strategy;
    }
	public Strategy getSubStrategy(String strategy_name) {
		Strategy strategy = null;
		switch(strategy_name) {
		case "FullBio":
			strategy = fullBio;
			break;
		case "ProxyBBS":
			strategy = proxyBBS;
			break;
		case "BioMech":
			strategy = bioMech;
			break;
		case "FullBioFE":
			strategy = fullBioFE;
			break;
		case "BioMechFE":
			strategy = bioMechFE;
			break;
		case "FullMech":
			strategy = fullMech;
			break;
		case "BioGreedyFE":
			strategy = bioGreedyFE;
			break;
		case "MechGreedyFE":
			strategy = mechGreedyFE;
			break;
		case "TwoPortWraith":
			strategy = twoPortWraith;
			break;
		case "ProxyEightRax":
			strategy = proxyEightRax;
			break;
		case "VultureRush":
			strategy = vultureRush;
			break;
		case "TheNitekat":
			strategy = theNiteKat;
			break;
		case "JoyORush":
			strategy = joyORush;
			break;
		case "14CC":
			strategy = fastCC;
			break;
		}
		return strategy;
	}
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
}
