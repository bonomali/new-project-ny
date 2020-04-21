# frontend

A Next.js app.

## Build and run

### Local

```bash
npm run dev
# or
yarn dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.

### With Docker

```bash
docker build --tag frontend:1.0 .
docker run --publish 3000:3000 --name frontend frontend:1.0
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.
