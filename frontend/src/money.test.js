import { test } from 'node:test';
import assert from 'node:assert/strict';
import { formatCHF, toMillirappen } from './money.js';

test('converting CHF to millirappen should work', () => {
  assert.equal(toMillirappen('1'), 100_000);
  assert.equal(toMillirappen('12.50'), 1_250_000);
  assert.equal(toMillirappen('0.00001'), 1);
});

test('rounding to whole millirappen should work', () => {
  assert.equal(toMillirappen('0.1'), 10_000);
  assert.equal(toMillirappen('0.000004'), 0);
});

test('text that is not a number being rejected should work', () => {
  assert.throws(() => toMillirappen('abc'), /Betrag/);
  assert.throws(() => toMillirappen(''), /Betrag/);
});

test('formatting millirappen as CHF should work', () => {
  assert.equal(formatCHF(1_250_000), 'CHF 12.50');
  assert.equal(formatCHF(0), 'CHF 0.00');
});

test('formatting a negative balance should work', () => {
  assert.equal(formatCHF(-50_000), 'CHF -0.50');
});
