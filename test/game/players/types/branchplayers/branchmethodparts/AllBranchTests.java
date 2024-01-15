package test.game.players.types.branchplayers.branchmethodparts;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({BranchTest.class, BranchBackwardTest.class, BranchDownTest.class, 
                BranchForwardTest.class, BranchRightTest.class, BranchEmptyTest.class})
public class AllBranchTests {
  
}