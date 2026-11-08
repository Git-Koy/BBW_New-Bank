import { createApi } from './api.js';

/**
 * The API used by the app. The URL comes from the environment (.env.development), not from the code.
 */
export const bankApi = createApi(import.meta.env.VITE_API_URL);
