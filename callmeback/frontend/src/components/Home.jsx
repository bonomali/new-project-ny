import React, {useState, useEffect} from 'react';
import { Container, Button, Form, Input, Header } from 'semantic-ui-react'
import { useHistory } from 'react-router-dom';

function Home(props) {
  const [name, setName] = useState('')
  const [number, setNumber] = useState('')
  const [topic, setTopic] = useState('')
  const [requestButtonEnabled, setRequestButtonEnabled] = useState(false)
  const history = useHistory();
  const validPhoneNumber = /((\(\d{3}\)?)|(\d{3}))([\s-./]?)(\d{3})([\s-./]?)(\d{4})/;

  useEffect(() => {
    if (validPhoneNumber.test(number)) {
      // only set requestButtonEnabled to true if number is provied
      // AND requestButtonEnabled is not already true (don't re-render if we don't need to)
      if (!requestButtonEnabled) setRequestButtonEnabled(true)
    } else {
      if (requestButtonEnabled) setRequestButtonEnabled(false)
    }
  }, [validPhoneNumber, number, requestButtonEnabled])

  const handleSubmit = (evt) => {
    evt.preventDefault()
    const reservation = {
      name: name,
      number: number,
      topic: topic,
    };
    // TODO: Send reservation to backend route api/reservations.
    // Get ID of reservation from response, and direct the user to
    // /reservations/{id} where they will see information about callback.
    let id = "sampleresid"
    history.push('/reservations/' + id)
  }

  return (
    <Container text textAlign='center' className='paper'>
      <div >
        <Header as="h1">Request a call back</Header>
        <div>
          Rather than waiting on hold, New York State will
          call you back.
        </div>
        <br/>
        <Form onSubmit={handleSubmit}>
        <Form.Field 
          control={Input}
          value={name}
          placeholder='Name (optional)'
          onChange={(evt) => {setName(evt.target.value)}}
        />
        <Form.Field
          required 
          control={Input} 
          value={number}
          type="tel"
          pattern="/((\(\d{3}\)?)|(\d{3}))([\s-./]?)(\d{3})([\s-./]?)(\d{4})/"
          placeholder='Phone number' 
          onChange={(evt) => {setNumber(evt.target.value)}} 
        />
        <Form.TextArea
          placeholder="What can we help you with?"
          value={topic}
          onChange={(evt) => {setTopic(evt.target.value)}}
        />
        <br/>
        <Button
          type="submit"
          variant="contained"
          color="blue"
          disabled={!requestButtonEnabled}
          className="submit"
        >
          Send request
        </Button>
      </Form>
      </div>
    </Container>
  );
}

export default Home;