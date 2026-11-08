import { test } from 'node:test';
import assert from 'node:assert/strict';
import { createApi } from './api.js';

/**
 * Fake fetch that remembers the calls and answers with the given status and body
 */
function fakeFetch(status, body) {
  const calls = [];
  const fetchFn = async (url, options) => {
    calls.push({ url, options });
    return {
      ok: status >= 200 && status < 300,
      status,
      json: async () => body,
    };
  };
  return { fetchFn, calls };
}

test('creating a savings account calls the right endpoint', async () => {
  const { fetchFn, calls } = fakeFetch(201, { id: 'S-1000' });
  const api = createApi('http://backend', fetchFn);

  const result = await api.createSavingsAccount();

  assert.deepEqual(result, { id: 'S-1000' });
  assert.equal(calls[0].url, 'http://backend/accounts/savings');
  assert.equal(calls[0].options.method, 'POST');
});

test('creating a salary account sends the credit limit as JSON', async () => {
  const { fetchFn, calls } = fakeFetch(201, { id: 'P-1000' });
  const api = createApi('http://backend', fetchFn);

  await api.createSalaryAccount(-50_000);

  assert.equal(calls[0].url, 'http://backend/accounts/salary');
  assert.deepEqual(JSON.parse(calls[0].options.body), { creditLimit: -50_000 });
  assert.equal(calls[0].options.headers['Content-Type'], 'application/json');
});

test('a deposit sends amount and text to the account', async () => {
  const { fetchFn, calls } = fakeFetch(200, { balance: 1500 });
  const api = createApi('http://backend', fetchFn);

  const result = await api.deposit('S-1000', 1500, 'Lohn');

  assert.equal(result.balance, 1500);
  assert.equal(calls[0].url, 'http://backend/accounts/S-1000/deposit');
  assert.deepEqual(JSON.parse(calls[0].options.body), { amount: 1500, text: 'Lohn' });
});

test('a withdrawal calls the withdraw endpoint', async () => {
  const { fetchFn, calls } = fakeFetch(200, { balance: 1000 });
  const api = createApi('http://backend', fetchFn);

  await api.withdraw('S-1000', 500, '');

  assert.equal(calls[0].url, 'http://backend/accounts/S-1000/withdraw');
});

test('the error message of the backend is thrown', async () => {
  const { fetchFn } = fakeFetch(400, { error: 'Amount is greater than balance' });
  const api = createApi('http://backend', fetchFn);

  await assert.rejects(() => api.withdraw('S-1000', 1, ''), { message: 'Amount is greater than balance' });
});

test('an error without a message gets the status code', async () => {
  const { fetchFn } = fakeFetch(500, null);
  const api = createApi('http://backend', fetchFn);

  await assert.rejects(() => api.getBankBalance(), /500/);
});
