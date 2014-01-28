package org.mule.kicks.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * This transformer will take two lists as input and create a third one that
 * will be the merge of the previous two. The identity of list's element is
 * defined by its Name.
 * 
 * @author damian.sima
 */
public class SFDCAccountsMerge extends AbstractMessageTransformer {

	private static final String ACCOUNTS_COMPANY_A = "accountsFromOrgA";
	private static final String ACCOUNTS_COMPANY_B = "accountsFromOrgB";

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		List<Map<String, String>> mergedUsersList = mergeList(getAccountsList(message, ACCOUNTS_COMPANY_A), getAccountsList(message, ACCOUNTS_COMPANY_B));

		return mergedUsersList;
	}

	private List<Map<String, String>> getAccountsList(MuleMessage message, String propertyName) {
		Iterator<Map<String, String>> iterator = message.getInvocationProperty(propertyName);
		return consumeIterator(iterator);
	}

	private List<Map<String, String>> consumeIterator(Iterator<Map<String, String>> iterator) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	/**
	 * The method will merge the accounts from the two lists creating a new one.
	 * 
	 * @param accountsFromOrgA
	 *            accounts from organization A
	 * @param accountsFromOrgB
	 *            accounts from organization B
	 * @return a list with the merged content of the to input lists
	 */
	private List<Map<String, String>> mergeList(List<Map<String, String>> accountsFromOrgA, List<Map<String, String>> accountsFromOrgB) {
		List<Map<String, String>> mergedUsersList = new ArrayList<Map<String, String>>();

		// Put all accounts from A in the merged contactList
		for (Map<String, String> accountFromA : accountsFromOrgA) {
			Map<String, String> mergedAccount = createMergedAccount(accountFromA);
			mergedAccount.put("IDInA", accountFromA.get("Id"));
			mergedAccount.put("IndustryInA", accountFromA.get("Industry"));
			mergedAccount.put("NumberOfEmployeesInA", accountFromA.get("NumberOfEmployees"));
			mergedUsersList.add(mergedAccount);
		}

		// Add the new accounts from B and update the exiting ones
		for (Map<String, String> accountFromB : accountsFromOrgB) {
			Map<String, String> accountFromA = findAccountInList(accountFromB.get("Name"), mergedUsersList);
			if (accountFromA != null) {
				accountFromA.put("IDInB", accountFromB.get("Id"));
				accountFromA.put("IndustryInB", accountFromB.get("Industry"));
				accountFromA.put("NumberOfEmployeesInB", accountFromB.get("NumberOfEmployees"));
			} else {
				Map<String, String> mergedAccount = createMergedAccount(accountFromB);
				mergedAccount.put("IDInB", accountFromB.get("Id"));
				mergedAccount.put("IndustryInB", accountFromB.get("Industry"));
				mergedAccount.put("NumberOfEmployeesInB", accountFromB.get("NumberOfEmployees"));
				mergedUsersList.add(mergedAccount);
			}

		}
		return mergedUsersList;
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
