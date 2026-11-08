import { useEffect, useState } from 'react';
import { bankApi } from '../bankApi.js';
import { formatCHF, toMillirappen } from '../money.js';

/**
 * Shows one account with its balance and bookings, and deposits or withdraws (UC-02, UC-03, UC-04)
 */
export default function AccountPanel({ run, busy, createdId }) {
  const [input, setInput] = useState('');
  const [account, setAccount] = useState(null);
  const [bookings, setBookings] = useState([]);
  const [amount, setAmount] = useState('');
  const [text, setText] = useState('');

  async function load(id) {
    await run(async () => {
      setAccount(await bankApi.getAccount(id));
      setBookings(await bankApi.getBookings(id));
    });
  }

  // a newly opened account is shown right away
  useEffect(() => {
    if (createdId) {
      setInput(createdId);
      load(createdId);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [createdId]);

  async function transact(kind) {
    const worked = await run(async () => {
      const millirappen = toMillirappen(amount);
      if (kind === 'deposit') {
        await bankApi.deposit(account.id, millirappen, text);
      } else {
        await bankApi.withdraw(account.id, millirappen, text);
      }
    });
    if (worked) {
      setAmount('');
      setText('');
      await load(account.id);
    }
  }

  return (
    <section className="card">
      <h2>Konto</h2>
      <form className="form inline" onSubmit={(e) => { e.preventDefault(); load(input.trim()); }}>
        <label>
          Kontonummer
          <input value={input} onChange={(e) => setInput(e.target.value)} placeholder="z. B. S-1000" />
        </label>
        <button type="submit" disabled={busy || !input.trim()}>Laden</button>
      </form>

      {account && (
        <>
          <p className="balance">
            {account.type} <strong>{account.id}</strong>
            <span>{formatCHF(account.balance)}</span>
          </p>

          <div className="form">
            <label>
              Betrag (CHF)
              <input value={amount} onChange={(e) => setAmount(e.target.value)} inputMode="decimal" placeholder="z. B. 12.50" />
            </label>
            <label>
              Text (optional)
              <input value={text} onChange={(e) => setText(e.target.value)} />
            </label>
            <div className="buttons">
              <button type="button" disabled={busy || !amount} onClick={() => transact('deposit')}>Einzahlen</button>
              <button type="button" disabled={busy || !amount} onClick={() => transact('withdraw')}>Auszahlen</button>
            </div>
          </div>

          <h3>Kontoauszug</h3>
          {bookings.length === 0 ? (
            <p className="muted">Noch keine Buchungen.</p>
          ) : (
            <table>
              <thead>
                <tr><th>Datum</th><th>Text</th><th className="num">Betrag</th></tr>
              </thead>
              <tbody>
                {bookings.map((booking, index) => (
                  <tr key={index}>
                    <td>{new Date(booking.date).toLocaleString('de-CH')}</td>
                    <td>{booking.text}</td>
                    <td className={booking.amount < 0 ? 'num negative' : 'num'}>{formatCHF(booking.amount)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </>
      )}
    </section>
  );
}
