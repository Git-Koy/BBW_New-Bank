import { useState } from 'react';
import { bankApi } from '../bankApi.js';
import { toMillirappen } from '../money.js';

/**
 * Opens a new account of the chosen type (UC-01)
 */
export default function CreateAccount({ run, busy, onCreated }) {
  const [type, setType] = useState('savings');
  const [creditLimit, setCreditLimit] = useState('-500');
  const [created, setCreated] = useState(null);

  async function handleSubmit(event) {
    event.preventDefault();
    await run(async () => {
      let result;
      if (type === 'savings') {
        result = await bankApi.createSavingsAccount();
      } else if (type === 'promo-youth') {
        result = await bankApi.createPromoYouthSavingsAccount();
      } else {
        result = await bankApi.createSalaryAccount(toMillirappen(creditLimit));
      }
      setCreated(result.id);
      onCreated(result.id);
    });
  }

  return (
    <section className="card">
      <h2>Konto eröffnen</h2>
      <form onSubmit={handleSubmit} className="form">
        <label>
          Kontotyp
          <select value={type} onChange={(e) => setType(e.target.value)}>
            <option value="savings">Sparkonto</option>
            <option value="promo-youth">Promo-Jugendsparkonto</option>
            <option value="salary">Lohnkonto</option>
          </select>
        </label>
        {type === 'salary' && (
          <label>
            Kreditlimite (CHF, 0 oder negativ)
            <input value={creditLimit} onChange={(e) => setCreditLimit(e.target.value)} inputMode="decimal" />
          </label>
        )}
        <button type="submit" disabled={busy}>Eröffnen</button>
      </form>
      {created && <p className="success">Neues Konto: <strong>{created}</strong></p>}
    </section>
  );
}
