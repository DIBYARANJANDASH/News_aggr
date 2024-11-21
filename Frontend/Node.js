import { createClient } from 'redis';

const client = createClient({
    password: '2LQYUU0wfgvItNgbFPkVAq3C4FohDQ9X',
    socket: {
        host: 'redis-13763.c253.us-central1-1.gce.redns.redis-cloud.com',
        port: 13763
    }
});

client.connect()
    .then(() => console.log('Connected to Redis Cloud!'))
    .catch(err => console.error('Redis connection error:', err));

export default client;
