package ecgberht;

import org.junit.jupiter.api.Test;
import org.openbw.bwapi4j.type.Race;

import static org.junit.jupiter.api.Assertions.*;

class EnemyInfoTest {
    /**
    *purpose : test EnmyInfo()
    *if there are new EnemyInfo, its not same with null
     */
    @Test
    void EnemyInfoTest(){
        EnemyInfo enemyInfo = new EnemyInfo("Zerg", Race.Zerg);
        assertNotNull(enemyInfo);

    }
    /**
     * purpose : test StrategyOpponentHistory class's method StrategyOpponetHistory
     *     there are 1 if-else case, so 2 branch
     *     branch 1-1) win is true
     *     return wins is 1 if it's win
     */
    @Test
    void UpdateStrategy_newOpponentHistory_Win(){
        EnemyInfo enemyInfo = new EnemyInfo("Zerg", Race.Zerg);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,true );
        assertEquals(enemyInfo.history.get(0).wins,1 );
    }
    /**
    * purpose : test StrategyOpponentHistory class's method StrategyOpponetHistory
    *       there are 1 if-else case, so 2 branch
    *       branch 1-2)win is false
    *       return losses is 1 if it's loss
     */
    @Test
    void UpdateStrategy_newOpponentHistory_Fail(){
        EnemyInfo enemyInfo = new EnemyInfo("Zerg", Race.Zerg);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,false );
        assertEquals(enemyInfo.history.get(0).losses,1 );
    }

    /**
     * purpose : test updateStrategyOpponentHistory's if-else
     *    there are 2 if-else, and 3 value, so 5 branch
     *    branch 2-1) strategyName : same / mapSize: same / win: true
     *    return wines is 2 if it's same strageyOpponentHistory, win.
     */
    @Test
    void UpdateStrategy_duplicationOpponentHistory_win(){
        EnemyInfo enemyInfo = new EnemyInfo("Zerg", Race.Zerg);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,true);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,true);
        assertEquals(enemyInfo.history.get(0).wins,2 );
    }

    /**
     * purpose : test updateStrategyOpponentHistory's if-else
     *    there are 2 if-else, and 3 value, so 5 branch
     *    branch 2-2) strategyName : same / mapSize: same / win: false
     *    return losses is 2 if it's same strageyOpponentHistory, false.
     */
    @Test
    void UpdateStrategy_duplicationOpponentHistory_false(){
        EnemyInfo enemyInfo = new EnemyInfo("Zerg", Race.Zerg);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,false);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,false);
        assertEquals(enemyInfo.history.get(0).losses,2 );
    }

    /**
     * purpose : test updateStrategyOpponentHistory's if-else
     *    there are 2 if-else, and 3 value, so 5 branch
     *    branch 2-3) strategyName : same / mapSize: different
     *    return history size ==2 if there are two StrategyOpponent
     */
    @Test
    void UpdateStrategy_MapSizeDifferntOpponentHistory(){
        EnemyInfo enemyInfo = new EnemyInfo("Zerg", Race.Zerg);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,true);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",1000,true);
        assertEquals(enemyInfo.history.size(),2 );
    }

    /**
     * purpose : test updateStrategyOpponentHistory's if-else
     *    there are 2 if-else, and 3 value, so 6 branch
     *    branch 2-4) strategyName : different / mapSize: same
     *    return history size ==2 if there are two StrategyOpponent
     */
    @Test
    void UpdateStrategy_strategyNameDifferntOpponentHistory(){
        EnemyInfo enemyInfo = new EnemyInfo("Zerg", Race.Zerg);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,true);
        enemyInfo.updateStrategyOpponentHistory("BioMech",100,true);
        assertEquals(enemyInfo.history.size(),2 );
    }

    /**
     * purpose : test updateStrategyOpponentHistory's if-else
     *    there are 2 if-else, and 3 value, so 5 branch
     *    branch 2-5) strategyName : different / mapSize: different
     *    return history size ==2 if there are two StrategyOpponent
     */
    @Test
    void UpdateStrategy_strategyNameMapSizeDifferntOpponentHistory(){
        EnemyInfo enemyInfo = new EnemyInfo("Zerg", Race.Zerg);
        enemyInfo.updateStrategyOpponentHistory("BioGreedyFE",100,true);
        enemyInfo.updateStrategyOpponentHistory("BioMech",1000,true);
        assertEquals(enemyInfo.history.size(),2 );
    }




}