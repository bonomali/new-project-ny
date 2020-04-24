import React, {useState, useEffect} from 'react';
import People from './People.jsx';
import {Grid, Container, Form, Input, Button, Icon} from 'semantic-ui-react';
import axios from 'axios';

function Home() {
  const [people, setPeople] = useState([])

  useEffect(() => {
    const fetchPeople = async () => {
      try {
        const res = await axios.get("/api/v1/person");
        setPeople(res.data);
      }
      catch (error) {
        console.log(error); // Add other error handling.
      }
    };
    fetchPeople();
  }, []) // indicates that this should only be run if the field inside the bracket changes (in this case, it should only be run on original render since '' will never change)
  
  const [name, setName] = useState('')

  const handleSubmit = (evt) => {
    const addPerson = async () => {
      try {
        const res = await axios.post('/api/v1/person', { name: name });
        setPeople(people.concat(res.data))
      }
      catch (error) {
        console.log(error);
      }
    };
    addPerson();
    setName(''); // Clear input
  }

  return (
    <Container fluid>
    <Grid columns={2} divided padded='horizontally' relaxed className='main-grid'>
      <Grid.Column width={12} >
        <People people={people}/>
      </Grid.Column>
      <Grid.Column width={4}>
        <h2>Add Person</h2>
        <Form onSubmit={handleSubmit}>
          <h4>Information</h4>
          <Form.Group>
          <Form.Field required control={Input} value={name} label='Full Name' placeholder='Full Name' onChange={(evt, data) => {setName(data.value)}} />
          </Form.Group>
          <Form.Field>
          <Button primary type="submit">
            Submit<Icon name="right chevron" />
          </Button>
          </Form.Field>
        </Form>
      </Grid.Column>
    </Grid>
    </Container>
  );
}

export default Home;
