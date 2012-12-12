
package com.njnu.kai.aidl.service;

import com.njnu.kai.aidl.service.Person;

interface IStockQuoteService {
	String getQuote(in String ticker, in Person requester);
}

