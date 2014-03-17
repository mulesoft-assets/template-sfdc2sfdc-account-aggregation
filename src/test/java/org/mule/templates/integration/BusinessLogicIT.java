package org.mule.templates.integration;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.processor.chain.SubflowInterceptingChainLifecycleWrapper;
import org.mule.streaming.ConsumerIterator;
import org.mule.tck.junit4.rule.DynamicPort;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Mule Template that make calls to external systems.
 * 
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {
	private static final String ACCOUNTS_COMPANY_A = "accountsFromOrgA";
	private static final String ACCOUNTS_COMPANY_B = "accountsFromOrgB";

	@Rule
	public DynamicPort port = new DynamicPort ("http.port");
	
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
