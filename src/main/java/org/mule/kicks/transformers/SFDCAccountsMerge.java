package org.mule.kicks.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.google.common.collect.Lists;

/**
 * This transformer will take two lists as input and create a third one that will be the merge of the previous two. The identity of list's element is defined by
 * its Name.
 * 
 * @author damian.sima
 */
public class SFDCAccountsMerge extends AbstractMessageTransformer {

	private static final String ACCOUNTS_COMPANY_A = "accountsFromOrgA";
	private static final String ACCOUNTS_COMPANY_B = "accountsFromOrgB";

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		List<Map<String, Object>> mergedUsersList = mergeAccounts(getAccountsList(message, ACCOUNTS_COMPANY_A), getAccountsList(message, ACCOUNTS_COMPANY_B));

		return mergedUsersList;
	}

	private List<Map<String, Object>> getAccountsList(MuleMessage message, String propertyName) {
		Iterator<Map<String, Object>> iterator = message.getInvocationProperty(propertyName);
		return Lists.newArrayList(iterator);
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
	private List<Map<String, Object>> mergeAccounts(List<Map<String, Object>> accountsFromOrgA, List<Map<String, Object>> accountsFromOrgB) {
		List<Map<String, Object>> mergedAccountList = new ArrayList<Map<String, Object>>();

		// Put all accounts from A in the merged list
		for (Map<String, Object> accountFromA : accountsFromOrgA) {
			Map<String, Object> mergedAccount = createMergedAccount(accountFromA);
			mergedAccount.put("IDInA", accountFromA.get("Id"));
			mergedAccount.put("IndustryInA", accountFromA.get("Industry"));
			mergedAccount.put("NumberOfEmployeesInA", accountFromA.get("NumberOfEmployees"));
			mergedAccount.put("Contacts", accountFromA.get("Contacts"));
			mergedAccount.put("ContactsCount", getContactRecords(accountFromA).size());

			mergedAccountList.add(mergedAccount);
		}

		// Add the new accounts from B and update the exiting ones
		for (Map<String, Object> accountFromB : accountsFromOrgB) {
			Map<String, Object> mergedAccount = findAccountInList(accountFromB.get("Name"), mergedAccountList);
			if (mergedAccount == null) {
				mergedAccount = createMergedAccount(accountFromB);
				mergedAccountList.add(mergedAccount);
			}

			mergedAccount.put("IDInB", accountFromB.get("Id"));
			mergedAccount.put("IndustryInB", accountFromB.get("Industry"));
			mergedAccount.put("NumberOfEmployeesInB", accountFromB.get("NumberOfEmployees"));
			mergedAccount.put("ContactsCount", mergeContacts(getContactRecords(mergedAccount), getContactRecords(accountFromB)).size());
		}
		return mergedAccountList;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getContactRecords(Map<String, Object> account) {
		Map<String, Object> contactsMap = account.get("Contacts") == null ? new HashMap<String, Object>() : (Map<String, Object>) account.get("Contacts");
		List<Map<String, Object>> contactRecords = (List<Map<String, Object>>) contactsMap.get("records");
		return contactRecords == null ? new ArrayList<Map<String, Object>>() : contactRecords;
	}

	private List<Map<String, Object>> mergeContacts(List<Map<String, Object>> contactsA, List<Map<String, Object>> contactsB) {
		List<Map<String, Object>> mergedContacts = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> contactA : contactsA) {
			Map<String, Object> mergedContact = createMergedContact(contactA);

			mergedContact.put("FirstNameInA", contactA.get("FirstName"));
			mergedContact.put("LastNameInA", contactA.get("LastName"));
		}

		for (Map<String, Object> contactB : contactsB) {
			Map<String, Object> mergedContact = findContactInList(contactB.get("Email"), mergedContacts);
			if (mergedContact == null) {
				mergedContact = createMergedContact(contactB);
				mergedContacts.add(mergedContact);
			}

			mergedContact.put("FirstNameInB", contactB.get("FirstName"));
			mergedContact.put("LastNameInB", contactB.get("LastName"));
		}

		return mergedContacts;
	}

	private Map<String, Object> createMergedContact(Map<String, Object> contact) {
		Map<String, Object> mergedContact = new HashMap<String, Object>();
		mergedContact.put("Email", contact.get("Email"));
		mergedContact.put("FirstNameInA", "");
		mergedContact.put("LastNameInA", "");
		mergedContact.put("FirstNameInB", "");
		mergedContact.put("LastNameInB", "");
		return mergedContact;
	}

	private Map<String, Object> createMergedAccount(Map<String, Object> account) {
		Map<String, Object> mergedAccount = new HashMap<String, Object>();
		mergedAccount.put("Name", account.get("Name"));
		mergedAccount.put("IDInA", "");
		mergedAccount.put("IndustryInA", "");
		mergedAccount.put("NumberOfEmployeesInA", "");
		mergedAccount.put("IDInB", "");
		mergedAccount.put("IndustryInB", "");
		mergedAccount.put("NumberOfEmployeesInB", "");
		mergedAccount.put("Contacts", new HashMap<String, Object>());
		return mergedAccount;
	}

	private Map<String, Object> findAccountInList(Object accountName, List<Map<String, Object>> orgList) {
		for (Map<String, Object> account : orgList) {
			if (account.get("Name")
						.equals(accountName)) {
				return account;
			}
		}
		return null;
	}

	private Map<String, Object> findContactInList(Object contactEmail, List<Map<String, Object>> contactList) {
		for (Map<String, Object> contact : contactList) {
			if (contact.get("Email")
						.equals(contactEmail)) {
				return contact;
			}
		}
		return null;
	}

}
