import { useState } from 'react';
import CreateAccount from './components/CreateAccount.jsx';
import AccountPanel from './components/AccountPanel.jsx';
import BankOverview from './components/BankOverview.jsx';

/**
 * The app: open an account, work with one account and look at the bank.
 * Loading and error state live here, so every part shows them the same way.
 */
export default function App() {
  const [createdId, setCreatedId] = useState('');
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Runs a call to the backend, shows the loading state and the error message of the backend
   * @returns {Promise<boolean>} true if the call worked
   */
  async function run(action) {
    setBusy(true);
    setError(null);
    try {
      await action();
      return true;
    } catch (e) {
      setError(e.message);
      return false;
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="app">
      <header className="app-header">
        <h1>New Bank</h1>
        {busy && <span className="loading" role="status">Lädt …</span>}
      </header>

      {error && (
        <p className="error" role="alert">
          {error}
          <button type="button" className="link" onClick={() => setError(null)}>schliessen</button>
        </p>
      )}

      <CreateAccount run={run} busy={busy} onCreated={setCreatedId} />
      <AccountPanel run={run} busy={busy} createdId={createdId} />
      <BankOverview run={run} busy={busy} />
    </main>
  );
}
