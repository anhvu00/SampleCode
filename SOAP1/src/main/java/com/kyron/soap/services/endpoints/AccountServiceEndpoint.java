package com.kyron.soap.services.endpoints;


import generated_sources.myweb.Account;
import generated_sources.myweb.accountservice.AccountDetailsRequest;
import generated_sources.myweb.accountservice.AccountDetailsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.kyron.soap.services.AccountService;

/**
 * The Class AccountService.
 */
@Endpoint
public class AccountServiceEndpoint
{
	private static final String TARGET_NAMESPACE = "generated_sources/myweb/accountservice";

	@Autowired
	private AccountService accountService_i;

	/**
	 * Gets the account details.
	 *
	 * @param accountNumber the account number
	 * @return the account details
	 */
	@PayloadRoot(localPart = "AccountDetailsRequest", namespace = TARGET_NAMESPACE)
	public @ResponsePayload AccountDetailsResponse getAccountDetails(@RequestPayload AccountDetailsRequest request)
	{
		AccountDetailsResponse response = new AccountDetailsResponse();

		/* call Spring injected service implementation to retrieve account data */
		Account account = accountService_i.getAccountDetails(request.getAccountNumber());
		response.setAccountDetails(account);
		return response;
	}

	public void setAccountService(AccountService accountService_p)
	{
		this.accountService_i = accountService_p;
	}
}