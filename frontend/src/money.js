/**
 * Conversion between CHF (what the user types and reads) and millirappen (what the backend uses).
 * 100'000 millirappen = CHF 1.00
 */
const MILLIRAPPEN_PER_FRANC = 100_000;

/**
 * Converts a CHF amount like "12.50" into millirappen
 * @param {string} chf the amount in CHF
 * @returns {number} the amount in whole millirappen
 * @throws {Error} if the text is not a number
 */
export function toMillirappen(chf) {
  const value = Number(chf);

  if (chf === '' || Number.isNaN(value)) {
    throw new Error('Betrag ist keine Zahl');
  }

  return Math.round(value * MILLIRAPPEN_PER_FRANC);
}

/**
 * Formats millirappen as CHF with two decimal places, for example 1250000 becomes "CHF 12.50"
 * @param {number} millirappen the amount in millirappen
 * @returns {string} the formatted amount
 */
export function formatCHF(millirappen) {
  return `CHF ${(millirappen / MILLIRAPPEN_PER_FRANC).toFixed(2)}`;
}
