import React, {useState, useEffect} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import useStyles from  '../styles';

function Home() {
  const classes = useStyles();
  const [name, setName] = useState('')
  const [number, setNumber] = useState('')
  const [topic, setTopic] = useState('')
  const [enabled, setEnabled] = useState(false)

  useEffect(() => {
    if (name !== "" && number !== "" && topic !== "") {
      // only set enabled to true if name, number, and topic are selected
      // AND enabled is not already true (don't re-render if we don't need to)
      if (!enabled) setEnabled(true)
    } else {
      if (enabled) setEnabled(false)
    }
  })

  const handleSubmit = (evt) => {
    const reservation = {
      name: name,
      number: number,
      topic: topic,
    };
    // Send reservation to backend route api/reservations
  }

  return (
    <Container component="main" maxWidth="xs">
      <div className={classes.paper}>
        <Typography component="h1" variant="h5">
          Welcome to the call back queue!
        </Typography>
        <br />
        <Typography variant="subtitle1" align="center">
          Rather than waiting, New York State will
          call you back at a specific time.
        </Typography>
        <form className={classes.form} noValidate>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="name"
            label="Full Name"
            name="name"
            type="text"
            autoComplete="name"
            autoFocus
            value={name}
            onChange={(evt) => {setName(evt.target.value)}}
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="number"
            label="Phone Number"
            name="number"
            type="text"
            autoComplete="555-555-5555"
            value={number}
            onChange={(evt) => {setNumber(evt.target.value)}}
          />
          <br />
          <br />
          <Typography variant="subtitle1">
            What are you calling about?
          </Typography>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            multiline
            id="topic"
            label="Topic"
            name="topic"
            type="text"
            rows='4'
            value={topic}
            onChange={(evt) => {setTopic(evt.target.value)}}
          />
          <div align='center'>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            disabled={!enabled}
            className={classes.submit}
            onSubmit={handleSubmit}
          >
            Join the queue
          </Button>
          </div>
        </form>
      </div>
    </Container>
  );
}

export default Home;