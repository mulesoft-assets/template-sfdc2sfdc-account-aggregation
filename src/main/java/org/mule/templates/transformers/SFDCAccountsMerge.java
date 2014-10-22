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

/**
 * This transformer will take two lists as input and create a third one that
 * will be the merge of the previous two. The identity of list's element is
 * defined by its Name.
 * 
 * @author damian.sima
 */
public class SFDCAccountsMerge {

	/**
	 * The method will merge the accounts from the two lists creating a new one.
	 * 
	 * @param accountsFromOrgA
	 *            accounts from organization A
	 * @param accountsFromOrgB
	 *            accounts from organization B
	 * @return a list with the merged content of the to input lists
	 */
	List<Map<String, String>> mergeList(List<Map<String, String>> accountsFromOrgA, List<Map<String, String>> accountsFromOrgB) {
		List<Map<String, String>> mergedAccountList = new ArrayList<Map<String, String>>();

		// Put all accounts from A in the merged List
		for (Map<String, String> accountFromA : accountsFromOrgA) {
			Map<String, String> mergedAccount = createMergedAccount(accountFromA);
			mergedAccount.put("IDInA", accountFromA.get("Id"));
			mergedAccount.put("IndustryInA", accountFromA.get("Industry"));
			mergedAccount.put("NumberOfEmployeesInA", accountFromA.get("NumberOfEmployees"));
			mergedAccountList.add(mergedAccount);
		}

		// Add the new accounts from B and update the exiting ones
		for (Map<String, String> accountFromB : accountsFromOrgB) {
			Map<String, String> accountFromA = findAccountInList(accountFromB.get("Name"), mergedAccountList);
			if (accountFromA != null) {
				accountFromA.put("IDInB", accountFromB.get("Id"));
				accountFromA.put("IndustryInB", accountFromB.get("Industry"));
				accountFromA.put("NumberOfEmployeesInB", accountFromB.get("NumberOfEmployees"));
			} else {
				Map<String, String> mergedAccount = createMergedAccount(accountFromB);
				mergedAccount.put("IDInB", accountFromB.get("Id"));
				mergedAccount.put("IndustryInB", accountFromB.get("Industry"));
				mergedAccount.put("NumberOfEmployeesInB", accountFromB.get("NumberOfEmployees"));
				mergedAccountList.add(mergedAccount);
			}

		}
		return mergedAccountList;
	}

	private Map<String, String> createMergedAccount(Map<String, String> account) {
		Map<String, String> mergedAccount = new HashMap<String, String>();
		mergedAccount.put("Name", account.get("Name"));
		mergedAccount.put("IDInA", "");
		mergedAccount.put("IndustryInA", "");
		mergedAccount.put("NumberOfEmployeesInA", "");
		mergedAccount.put("IDInB", "");
		mergedAccount.put("IndustryInB", "");
		mergedAccount.put("NumberOfEmployeesInB", "");
		return mergedAccount;
	}

	private Map<String, String> findAccountInList(String accountName, List<Map<String, String>> orgList) {
		for (Map<String, String> account : orgList) {
			if (account.get("Name").equals(accountName)) {
				return account;
			}
		}
		return null;
	}

}
