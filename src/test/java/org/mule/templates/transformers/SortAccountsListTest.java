package org.mule.templates.transformers;

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
import org.mule.templates.transformers.SortAccountsList;

/**
 * The test validates that the {@link SortAccountsList} properly order a list of
 * maps based on it internal criteria.
 * 
 * @author damiansima
 */
@RunWith(MockitoJUnitRunner.class)
public class SortAccountsListTest {

	@Mock
	private MuleContext muleContext;

	@Test
	@SuppressWarnings("unchecked")
	public void testSort() throws TransformerException {
		List<Map<String, String>> originalList = createOriginalList();
		MuleMessage message = new DefaultMuleMessage(originalList, muleContext);

		SortAccountsList transformer = new SortAccountsList();
		List<Map<String, String>> sortedList = (List<Map<String, String>>) transformer
				.transform(message, "UTF-8");

		// System.out.println(originalList);
		// System.out.println(sortedList);
		Assert.assertEquals("The merged list obtained is not as expected",
				createExpectedList(), sortedList);

	}

	private List<Map<String, String>> createExpectedList() {
		Map<String, String> account0 = createEmptyMergedRecord(0);
		account0.put("IDInA", "0");

		Map<String, String> account1 = createEmptyMergedRecord(1);
		account1.put("IDInA", "1");
		account1.put("IDInB", "1");

		Map<String, String> account2 = createEmptyMergedRecord(2);
		account2.put("IDInB", "2");

		Map<String, String> account3 = createEmptyMergedRecord(3);
		account3.put("IDInA", "3");
		account3.put("IDInB", "3");

		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		userList.add(account0);
		userList.add(account2);
		userList.add(account1);
		userList.add(account3);

		return userList;
	}

	private List<Map<String, String>> createOriginalList() {
		Map<String, String> account0 = createEmptyMergedRecord(0);
		account0.put("IDInA", "0");

		Map<String, String> account1 = createEmptyMergedRecord(1);
		account1.put("IDInA", "1");
		account1.put("IDInB", "1");

		Map<String, String> account2 = createEmptyMergedRecord(2);
		account2.put("IDInB", "2");

		Map<String, String> account3 = createEmptyMergedRecord(3);
		account3.put("IDInA", "3");
		account3.put("IDInB", "3");

		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		userList.add(account0);
		userList.add(account1);
		userList.add(account2);
		userList.add(account3);

		return userList;
	}

	private Map<String, String> createEmptyMergedRecord(Integer secuense) {
		Map<String, String> account = new HashMap<String, String>();
		account.put("Name", "SomeName_" + secuense);
		account.put("IDInA", "");
		account.put("IndustryInA", "");
		account.put("NumberOfEmployeesInA", "");
		account.put("IDInB", "");
		account.put("IndustryInB", "");
		account.put("NumberOfEmployeesInB", "");
		return account;
	}

}
