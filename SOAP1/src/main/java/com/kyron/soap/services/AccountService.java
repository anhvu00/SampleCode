package com.kyron.soap.services;

import generated_sources.myweb.Account;


/**
 * The Interface AccountService.
 */
public interface AccountService
{

	/**
	 * Gets the account details.
	 *
	 * @param accountNumber the account number
	 * @return the account details
	 */
	public Account getAccountDetails(String accountNumber);
}
