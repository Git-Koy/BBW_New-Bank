import { useState } from 'react';
import { bankApi } from '../bankApi.js';
import { formatCHF } from '../money.js';

/**
 * Overview for the bank staff: balance of the bank and top 5 / bottom 5 (UC-05, UC-06)
 */
export default function BankOverview({ run, busy }) {
  const [overview, setOverview] = useState(null);

  async function refresh() {
    await run(async () => {
      const [balance, highest, lowest] = await Promise.all([
        bankApi.getBankBalance(),
        bankApi.getTop5Highest(),
        bankApi.getTop5Lowest(),
      ]);
      setOverview({ balance: balance.balance, highest, lowest });
    });
  }

  return (
    <section className="card">
      <h2>Bank</h2>
      <button type="button" disabled={busy} onClick={refresh}>Übersicht laden</button>

      {overview && (
        <>
          <p className="balance">Gesamtsaldo <span>{formatCHF(overview.balance)}</span></p>
          <div className="columns">
            <AccountList title="Top 5" accounts={overview.highest} />
            <AccountList title="Bottom 5" accounts={overview.lowest} />
          </div>
        </>
      )}
    </section>
  );
}

function AccountList({ title, accounts }) {
  return (
    <div>
      <h3>{title}</h3>
      {accounts.length === 0 ? (
        <p className="muted">Keine Konten.</p>
      ) : (
        <ol>
          {accounts.map((account) => (
            <li key={account.id}>
              {account.id} <span className="muted">{account.type}</span> {formatCHF(account.balance)}
            </li>
          ))}
        </ol>
      )}
    </div>
  );
}
