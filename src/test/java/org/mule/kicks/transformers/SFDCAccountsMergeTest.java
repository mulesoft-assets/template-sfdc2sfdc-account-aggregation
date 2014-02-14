package org.mule.kicks.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

@RunWith(MockitoJUnitRunner.class)
public class SFDCAccountsMergeTest {
	private static final String ACCOUNTS_COMPANY_A = "accountsFromOrgA";
	private static final String ACCOUNTS_COMPANY_B = "accountsFromOrgB";

	@Mock
	private MuleContext muleContext;

	@Test
	@SuppressWarnings("unchecked")
	public void testMerge() throws TransformerException {
		List<Map<String, Object>> accountsA = createAccountLists("A", 0, 1);
		List<Map<String, Object>> accountsB = createAccountLists("B", 1, 2);

		MuleMessage message = new DefaultMuleMessage(null, muleContext);
		message.setInvocationProperty(ACCOUNTS_COMPANY_A, accountsA.iterator());
		message.setInvocationProperty(ACCOUNTS_COMPANY_B, accountsB.iterator());

		SFDCAccountsMerge transformer = new SFDCAccountsMerge();
		List<Map<String, Object>> mergedList = (List<Map<String, Object>>) transformer.transform(message, "UTF-8");

		System.out.println(accountsA);
		System.out.println(accountsB);
		System.out.println(mergedList);

		Assert.assertEquals("The merged list obtained is not as expected", createExpectedList(), mergedList);
	}

	private List<Map<String, Object>> createExpectedList() {

		Map<String, Object> account0 = createEmptyMergedRecord(0);
		account0.put("IDInA", "0");
		account0.put("IndustryInA", "Goverment");
		account0.put("NumberOfEmployeesInA", "550.0");

		Map<String, Object> account1 = createEmptyMergedRecord(1);
		account1.put("IDInA", "1");
		account1.put("IndustryInA", "Goverment");
		account1.put("NumberOfEmployeesInA", "550.0");
		account1.put("IDInB", "1");
		account1.put("IndustryInB", "Goverment");
		account1.put("NumberOfEmployeesInB", "550.0");

		Map<String, Object> account2 = createEmptyMergedRecord(2);
		account2.put("IDInB", "2");
		account2.put("IndustryInB", "Goverment");
		account2.put("NumberOfEmployeesInB", "550.0");

		List<Map<String, Object>> userList = new ArrayList<Map<String, Object>>();
		userList.add(account0);
		userList.add(account1);
		userList.add(account2);

		return userList;
	}

	private Map<String, Object> createEmptyMergedRecord(int sequence) {
		Map<String, Object> contactsMap = new HashMap<String, Object>();
		
		Map<String, Object> account = new HashMap<String, Object>();
		account.put("Name", "SomeName_" + sequence);
		account.put("IDInA", "");
		account.put("IndustryInA", "");
		account.put("NumberOfEmployeesInA", "");
		account.put("IDInB", "");
		account.put("IndustryInB", "");
		account.put("NumberOfEmployeesInB", "");
		account.put("Contacts", contactsMap);
		account.put("ContactsCount", 0);
		return account;
	}

	private List<Map<String, Object>> createAccountLists(String orgId, int start, int end) {
		List<Map<String, Object>> accountList = new ArrayList<Map<String, Object>>();
		for (int i = start; i <= end; i++) {
			accountList.add(createAccount(orgId, i));
		}
		return accountList;
	}

	private Map<String, Object> createAccount(String orgId, int sequence) {
		Map<String, Object> account = new HashMap<String, Object>();

		account.put("Id", new Integer(sequence).toString());
		account.put("Name", "SomeName_" + sequence);
		account.put("Industry", "Goverment");
		account.put("NumberOfEmployees", "550.0");
		account.put("Contacts", new HashMap<String, Object>());

		return account;
	}
}
