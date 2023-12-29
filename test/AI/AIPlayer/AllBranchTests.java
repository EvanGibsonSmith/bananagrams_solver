package test.AI.AIPlayer;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({Branch.class, BranchBackward.class, BranchDown.class, 
               BranchForward.class, BranchRight.class})
public class AllBranchTests
{
}