package org.mule.kicks.integration;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.streaming.ConsumerIterator;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Mule Kick that make calls to external systems.
 * 
 */
public class BusinessLogicIT extends AbstractKickTestCase {
	private static final String ACCOUNTS_COMPANY_A = "accountsFromOrgA";
	private static final String ACCOUNTS_COMPANY_B = "accountsFromOrgB";

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGatherDataFlow() throws Exception {
		SubflowInterceptingChainLifecycleWrapper flow = getSubFlow("gatherDataFlow");
		flow.initialise();

		MuleEvent event = flow.process(getTestEvent("", MessageExchangePattern.REQUEST_RESPONSE));
		Set<String> flowVariables = event.getFlowVariableNames();

		Assert.assertTrue("The variable accountsFromOrgA is missing.", flowVariables.contains(ACCOUNTS_COMPANY_A));
		Assert.assertTrue("The variable accountsFromOrgB is missing.", flowVariables.contains(ACCOUNTS_COMPANY_B));

		ConsumerIterator<Map<String, String>> accountsFromOrgA = event.getFlowVariable(ACCOUNTS_COMPANY_A);
		ConsumerIterator<Map<String, String>> accountsFromOrgB = event.getFlowVariable(ACCOUNTS_COMPANY_B);

		Assert.assertTrue("There should be users in the variable usersFromOrgA.", accountsFromOrgA.size() != 0);
		Assert.assertTrue("There should be users in the variable usersFromOrgB.", accountsFromOrgB.size() != 0);

	}

}
