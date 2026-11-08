/**
 * Central API module. Every call to the backend goes through here, the components never call fetch themselves.
 * The base URL is passed in, so it is not hard coded and the tests can use a fake fetch.
 * @param {string} baseUrl the URL of the backend, for example "http://localhost:8080"
 * @param {Function} fetchFn the fetch function, the browser's fetch by default
 */
export function createApi(baseUrl, fetchFn = fetch) {
  /**
   * Sends a request and returns the JSON answer
   * @throws {Error} with the error message of the backend if the status is not 2xx
   */
  async function request(method, path, body) {
    const options = { method, headers: {} };

    if (body !== undefined) {
      options.headers['Content-Type'] = 'application/json';
      options.body = JSON.stringify(body);
    }

    const response = await fetchFn(baseUrl + path, options);
    const data = await response.json().catch(() => null);

    if (!response.ok) {
      throw new Error(data?.error ?? `Anfrage fehlgeschlagen (Status ${response.status})`);
    }

    return data;
  }

  const account = (id) => `/accounts/${encodeURIComponent(id)}`;

  return {
    createSavingsAccount: () => request('POST', '/accounts/savings'),
    createPromoYouthSavingsAccount: () => request('POST', '/accounts/promo-youth'),
    createSalaryAccount: (creditLimit) => request('POST', '/accounts/salary', { creditLimit }),
    getAccount: (id) => request('GET', account(id)),
    getBookings: (id) => request('GET', `${account(id)}/bookings`),
    deposit: (id, amount, text) => request('POST', `${account(id)}/deposit`, { amount, text }),
    withdraw: (id, amount, text) => request('POST', `${account(id)}/withdraw`, { amount, text }),
    getBankBalance: () => request('GET', '/bank/balance'),
    getTop5Highest: () => request('GET', '/bank/top5/highest'),
    getTop5Lowest: () => request('GET', '/bank/top5/lowest'),
  };
}
