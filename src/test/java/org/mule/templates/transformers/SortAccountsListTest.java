/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.config.MuleConfiguration;
import org.mule.api.transformer.TransformerException;

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
	
	@Mock
	private MuleConfiguration muleConfiguration;

	@Test
	@SuppressWarnings("unchecked")
	public void testSort() throws TransformerException {
		
		Mockito.when(muleContext.getConfiguration()).thenReturn(muleConfiguration);
		Mockito.when(muleConfiguration.getDefaultEncoding()).thenReturn("UTF-8");
		
		List<Map<String, String>> originalList = createOriginalList();
		MuleMessage message = new DefaultMuleMessage(originalList.iterator(), muleContext);

		SortAccountsList transformer = new SortAccountsList();
		List<Map<String, String>> sortedList = (List<Map<String, String>>) transformer
				.transform(message, "UTF-8");

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

		List<Map<String, String>> accountList = new ArrayList<Map<String, String>>();
		accountList.add(account0);
		accountList.add(account2);
		accountList.add(account1);
		accountList.add(account3);

		return accountList;
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

		List<Map<String, String>> accountList = new ArrayList<Map<String, String>>();
		accountList.add(account0);
		accountList.add(account1);
		accountList.add(account2);
		accountList.add(account3);

		return accountList;
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
