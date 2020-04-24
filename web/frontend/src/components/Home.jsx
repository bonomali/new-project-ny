import React, {useState} from 'react';
import People from './People.jsx';
import {Grid, Container, Form, Input, Button, Icon} from 'semantic-ui-react'

function Home() {
  const [people, setPeople] = useState(() => {
    // Here is where we could issue an aysnc API call to get people from our database (can use package axios).
    return [{id: "id", name: "name"}, {id: "id2", name: "name2"}]
  })
  
  const [name, setName] = useState('')
  const [id, setID] = useState('')

  const handleSubmit = () => {
    setPeople(people.concat({name: name, id: id}))
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
          <Form.Field required control={Input} label='Full Name' placeholder='Full Name' onChange={(evt, data) => {setName(data.value)}} />
          </Form.Group>
          <Form.Group>
          <Form.Field control={Input} label='ID' placeholder='ID' onChange={(evt, data) => {setID(data.value)}} />
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
